package ru.otus.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.entity.ItemPriceValueEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepo {

    private final JdbcTemplate jdbcTemplate;

    public void saveAllHierarchy(List<HierarchyEntity> entities){

        var sql = "insert into Hierarchy (code, name, version, is_deleted, create_date, update_date) "
                + "values (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                HierarchyEntity hierarchy = entities.get(i);

                ps.setString(1, hierarchy.getCode());
                ps.setString(2, hierarchy.getName());
                ps.setLong(3, hierarchy.getVersion());
                ps.setString(4, hierarchy.isDeleted() ? "Y" : "N");
                ps.setTimestamp(5, Timestamp.from(hierarchy.getCreateDate()));
                ps.setTimestamp(6, Timestamp.from(hierarchy.getUpdateDate()));
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }
    
    public void saveAll(List<ItemEntity> entities){

        var sql = "insert into item (code, name, hierarchy_id, version, is_deleted, create_date, update_date) "
                + "values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ItemEntity item = entities.get(i);

                ps.setString(1, item.getCode());
                ps.setString(2, item.getName());
                ps.setLong(3, item.getHierarchy().getId());
                ps.setLong(4, item.getVersion());
                ps.setString(5, item.isDeleted() ? "Y" : "N");
                ps.setTimestamp(6, Timestamp.from(item.getCreateDate()));
                ps.setTimestamp(7, Timestamp.from(item.getUpdateDate()));
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }

    public void saveAllPrices(List<ItemPriceValueEntity> entities){

        var sql = "INSERT INTO item_price_value (item_id, price_list_id, value, start_time, is_deleted, "
                + "create_date, update_date, version) "
                + "VALUES (?, ?, ?, current_timestamp, 'N', current_timestamp, current_timestamp, 1)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ItemPriceValueEntity priceValue = entities.get(i);

                ps.setLong(1, priceValue.getItem().getId());
                ps.setLong(2, priceValue.getPriceList().getId());
                ps.setBigDecimal(3, priceValue.getValue());
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }  
    
    public void saveAllBarcode(List<BarcodeEntity> entities){

        var sql = "INSERT INTO item_barcode "
                + "(item_id, barcode, description, is_default, is_deleted, create_date, update_date, version) "
                + "VALUES (?, ?, ?, ?, 'N', current_timestamp, current_timestamp, 1);";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BarcodeEntity barcodeEntity = entities.get(i);

                ps.setLong(1, barcodeEntity.getItem().getId());
                ps.setString(2, barcodeEntity.getBarcode());
                ps.setString(3, barcodeEntity.getDescription());
                ps.setString(4, barcodeEntity.isDefault() ? "Y" : "N");
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }
}

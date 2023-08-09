package ru.otus.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.response.BarcodeExportResponse;
import ru.otus.model.response.ItemPriceExportResponse;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static java.util.Objects.nonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemExportRepository {

    private final JdbcTemplate jdbcTemplate;

    public String getTempTable(List<Long> ids, String tempName) {

        String sql = String.format("CREATE TEMPORARY TABLE %s (id bigint primary key)", tempName);

        jdbcTemplate.update(sql);
        String batchInsertSql = String.format("INSERT INTO %s (id) VALUES (?)", tempName);
        jdbcTemplate.batchUpdate(batchInsertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long id = ids.get(i);
                if (id != null) {
                    ps.setLong(1, id);
                }
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
        return tempName;
    } 
    
    public void dropTempTable(String temTable) {

        String sql = String.format("DROP TABLE %s", temTable);
        jdbcTemplate.update(sql);
    }

    public Map<Long, HierarchyEntity> getHierarchyByTempTable(String tempTable) {

        String sql2 = """
                SELECT h.id,
                       code,
                       name,
                       parent_id,
                       is_deleted,
                       create_date,
                       update_date,
                       version
                FROM hierarchy h
                INNER JOIN
                """ + String.format(" %s ON %s.id = h.id", tempTable, tempTable);

        Map<Long, HierarchyEntity> result = new HashMap<>();
        jdbcTemplate.query(sql2, rs -> {

            long id = rs.getLong("id");
            String code = rs.getString("code");
            String name = rs.getString("name");

            if (nonNull(code)) {
                HierarchyEntity hierarchy = HierarchyEntity.builder()
                        .code(code)
                        .name(name)
                        .build();
                result.putIfAbsent(id, hierarchy);
            }
        });
        return result;
    }

    public Map<Long, List<BarcodeExportResponse>> getBarCodeByTempTable(String tempTable) {

        String sql2 = """
                SELECT
                ib.barcode AS barcode,
                ib.item_id AS itemId,
                ib.description AS description,
                ib.is_default AS isDefault
                FROM item_barcode ib
                INNER JOIN
                """+ String.format(" %s ON %s.id = ib.item_id", tempTable, tempTable);

        Map<Long, List<BarcodeExportResponse>> result = new HashMap<>();
        jdbcTemplate.query(sql2, rs -> {

            long itemId = rs.getLong("itemId");
            String barcode = rs.getString("barcode");
            String description = rs.getString("description");
            String isDefault = rs.getString("isDefault");

            if (nonNull(barcode)) {
                BarcodeExportResponse barcodeExport = BarcodeExportResponse.builder()
                        .barcode(barcode)
                        .description(description)
                        .isDefault(isDefault)
                        .build();
                result.putIfAbsent(itemId, new ArrayList<>());
                result.get(itemId).add(barcodeExport);
            }
        });
        return result;
    }

    public Map<Long, List<ItemPriceExportResponse>> getPriceValueByTempTable(String tempTable) {

        String sql2 = String.format("""
                SELECT ipv.item_id AS itemId, pl.code as priceListCode, ipv.value as value, ipv.start_time as startTime
                FROM item_price_value ipv
                JOIN %s ids ON ids.id = ipv.item_id
                JOIN price_list pl on pl.id = ipv.price_list_id
                """,tempTable);

        Map<Long, List<ItemPriceExportResponse>> result = new HashMap<>();
        jdbcTemplate.query(sql2, rs -> {

            long itemId = rs.getLong("itemId");
            String priceListCode = rs.getString("priceListCode");
            BigDecimal value = rs.getBigDecimal("value");
            Timestamp time = rs.getObject("startTime", Timestamp.class);
            Instant startTime = Optional.ofNullable(time).map(Timestamp::toInstant).orElse(null);

            if (nonNull(priceListCode)) {
                ItemPriceExportResponse priceExport= ItemPriceExportResponse.builder()
                        .priceListCode(priceListCode)
                        .value(value)
                        .startTime(startTime)
                        .build();
                result.putIfAbsent(itemId, new ArrayList<>());
                result.get(itemId).add(priceExport);
            }
        });
        return result;
    }
}

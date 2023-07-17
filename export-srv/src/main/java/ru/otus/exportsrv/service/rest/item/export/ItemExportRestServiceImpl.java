package ru.otus.exportsrv.service.rest.item.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.request.item.ExportItemFilter;
import ru.otus.exportsrv.model.response.item.export.ItemExportResponse;
import ru.otus.exportsrv.service.rest.RestClientImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemExportRestServiceImpl implements ItemExportRestService {

    @Value("${service.item-srv.baseUrl}")
    private String itemUrl;

    private final RestClientImpl restClient;

    @Override
    public List<ItemExportResponse> getExportItems(ExportItemFilter filter) {

        var paramType = new ParameterizedTypeReference<List<ItemExportResponse>>() {};
        try {
            String url = itemUrl + "/export/item";
            return restClient.sendPost(url, filter, paramType);
        } catch (Exception e) {
           log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}

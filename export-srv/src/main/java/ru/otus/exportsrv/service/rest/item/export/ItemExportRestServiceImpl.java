package ru.otus.exportsrv.service.rest.item.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.exportsrv.model.request.item.export.ExportItemFilter;
import ru.otus.exportsrv.model.response.item.export.ExportResponse;
import ru.otus.exportsrv.service.rest.RestClientImpl;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemExportRestServiceImpl implements ItemExportRestService {

    @Value("${service.item-srv.baseUrl}")
    private String itemUrl;

    private final RestClientImpl restClient;

    @Override
    public ExportResponse getExportItems(int page, int size, ExportItemFilter filter) {

        var paramType = new ParameterizedTypeReference<ExportResponse>() {};
        try {
            String url = UriComponentsBuilder.fromUriString(itemUrl + "/export/item")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .toUriString();
            return restClient.sendPost(url, filter, paramType);
        } catch (Exception e) {
           log.error(e.getMessage(), e);
        }
        return new ExportResponse();
    }
}

package ru.otus.model;

import lombok.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImportDataContext {

    private Set<String> itemCodes;
    private Set<String> hierarchyCodes;
    private Set<String> currencyCodes;
    private Set<String> priceListCodes;
    private Set<String> barcodesCodes;
    private Map<String, Integer> barCodeCountMap;
}

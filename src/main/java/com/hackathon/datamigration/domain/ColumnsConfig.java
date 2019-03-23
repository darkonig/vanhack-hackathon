package com.hackathon.datamigration.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ColumnsConfig {

    private List<String> columns;
    private List<Map<String, String>> sampleData;

}

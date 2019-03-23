package com.hackathon.datamigration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTable {

    private List<DataColumn> columns;
    private List<Map<String, String>> values;

}

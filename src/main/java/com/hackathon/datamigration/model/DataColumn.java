package com.hackathon.datamigration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataColumn {

    private int csvColumnIndex;
    private String csvColumn;
    private String fieldName;

}

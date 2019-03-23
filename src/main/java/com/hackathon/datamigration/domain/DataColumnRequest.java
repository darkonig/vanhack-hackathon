package com.hackathon.datamigration.domain;

import com.hackathon.datamigration.model.DataColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataColumnRequest {

    List<DataColumn> columns;

}

package com.hackathon.datamigration.core;


import com.hackathon.datamigration.core.impl.DataProcessCSV;
import com.hackathon.datamigration.domain.ColumnsConfig;
import com.hackathon.datamigration.model.DataColumn;
import com.hackathon.datamigration.model.DataTable;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataProcessorTest {

    private DataProcessor processor;
    private Path path;

    @Before
    public void setup() throws URISyntaxException {
        processor = new DataProcessCSV();

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("csv/data.migration.csv");
        path = Paths.get(resource.toURI());
    }

    @Test
    public void loadColumnsCSV() throws IOException {
        List<String> cols = Arrays.asList("sku/id","Name","short_desc","desc","price"
                ,"wholesale_price","avalaible_qtd","unit_multiplier","Option Desc");

        ColumnsConfig columnsConfig = processor.getColumns(path);

        assertThat(columnsConfig.getColumns())
                .containsAll(cols);

        assertThat(columnsConfig.getSampleData().get(0).get("sku/id"))
                .isEqualTo("2311");
    }

    @Test
    public void loadData() throws IOException {
        List<DataColumn> cols = Arrays.asList(
                DataColumn.builder()
                        .csvColumn("sku/id")
                        .fieldName("id")
                    .build(),
                DataColumn.builder()
                        .csvColumn("Name")
                        .fieldName("name")
                    .build()
        );

        DataTable cfg = processor.getDataTable(path, cols);

        assertThat(cfg.getColumns()).isEqualTo(cols);

        assertThat(cfg.getValues()).hasSize(2);

        assertThat(cfg.getValues().get(0).get("id"))
                .isEqualTo("2311");

        assertThat(cfg.getValues().get(0).get("name"))
                .isEqualTo("Candle");
    }

}
package com.hackathon.datamigration.core.impl;

import com.hackathon.datamigration.core.DataProcessor;
import com.hackathon.datamigration.domain.ColumnsConfig;
import com.hackathon.datamigration.exception.BusinessException;
import com.hackathon.datamigration.exception.TechnicalException;
import com.hackathon.datamigration.model.DataColumn;
import com.hackathon.datamigration.model.DataTable;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DataProcessCSV implements DataProcessor {

    @Override
    public ColumnsConfig getColumns(Path file) throws IOException {
        Reader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"));

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();

        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build()) {
            List<String> cols = Arrays.asList(getRow(csvReader));

            String[] sample1 = getRow(csvReader);
            String[] sample2 = getRow(csvReader);

            List<Map<String, String>> samples = new ArrayList<>(2);
            samples.add(getSample(cols, sample1));
            samples.add(getSample(cols, sample2));

            return ColumnsConfig.builder().columns(cols).sampleData(samples).build();
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        }
    }

    @Override
    public DataTable getDataTable(Path file, List<DataColumn> cfg) throws IOException {
        Reader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"));

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();

        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build()) {

            List<String> cols = Arrays.asList(getRow(csvReader));

            cfg.forEach(e -> e.setCsvColumnIndex(cols.indexOf(e.getCsvColumn())) );

            List<Map<String, String>> values = new ArrayList<>();
            String[] csvRow;
            while ((csvRow = csvReader.readNext()) != null) {
                Map<String, String> obj = new HashMap<>();
                for (DataColumn dataColumn : cfg) {
                    obj.put(dataColumn.getFieldName(), csvRow[dataColumn.getCsvColumnIndex()]);
                }
                values.add(obj);
            }

            return DataTable.builder().columns(cfg).values(values).build();
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        }
    }

    /*
     Get CSV row and if its null throw a BusinessException
     */
    private String[] getRow(CSVReader csvReader) throws IOException {
        String[] x = csvReader.readNext();
        if (x == null) {
            throw new BusinessException("Invalid CSV");
        }

        return x;
    }

    /*
    Parse a given row into a sample data
     */
    private Map<String, String> getSample(List<String> cols, String[] sample) {
        int i = 0;
        Map<String, String> result = new HashMap<>(cols.size());
        for (String col : cols) {
            result.put(col, sample[i++]);
        }

        return result;
    }

}

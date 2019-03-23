package com.hackathon.datamigration.core;

import com.hackathon.datamigration.core.impl.DataProcessCSV;

public class DataProcessorFactory {

    public static DataProcessor get(String fileName) {
        if (fileName.toLowerCase().endsWith(".csv")) {
            return new DataProcessCSV();
        }

        return null;
    }

}

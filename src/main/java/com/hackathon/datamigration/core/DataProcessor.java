package com.hackathon.datamigration.core;

import com.hackathon.datamigration.domain.ColumnsConfig;
import com.hackathon.datamigration.model.DataColumn;
import com.hackathon.datamigration.model.DataTable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DataProcessor {

    /**
     * Get file columns and sample data
     *
     * @param file File to be processed
     * @return Columns and sample data from the given file
     * @throws IOException
     */
    ColumnsConfig getColumns(Path file) throws IOException;

    /**
     * Get the given data values and parse to the given migration structure
     *
     * @param file File to be processed
     * @param cfg Columns config
     * @return
     * @throws IOException
     */
    DataTable getDataTable(Path file, List<DataColumn> cfg) throws IOException;

}

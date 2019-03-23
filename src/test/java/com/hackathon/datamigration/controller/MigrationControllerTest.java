package com.hackathon.datamigration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.datamigration.domain.DataColumnRequest;
import com.hackathon.datamigration.model.DataColumn;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MigrationController.class)
@AutoConfigureMockMvc
//@TestPropertySource(
//        locations = "classpath:application-integration.yml")
public class MigrationControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void update() {

    }

    @Test
    public void loadColumns() throws Exception {
        mockMvc.perform(get("/migration/data.migration.csv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.columns", Matchers.contains("sku/id", "Name", "short_desc", "desc", "price"
                        , "wholesale_price", "avalaible_qtd", "unit_multiplier", "Option Desc")))
                .andExpect(jsonPath("$.sampleData[0].Name", Matchers.is("Candle")));
    }

    @Test
    public void processFile() throws Exception {
        DataColumnRequest config = DataColumnRequest.builder()
                .columns(Arrays.asList(
                        DataColumn.builder()
                                .csvColumn("sku/id")
                                .fieldName("id")
                                .build(),
                        DataColumn.builder()
                                .csvColumn("Name")
                                .fieldName("name")
                                .build()
                ))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(config);

        mockMvc.perform(post("/migration/data.migration.csv")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.columns[0].fieldName", Matchers.is("id")))
                .andExpect(jsonPath("$.columns[1].fieldName", Matchers.is("name")))
                .andExpect(jsonPath("$.values", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.values[0].name", Matchers.is("Candle")));
    }
}
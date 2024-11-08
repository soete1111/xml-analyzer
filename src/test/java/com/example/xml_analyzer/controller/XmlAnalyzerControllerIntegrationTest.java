package com.example.xml_analyzer.controller;

import com.example.xml_analyzer.model.AnalysisRequest;
import com.example.xml_analyzer.model.AnalysisResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class XmlAnalyzerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void analyzeXml_WithValidUrl_ShouldReturnAnalysis() {
        AnalysisRequest request = new AnalysisRequest();
        request.setUrl("https://merapar-assessment-task.s3.eu-central-1.amazonaws.com/arabic-posts.xml");

        ResponseEntity<AnalysisResponse> response = restTemplate.postForEntity(
            "http://localhost:" + port + "/analyze/",
            request,
            AnalysisResponse.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAnalyseDate());
    }
} 
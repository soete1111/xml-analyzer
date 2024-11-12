package com.example.xml_analyzer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.example.xml_analyzer.model.AnalysisRequest;
import com.example.xml_analyzer.model.AnalysisResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class XmlAnalyzerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static ClientAndServer mockServer;
    private static MockServerClient mockServerClient;
    private static final int MOCK_SERVER_PORT = 1080;

    @BeforeAll
    static void startMockServer() {
        mockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
        mockServerClient = new MockServerClient("localhost", MOCK_SERVER_PORT);
    }

    @AfterAll
    static void stopMockServer() {
        mockServerClient.close();
        mockServer.stop();
    }

    @Test
    void analyzeXml_WithValidUrl_ShouldReturnAnalysis() {

        String sampleXml = """
                <?xml version="1.0" encoding="utf-8"?>
                <posts>
                    <row Id="1" PostTypeId="1" AcceptedAnswerId="2" CreationDate="2023-01-01T12:00:00" Score="5" />
                    <row Id="2" PostTypeId="2" CreationDate="2023-01-02T12:00:00" Score="3" />
                </posts>
                """;

        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/Posts.xml"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/xml")
                                .withBody(sampleXml));

        AnalysisRequest request = new AnalysisRequest();
        request.setUrl("http://localhost:" + MOCK_SERVER_PORT + "/Posts.xml");

        ResponseEntity<AnalysisResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/analyze/",
                request,
                AnalysisResponse.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAnalyseDate());
        assertEquals("Posts.xml", response.getBody().getFileName());
        assertEquals(2, response.getBody().getDetails().getTotalPosts());
        assertEquals(1, response.getBody().getDetails().getTotalAcceptedPosts());
        assertEquals(4, response.getBody().getDetails().getAvgScore());
    }

    @Test
    void analyzeXml_WithInvalidUrl_ShouldReturnError() {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/nonexistent.xml"))
                .respond(
                        response()
                                .withStatusCode(500));

        AnalysisRequest request = new AnalysisRequest();
        request.setUrl("http://localhost:" + MOCK_SERVER_PORT + "/nonexistent.xml");

        ResponseEntity<AnalysisResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/analyze/",
                request,
                AnalysisResponse.class);

        assertEquals(500, response.getStatusCode().value());
    }
}
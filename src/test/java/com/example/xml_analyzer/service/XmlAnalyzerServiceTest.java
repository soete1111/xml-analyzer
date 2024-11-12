package com.example.xml_analyzer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.example.xml_analyzer.exception.XmlAnalysisException;
import com.example.xml_analyzer.model.AnalysisResponse;

@ExtendWith(MockitoExtension.class)
class XmlAnalyzerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private XmlAnalyzerService xmlAnalyzerService;

    @BeforeEach
    void setUp() {
        xmlAnalyzerService = new XmlAnalyzerService(restTemplate);
    }

    @Test
    void analyzeXml_ShouldCalculateCorrectMetricsForValidXmlPosts() {
        String testUrl = "https://example.com/Posts.xml";
        String sampleXml = """
                <?xml version="1.0" encoding="utf-8"?>
                <posts>
                    <row Id="1" PostTypeId="1" AcceptedAnswerId="2" CreationDate="2023-01-01T12:00:00" Score="5" />
                    <row Id="2" PostTypeId="2" CreationDate="2023-01-02T12:00:00" Score="3" />
                </posts>
                """;

        when(restTemplate.getForObject(eq(testUrl), eq(byte[].class)))
                .thenReturn(sampleXml.getBytes());

        AnalysisResponse response = xmlAnalyzerService.analyzeXml(testUrl);

        assertNotNull(response);
        assertNotNull(response.getAnalyseDate());
        assertEquals("Posts.xml", response.getFileName());
        assertEquals(2, response.getDetails().getTotalPosts());
        assertEquals(1, response.getDetails().getTotalAcceptedPosts());
        assertEquals(4, response.getDetails().getAvgScore());
    }

    @Test
    void analyzeXml_WithInvalidUrl_ShouldThrowException() {
        String invalidUrl = "invalid-url";
        when(restTemplate.getForObject(eq(invalidUrl), eq(byte[].class)))
                .thenThrow(new RuntimeException("Invalid URL"));

        XmlAnalysisException exception = assertThrows(XmlAnalysisException.class,
                () -> xmlAnalyzerService.analyzeXml(invalidUrl));

        assertEquals("Error analyzing XML file: Invalid URL", exception.getMessage());
    }
}
package com.example.xml_analyzer.service;

import com.example.xml_analyzer.exception.XmlAnalysisException;
import com.example.xml_analyzer.model.AnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class XmlAnalyzerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private XmlAnalyzerService xmlAnalyzerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        xmlAnalyzerService = new XmlAnalyzerService(restTemplate);
    }

    @Test
    void analyzeXml_WithValidUrl_ShouldReturnAnalysis() {
        String testUrl = new File("src/test/java/com/example/xml_analyzer/resources/test-posts.xml").toURI().toString();
        AnalysisResponse response = xmlAnalyzerService.analyzeXml(testUrl);
        
        assertNotNull(response);
        assertNotNull(response.getAnalyseDate());
        assertEquals("test-posts.xml", response.getFileName());
        assertTrue(response.getDetails().getTotalPosts() > 0);
    }

    @Test
    void analyzeXml_WithInvalidUrl_ShouldThrowException() {
        String invalidUrl = "invalid-url";
        assertThrows(XmlAnalysisException.class, () -> 
            xmlAnalyzerService.analyzeXml(invalidUrl)
        );
    }
} 
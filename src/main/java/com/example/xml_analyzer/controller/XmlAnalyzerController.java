package com.example.xml_analyzer.controller;

import com.example.xml_analyzer.exception.XmlAnalysisException;
import com.example.xml_analyzer.model.ErrorResponse;
import com.example.xml_analyzer.model.AnalysisRequest;
import com.example.xml_analyzer.model.AnalysisResponse;
import com.example.xml_analyzer.service.XmlAnalyzerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analyze/")
public class XmlAnalyzerController {

    private final XmlAnalyzerService xmlAnalyzerService;

    public XmlAnalyzerController(XmlAnalyzerService xmlAnalyzerService) {
        this.xmlAnalyzerService = xmlAnalyzerService;
    }

    @PostMapping
    public ResponseEntity<AnalysisResponse> analyzeXml(@RequestBody AnalysisRequest request) {
        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        return ResponseEntity.ok(xmlAnalyzerService.analyzeXml(request.getUrl()));
    }

    @ExceptionHandler(XmlAnalysisException.class)
    public ResponseEntity<ErrorResponse> handleXmlAnalysisException(XmlAnalysisException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "XML Analysis failed",
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid input",
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
} 
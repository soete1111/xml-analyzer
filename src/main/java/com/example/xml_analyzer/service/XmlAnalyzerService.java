package com.example.xml_analyzer.service;

import com.example.xml_analyzer.exception.XmlAnalysisException;
import com.example.xml_analyzer.model.AnalysisResponse;
import com.example.xml_analyzer.model.Details;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class XmlAnalyzerService {
    private final RestTemplate restTemplate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public XmlAnalyzerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AnalysisResponse analyzeXml(String url) {
        AnalysisResponse response = new AnalysisResponse();
        response.setAnalyseDate(LocalDateTime.now());
        response.setFileName(extractFileName(url));
        Details details = new Details();

        try (InputStream inputStream = new URL(url).openStream()) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            long totalPosts = 0;
            long totalAcceptedPosts = 0;
            double scoreSum = 0;
            LocalDateTime firstPost = null;
            LocalDateTime lastPost = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT && "row".equals(reader.getLocalName())) {
                    totalPosts++;

                    // Get creation date
                    String creationDate = reader.getAttributeValue(null, "CreationDate");
                    if (creationDate != null) {
                        LocalDateTime postDate = LocalDateTime.parse(creationDate, DATE_FORMATTER);
                        
                        if (firstPost == null || postDate.isBefore(firstPost)) {
                            firstPost = postDate;
                        }
                        if (lastPost == null || postDate.isAfter(lastPost)) {
                            lastPost = postDate;
                        }
                    }

                    // Check if post is accepted
                    String acceptedAnswerId = reader.getAttributeValue(null, "AcceptedAnswerId");
                    if (acceptedAnswerId != null && !acceptedAnswerId.isEmpty()) {
                        totalAcceptedPosts++;
                    }

                    // Get score
                    String score = reader.getAttributeValue(null, "Score");
                    if (score != null) {
                        scoreSum += Double.parseDouble(score);
                    }
                }
            }

            // Set the details
            details.setTotalPosts(totalPosts);
            details.setTotalAcceptedPosts(totalAcceptedPosts);
            details.setFirstPost(firstPost);
            details.setLastPost(lastPost);
            details.setAvgScore(totalPosts > 0 ? scoreSum / totalPosts : 0);
            
            response.setDetails(details);

        } catch (XMLStreamException | IOException e) {
            throw new XmlAnalysisException("Error analyzing XML file: " + e.getMessage(), e);
        }

        return response;
    }

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
} 
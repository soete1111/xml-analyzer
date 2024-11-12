package com.example.xml_analyzer.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.example.xml_analyzer.exception.XmlAnalysisException;
import com.example.xml_analyzer.model.AnalysisResponse;
import com.example.xml_analyzer.model.Details;

@Service
public class XmlAnalyzerService {
    private final RestTemplate restTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String FILE_PATH = "src/test/java/com/example/xml_analyzer/resources/3dprinting-posts.xml";

    public XmlAnalyzerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AnalysisResponse analyzeXml(String url) {
        AnalysisResponse response = new AnalysisResponse();
        response.setAnalyseDate(LocalDateTime.now());
        response.setFileName(extractFileName(url));

        Details details = new Details();
        TimingMetrics timing = new TimingMetrics();

        try {
            timing.startDownload();

            /*
             * For local testing
             * try (InputStream stream = new BufferedInputStream(
             * new FileInputStream(FILE_PATH),
             * 64 * 1024)) {
             */
            try (InputStream stream = new BufferedInputStream(
                    new ByteArrayInputStream(restTemplate.getForObject(url, byte[].class)),
                    64 * 1024)) {

                /*
                 * First approach: XMLStreamReader
                 * XMLStreamReader not slower than SAXParser for bigger files, but whatever
                 * XMLStreamReader reader = createXmlReader(stream);
                 * parseXmlContent(reader, details);
                 */

                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                SAXParser saxParser = factory.newSAXParser();
                PostHandler handler = new PostHandler(details);
                saxParser.parse(new InputSource(stream), handler);
                timing.endDownload();
            }

            timing.logTimings();
            response.setDetails(details);
            return response;

        } catch (Exception e) {
            throw new XmlAnalysisException("Error analyzing XML file: " + e.getMessage(), e);
        }
    }

    private class PostHandler extends DefaultHandler {
        private final Details details;
        private int totalPosts = 0;
        private int totalAcceptedPosts = 0;
        private long scoreSum = 0;
        private LocalDateTime firstPostDate = null;
        private LocalDateTime lastPostDate = null;

        public PostHandler(Details details) {
            this.details = details;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("row".equals(qName)) {
                totalPosts++;

                if (attributes.getValue("AcceptedAnswerId") != null) {
                    totalAcceptedPosts++;
                }

                scoreSum += Optional.ofNullable(attributes.getValue("Score"))
                        .map(Integer::parseInt)
                        .orElse(0);

                LocalDateTime postDate = parseDateTime(attributes.getValue("CreationDate")).orElse(null);
                if (firstPostDate == null) {
                    firstPostDate = postDate;
                }
                if (postDate != null) {
                    lastPostDate = postDate;
                }
            }
        }

        @Override
        public void endDocument() {
            details.setTotalPosts(totalPosts);
            details.setTotalAcceptedPosts(totalAcceptedPosts);
            details.setFirstPostDate(firstPostDate);
            details.setLastPostDate(lastPostDate);
            details.setAvgScore(totalPosts > 0 ? scoreSum / totalPosts : 0);
        }
    }

    private static class TimingMetrics {
        private long startTime;
        private long totalTime;

        void startDownload() {
            startTime = System.currentTimeMillis();
        }

        void endDownload() {
            totalTime = System.currentTimeMillis() - startTime;
        }

        void logTimings() {
            System.out.println("Total processing time: " + totalTime + "ms");
        }

    }

    private String extractFileName(String url) {
        return Optional.of(url)
                .filter(u -> u.contains("/"))
                .map(u -> u.substring(u.lastIndexOf('/') + 1))
                .orElse(url);
    }

    private Optional<LocalDateTime> parseDateTime(String str) {
        try {
            return Optional.ofNullable(str)
                    .map(s -> LocalDateTime.parse(s, DATE_FORMATTER));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
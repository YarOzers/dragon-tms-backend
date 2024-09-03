package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import com.example.dragontmsbackend.repository.TestCaseResultRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TestCaseResultService {

    private final TestCaseResultRepository testCaseResultRepository;
    private final TestCaseRepository testCaseRepository;

    public TestCaseResultService(TestCaseResultRepository testCaseResultRepository, TestCaseRepository testCaseRepository) {
        this.testCaseResultRepository = testCaseResultRepository;
        this.testCaseRepository = testCaseRepository;
    }


    public List<TestCaseResult> extractTestResults(String xmlData) throws Exception {
        List<TestCaseResult> testCaseResults = new ArrayList<>();

        // Парсинг XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

        // Извлечение всех элементов <testcase>
        NodeList testCaseNodes = document.getElementsByTagName("testcase");

        for (int i = 0; i < testCaseNodes.getLength(); i++) {
            Element testCaseElement = (Element) testCaseNodes.item(i);

            String testCaseId = testCaseElement.getAttribute("name");
            String className = testCaseElement.getAttribute("classname");
            String time = testCaseElement.getAttribute("time");

            NodeList failureNodes = testCaseElement.getElementsByTagName("failure");
            boolean isFailed = failureNodes.getLength() > 0;

            TestCaseResult result = new TestCaseResult(testCaseId, className, time, isFailed);
            testCaseResults.add(result);
        }

        return testCaseResults;
    }


}

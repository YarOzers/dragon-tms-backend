package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.model.testcase.TestCaseData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TestCaseExportService {
    public byte[] exportTestCasesToXlsx(List<TestCase> testCases) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Test Cases");

            // Создаем заголовки столбцов
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Type");
            headerRow.createCell(3).setCellValue("Automation Flag");
            headerRow.createCell(4).setCellValue("Folder");
            headerRow.createCell(5).setCellValue("Pre_condition");
            headerRow.createCell(6).setCellValue("Steps");
            headerRow.createCell(7).setCellValue("Post_condition");
            headerRow.createCell(8).setCellValue("Priority");


            // Заполняем строки с данными
            int rowIdx = 1;
            for (TestCase testCase : testCases) {
                TestCaseData data = testCase.getData().get(testCase.getData().size() - 1);
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(testCase.getId());
                row.createCell(1).setCellValue(testCase.getName());
                row.createCell(2).setCellValue(testCase.getType().toString());
                row.createCell(3).setCellValue(testCase.getAutomationFlag().toString());
                row.createCell(4).setCellValue(testCase.getFolder() != null ? testCase.getFolder().getName() : "");

                // Формируем строки для предусловий, шагов и постусловий
                String preConditionsText = cleanText(IntStream.range(0, data.getPreConditions().size())
                        .mapToObj(i -> {
                            var pc = data.getPreConditions().get(i);
                            return "STEP " + (i + 1) + ": ACTION: " + pc.getAction() + " - EXPECTED: " + pc.getExpectedResult();
                        })
                        .collect(Collectors.joining("\n")));
                row.createCell(5).setCellValue(preConditionsText);

                String stepsText = cleanText(IntStream.range(0,data.getSteps().size())
                        .mapToObj(i -> {
                            var step = data.getSteps().get(i);
                                return "STEP " + (i + 1) + ": ACTION: " + step.getAction() + " - EXPECTED: " + step.getExpectedResult();
                        })
                        .collect(Collectors.joining("\n")));
                row.createCell(6).setCellValue(stepsText);

                String postConditionsText = cleanText(IntStream.range(0, data.getPostConditions().size())
                        .mapToObj(i -> {
                            var pc = data.getPostConditions().get(i);
                            return "STEP " + (i + 1) + ": ACTION: " + pc.getAction() + " - EXPECTED: " + pc.getExpectedResult();
                        })
                        .collect(Collectors.joining("\n")));
                row.createCell(7).setCellValue(postConditionsText);
                row.createCell(8).setCellValue(data.getPriority().toString());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Метод для удаления XML-тегов и изображений в формате Base64
    private String cleanText(String text) {
        // Удаляем XML-теги
        text = text.replaceAll("<[^>]*>", "");
        text = text.replaceAll("&nbsp;", "");

        // Удаляем содержимое изображений в формате Base64 (например, <img src="data:image/png;base64,....">)
        text = text.replaceAll("data:image/[^;]+;base64,[a-zA-Z0-9+/=]+", "[IMAGE REMOVED]");

        return text;
    }
}

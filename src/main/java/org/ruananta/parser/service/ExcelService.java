package org.ruananta.parser.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public void writeToExcel(List<String[]> data, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Parsed Data");

        int rowCount = 0;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (String cellData : rowData) {
                row.createCell(columnCount++).setCellValue(cellData);
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }
}


package com.te.timex.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.te.timex.model.ExpenseList;
import com.te.timex.model.Timesheet;

public class TimeExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Timesheet> timesheet;

	// @Value("${expenseReport_path}")
	// private String path;

//	    expenseReport_path
	public TimeExcelExporter(List<Timesheet> timesheet) {
		this.timesheet = timesheet;
		workbook = new XSSFWorkbook();
	}

	public void export(HttpServletResponse response) throws IOException {

		writeHeaderLine();
		writeDataLines();

		String path = "C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\time_report";
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
         
    //    String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		String fileName = "Time Report_"+ currentDateTime + ".xls";
		
		//File.Exists(@"C:\Users\Me\Desktop\JAM_MACHINE\JAMS\record.txt"
		
		File xlsFile = new File(path + "\\" + fileName); // 저장경로 설정
		//Files.deleteIfExists(file.toPath()); 
		if(xlsFile.exists()) {
			xlsFile.delete();
		}
		FileOutputStream fos = new FileOutputStream(xlsFile);
		workbook.write(fos);
		fos.close();
	//	downloadFile(xlsFile);
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Time");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Date", style);
		createCell(row, 1, "Project", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Timesheet ts : timesheet) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			
			createCell(row, columnCount++, ts.getMon(), style);
			createCell(row, columnCount++, ts.getTue(), style);
		//	createCell(row, columnCount++, el.getExpense().getName(), style);
		

		}
	}

}
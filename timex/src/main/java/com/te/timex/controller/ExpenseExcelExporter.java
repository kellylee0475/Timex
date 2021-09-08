
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

public class ExpenseExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<ExpenseList> expenseList;


	public ExpenseExcelExporter(List<ExpenseList> expenseList) {
		this.expenseList = expenseList;
		workbook = new XSSFWorkbook();
	}

	public void export(HttpServletResponse response) throws IOException {

		writeHeaderLine();
		writeDataLines();

		//Change2
		String path = "C:\\Users\\Administrator\\Desktop\\Timex\\expense_report";
		//String path = "C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\expense_report";
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
 
		String fileName = "Expense Report_"+ currentDateTime + ".xls";
		
		File xlsFile = new File(path + "\\" + fileName); // save path

		if(xlsFile.exists()) {
			xlsFile.delete();
		}
		FileOutputStream fos = new FileOutputStream(xlsFile);
		workbook.write(fos);
		fos.close();

	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Expenses");
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Date", style);
		createCell(row, 1, "Project", style);
		createCell(row, 2, "Category", style);
		createCell(row, 3, "Unit Cost", style);
		createCell(row, 4, "Qty", style);
		createCell(row, 5, "Total Cost", style);
		createCell(row, 6, "Status", style);

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

		for (ExpenseList el : expenseList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			
			createCell(row, columnCount++, el.getDate().toString(), style);
			createCell(row, columnCount++, el.getProject().getNumber()+" "+el.getProject().getTitle(), style);
			createCell(row, columnCount++, el.getExpense().getName(), style);
			createCell(row, columnCount++, el.getExpense().getUnitcost(), style);
			createCell(row, columnCount++, el.getQty(), style);
			createCell(row, columnCount++, el.getTotalcost(), style);
			if(el.getStatus()==1) {
				createCell(row, columnCount++, "Approved", style);
			}else if(el.getStatus()==2) {
				createCell(row, columnCount++, "Pending", style);
			}else {
				createCell(row, columnCount++, "Rejected", style);
			}
		}
	}

}
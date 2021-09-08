
package com.te.timex.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.te.timex.model.Expense;
import com.te.timex.model.ExpenseList;
import com.te.timex.repository.ExpenseListRepository;
import com.te.timex.repository.ExpenseRepository;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;
import com.te.timex.service.ExpenseListService;
import com.te.timex.service.ReportService;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {

	@Value("${upload_path}")
	private String upload_path;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private ExpenseListRepository expenseListRepository;
	
	int user_id;

	@GetMapping
	public String index(Model model, Authentication authentication) {
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
		List<ExpenseList> expenselist = expenseListRepository.findByUserId(user_id);
		model.addAttribute("expenselist", expenselist);
		return "expenses/expenses";
	}

	@GetMapping("/openFile")
	public ResponseEntity<InputStreamResource> getTermsConditions(@RequestParam(value = "param1") String path,
			@RequestParam(value = "param2") String name) throws IOException {

		String filePath = path;
		String fileName = name;
		File file = new File(filePath + "\\" + fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "inline;filename=" + fileName);
		Path source = Paths.get(filePath + "\\" + fileName);

		String fileType = Files.probeContentType(source);
		System.out.println(fileType);

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType(fileType)).body(resource);
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("upload_receipt") MultipartFile file, RedirectAttributes attributes,
			@ModelAttribute("expenseinfo") ExpenseList expenselist) {

		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}
		
		// normalize the file path
		String fileName = file.getOriginalFilename();
		
		//Change3
		String upload_path = "C:\\Users\\Administrator\\Desktop\\Timex\\expense_receipt";
		//String upload_path = "C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\expense_receipt";
		
		// save the file on the local file system
		try {
			Path path = Paths.get(upload_path + fileName);		
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/";
	}

	@GetMapping("/selectExpense")
	public ResponseEntity<List<Expense>> selectCategory() {

		List<Expense> expenses = expenseRepository.findAll();
		if (expenses.isEmpty()) {
			System.out.println("empty");
		}
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}

	@GetMapping("/selectExpenseDetail")
	public ResponseEntity selectExpenseDetail(int expense_id) {
		Optional<Expense> expenseDetail = expenseRepository.findById(expense_id);
		return new ResponseEntity(expenseDetail, HttpStatus.OK);
	}
     
    
	@PostMapping("/exportExpenses")
	public void exportExpenses(@RequestBody HashMap<String, Object> param,
			HttpServletResponse response) throws IOException {

        List<ExpenseList> expensereport = reportService.getExpenseList(param);
        ExpenseExcelExporter excelExporter = new ExpenseExcelExporter(expensereport);
        
        excelExporter.export(response);    
	}
	
	@GetMapping("/downloadReport")
	public void downloadZip(HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		//Change4
		String path = "C:\\Users\\Administrator\\Desktop\\Timex\\expense_report";
		//String path = "C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\expense_report";
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());         
		String fileName = "Expense Report_"+ currentDateTime + ".xls";

		File file = new File(path+"\\"+fileName);
		if (file.exists() && file.isFile()) {
			FileInputStream inputstream = new FileInputStream(file);

			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=Expense Report.xls");
			response.setHeader("Content-Type", "application/xls");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setStatus(HttpServletResponse.SC_OK);

			OutputStream out = response.getOutputStream();

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				FileCopyUtils.copy(fis, out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null)
					fis.close();
				out.flush();
				out.close();
			}

		}
	}

	@PostMapping("/saveExpenses")
	@ResponseBody
	public String saveExpenses(@RequestParam("upload_receipt") MultipartFile uploadfile, String date2, String user_id2,
			String project_id2, String expense_id2, String qty2, String total_amount2) throws Exception {
	
		ExpenseList expenseList = new ExpenseList();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = simpleDateFormat.parse(date2);
			expenseList.setDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int expense_id = Integer.parseInt(expense_id2);
		int project_id = Integer.parseInt(project_id2);
		user_id = Integer.parseInt(user_id2);
		int qty = Integer.parseInt((String) qty2);
		String total_amount = (String) total_amount2;

		if (uploadfile.isEmpty()) {
		} else {
			// 업로드된 실행파일이 저장될 위치
			String path = upload_path;
			String folder_name = "receipt_userId" + user_id;
			String file_path = path + "\\" + folder_name;
			String file_name = user_id + "_" + date2 + "_" + uploadfile.getOriginalFilename();

			// create folder
			File folder = new File(path, folder_name);
	
			if (!folder.exists()) { // create folder if not exist
				folder.mkdir();
			}

			File file = new File(file_path, file_name);
			if (file.exists()) {// if file already exist, delete	
				file.delete();
			}
			expenseList.setFilename(file_name);
			expenseList.setFilepath(file_path);

			// 임시디렉토리에 업로드된 파일을 지정된 디렉토리로 복사
			try {
				FileCopyUtils.copy(uploadfile.getBytes(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		expenseList.setExpenseId(expense_id);
		expenseList.setProjectId(project_id);
		expenseList.setUserId(user_id);
		expenseList.setStatus(2);
		expenseList.setQty(qty);
		expenseList.setTotalcost(total_amount);

		ExpenseList expenseListId = expenseListRepository.save(expenseList);

		String message = "hi";

		return message;

	}


}
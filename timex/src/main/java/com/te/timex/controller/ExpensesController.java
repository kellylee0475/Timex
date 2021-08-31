
package com.te.timex.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
import com.te.timex.model.Week;
import com.te.timex.repository.ExpenseListRepository;
import com.te.timex.repository.ExpenseRepository;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;
import com.te.timex.service.ExpenseListService;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {

	@Value("${upload_path}")
	private String upload_path;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private ExpenseListRepository expenseListRepository;

	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private ExpenseListService expenseListService;

	int user_id;

	@GetMapping
	public String index(Model model, Authentication authentication) {
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
		// List<ExpenseList> expenselist = expenseListService.findByUserId(user_id);

		// @Query("SELECT p from POSTS p inner join p.user u where u.username=?1")
		// System.out.println("***************1");
		// System.out.println(user_id);

		List<ExpenseList> expenselist = expenseListRepository.findByUserId(user_id);
		// System.out.println("***************2");

		// System.out.println(expenselist.toString());
		// System.out.println("***************3");
		model.addAttribute("expenselist", expenselist);
		// System.out.println("***************!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// System.out.println(expenselist.toString());

		// Query query =
		// em.createQuery("SELECT m.userName, m.age FROM Member m");//(2)

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
		System.out.println();
		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}
		// normalize the file path
		String fileName = file.getOriginalFilename();
		String upload_path = "C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\expense_receipt";
		System.out.println("fileName = " + fileName);
		System.out.println("upload_path = " + upload_path);
		// save the file on the local file system
		try {

			Path path = Paths.get(upload_path + fileName);
			System.out.println("here + " + path);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("here2");
			e.printStackTrace();
		}

		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/";
	}

	@GetMapping("/selectExpense")
	public ResponseEntity<List<Expense>> selectCategory() {
		// System.out.println("selectExpense");
		List<Expense> expenses = expenseRepository.findAll();
		if (expenses.isEmpty()) {
			System.out.println("empty");
		}
		// System.out.println("here!!!!!!!!");
		// System.out.println(expenses.toString());
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}

	@GetMapping("/selectExpenseDetail")
	public ResponseEntity selectExpenseDetail(int expense_id) {
		// System.out.println(expense_id);
		Optional<Expense> expenseDetail = expenseRepository.findById(expense_id);

		// System.out.println(expenseDetail.toString());
		return new ResponseEntity(expenseDetail, HttpStatus.OK);
	}

	@PostMapping("/exportExpenses")
	@ResponseBody
	public String exportExpenses(@RequestBody HashMap<String, Object> param) {
		if (param.get("project_id").toString().equals("allproject")) {
			int project_id = 0;
		} else {
			int project_id = Integer.parseInt((String) param.get("project_id"));
		}

		if (param.get("expense_id").toString().equals("allcategory")) {
			int expense_id = 0;
		} else {
			int expense_id = Integer.parseInt((String) param.get("expense_id"));
		}

		String timeframe = param.get("date").toString();
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String startDay,endDay;
		String[] date = today.split("-");
		String year = date[0];
		String month = date[1];
	//	int lastmonth = Integer.parseInt(month)-1;
		String lastmonth = String.valueOf(Integer.parseInt(month)-1);
		String lastyear = String.valueOf(Integer.parseInt(year)-1);
		String day = date[2];
		int userId = user_id;
	
		
		switch (timeframe) {
//		case "thisweek":
//			
//			break;
//		case "lastweek":
//			break;
		case "thismonth":
			startDay = year+"-"+month+"-01";
		    endDay = year+"-"+month+"-31";
		    System.out.println("startDay = "+startDay+" endDay = "+endDay);
			break;
		case "lastmonth":
		  startDay = year+"-"+lastmonth+"-01";
	      endDay = year+"-"+lastmonth+"-31";
		    System.out.println("startDay = "+startDay+" endDay = "+endDay);

			break;
		case "thisyear":
		  startDay = year+"-01-01";
	      endDay = year+"-12-31";
		    System.out.println("startDay = "+startDay+" endDay = "+endDay);

			break;
		case "lastyear":
		  startDay = lastyear+"-01-01";
	      endDay = lastyear+"-12-31";
		    System.out.println("startDay = "+startDay+" endDay = "+endDay);

			break;
		case "alltime":
			startDay = "0000-01-01";
		     endDay = today;
		    System.out.println("startDay = "+startDay+" endDay = "+endDay);

		 
			break;
		case "custom":
			
			startDay = (String) param.get("start_date");
		     endDay = (String) param.get("end_date");
		    System.out.println("startDay = "+startDay+" endDay = "+endDay);
			break;
		default:
			// code block
		}

		//ArrayList expenseReport = expenseListRepository.findByUserId
		
		
		return upload_path;
	}

	@PostMapping("/saveExpenses")
	@ResponseBody
	public String saveExpenses(@RequestParam("upload_receipt") MultipartFile uploadfile, String date2, String user_id2,
			String project_id2, String expense_id2, String qty2, String total_amount2) throws Exception {
		// System.out.println("saveExpenses");

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
			System.out.println("is empty!!!!!!!!!!!!!1");
		} else {
			// 업로드된 실행파일이 저장될 위치
			String path = upload_path;
			String folder_name = "receipt_userId" + user_id;
			String file_path = path + "\\" + folder_name;
			String file_name = user_id + "_" + date2 + "_" + uploadfile.getOriginalFilename();

			// 폴더생성
			File folder = new File(path, folder_name);
			System.out.println("folder = " + folder);
			System.out.println("path = " + path);
			System.out.println("file_path = " + file_path);
			System.out.println("file_name = " + file_name);
			if (!folder.exists()) { // 폴더없을경우 폴더생성
				System.out.println("폴더없음");
				folder.mkdir();
			}

			File file = new File(file_path, file_name);
			if (file.exists()) {// 파일 이미존재할경우 삭제
				System.out.println("파일있음");
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

//		String a = Integer.toString(expenseListId.getId());

		String message = "hi";

		return message;

	}

//	@PostMapping("/saveExpenses")
//	@ResponseBody
//	public String saveExpenses(@RequestBody HashMap<String, Object> param,Authentication authentication){
//		System.out.println("saveExpenses");
//		System.out.println(param);		
//	
//	//	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
//	//	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//	//	System.out.println(userDetails.getUsername());
//	
//		ExpenseList expenseList = new ExpenseList();
//		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//		Date date;
//		try {
//			date = simpleDateFormat.parse((String) param.get("date"));
//			expenseList.setDate(date);
//		} catch (ParseException e) {		
//			e.printStackTrace();
//		}
//		
//		int expense_id=Integer.parseInt((String) param.get("expense_id"));
//
//		int project_id=Integer.parseInt((String) param.get("project_id"));
//
//		user_id=(int) param.get("user_id");
//		int qty = Integer.parseInt((String) param.get("qty"));
//		String total_amount = (String)param.get("total_amount");
//		expenseList.setExpenseId(expense_id);
//		expenseList.setProjectId(project_id);
//		expenseList.setUserId(user_id);
//		expenseList.setStatus(2);
//		expenseList.setQty(qty);
//		expenseList.setTotal_amount(total_amount);
//
//
//		ExpenseList expenseListId = expenseListRepository.save(expenseList);
//		String a = Integer.toString(expenseListId.getId());
//		
//		String message = a;
//        
//        return message;
//		
//		
//	}

}
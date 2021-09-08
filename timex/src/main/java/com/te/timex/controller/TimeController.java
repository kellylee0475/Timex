
package com.te.timex.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.te.timex.model.Project;
import com.te.timex.model.ProjectTask;
import com.te.timex.model.Timesheet;
import com.te.timex.model.Week;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.ProjectTaskRepository;
import com.te.timex.repository.TimesheetRepository;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;
import com.te.timex.service.ReportService;

@Controller
@RequestMapping("/time")
public class TimeController {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WeekRepository weekRepository;

	@Autowired
	private ReportService reportService;

	int user_id;
	int currentWeekId;
	Week currentWeek;

	@GetMapping()
	public String index(Authentication authentication, Model model,
			@RequestParam(required = false, value = "weekId") Optional<String> weekId, // time.html에서 action thyemeleaf로
																						// 가져오는값
			@RequestParam(name = "pickedDate", required = false, defaultValue = "2021") String pickedDate) {// ajax에서
																											// 가져오는값

		// 1. get User Information
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
		int week_number = 0;
		int year = 0;
		
		// 2.get WeekId when you click previous or next in time.html page
		if (weekId.isPresent()) {
			int previousWeekId = Integer.parseInt(weekId.get());
			currentWeek = weekRepository.findById(previousWeekId);
			currentWeekId = currentWeek.getId();

		} else {// current

			if (!pickedDate.equals("2021")) {
				ArrayList a = Common.getWeekNumber2(pickedDate);		
				week_number = (int) a.get(1);
				year = (int) a.get(0);
			} else {
				// 2.current year
				year = Calendar.getInstance().get(Calendar.YEAR);// this year
				// 3. update the table with current year
				HashMap weeklist = common.weekList(year);
				for (int i = 1; i < weeklist.size(); i++) {
					if (weekRepository.findByYearAndWeekNumber(2021, i).equals(null)) {// 존재안할경우 insert
						Week week = new Week();
						week.setYear(2021);
						week.setWeekNumber(i);
						week.setPeriod((String) weeklist.get(i));
						weekRepository.save(week);
					}				
				}

				// 4. get Week Number of today from common.java
				week_number = common.getWeekNumber();
			}
			// 5. get weekId from week table with param year, week_number
			currentWeek = weekRepository.findByYearAndWeekNumber(year, week_number);	
			currentWeekId = currentWeek.getId();
		}

		// 6. get data from timesheet table with param currentWeekID and userId
		ArrayList<Timesheet> currentWeekList = timesheetRepository.findByUserIdAndWeekId(user_id, currentWeekId);

		model.addAttribute("user_id", user_id);
		model.addAttribute("currentWeek", currentWeek);
		model.addAttribute("currentWeekId", currentWeekId);
		model.addAttribute("currentWeekList", currentWeekList);
		
		String totalSun = "", totalMon = "", totalTue = "", totalWed = "", totalThur = "", totalFri = "", totalSat = "";
		int SunHH = 0, MonHH = 0, TueHH = 0, WedHH = 0, ThurHH = 0, FriHH = 0, SatHH = 0, SunMM = 0, MonMM = 0,
				TueMM = 0, WedMM = 0, ThurMM = 0, FriMM = 0, SatMM = 0;
		int TotalHH = 0, TotalMM = 0;
		for (int i = 0; i < currentWeekList.size(); i++) {
			if (currentWeekList.get(i).getSun().contains(":")) {
				SunHH = SunHH + Integer.parseInt((String) (currentWeekList.get(i).getSun().split(":")[0]));
				SunMM = SunMM + Integer.parseInt((String) (currentWeekList.get(i).getSun().split(":")[1]));
			}
			if (currentWeekList.get(i).getMon().contains(":")) {
				MonHH = MonHH + Integer.parseInt((String) (currentWeekList.get(i).getMon().split(":")[0]));
				MonMM = MonMM + Integer.parseInt((String) (currentWeekList.get(i).getMon().split(":")[1]));
			}
			if (currentWeekList.get(i).getTue().contains(":")) {
				TueHH = TueHH + Integer.parseInt((String) (currentWeekList.get(i).getTue().split(":")[0]));
				TueMM = TueMM + Integer.parseInt((String) (currentWeekList.get(i).getTue().split(":")[1]));
			}
			if (currentWeekList.get(i).getWed().contains(":")) {
				WedHH = WedHH + Integer.parseInt((String) (currentWeekList.get(i).getWed().split(":")[0]));
				WedMM = WedMM + Integer.parseInt((String) (currentWeekList.get(i).getWed().split(":")[1]));
			}
			if (currentWeekList.get(i).getThur().contains(":")) {
				ThurHH = ThurHH + Integer.parseInt((String) (currentWeekList.get(i).getThur().split(":")[0]));
				ThurMM = ThurMM + Integer.parseInt((String) (currentWeekList.get(i).getThur().split(":")[1]));
			}
			if (currentWeekList.get(i).getFri().contains(":")) {
				FriHH = FriHH + Integer.parseInt((String) (currentWeekList.get(i).getFri().split(":")[0]));
				FriMM = FriMM + Integer.parseInt((String) (currentWeekList.get(i).getFri().split(":")[1]));
			}
			if (currentWeekList.get(i).getSat().contains(":")) {
				SatHH = SatHH + Integer.parseInt((String) (currentWeekList.get(i).getSat().split(":")[0]));
				SatMM = SatMM + Integer.parseInt((String) (currentWeekList.get(i).getSat().split(":")[1]));
			}
		}

		while (SunMM >= 60) {
			SunHH++;
			SunMM = SunMM - 60;
		}
		while (MonMM >= 60) {
			MonHH++;
			MonMM = MonMM - 60;
		}
		while (TueMM >= 60) {
			TueHH++;
			TueMM = TueMM - 60;
		}
		while (WedMM >= 60) {
			WedHH++;
			WedMM = WedMM - 60;
		}
		while (ThurMM >= 60) {
			ThurHH++;
			ThurMM = ThurMM - 60;
		}
		while (FriMM >= 60) {
			FriHH++;
			FriMM = FriMM - 60;
		}
		while (SatMM >= 60) {
			SatHH++;
			SatMM = SatMM - 60;
		}
		totalSun = String.valueOf(SunHH) + ":" + String.valueOf(SunMM);
		totalMon = String.valueOf(MonHH) + ":" + String.valueOf(MonMM);
		totalTue = String.valueOf(TueHH) + ":" + String.valueOf(TueMM);
		totalWed = String.valueOf(WedHH) + ":" + String.valueOf(WedMM);
		totalThur = String.valueOf(ThurHH) + ":" + String.valueOf(ThurMM);
		totalFri = String.valueOf(FriHH) + ":" + String.valueOf(FriMM);
		totalSat = String.valueOf(SatHH) + ":" + String.valueOf(SatMM);
	
		if (totalSun.contains(":") && String.valueOf(SunMM).equals("0")) {
			totalSun = String.valueOf(SunHH) + ":00";
		}
		if (totalMon.contains(":") && String.valueOf(MonMM).equals("0")) {		
			totalMon = String.valueOf(MonHH) + ":00";
		}
		if (totalTue.contains(":") && String.valueOf(TueMM).equals("0")) {
			totalTue = String.valueOf(TueHH) + ":00";
		}
		if (totalWed.contains(":") && String.valueOf(WedMM).equals("0")) {
			totalWed = String.valueOf(WedHH) + ":00";
		}
		if (totalThur.contains(":") && String.valueOf(ThurMM).equals("0")) {
			totalThur = String.valueOf(ThurHH) + ":00";
		}
		if (totalFri.contains(":") && String.valueOf(FriMM).equals("0")) {
			totalFri = String.valueOf(FriHH) + ":00";
		}
		if (totalSat.contains(":") && String.valueOf(SatMM).equals("0")) {
			totalSat = String.valueOf(SatHH) + ":00";
		}
		TotalHH = SunHH + MonHH + TueHH + WedHH + ThurHH + FriHH + SatHH;
		TotalMM = SunMM + MonMM + TueMM + WedMM + ThurMM + FriMM + SatMM;
		String totalHr = String.valueOf(TotalHH) + ":" + String.valueOf(TotalMM);
		
		while (TotalMM >= 60) {
			TotalHH++;
			TotalMM = TotalMM - 60;
		}
		if (totalHr.contains(":") && String.valueOf(TotalMM).equals("0")) {
			totalHr = String.valueOf(TotalHH) + ":00";
		}
				
		// 7.Total Hours
		Map<String, String> totalHrs = new HashMap<String, String>();

		totalHrs.put("totalSun", totalSun);
		totalHrs.put("totalMon", totalMon);
		totalHrs.put("totalTue", totalTue);
		totalHrs.put("totalWed", totalWed);
		totalHrs.put("totalThur", totalThur);
		totalHrs.put("totalFri", totalFri);
		totalHrs.put("totalSat", totalSat);
		totalHrs.put("total", totalHr);

		model.addAttribute("totalHours", totalHrs);
		return "time/time";
	}

	@PostMapping("/exportTime")
	public void exportTime(@RequestBody HashMap<String, Object> param, HttpServletResponse response)
			throws IOException {

		List<Timesheet> timesheet = reportService.getTimeList(param);
		TimeExcelExporter excelExporter = new TimeExcelExporter(timesheet);

		excelExporter.export(response);
	}

	@GetMapping("/downloadReport")
	public void downloadZip(HttpServletResponse response, HttpServletRequest request) throws IOException {
		//Change5
		//String path = "C:\\Users\\Administrator\\Desktop\\Timex\\time_report";
		String path = "C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\time_report";
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateTime = dateFormatter.format(new Date());

		String fileName = "Time Report_" + currentDateTime + ".xls";
		File file = new File(path + "\\" + fileName);
		if (file.exists() && file.isFile()) {
			FileInputStream inputstream = new FileInputStream(file);

			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=Time Report.xls");
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

	@GetMapping("/setDate")
	public String setDate(RedirectAttributes redirectAttributes,
			@RequestParam(name = "pickedDate", required = false, defaultValue = "2021") String pickedDate) {

		redirectAttributes.addAttribute("pickedDate", pickedDate);
		return "redirect:/time";
	}

	@GetMapping("/addProject")
	public String modal1() {
		return "time/addProject";
	}

	@GetMapping("/selectProject")
	public ResponseEntity<List<Project>> selectProject() {

		List<Project> projects = projectRepository.findAll();

		if (projects.isEmpty()) {
			System.out.println("empty");
		}
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	@GetMapping("/selectTask")
	public ResponseEntity<List<ProjectTask>> selectTask(int project_id) {

		List<ProjectTask> protask = projectTaskRepository.findByProjectId(project_id);
		if (protask.isEmpty()) {
			System.out.println("empty");
		}
		return new ResponseEntity<List<ProjectTask>>(protask, HttpStatus.OK);
	}

	@PostMapping("/deleteProjectTask")
	@ResponseBody
	public String deleteProjectTask(@RequestBody HashMap<String, Object> param) {
		// delete data with the param timesheetId from time.html page
		int timesheetId = Integer.parseInt((String) param.get("timesheetId"));
		timesheetRepository.deleteById(timesheetId);

		String message = "Yes";
		return message;

	}

	@PostMapping("/editProjectTask")
	@ResponseBody
	public String editProjectTask(@RequestBody HashMap<String, Object> param) {

		// 1. get all information from time.html
		int timesheetId = Integer.parseInt((String) param.get("timesheetId"));
		int user_id = Integer.parseInt((String) param.get("user_id"));
		int project_task_id = Integer.parseInt((String) param.get("project_task_id"));
		int week_id = Integer.parseInt((String) param.get("week_id"));

		String sun = (String) param.get("sun");
		String mon = (String) param.get("mon");
		String tue = (String) param.get("tue");
		String wed = (String) param.get("wed");
		String thur = (String) param.get("thur");
		String fri = (String) param.get("fri");
		String sat = (String) param.get("sat");

		// 2. timesheet model에 저장
		Timesheet timesheet = new Timesheet();
		timesheet.setId(timesheetId);
		timesheet.setUserId(user_id);
		timesheet.setWeekId(week_id);
		timesheet.setProjecttaskId(project_task_id);
		timesheet.setSun(sun);
		timesheet.setMon(mon);
		timesheet.setTue(tue);
		timesheet.setWed(wed);
		timesheet.setThur(thur);
		timesheet.setFri(fri);
		timesheet.setSat(sat);

		// 3.save
		timesheetRepository.save(timesheet);

		String message = "Yes";
		return message;
	}

	@PostMapping("/saveProjectTask")
	@ResponseBody
	public Timesheet saveProjectTask(@RequestBody HashMap<String, Object> param) {

		Timesheet timesheet = new Timesheet();		

		// 1. time.html에서 넘겨온 파라미터 user_id, project_task_id, currentWeekId로 이미 존재하는지 체크
		int project_task_id = Integer.parseInt((String) param.get("project_task_id"));
		int user_id = Integer.parseInt((String) param.get("user_id"));
		int week_id = Integer.parseInt((String) param.get("currentWeekId"));

		// 2. timesheet테이블에가서 체크
		boolean exist = timesheetRepository.existsByUserIdAndWeekIdAndProjecttaskId(user_id, week_id, project_task_id);

		// 3. 존재하지않을경우 timesheet에 받은 파라미터 넣어서 save
		if (exist == false) {		
			timesheet.setUserId(user_id);
			timesheet.setProjecttaskId(project_task_id);
			timesheet.setWeekId(week_id);
			timesheet.setSun("");
			timesheet.setMon("");
			timesheet.setTue("");
			timesheet.setWed("");
			timesheet.setThur("");
			timesheet.setFri("");
			timesheet.setSat("");
			timesheet = timesheetRepository.save(timesheet);
			String timesheetId = String.valueOf(timesheet.getId());
			int id = Integer.parseInt(timesheetId);
			ProjectTask projecttask = projectTaskRepository.findById(project_task_id);
			System.out.println(projecttask.toString());
			timesheet.setProjecttask(projecttask);			
			timesheet = timesheetRepository.findById(id);			
		} else {
			timesheet = null;
		}
		return timesheet;
	}

}
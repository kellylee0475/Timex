
package com.te.timex.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	int user_id;
	int currentWeekId;
	Week currentWeek;

	@GetMapping()
	public String index(Authentication authentication, Model model,
			@RequestParam(required = false, value = "weekId") Optional<String> weekId, // time.html에서 action thyemeleaf로
																						// 가져오는값
			@RequestParam(name="pickedDate", required = false, defaultValue = "2021") String pickedDate) {// ajax에서 가져오는값

		// 1. user정보가져오기
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
		int week_number = 0;
		int year=0;
		// 2.time.html에서 previous 또는 next 주 버튼클릭했을때 weekId를 가져온다
		if (weekId.isPresent()) {
			int previousWeekId = Integer.parseInt(weekId.get());
			currentWeek = weekRepository.findById(previousWeekId);
			currentWeekId = currentWeek.getId();

		} else {// 현재

		//	if (!pickedDate.isEmpty() || pickedDate != "" || pickedDate != null ) {
				if (!pickedDate.equals("2021")) {
				System.out.println("here is picked date");
				System.out.println(pickedDate);
				// LocalDate today = LocalDate.now();
				ArrayList a = Common.getWeekNumber2(pickedDate);
			//	System.out.println(a.get(0));
				week_number=(int) a.get(1);
				year=(int) a.get(0);
			} else {
				// 2.현재 year가져오기
				year = Calendar.getInstance().get(Calendar.YEAR);// this year
				// 3. 현재 year로 week 테이블 업데이트(1년에 한번)
				HashMap weeklist = common.weekList(year);
				for (int i = 1; i < weeklist.size(); i++) {
					if (weekRepository.findByYearAndWeekNumber(2021, i).equals(null)) {// 존재안할경우 insert
						Week week = new Week();
						week.setYear(2021);
						week.setWeekNumber(i);
						week.setPeriod((String) weeklist.get(i));
						weekRepository.save(week);
					}
					// System.out.println("already existing");
				}

				// 4. common.java에서 오늘 날짜에따른 week number가져오기
				week_number = common.getWeekNumber();
			}
			System.out.println("year = "+year +" week_number = "+week_number);
			// 5. week테이블에서 year, week number로 week id 가져오기
			currentWeek = weekRepository.findByYearAndWeekNumber(year, week_number);
			//System.out.println(currentWeek.toString());
			currentWeekId = currentWeek.getId();
			System.out.println(currentWeekId);
		}

		// 6. timesheet테이블에서 currentweek와 현재 user_id값을 만족하는 데이터가져오기
		ArrayList<Timesheet> currentWeekList = timesheetRepository.findByUserIdAndWeekId(user_id, currentWeekId);
System.out.println(currentWeekList);
		System.out.println(currentWeekList);
		model.addAttribute("user_id", user_id);
		model.addAttribute("currentWeek", currentWeek);
		model.addAttribute("currentWeekId", currentWeekId);
		model.addAttribute("currentWeekList", currentWeekList);
		String totalHH = "", totalMM = "";
		String totalSun = "", totalMon = "", totalTue = "", totalWed = "", totalThur = "", totalFri = "", totalSat = "";
		int SunHH = 0, MonHH = 0, TueHH = 0, WedHH = 0, ThurHH = 0, FriHH = 0, SatHH = 0, SunMM = 0, MonMM = 0,
				TueMM = 0, WedMM = 0, ThurMM = 0, FriMM = 0, SatMM = 0;
		int TotalHH = 0, TotalMM = 0;
		for (int i = 0; i < currentWeekList.size(); i++) {
			System.out.println(currentWeekList.get(i).getSun().substring(0, 2));
			SunHH = SunHH + Integer.parseInt((String) (currentWeekList.get(i).getSun().substring(0, 2)));
			MonHH = MonHH + Integer.parseInt((String) (currentWeekList.get(i).getMon().substring(0, 2)));
			TueHH = TueHH + Integer.parseInt((String) (currentWeekList.get(i).getTue().substring(0, 2)));
			WedHH = WedHH + Integer.parseInt((String) (currentWeekList.get(i).getWed().substring(0, 2)));
			ThurHH = ThurHH + Integer.parseInt((String) (currentWeekList.get(i).getThur().substring(0, 2)));
			FriHH = FriHH + Integer.parseInt((String) (currentWeekList.get(i).getFri().substring(0, 2)));
			SatHH = SatHH + Integer.parseInt((String) (currentWeekList.get(i).getSat().substring(0, 2)));
			SunMM = SunMM + Integer.parseInt((String) (currentWeekList.get(i).getSun().substring(3, 5)));
			MonMM = MonMM + Integer.parseInt((String) (currentWeekList.get(i).getMon().substring(3, 5)));
			TueMM = TueMM + Integer.parseInt((String) (currentWeekList.get(i).getTue().substring(3, 5)));
			WedMM = SunHH + Integer.parseInt((String) (currentWeekList.get(i).getWed().substring(3, 5)));
			ThurMM = ThurMM + Integer.parseInt((String) (currentWeekList.get(i).getThur().substring(3, 5)));
			FriMM = FriMM + Integer.parseInt((String) (currentWeekList.get(i).getFri().substring(3, 5)));
			SatMM = SatMM + Integer.parseInt((String) (currentWeekList.get(i).getSat().substring(3, 5)));

			// totalSun =totalSun+ currentWeekList.get(i).getSun();
			// totalMon =totalMon+ currentWeekList.get(i).getMon();
			// totalTue =totalTue+ currentWeekList.get(i).getTue();
			// totalWed =totalWed+ currentWeekList.get(i).getWed();
			// totalThur =totalThur+ currentWeekList.get(i).getThur();
			// totalFri =totalFri+ currentWeekList.get(i).getFri();
			// totalSat =totalSat+ currentWeekList.get(i).getSat();
		}

		totalSun = String.valueOf(SunHH) + ":" + String.valueOf(SunMM);
		totalMon = String.valueOf(MonHH) + ":" + String.valueOf(MonMM);
		totalTue = String.valueOf(TueHH) + ":" + String.valueOf(TueMM);
		totalWed = String.valueOf(WedHH) + ":" + String.valueOf(WedMM);
		totalThur = String.valueOf(ThurHH) + ":" + String.valueOf(ThurMM);
		totalFri = String.valueOf(FriHH) + ":" + String.valueOf(FriMM);
		totalSat = String.valueOf(SatHH) + ":" + String.valueOf(SatMM);

		TotalHH = SunHH + MonHH + TueHH + WedHH + ThurHH + FriHH + SatHH;
		TotalMM = SunMM + MonMM + TueMM + WedMM + ThurMM + FriMM + SatMM;
		String totalHr = String.valueOf(TotalHH) + ":" + String.valueOf(TotalMM);
		// String totalSun = String.valueOf(SunHH)+":"+String.valueOf(SunMM);

		// 7.총합
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
		System.out.println(totalHrs);
		System.out.println(currentWeek);
		System.out.println(currentWeekId);
		System.out.println(currentWeekList);
		
		return "time/time";
	}

	@GetMapping("/setDate")
	public String setDate(RedirectAttributes redirectAttributes,@RequestParam(name="pickedDate", required = false, defaultValue = "2021") String pickedDate) {
		System.out.println("setDate!!!!!!!!!!!!!!!!!!!!!!!!  "+pickedDate);
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

		if (projects.isEmpty()) {// 고객에 등록된 솔루션이 없을경우
			System.out.println("empty");
		}
		// System.out.println(projects);
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	@GetMapping("/selectTask")
	public ResponseEntity<List<ProjectTask>> selectTask(int project_id) {

		List<ProjectTask> protask = projectTaskRepository.findByProjectId(project_id);

		if (protask.isEmpty()) {// 고객에 등록된 솔루션이 없을경우
			System.out.println("empty");
		}

		return new ResponseEntity<List<ProjectTask>>(protask, HttpStatus.OK);
	}

	@PostMapping("/deleteProjectTask")
	@ResponseBody
	public String deleteProjectTask(@RequestBody HashMap<String, Object> param) {
		// time.html에서 받아온 timesheetId로 데이터삭제
		int timesheetId = Integer.parseInt((String) param.get("timesheetId"));
		timesheetRepository.deleteById(timesheetId);

		String message = "Yes";
		return message;

	}

	@PostMapping("/editProjectTask")
	@ResponseBody
	public String editProjectTask(@RequestBody HashMap<String, Object> param) {
		System.out.println("here?");
		// 1. time.html에서 정보 모두가져온다
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

		// 3.저장
		timesheetRepository.save(timesheet);

		String message = "Yes";
		return message;
	}

	@PostMapping("/saveProjectTask")
	@ResponseBody
	public Timesheet saveProjectTask(@RequestBody HashMap<String, Object> param) {

		Timesheet timesheet = new Timesheet();
		// ArrayList<Timesheet> currentTimesheet = new ArrayList<Timesheet>();

		// 1. time.html에서 넘겨온 파라미터 user_id, project_task_id, currentWeekId로 이미 존재하는지 체크
		int project_task_id = Integer.parseInt((String) param.get("project_task_id"));
		int user_id = Integer.parseInt((String) param.get("user_id"));
		int week_id = Integer.parseInt((String) param.get("currentWeekId"));

		// 2. timesheet테이블에가서 체크
		boolean exist = timesheetRepository.existsByUserIdAndWeekIdAndProjecttaskId(user_id, week_id, project_task_id);

		// 3. 존재하지않을경우 timesheet에 받은 파라미터 넣어서 save
		if (exist == false) {
			System.out.println("new timesheet");
			timesheet.setUserId(user_id);
			timesheet.setProjecttaskId(project_task_id);
			timesheet.setWeekId(week_id);
			timesheet.setSun("00:00");
			timesheet.setMon("00:00");
			timesheet.setTue("00:00");
			timesheet.setWed("00:00");
			timesheet.setThur("00:00");
			timesheet.setFri("00:00");
			timesheet.setSat("00:00");
			timesheet = timesheetRepository.save(timesheet);
			String timesheetId = String.valueOf(timesheet.getId());
			int id = Integer.parseInt(timesheetId);
			ProjectTask projecttask = projectTaskRepository.findById(project_task_id);
			System.out.println(projecttask.toString());
			timesheet.setProjecttask(projecttask);
			System.out.println(id);
			timesheet = timesheetRepository.findById(id);
			// currentTimesheet = timesheetRepository.findByUserIdAndWeekId(user_id,
			// currentWeekId);
			System.out.println("here");
			System.out.println(timesheet.toString());
		} else {
			timesheet = null;
			System.out.println("already exisiting timesheet");

		}
		return timesheet;
	}

}
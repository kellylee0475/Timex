
package com.te.timex.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

import com.te.timex.model.Project;
import com.te.timex.model.ProjectTask;
import com.te.timex.model.Timesheet;
import com.te.timex.model.Week;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.ProjectTaskRepository;
import com.te.timex.repository.TimesheetRepository;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;
import com.te.timex.service.ProjectService;

@Controller
@RequestMapping("/time")
public class TimeController {
	@Autowired
	private ProjectService projectService;
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
			@RequestParam(required = false, value = "weekId") Optional<String> weekId) {

		// 1. user정보가져오기
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);

		// 2.time.html에서 previous 또는 next 주 버튼클릭했을때 weekId를 가져온다
		if (weekId.isPresent()) {
			int previousWeekId = Integer.parseInt(weekId.get());
			currentWeek = weekRepository.findById(previousWeekId);
			currentWeekId = currentWeek.getId();

		} else {// 현재

			// 2.현재 year가져오기
			int year = Calendar.getInstance().get(Calendar.YEAR);// this year
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
			int week_number = common.getWeekNumber();

			// 5. week테이블에서 year, week number로 week id 가져오기
			currentWeek = weekRepository.findByYearAndWeekNumber(year, week_number);
			currentWeekId = currentWeek.getId();
		}

		// 6. timesheet테이블에서 currentweek와 현재 user_id값을 만족하는 데이터가져오기
		ArrayList currentWeekList = timesheetRepository.findByUserIdAndWeekId(user_id, currentWeekId);

		model.addAttribute("user_id", user_id);
		model.addAttribute("currentWeek", currentWeek);
		model.addAttribute("currentWeekId", currentWeekId);
		model.addAttribute("currentWeekList", currentWeekList);

		return "time/time";
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
		// 1. time.html에서 정보 모두가져온다
		int timesheetId = Integer.parseInt((String) param.get("timesheetId"));
		int user_id = Integer.parseInt((String) param.get("user_id"));
		int project_task_id = Integer.parseInt((String) param.get("project_task_id"));
		int week_id = Integer.parseInt((String) param.get("week_id"));
		int sun = Integer.parseInt((String) param.get("sun"));
		int mon = Integer.parseInt((String) param.get("mon"));
		int tue = Integer.parseInt((String) param.get("tue"));
		int wed = Integer.parseInt((String) param.get("wed"));
		int thur = Integer.parseInt((String) param.get("thur"));
		int fri = Integer.parseInt((String) param.get("fri"));
		int sat = Integer.parseInt((String) param.get("sat"));

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
	public String saveProjectTask(@RequestBody HashMap<String, Object> param) {
		String message;
		Timesheet timesheet = new Timesheet();

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
			timesheet = timesheetRepository.save(timesheet);
			String timesheetId = String.valueOf(timesheet.getId());
			message = timesheetId;
		} else {
			System.out.println("already exisiting timesheet");
			message = "No";
		}
		return message;
	}

}
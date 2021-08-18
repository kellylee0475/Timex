
package com.te.timex.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
public class TimeController{
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
//	int currentWeek;
	int currentWeekId;
	@GetMapping
	public String index(Model model, Authentication authentication) {
		
		//1. user정보가져오기
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);

		//2.현재 year가져오기
		int year = Calendar.getInstance().get(Calendar.YEAR);//this year

		//3. 현재 year로 week 테이블 업데이트(1년에 한번)
		HashMap weeklist = common.weekList(year);
		for(int i=1;i<weeklist.size();i++) {
			if(weekRepository.findByYearAndWeekNumber(2021,i).equals(null)) {//존재안할경우 insert
				Week week = new Week();
				week.setYear(2021);
				week.setWeekNumber(i);
				week.setPeriod((String)weeklist.get(i));
				weekRepository.save(week);				
			}
			//System.out.println("already existing");	
		}
		
		//4. common.java에서 오늘 날짜에따른 week number가져오기
		int week_number = common.getWeekNumber();
		
		//5. week테이블에서 year, week number로 week id 가져오기
		Week currentWeek = weekRepository.findByYearAndWeekNumber(year, week_number);
		System.out.println(currentWeek.toString());
		
		currentWeekId = currentWeek.getId();
		
		//6. timesheet테이블에서 currentweek와 현재 user_id값을 만족하는 데이터가져오기
		ArrayList currentWeekList = timesheetRepository.findByUserIdAndWeekId(user_id,currentWeekId);
		System.out.println(currentWeekList.toString());
		model.addAttribute("user_id",user_id);
		model.addAttribute("currentWeek",currentWeek);
		model.addAttribute("currentWeekList",currentWeekList);
		return "time/time";
	}
	
	@GetMapping("/addProject")
    public String modal1() {
        return "time/addProject";
    }
	

	
	@GetMapping("/selectProject")
	public ResponseEntity<List<Project>> selectProject() {
//		System.out.println("came to controller!!!");
		List<Project> projects = projectRepository.findAll();

		if (projects.isEmpty()) {// 고객에 등록된 솔루션이 없을경우
			System.out.println("empty");
		}
		//System.out.println(projects);
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	@GetMapping("/selectTask")
	public ResponseEntity<List<ProjectTask>> selectTask(int project_id) {
	
		List<ProjectTask> protask = projectTaskRepository.findByProjectId(project_id);
		System.out.println("*********************************");
		System.out.println(protask);
		if (protask.isEmpty()) {// 고객에 등록된 솔루션이 없을경우
			System.out.println("empty");
		}
		
		//System.out.println(protask.get(0));
		return new ResponseEntity<List<ProjectTask>>(protask, HttpStatus.OK);
	}

	@PostMapping("/saveProjectTask")
	@ResponseBody
	public String saveProjectTask(@RequestBody HashMap<String, Object> param,Authentication authentication){
		System.out.println("here?&&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println(param);		
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
	
		Timesheet timesheet = new Timesheet();
		
		
		int project_task_id=Integer.parseInt((String) param.get("project_task_id"));

		timesheet.setUserId(user_id);
		timesheet.setProject_task_id(project_task_id);
		
		timesheetRepository.save(timesheet);

		String message = "success saving";
        
        return message;
		
		
	}

}
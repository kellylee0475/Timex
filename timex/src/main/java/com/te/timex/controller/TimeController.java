
package com.te.timex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.te.timex.model.Project;
import com.te.timex.model.ProjectTask;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.ProjectTaskRepository;
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
	@GetMapping
	public String index() {
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


}
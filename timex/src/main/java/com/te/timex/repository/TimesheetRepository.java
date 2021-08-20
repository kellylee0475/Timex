package com.te.timex.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.Timesheet;


public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

	ArrayList<Timesheet> findByUserIdAndWeekId(int user_id,int currentWeekId);
//	ArrayList<Timesheet> findByWeekId(int userId);

	Timesheet findByUserIdAndWeekIdAndProjecttaskId(int user_id, int week_id, int project_task_id);

	boolean existsByUserIdAndWeekIdAndProjecttaskId(int user_id, int week_id, int project_task_id);
	
}
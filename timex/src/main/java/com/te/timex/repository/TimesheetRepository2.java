package com.te.timex.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.timex.model.Timesheet;
import com.te.timex.model.Timesheet2;


public interface TimesheetRepository2 extends JpaRepository<Timesheet2, Integer> {

	ArrayList<Timesheet2> findByUserIdAndWeekId(int user_id,int currentWeekId);

	ArrayList<Timesheet2> findByUserId(int user_id);
	
	Timesheet2 findById(int id);
	
	Timesheet2 findByUserIdAndWeekIdAndProjecttaskIdOrderByProjecttaskId(int user_id, int week_id, int project_task_id);

	boolean existsByUserIdAndWeekIdAndProjecttaskId(int user_id, int week_id, int project_task_id);


//
//			@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + 
//					"	FROM timesheet ts " + 
//					"	inner join week w on w.id=ts.week_id " + 
//					"	inner join project_task pt on pt.id=ts.project_task_id " + 
//					"	inner join project p on p.id=pt.project_id  " + 
//					"	inner join task t on t.id=pt.task_id  "
//					+ " Where ts.user_id=?1 and w.year=?2 ", nativeQuery = true)
//	List<Timesheet2> findByUserIdAndYear(int user_id, int year);
//			
//
//			@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + 
//					"	FROM timesheet ts " + 
//					"	inner join week w on w.id=ts.week_id " + 
//					"	inner join project_task pt on pt.id=ts.project_task_id " + 
//					"	inner join project p on p.id=pt.project_id  " + 
//					"	inner join task t on t.id=pt.task_id  "
//					+ " Where ts.user_id=?1 ", nativeQuery = true)
//	List<Timesheet2> findByUserId2(int user_id);
//			
//			
//			@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + 
//					"	FROM timesheet ts " + 
//					"	inner join week w on w.id=ts.week_id " + 
//					"	inner join project_task pt on pt.id=ts.project_task_id " + 
//					"	inner join project p on p.id=pt.project_id  " + 
//					"	inner join task t on t.id=pt.task_id  "
//					+ " Where ts.user_id=?1 and ts.week_id=?2 ", nativeQuery = true)
//	List<Timesheet2> findByUserAndWeekId(int user_id,int week_id);
//
//			
//			
//			@Query(value = "SELECT ts.*,w.*,p.number,p.*,t.name " + 
//					"	FROM timesheet ts " + 
//					"	inner join week w on w.id=ts.week_id " + 
//					"	inner join project_task pt on pt.id=ts.project_task_id " + 
//					"	inner join project p on p.id=pt.project_id  " + 
//					"	inner join task t on t.id=pt.task_id  "
//					+ " Where ts.user_id=?1 and ts.week_id=?2 and p.id=?3 ", nativeQuery = true)
//	List<Timesheet2> findByUserIdAndWeekIdAndProjectId(int user_id,int week_id,int project_id);
//			
//			
//			
//			@Query(value = "SELECT ts.*,w.*,p.number,p.*,t.name " + 
//					"	FROM timesheet ts " + 
//					"	inner join week w on w.id=ts.week_id " + 
//					"	inner join project_task pt on pt.id=ts.project_task_id " + 
//					"	inner join project p on p.id=pt.project_id  " + 
//					"	inner join task t on t.id=pt.task_id  "
//					+ " Where ts.user_id=?1 and w.year=?2 and p.id=?3 ", nativeQuery = true)
//	List<Timesheet2> findByUserIdAndYearAndProjectId(int user_id,int year,int project_id);
//			
//			
//			@Query(value = "SELECT ts.*,w.*,p.*,t.name " + 
//					"	FROM timesheet ts " + 
//					"	inner join week w on w.id=ts.week_id " + 
//					"	inner join project_task pt on pt.id=ts.project_task_id " + 
//					"	inner join project p on p.id=pt.project_id  " + 
//					"	inner join task t on t.id=pt.task_id  "
//					+ " Where ts.user_id=?1  and p.id=?2 ", nativeQuery = true)
//	List<Timesheet2> findByUserIdAndProjectId(int user_id,int project_id);

			boolean existsByUserIdAndWeekIdAndProjecttaskIdAndDay(int user_id, int week_id, int project_task_id,
					String day);
			
			
}
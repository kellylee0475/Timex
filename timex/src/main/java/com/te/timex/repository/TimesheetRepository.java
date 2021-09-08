package com.te.timex.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.timex.model.Timesheet;

public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

	ArrayList<Timesheet> findByUserIdAndWeekId(int user_id, int currentWeekId);

	ArrayList<Timesheet> findByUserId(int user_id);

	Timesheet findById(int id);

	Timesheet findByUserIdAndWeekIdAndProjecttaskIdOrderByProjecttaskId(int user_id, int week_id, int project_task_id);

	boolean existsByUserIdAndWeekIdAndProjecttaskId(int user_id, int week_id, int project_task_id);

	@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 and w.year=?2 ", nativeQuery = true)
	List<Timesheet> findByUserIdAndYear(int user_id, int year);

	@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 ", nativeQuery = true)
	List<Timesheet> findByUserId2(int user_id);

	@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 and ts.week_id=?2 ", nativeQuery = true)
	List<Timesheet> findByUserAndWeekId(int user_id, int week_id);

	@Query(value = "SELECT ts.*,w.*,p.number,p.title,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 and ts.week_id>=?2 and ts.week_id<=?3 order by week_id desc ", nativeQuery = true)
	List<Timesheet> findByUserAndWeekIds(int user_id, int startWeekId, int endWeekId);

	@Query(value = "SELECT ts.*,w.*,p.number,p.*,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 and ts.week_id=?2 and p.id=?3 ", nativeQuery = true)
	List<Timesheet> findByUserIdAndWeekIdAndProjectId(int user_id, int week_id, int project_id);

	@Query(value = "SELECT ts.*,w.*,p.number,p.*,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 and ts.week_id>=?2 and ts.week_id<=?3 and p.id=?4 order by week_id desc ", nativeQuery = true)
	List<Timesheet> findByUserIdAndWeekIdsAndProjectId(int user_id, int startWeekId, int endWeekId, int project_id);

	@Query(value = "SELECT ts.*,w.*,p.number,p.*,t.name " + "	FROM timesheet ts "
			+ "	inner join week w on w.id=ts.week_id " + "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1 and w.year=?2 and p.id=?3 ", nativeQuery = true)
	List<Timesheet> findByUserIdAndYearAndProjectId(int user_id, int year, int project_id);

	@Query(value = "SELECT ts.*,w.*,p.*,t.name " + "	FROM timesheet ts " + "	inner join week w on w.id=ts.week_id "
			+ "	inner join project_task pt on pt.id=ts.project_task_id "
			+ "	inner join project p on p.id=pt.project_id  " + "	inner join task t on t.id=pt.task_id  "
			+ " Where ts.user_id=?1  and p.id=?2 ", nativeQuery = true)
	List<Timesheet> findByUserIdAndProjectId(int user_id, int project_id);

	List<Timesheet> findByUserIdOrderByWeekIdDesc(int user_id);

}
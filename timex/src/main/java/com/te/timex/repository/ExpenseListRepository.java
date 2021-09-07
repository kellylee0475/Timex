package com.te.timex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.timex.model.ExpenseList;
import com.te.timex.model.Timesheet;

public interface ExpenseListRepository extends JpaRepository<ExpenseList, Integer> {

	ExpenseList save(List<ExpenseList> expenseList);

//	List<ExpenseList> findByUserId(int user_id);

	@Query(value = "SELECT el.*,e.*,p.*,u.* FROM expense_list el " + "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id " + "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1 order by date desc", nativeQuery = true)
	List<ExpenseList> findByUserId(int user_Id);

	@Query(value = "SELECT el.*,e.*,p.*,u.* " + "FROM expense_list el " + "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id " + "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1 and el.project_id=?2 and el.expense_id=?3 and " +
			// "el.date >='?4' AND el.date <'?5' ", nativeQuery = true)
			"el.date >=to_timestamp(?4,'YYYY-MM-DD') AND el.date <= to_timestamp(?5,'YYYY-MM-DD')  ", nativeQuery = true)
	// "(date_trunc('day',el.date) >='?4'AND date_trunc('day',el.date) <'?5') ",
	// nativeQuery = true)
	List<ExpenseList> findByUserIdAndProjectIdAndExpenseIdAndDate(int user_id, int project_id, int expense_id,
			String startdate, String enddate);

	@Query(value = "SELECT el.*,e.*,p.*,u.* " + "FROM expense_list el " + "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id " + "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1  and "
			+ "el.date >=to_timestamp(?2,'YYYY-MM-DD') AND el.date <= to_timestamp(?3,'YYYY-MM-DD')  ", nativeQuery = true)

	List<ExpenseList> findByUserIdAndDate(int user_id, String startdate, String enddate);

	@Query(value = "SELECT el.*,e.*,p.*,u.* " + "FROM expense_list el " + "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id " + "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1  and el.project_id=?2 and "
			+ "el.date >=to_timestamp(?3,'YYYY-MM-DD') AND el.date <= to_timestamp(?4,'YYYY-MM-DD')  ", nativeQuery = true)
	List<ExpenseList> findByUserIdAndProjectIdAndDate(int user_id, int project_id, String startdate, String enddate);

	@Query(value = "SELECT el.*,e.*,p.*,u.* " + "FROM expense_list el " + "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id " + "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1  and el.expense_id=?2 and "
			+ "el.date >=to_timestamp(?3,'YYYY-MM-DD') AND el.date <= to_timestamp(?4,'YYYY-MM-DD')  ", nativeQuery = true)

	List<ExpenseList> findByUserIdAndExpenseIdAndDate(int user_id, int expense_id, String startdate, String enddate);

//	

}
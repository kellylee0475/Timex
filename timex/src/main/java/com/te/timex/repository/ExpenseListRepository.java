package com.te.timex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.timex.model.ExpenseList;
import com.te.timex.model.Timesheet;


public interface ExpenseListRepository extends JpaRepository<ExpenseList, Integer> {

	ExpenseList save(List<ExpenseList> expenseList);

//	List<ExpenseList> findByUserId(int user_id);
	
	@Query(value="SELECT el.*,e.*,p.*,u.* FROM expense_list el "
			+ "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id "
			+ "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1 order by date desc", nativeQuery = true)
	List<ExpenseList> findByUserId(int user_Id);
	
	
//	@Query(value="SELECT el.*,e.*,p.*,u.* \r\n" + 
//			"FROM expense_list el \r\n" + 
//			"inner join expense e on e.id=el.expense_id \r\n" + 
//			"inner join project p on p.id=el.project_id \r\n" + 
//			"inner join users u on u.id=el.user_id \r\n" + 
//			"Where el.user_id=9 and \r\n" + 
//			"(date_trunc('day',el.date) >='2021-08-09'AND date_trunc('day',el.date) <'2021-08-20')   ", nativeQuery = true)
//	List<ExpenseList> findByUserIdAndProjectIdAndExpenseIdAndDate(int user_id, int project_id, int expense_id, String startdate, String enddate);

//	
	
}
package com.te.timex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.timex.model.ExpenseList;


public interface ExpenseListRepository extends JpaRepository<ExpenseList, Integer> {

	ExpenseList save(List<ExpenseList> expenseList);

//	List<ExpenseList> findByUserId(int user_id);
	
	@Query(value="SELECT el.*,e.*,p.*,u.* FROM expense_list el "
			+ "inner join expense e on e.id=el.expense_id "
			+ "inner join project p on p.id=el.project_id "
			+ "inner join users u on u.id=el.user_id "
			+ "Where el.user_id=?1", nativeQuery = true)
	List<ExpenseList> findByUserId(int user_Id);
//	
	
}
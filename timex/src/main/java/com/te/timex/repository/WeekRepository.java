package com.te.timex.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.timex.model.Week;


public interface WeekRepository extends JpaRepository<Week, Integer> {

	
	Week findByYearAndWeekNumber(int year, int week_number);
	@Query(value = "SELECT id " + 
			"FROM week " + 
			" Where year=?1 and week_number >=?2 and week_number <=?3  ", nativeQuery = true)
	ArrayList findByYearAndWeekNumbers(int year, int startWeekNum, int endWeekNum);
	
	Week findByYear(int year);
	
	Week findById(int previousWeekId);

}
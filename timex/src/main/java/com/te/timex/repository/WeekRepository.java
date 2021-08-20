package com.te.timex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.Week;


public interface WeekRepository extends JpaRepository<Week, Integer> {

	
	Week findByYearAndWeekNumber(int year, int week_number);

	Week findByYear(int year);
	
	Week findById(int previousWeekId);

}
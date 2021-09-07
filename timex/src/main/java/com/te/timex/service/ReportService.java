package com.te.timex.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.timex.controller.Common;
import com.te.timex.model.ExpenseList;
import com.te.timex.model.Timesheet;
import com.te.timex.model.Week;
import com.te.timex.repository.ExpenseListRepository;
import com.te.timex.repository.TimesheetRepository;
import com.te.timex.repository.WeekRepository;

@Service
public class ReportService {
	@Autowired
	ExpenseListRepository expenseListRepository;
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	WeekRepository weekRepository;

	
	public List<ExpenseList> getExpenseList(HashMap<String, Object> param) {

		System.out.println(param.get("project_id").toString());
		System.out.println(param.get("expense_id").toString());
		int project_id, expense_id;
		if (param.get("project_id").toString().equals("allproject")) {
			project_id = 0;
		} else {
			project_id = Integer.parseInt((String) param.get("project_id"));
		}

		if (param.get("expense_id").toString().equals("allcategory")) {
			expense_id = 0;
		} else {
			expense_id = Integer.parseInt((String) param.get("expense_id"));
		}

		String timeframe = param.get("date").toString();
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String startDay = "", endDay = "";
		String[] date = today.split("-");
		String year = date[0];
		String month = date[1];
		String lastmonth = String.valueOf(Integer.parseInt(month) - 1);
		String lastyear = String.valueOf(Integer.parseInt(year) - 1);
		String day = date[2];
		
		int user_id =  Integer.parseInt((String) param.get("user_id").toString());
	//	System.out.println(user_id2);
		Common common = new Common();
		ArrayList weekinfo;
		Week currentWeek;
		String period;
		String[] period2;
		switch (timeframe) {
		case "thisweek":
			weekinfo = common.getWeekNumber2(today);
			currentWeek = weekRepository.findByYearAndWeekNumber((int) weekinfo.get(0), (int) weekinfo.get(1));
			period = currentWeek.getPeriod();
			period2 = period.split("~");
			startDay = period2[0].replace('/', '-');
			endDay = period2[1].replace('/', '-');
			break;
		case "lastweek":
			weekinfo = common.getWeekNumber2(today);
			currentWeek = weekRepository.findByYearAndWeekNumber((int) weekinfo.get(0), (int) weekinfo.get(1) - 1);
			period = currentWeek.getPeriod();
			period2 = period.split("~");
			startDay = period2[0].replace('/', '-');
			endDay = period2[1].replace('/', '-');
			break;
		case "thismonth":
			System.out.println(month);
			startDay = year + "-" + month + "-01";
			endDay = year + "-" + month + "-31";
			if (month.equals("01") || month.equals("03") || month.equals("05") || month.equals("07")
					|| month.equals("08") || month.equals("10") || month.equals("12")) {
				endDay = year + "-" + month + "-31";
			} else if (month.equals("02")) {
				endDay = year + "-" + month + "-28";
			} else {
				endDay = year + "-" + month + "-30";
			}
			break;
		case "lastmonth":
			System.out.println(lastmonth);
			startDay = year + "-" + lastmonth + "-01";
			if (lastmonth.equals("1") || lastmonth.equals("3") || lastmonth.equals("5") || lastmonth.equals("7")
					|| lastmonth.equals("8") || lastmonth.equals("10") || lastmonth.equals("12")) {
				endDay = year + "-" + lastmonth + "-31";
			} else if (lastmonth == "2") {
				endDay = year + "-" + lastmonth + "-28";
			} else {
				endDay = year + "-" + lastmonth + "-30";
			}
			break;
		case "thisyear":
			startDay = year + "-01-01";
			endDay = year + "-12-31";
			break;
		case "lastyear":
			startDay = lastyear + "-01-01";
			endDay = lastyear + "-12-31";
			break;
		case "alltime":
			startDay = "0000-01-01";
			endDay = today;
			break;
		case "custom":
			startDay = (String) param.get("start_date");
			endDay = (String) param.get("end_date");

			break;
		default:
			System.out.println("everyoneher?");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String start_date = "", end_date = "";
		try {
			Date convertedStartDate = sdf.parse(startDay);
			start_date = sdf.format(convertedStartDate);
			Date convertedEndDate = sdf.parse(endDay);
			end_date = sdf.format(convertedEndDate);
			// System.out.println(start_date);
			 System.out.println("startDay = " + start_date + " endDay = " + end_date);

		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		System.out.println("&&&&&&&&&&&&&&2");

		 System.out.println("user_id" + user_id);
		 System.out.println(project_id);
		 System.out.println(expense_id);
		 System.out.println(start_date);
		 System.out.println(end_date);
		List<ExpenseList> expenseReport = null;
		if (project_id == 0 && expense_id != 0) {
			System.out.println("here1");
			expenseReport = expenseListRepository.findByUserIdAndExpenseIdAndDate(user_id, expense_id, start_date,
					end_date);
		} else if (project_id != 0 && expense_id == 0) {
			System.out.println("here2");
			expenseReport = expenseListRepository.findByUserIdAndProjectIdAndDate(user_id, project_id, start_date,
					end_date);
		} else if (project_id == 0 && expense_id == 0) {
			System.out.println("here3");
			expenseReport = expenseListRepository.findByUserIdAndDate(user_id, start_date, end_date);
		} else {
			System.out.println("here4");
			expenseReport = expenseListRepository.findByUserIdAndProjectIdAndExpenseIdAndDate(user_id, project_id,
					expense_id, start_date, end_date);

		}

		System.out.println(expenseReport);

		return expenseReport;
	}

	public List<Timesheet> getTimeList(HashMap<String, Object> param) {

		int project_id;
		if (param.get("project_id").toString().equals("allproject")) {
			project_id = 0;
		} else {
			project_id = Integer.parseInt((String) param.get("project_id"));
		}

		String timeframe = param.get("date").toString();
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String[] date = today.split("-");
		String year = date[0];
		String month = date[1];
		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		List<Timesheet> timesheetReport = null;
		int user_id =  Integer.parseInt((String) param.get("user_id"));
		Common common = new Common();
		ArrayList weekinfo;
		Week currentWeek;

		int weekId;

		switch (timeframe) {
		case "thisweek":
			weekinfo = common.getWeekNumber2(today);
			currentWeek = weekRepository.findByYearAndWeekNumber((int) weekinfo.get(0), (int) weekinfo.get(1));
			weekId = currentWeek.getId();
			if (project_id == 0) {
				timesheetReport = timesheetRepository.findByUserIdAndWeekId(user_id, weekId);
			} else {
				timesheetReport = timesheetRepository.findByUserIdAndWeekIdAndProjectId(user_id, weekId, project_id);
			}
			break;
		case "lastweek":
			weekinfo = common.getWeekNumber2(today);
			System.out.println(weekinfo);
			currentWeek = weekRepository.findByYearAndWeekNumber((int) weekinfo.get(0), (int) weekinfo.get(1) - 1);
			weekId = currentWeek.getId();
			if (project_id == 0) {
				timesheetReport = timesheetRepository.findByUserIdAndWeekId(user_id, weekId);
			} else {
				timesheetReport = timesheetRepository.findByUserIdAndWeekIdAndProjectId(user_id, weekId, project_id);
			}
			break;
		case "thismonth":
			System.out.println(month);
			ArrayList weeknumbers = common.thisMonth(year, month);
			int startWeekNum = (int) weeknumbers.get(0);
			int endWeekNum = (int) weeknumbers.get(weeknumbers.size() - 1);
			ArrayList weekIds = weekRepository.findByYearAndWeekNumbers(yearInt, startWeekNum, endWeekNum);
			int startWeekId = (int) weekIds.get(0);
			int endWeekId = (int) weekIds.get(weekIds.size() - 1);
			if (project_id == 0) {
				System.out.println(startWeekId);
				System.out.println(endWeekId);
				timesheetReport = timesheetRepository.findByUserAndWeekIds(user_id, startWeekId, endWeekId);
			} else {
				timesheetReport = timesheetRepository.findByUserIdAndWeekIdsAndProjectId(user_id, startWeekId,
						endWeekId, project_id);
			}
			break;
		case "lastmonth":
			System.out.println(month);
			String lastMonth = String.valueOf(monthInt - 1);

			ArrayList weeknumbers2 = common.thisMonth(year, lastMonth);
			int startWeekNum2 = (int) weeknumbers2.get(0);
			int endWeekNum2 = (int) weeknumbers2.get(weeknumbers2.size() - 1);
			ArrayList weekIds2 = weekRepository.findByYearAndWeekNumbers(yearInt, startWeekNum2, endWeekNum2);
			int startWeekId2 = (int) weekIds2.get(0);
			int endWeekId2 = (int) weekIds2.get(weekIds2.size() - 1);
			if (project_id == 0) {
				timesheetReport = timesheetRepository.findByUserAndWeekIds(user_id, startWeekId2, endWeekId2);
			} else {
				timesheetReport = timesheetRepository.findByUserIdAndWeekIdsAndProjectId(user_id, startWeekId2,
						endWeekId2, project_id);
			}

			break;
		case "thisyear":

			if (project_id == 0) {
				timesheetReport = timesheetRepository.findByUserIdAndYear(user_id, yearInt);

			} else {
				timesheetReport = timesheetRepository.findByUserIdAndYearAndProjectId(user_id, yearInt, project_id);
			}

			break;
		case "lastyear":
			if (project_id == 0) {
			
				timesheetReport = timesheetRepository.findByUserIdAndYear(user_id, yearInt - 1);
			
			} else {
				timesheetReport = timesheetRepository.findByUserIdAndYearAndProjectId(user_id, yearInt - 1, project_id);

			}
			break;
		case "alltime":
			if (project_id == 0) {
			//	System.out.println("project_id? = "+project_id);
			//	System.out.println("user_id=?");
			//	System.out.println(user_id);
				timesheetReport = timesheetRepository.findByUserIdOrderByWeekIdDesc(user_id);

			} else {
				timesheetReport = timesheetRepository.findByUserIdAndProjectId(user_id, project_id);
			}
			break;
//		case "custom":
//			startDay = (String) param.get("start_date");
//			endDay = (String) param.get("end_date");
//
//			break;
		default:
			System.out.println("everyoneher?");
		}

	//	System.out.println("*************!!!! timesheetreport");
		//System.out.println(timesheetReport);

		return timesheetReport;
	}

}
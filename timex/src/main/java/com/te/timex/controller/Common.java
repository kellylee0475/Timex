
package com.te.timex.controller;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.te.timex.model.User;
import com.te.timex.model.Week;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;

@Component
public class Common {
	@Autowired
	private static WeekRepository weekRepository;

	// private UserRepository userRepository;

	public int getUserId(Authentication authentication, UserRepository userRepository) {

		// 로그인한 정보로 email주소 가져온다
		String email = authentication.getName();

		// 로그인한 email정보로 user_id 가져온다
		User user = userRepository.findByEmail(email);
		int user_id = user.getId();

		return user_id;
	}

	public static ArrayList getDates(int year) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, year);

		ArrayList datelist = new ArrayList();
		int weeknum = 0;
		String weekdate = "";

		for (int month = 1; month <= 12; month++) {

			cal.set(Calendar.MONTH, month - 1);

			for (int week = 1; week < cal.getMaximum(Calendar.WEEK_OF_MONTH); week++) {
				cal.set(Calendar.WEEK_OF_MONTH, week);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

				int startDay = cal.get(Calendar.DAY_OF_MONTH);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

				int endDay = cal.get(Calendar.DAY_OF_MONTH);

				if (week == 1 && startDay >= 7) {
					int previousmonth = month - 1;
					if (previousmonth == 0) {
						previousmonth = 12;

						int previousyear = year - 1;
						weekdate = previousyear + "/" + previousmonth + "/" + startDay + " ~ " + year + "/" + month
								+ "/" + endDay;
						datelist.add(weeknum, weekdate);
						weeknum++;
						// System.out.println(previousyear + "/" + previousmonth + "/" + startDay + " ~
						// " + year + "/"+ month + "/" + endDay);
					} else {
						weekdate = year + "/" + previousmonth + "/" + startDay + " ~ " + year + "/" + month + "/"
								+ endDay;
						datelist.add(weeknum, weekdate);
						weeknum++;
						// System.out.println(year + "/" + previousmonth + "/" + startDay + " ~ " + year
						// + "/" + month + "/" + endDay);
					}

				} else if (week == cal.getMaximum(Calendar.WEEK_OF_MONTH) - 1 && endDay <= 7) {

					if (month == 12) {
						int nextyear = year + 1;
						weekdate = year + "/" + month + "/" + startDay + " ~ " + nextyear + "/" + 1 + "/" + endDay;
						datelist.add(weeknum, weekdate);
						weeknum++;
						// System.out.println(year + "/" + month + "/" + startDay + " ~ " + nextyear +
						// "/" + 1 + "/" + endDay);
					}

				} else {
					weekdate = year + "/" + month + "/" + startDay + " ~ " + year + "/" + month + "/" + endDay;
					datelist.add(weeknum, weekdate);
					weeknum++;
					// System.out.println(year + "/" + month + "/" + startDay + " ~ " + year + "/" +
					// month + "/" + endDay);
				}
			}
		}
		return datelist;
	}

	public static HashMap weekList(int year) {
		HashMap weeklist = new HashMap();
		int totalnum = getDates(year).size();
		for (int i = 0; i < totalnum; i++) {
			weeklist.put(i + 1, getDates(year).get(i));

		}
		return weeklist;
	}

	public static ArrayList thisMonth(String year, String month) {

		int intMonth = Integer.parseInt(month);
		int intYear = Integer.parseInt(year);
		Calendar cal = Calendar.getInstance();
		int nextMonth = intMonth + 1;
		cal.set(Calendar.YEAR, intYear);

		ArrayList datelist = new ArrayList();
		int weeknum = 0;
		String weekdate = "";

		cal.set(Calendar.MONTH, intMonth - 1);

		for (int week = 1; week < cal.getMaximum(Calendar.WEEK_OF_MONTH); week++) {
			cal.set(Calendar.WEEK_OF_MONTH, week);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

			int startDay = cal.get(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

			int endDay = cal.get(Calendar.DAY_OF_MONTH);
			System.out.println("week = " + week);
			System.out.println("startyDay = " + startDay);
			int nextmonth = intMonth + 1;
			if (week == 1 && startDay >= 7) {
				int previousmonth = intMonth - 1;

				if (previousmonth == 0) {
					previousmonth = 12;

					int previousyear = intYear - 1;
					weekdate = previousyear + "/" + previousmonth + "/" + startDay + " ~ " + intYear + "/" + intMonth
							+ "/" + endDay;
					datelist.add(weeknum, weekdate);
					weeknum++;
				} else {
					weekdate = intYear + "/" + previousmonth + "/" + startDay + " ~ " + intYear + "/" + intMonth + "/"
							+ endDay;
					datelist.add(weeknum, weekdate);	
					weeknum++;
				}

			} else if (nextmonth == 13 && startDay >= 26) {			
		
				weekdate = intYear + "/" + intMonth + "/" + startDay + " ~ " + intYear + "/" + 1 + "/" + endDay;
						datelist.add(weeknum, weekdate);
			} else if (week == 5 && startDay >= 26) {// next month
				weekdate = intYear + "/" + intMonth + "/" + startDay + " ~ " + intYear + "/" + nextMonth + "/" + endDay;
				datelist.add(weeknum, weekdate);				
				weeknum++;
			} else {
				weekdate = intYear + "/" + intMonth + "/" + startDay + " ~ " + intYear + "/" + intMonth + "/" + endDay;
				datelist.add(weeknum, weekdate);				
				weeknum++;
			}
		}

	//	System.out.println(datelist);
		//weekList(intYear);
		ArrayList weeknumbers = new ArrayList();
		HashMap weeks = weekList(intYear);
		for(int i=1; i<weeks.size();i++) {
			for(int j=0;j<datelist.size();j++) {
				String list = datelist.get(j).toString();
				if(weeks.get(i).toString().contains(datelist.get(j).toString())) {
					weeknumbers.add(i);
					
				}
				
			}
			
			
		}
		
	//	if(weekList(intYear).get(0).toString().contains("2021/01/24")) {
	//		System.out.println("yes");
//		}else {
//			System.out.println("n");
//		}
		return weeknumbers;

	}

	public static int getWeekNumber() {
		LocalDate today = LocalDate.now();
		// LocalDate today = LocalDate.parse("2021-08-22");
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		String week_number_string = String.format("%02d", today.get(weekFields.weekOfWeekBasedYear()));
		String year_string = String.format("%d", today.get(weekFields.weekBasedYear()));
		int year = Integer.parseInt((String) year_string);
		int week_number = Integer.parseInt((String) week_number_string);

		return week_number;
	}

	public static ArrayList getWeekNumber2(String pickedDate) {
		// LocalDate today = LocalDate.now();
		ArrayList list = new ArrayList<>();
		LocalDate today = LocalDate.parse(pickedDate);
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		String week_number_string = String.format("%02d", today.get(weekFields.weekOfWeekBasedYear()));
		String year_string = String.format("%d", today.get(weekFields.weekBasedYear()));
		int year = Integer.parseInt((String) year_string);
		int week_number = Integer.parseInt((String) week_number_string);

		list.add(year);
		list.add(week_number);
		return list;
	}

	/*
	 * public static ArrayList getthisMonth(String pickedDate){
	 * 
	 * ArrayList list = new ArrayList<>(); LocalDate today =
	 * LocalDate.parse(pickedDate);
	 * 
	 * WeekFields weekFields = WeekFields.of(Locale.getDefault());
	 * System.out.println("weekFields = "+weekFields); String week_number_string =
	 * String.format("%02d", today.get(weekFields.weekOfWeekBasedYear())); String
	 * year_string=String.format("%d", today.get(weekFields.weekBasedYear())); int
	 * year = Integer.parseInt((String)year_string); int
	 * week_number=Integer.parseInt((String) week_number_string);
	 * 
	 * list.add(year); list.add(week_number); return list; }
	 */
//	public static void getWeekId(int year, int week_number, Week) {
//		System.out.println("here??!@#");
//		weekRepository.findById(62);
//		System.out.println("tutu");
//		weekRepository.findByYearAndWeekNumber(year, week_number);
//		System.out.println("hi");
//		//System.out.println(week);
//	}

}

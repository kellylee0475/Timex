
package com.te.timex.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;

@Component
public class Common{
	@Autowired
	private static WeekRepository weekRepository;
	
	//private UserRepository userRepository;

	public int getUserId(Authentication authentication,UserRepository userRepository) {
	
		//로그인한 정보로 email주소 가져온다
		String email = authentication.getName();

		//로그인한  email정보로 user_id 가져온다
		User user= userRepository.findByEmail(email);
		int user_id=user.getId();
		
		return user_id;		
	}
	
	public static ArrayList getDates(int year) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, year);

		ArrayList datelist = new ArrayList();
		int weeknum=0;
		String weekdate="";
		
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
						weekdate = previousyear  + "/" + previousmonth + "/" + startDay + " ~ " + year + "/"+ month + "/" + endDay; 
						datelist.add(weeknum, weekdate);
						weeknum++;
					//	System.out.println(previousyear  + "/" + previousmonth + "/" + startDay + " ~ " + year + "/"+ month + "/" + endDay);
					} else {
						weekdate = year + "/" + previousmonth + "/" + startDay + " ~ " + year + "/"	+ month + "/" + endDay;
						datelist.add(weeknum, weekdate);
						weeknum++;
					//	System.out.println(year + "/" + previousmonth + "/" + startDay + " ~ " + year + "/"	+ month + "/" + endDay);
					}
					
				} else if (week == cal.getMaximum(Calendar.WEEK_OF_MONTH) - 1 && endDay <= 7) {

					if (month == 12) {
						int nextyear = year + 1;
						weekdate = year + "/" + month + "/" + startDay + " ~ " + nextyear + "/"	+ 1 + "/" + endDay;
						datelist.add(weeknum, weekdate);
						weeknum++;
					//	System.out.println(year + "/" + month + "/" + startDay + " ~ " + nextyear + "/"	+ 1 + "/" + endDay);
					}
					
				} else {
					weekdate = year + "/" + month + "/" + startDay + " ~ " + year + "/"	+ month + "/" + endDay;
					datelist.add(weeknum, weekdate);
					weeknum++;
					//System.out.println(year + "/" + month + "/" + startDay + " ~ " + year + "/"	+ month + "/" + endDay);
				}
			}
		}		
		return datelist;
	}
	public static HashMap weekList(int year) {
	HashMap weeklist = new HashMap();
	int totalnum = getDates(year).size();
	for (int i=0;i<totalnum;i++) {
		weeklist.put(i+1, getDates(year).get(i));
		
	}
	return weeklist;
}

	public static int getWeekNumber(){
		LocalDate today = LocalDate.now();
     //  LocalDate today = LocalDate.parse("2021-08-22");
       WeekFields weekFields = WeekFields.of(Locale.getDefault());
       String  week_number_string = String.format("%02d", today.get(weekFields.weekOfWeekBasedYear()));
       String year_string=String.format("%d", today.get(weekFields.weekBasedYear()));
       int year = Integer.parseInt((String)year_string);
       int week_number=Integer.parseInt((String) week_number_string);

       return week_number;
	}
	public static ArrayList getWeekNumber2(String pickedDate){
	//	LocalDate today = LocalDate.now();
		ArrayList list = new ArrayList<>();
    LocalDate today = LocalDate.parse(pickedDate);
       WeekFields weekFields = WeekFields.of(Locale.getDefault());
       String  week_number_string = String.format("%02d", today.get(weekFields.weekOfWeekBasedYear()));
       String year_string=String.format("%d", today.get(weekFields.weekBasedYear()));
       int year = Integer.parseInt((String)year_string);
       int week_number=Integer.parseInt((String) week_number_string);

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
	public static void main(String[] args) {
	
	
	}
	
	

}


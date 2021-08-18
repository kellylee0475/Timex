//
//package com.te.timex.controller;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.WeekFields;
//import java.util.Locale;
//import java.util.stream.Stream;
//
//class Pair<FirstType, SecondType> {
//    private FirstType first;
//    private SecondType second;
//
//    private Pair() {
//        first = null;
//        second = null;
//    }
//
//    private Pair(FirstType first, SecondType second) {
//        this.first = first;
//        this.second = second;
//    }
//
//    public static <T, S> Pair make(T first, S second) {
//        return new Pair(first, second);
//    }
//
//    public FirstType getFirst() {
//        return first;
//    }
//
//    public SecondType getSecond() {
//        return second;
//    }
//}
//
//public class WeekNumbering {
//	//reference https://nidev.gitlab.io/2021/01/01/2020%EB%85%84-12%EC%9B%94-30%EC%9D%BC%EC%9D%80-%EB%AA%87%EB%B2%88%EC%A7%B8-%EC%A3%BC%EC%9D%B8%EA%B0%80-feat-Java/
//	public static void getT(){
//		 // 아래의 날짜를 수정해서 돌린다.
//        LocalDate today = LocalDate.parse("2021-08-21");
//        
//        System.out.println(" >> Today : " + today.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        Stream.<Pair<WeekFields, String>>of(
//        //    Pair.make(WeekFields.ISO, "ISO"),
//            Pair.make(WeekFields.of(Locale.US), "US")
//      //      Pair.make(WeekFields.of(Locale.FRANCE), "FRANCE"),
//      //      Pair.make(WeekFields.of(Locale.KOREA), "KOREA")
//        ).sequential().forEach((pair) -> {
//            System.out.println("==== " + pair.getSecond() + " ====");
//
//            WeekFields weekFields = pair.getFirst();
//
//            System.out.println("MinimalDaysInFirstWeek = " + weekFields.getMinimalDaysInFirstWeek());
//            System.out.println("FirstDayOfWeek = " + weekFields.getFirstDayOfWeek());
//            System.out.println("weekOfYear = " + today.get(weekFields.weekOfYear()));
//            System.out.println("weekBasedYear = " + today.get(weekFields.weekBasedYear()));
//            System.out.println("weekOfWeekBasedYear = " + today.get(weekFields.weekOfWeekBasedYear()));
//        });
//	}
//    public static void main(String[] args) {
//       
//    }
//}
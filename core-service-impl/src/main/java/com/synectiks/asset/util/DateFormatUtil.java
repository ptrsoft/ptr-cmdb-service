package com.synectiks.asset.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

public final class DateFormatUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateFormatUtil.class);
	
	public static final String changeDateFormat(final String newDateFormat, final String orgDateFormat, final String dateValue) throws ParseException, Exception {
		if(StringUtils.isBlank(dateValue) || StringUtils.isBlank(newDateFormat )  || StringUtils.isBlank(orgDateFormat)){
			logger.warn(String.format("Returning null due to null in the given fields. Date : %s, new date format : %s, original date format : %s", dateValue, newDateFormat, orgDateFormat));
			return null;
		}
		try{
			Date orgUtilDate = new SimpleDateFormat(orgDateFormat).parse(dateValue);
			String formattedDate = new SimpleDateFormat(newDateFormat).format(orgUtilDate);
			logger.info(String.format("Changing dateFormat - new date format : %s, old date : %s, new date : %s",newDateFormat,dateValue,formattedDate));
			return formattedDate;
		}catch (ParseException e){
			logger.error("ParseException in date formate conversion : " , e);
			throw e;
		}catch (Exception e){
			logger.error("Exception in date formate conversion : " , e);
			throw e;
		}
	}
	
	public static final String convertUtilDateToString(String targetDateFormat, Date date) throws Exception {
		String newDt = null;
		try{
			newDt = new SimpleDateFormat(targetDateFormat).format(date.getTime());
			logger.info(String.format("Date format conversion :old date : %s, new date : %s",date,newDt));
		}catch (Exception e){
			logger.error("Exception in date formate conversion : " , e);
			throw e;
		}
        
        return newDt;
    }
	
	public static final Date convertStringToUtilDate(String dateFormat, String date) throws ParseException  {
		Date newDt = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
//		try{
			sdf.applyPattern(dateFormat);
			newDt = sdf.parse(date);
			logger.info(String.format("Date conversion from string to util date :old date : %s, new date : %s",date,newDt));
//		}catch (Exception e){
//			logger.error("Exception in date conversion from string to util date: " , e);
//			throw e;
//		}
        return newDt;
	}
	
	public static final String subtractDays(String dtFormat, String dt, int days) throws ParseException {
//        String dtFormat = "yyyy-MM-dd";
        Date date = new SimpleDateFormat(dtFormat).parse(dt);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        String newDt = new SimpleDateFormat(dtFormat).format(cal.getTime());
        return newDt;
    }
	
	public static final String subtractDays(String dtFormat, Date date, int days) throws ParseException {
//      String dtFormat = "yyyy-MM-dd";
//      Date date = new SimpleDateFormat(dtFormat).parse(dt);
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      cal.add(Calendar.DATE, -days);
      String newDt = new SimpleDateFormat(dtFormat).format(cal.getTime());
      return newDt;
   }
	
	public final static Date converUtilDateFromLocaDate(LocalDate localDate) {
		if(localDate == null) return null;
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	public final static java.sql.Date converSqlDateFromLocaDate(LocalDate localDate) {
		if(localDate == null) return null;
	    long dt =  Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime();
	    return new java.sql.Date(dt);
	}
	public final static Date convertUtilDateFromLocalDateTime(LocalDateTime localDateTime) {
		if(localDateTime == null) return null;
	    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	public final static LocalDate convertLocalDateFromUtilDate(Date date) {
		if(date == null) return null;
	   return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public final static String convertLocalDateToString(LocalDate dt, String targetFormat) {
		if(dt == null || targetFormat == null) return null;
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(targetFormat);
        String text = dt.format(formatters);
        logger.debug("Formated local date : "+text);
        return text;
	}
	
	public final static LocalDate convertStringToLocalDate(String dt, String targetFormat) {
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(targetFormat);
		LocalDate localDate = LocalDate.parse(dt, formatters);
		 logger.debug("Local date from string date: "+localDate);
		return localDate;
	}
	
	public final static LocalDate getLocalDateFromString(String strDate) {
    	String stDt[] = strDate.split("/");
        if(stDt[0].length() == 1) {
        	stDt[0] = "0"+stDt[0];
        }
        if(stDt[1].length() == 1) {
        	stDt[1] = "0"+stDt[1];
        }
        return convertStringToLocalDate(stDt[2]+"-"+stDt[0]+"-"+stDt[1], Constants.DEFAULT_DATE_FORMAT);
    }
	
	public static final Date convertInstantToUtilDate(DateTimeFormatter formatter, Instant instant) throws ParseException {
		return DateFormatUtil.convertStringToUtilDate(Constants.DEFAULT_DATE_FORMAT,formatter.format(instant));
	}
	
	public static int calculateAge(LocalDate dateOfBirth) {
	  LocalDate now = LocalDate.now(); 
	  Period diff = Period.between(dateOfBirth, now); //difference between the dates is calculated
	  logger.debug("Age : " + diff.getYears());
	  return diff.getYears();
	}
	
	/**
	 * 
	 * @param stringDate e.g 20-02-2023 14:50:40
	 * @param pattern e.g dd-MM-yyyy H:mm:ss
	 * @return
	 */
	public static final Instant convertStringToInstant(String stringDate, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.US); 
		LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter); 
		ZoneId zoneId = ZoneId.of("Asia/Kolkata"); 
		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId); 
		return zonedDateTime.toInstant();
	}
	

	
		public static List<LocalDate> findDatesBetween(LocalDate startDate, LocalDate endDate) {
			List<LocalDate> dates = new ArrayList<>();
			long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);

			for (long i = 0; i <= numOfDaysBetween; i++) {
				LocalDate date = startDate.plusDays(i);
				dates.add(date);
			}

			return dates;
		}

		public static void testFindDatesBetween(String startDateString, String endDateString) {
			String targetFormat = "yyyy-MM-dd";
//			LocalDate startDate = LocalDate.of(2023, 1, 1);
//			LocalDate endDate = LocalDate.of(2023, 12, 31);

			LocalDate startDate = convertStringToLocalDate(startDateString, targetFormat);
			LocalDate endDate = convertStringToLocalDate(endDateString, targetFormat);
			List<LocalDate> datesBetween = findDatesBetween(startDate, endDate);

			for (LocalDate date : datesBetween) {
				System.out.println(date);
			}
		}

	public static List<String> find24HoursOfDay (String inputDate) {
		// Input localDateTime in the format: "yyyy-MM-dd"
//		String inputDate = "2023-07-11";
		List<String> localDateTimeList = new ArrayList<>();
		// Create LocalDateTime object from the input localDateTime
		LocalDateTime localDateTime = LocalDateTime.parse(inputDate + "T00:00:00");

		// Define the localDateTime format for printing
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Print the 24 hours of the given localDateTime
		for (int hour = 0; hour < 24; hour++) {
			String dateTime = localDateTime.format(formatter);
//			System.out.println(dateTime);
			localDateTimeList.add(dateTime);

			localDateTime = localDateTime.plusHours(1);
		}
		return localDateTimeList;
	}

	public static void randomHourJsonArray (String dateString) {
		// Specify the date for which you want to generate the random numbers
//			String dateString = "2023-07-11";

		// Create a LocalDateTime object from the given date
		LocalDateTime date = LocalDateTime.parse(dateString + "T00:00:00");

		// Create a JSON array to store the results
		JSONArray jsonArray = new JSONArray();

		// Generate random numbers for each hour of the day
		for (int i = 0; i < 24; i++) {
			// Generate a random number
			Random random = new Random();
			int randomNumber = random.nextInt(100);

			// Create a JSON object for each hour and random number
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("hour", i);
			jsonObject.put("randomNumber", randomNumber);

			// Add the JSON object to the array
			jsonArray.put(jsonObject);

			// Increment the date by one hour
			date = date.plusHours(1);
		}

		// Print the JSON array
		System.out.println(jsonArray.toString());
	}

	public static void weekOfMonthExample () {
		LocalDate date = LocalDate.of(2023, 7, 12);

		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int weekOfMonth = date.get(weekFields.weekOfMonth());
		System.out.println("Week of the month: " + weekOfMonth);
	}

	public static void weekOfMonthExampleWithStartAndEndDate() {
		// Example date
		LocalDate date = LocalDate.of(2023, 7, 31);

		// Find the start and end dates of the week
		LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		// Get the week of the month
		int weekOfMonth = date.get(WeekFields.ISO.weekOfMonth());

		// Print the results
		System.out.println("Date: " + date);
		System.out.println("Week of Month: " + weekOfMonth);
		System.out.println("Start of Week: " + startOfWeek);
		System.out.println("End of Week: " + endOfWeek);
	}

	public static JSONObject generateTestCostData(String startDateString, String endDateString){
		String targetFormat = "yyyy-MM-dd";
		LocalDate startDate = convertStringToLocalDate(startDateString, targetFormat);
		LocalDate endDate = convertStringToLocalDate(endDateString, targetFormat);
		List<LocalDate> datesBetween = findDatesBetween(startDate, endDate);
		Random random = new Random();

		Map<String, String> hourCostMap = new HashMap<>();
		Map<String, String> dailyCostMap = new HashMap<>();
		Map<String, String> monthlyCostMap = new HashMap<>();
		Map<String, String> yearlyCostMap = new HashMap<>();

		JSONObject jsonObject = new JSONObject();
		String month = null;
		long monthlyCostSum = 0L;

		for (LocalDate date : datesBetween) {
//			System.out.println(date);
			String day = convertLocalDateToString(date, targetFormat);
			List<String> hoursList = find24HoursOfDay(day);

			long dailyCostSum = 0L;
			for(String dailyHours: hoursList){
//				System.out.println(dailyHours);
				int randomNumber = random.nextInt(100);
				dailyCostSum += randomNumber;
				hourCostMap.put(dailyHours, String.valueOf(randomNumber));
			}
			jsonObject.put("HOURLYCOST", hourCostMap);
			dailyCostMap.put(day, String.valueOf(dailyCostSum));
			jsonObject.put("DAILYCOST",dailyCostMap);

			if(!date.getMonth().name().equalsIgnoreCase(month)){
				month = date.getMonth().name();
				monthlyCostSum = 0;
//				monthlyCostSum += dailyCostSum;
			}
			else{
//				monthlyCostSum += dailyCostSum;
			}
			monthlyCostSum += dailyCostSum;
			monthlyCostMap.put(date.getYear()+"-"+date.getMonth().getValue(),String.valueOf(monthlyCostSum));
			jsonObject.put("MONTHLYCOST",monthlyCostMap);


//			System.out.println("Month: "+month);
		}
//		System.out.println("Month map class: "+jsonObject.get("MONTHLYCOST").getClass());
		JSONObject monthJson = (JSONObject)jsonObject.get("MONTHLYCOST");

		Set<Integer> yearSet = new HashSet<>();
		for(String key : monthJson.keySet()) {
			yearSet.add(Integer.parseInt(key.split("-")[0]));
		}
		for (Integer yearKey : yearSet) {
			Map<Integer, Long> map = new HashMap<>();
			for(String key : monthJson.keySet()) {
				if(key.contains(yearKey.toString())){
					if(map.containsKey(yearKey)){
						Long val = map.get(yearKey)+Long.parseLong((String)monthJson.get(key));
						map.put(yearKey, val);
					}else{
						map.put(yearKey, Long.parseLong((String)monthJson.get(key)));
					}
				}
			}
			for (Integer key : map.keySet()) {
				yearlyCostMap.put(String.valueOf(key),String.valueOf(map.get(key)));
			}
			map.clear();
			jsonObject.put("YEARLYCOST",yearlyCostMap);
		}
	 
		return jsonObject;
	}
	
	public static JSONObject generateTestSlaData(String startDateString, String endDateString){
		String targetFormat = "yyyy-MM-dd";
		LocalDate startDate = convertStringToLocalDate(startDateString, targetFormat);
		LocalDate endDate = convertStringToLocalDate(endDateString, targetFormat);
		List<LocalDate> datesBetween = findDatesBetween(startDate, endDate);
		Random random = new Random();

		Map<String, String> hourCostMap = new HashMap<>();
		Map<String, String> dailyCostMap = new HashMap<>();
		Map<String, String> monthlyCostMap = new HashMap<>();
		Map<String, String> yearlyCostMap = new HashMap<>();

		JSONObject jsonObject = new JSONObject();
		String month = null;
		long monthlyCostSum = 0L;

		for (LocalDate date : datesBetween) {
//			System.out.println(date);
			String day = convertLocalDateToString(date, targetFormat);
			List<String> hoursList = find24HoursOfDay(day);

			long dailyCostSum = 0L;
			for(String dailyHours: hoursList){
//				System.out.println(dailyHours);
				int randomNumber = random.nextInt(100);
				dailyCostSum += randomNumber;
				hourCostMap.put(dailyHours, String.valueOf(randomNumber));
			}
			jsonObject.put("HOURLYSLA", hourCostMap);
			dailyCostMap.put(day, String.valueOf(dailyCostSum));
			jsonObject.put("DAILYSLA",dailyCostMap);

			if(!date.getMonth().name().equalsIgnoreCase(month)){
				month = date.getMonth().name();
				monthlyCostSum = 0;
//				monthlyCostSum += dailyCostSum;
			}
			else{
//				monthlyCostSum += dailyCostSum;
			}
			monthlyCostSum += dailyCostSum;
			monthlyCostMap.put(date.getYear()+"-"+date.getMonth().getValue(),String.valueOf(monthlyCostSum));
			jsonObject.put("MONTHLYSLA",monthlyCostMap);


//			System.out.println("Month: "+month);
		}
//		System.out.println("Month map class: "+jsonObject.get("MONTHLYCOST").getClass());
		JSONObject monthJson = (JSONObject)jsonObject.get("MONTHLYSLA");

		Set<Integer> yearSet = new HashSet<>();
		for(String key : monthJson.keySet()) {
			yearSet.add(Integer.parseInt(key.split("-")[0]));
		}
		for (Integer yearKey : yearSet) {
			Map<Integer, Long> map = new HashMap<>();
			for(String key : monthJson.keySet()) {
				if(key.contains(yearKey.toString())){
					if(map.containsKey(yearKey)){
						Long val = map.get(yearKey)+Long.parseLong((String)monthJson.get(key));
						map.put(yearKey, val);
					}else{
						map.put(yearKey, Long.parseLong((String)monthJson.get(key)));
					}
				}
			}
			for (Integer key : map.keySet()) {
				yearlyCostMap.put(String.valueOf(key),String.valueOf(map.get(key)));
			}
			map.clear();
			jsonObject.put("YEARLYSLA",yearlyCostMap);
		}
	 
		return jsonObject;
	}



	public static void bb() {
		// read string into a json
		// get elementLists as jsonarray
		// iterate the jsonarray and for each element run testCostDataGenerator and put each cost in element

		String js = "{\n" +
				"    \"stats\": {},\n" +
				"    \"category\": \"eks\",\n" +
				"    \"metaData\": {},\n" +
				"    \"hostingType\": \"cluster\",\n" +
				"    \"elementLists\": [\n" +
				"        {\n" +
				"            \"arn\": \"arn:aws:eks:us-east-1:657907747545:cluster/my-eks-cluster1\",\n" +
				"            \"name\": \"my-eks-cluster1\"\n" +
				"        },\n" +
				"        {\n" +
				"            \"arn\": \"arn:aws:eks:us-east-1:657907747545:cluster/my-eks-cluster2\",\n" +
				"            \"name\": \"my-eks-cluster2\"\n" +
				"        }\n" +
				"    ]\n" +
				"}";
		ObjectMapper objectMapper = Constants.instantiateMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(js);
			if (rootNode != null) {
				JsonNode elmJson = rootNode.get("elementLists");
				if (elmJson != null && elmJson.isArray()) {
					Iterator<JsonNode> iterator = elmJson.iterator();
					while (iterator.hasNext()) {
						ObjectNode jsonNode = (ObjectNode) iterator.next();
						JSONObject obj = generateTestCostData("2022-11-01", "2023-07-12");
						for (String key : obj.keySet()) {
							jsonNode.put(key, objectMapper.readTree(obj.get(key).toString()));
						}

						System.out.println(jsonNode.toString());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


		public static void AddMinutesToInstant() {
			// Current Instant
			Instant currentInstant = Instant.now();
			System.out.println("Current Instant: " + currentInstant);

			// Add 10 minutes to the current Instant
			Instant modifiedInstant = currentInstant.plus(70, ChronoUnit.MINUTES);
			System.out.println("Modified Instant: " + modifiedInstant);
		}


		// Set the session timeout duration (e.g., 30 minutes)

		// Method to check if the session has timed out
		public static boolean isSessionTimedOut(Instant lastActivityTime) {
			long SESSION_TIMEOUT_MINUTES = 30;
			Duration SESSION_TIMEOUT_DURATION = Duration.ofMinutes(SESSION_TIMEOUT_MINUTES);

			Instant currentTime = Instant.now();
			Duration durationSinceLastActivity = Duration.between(lastActivityTime, currentTime);
			System.out.println("last time "+lastActivityTime+", current time: "+currentTime+", Duration: "+durationSinceLastActivity);

			return durationSinceLastActivity.compareTo(SESSION_TIMEOUT_DURATION) >= 0;
		}

		public static void testSessionTimeout() {
			// Simulate the last activity time (replace this with your actual last activity time)
			Instant lastActivityTime = Instant.parse("2023-07-23T12:00:00Z");

			// Check if the session has timed out
			if (isSessionTimedOut(lastActivityTime)) {
				System.out.println("Session has timed out.");
			} else {
				System.out.println("Session is still active.");
			}
		}

		public Map<String, LocalDate>  getFirstAndLastDateOfYearMonth(int year, int month){
//			YearMonth startYearMonth = YearMonth.now();
			LocalDate startOfMonthDate = YearMonth.of(year, month).atDay(1);
			LocalDate endOfMonthDate   = YearMonth.of(year, month).atEndOfMonth();
//			System.out.println(startYearMonth);
			System.out.println("start date: "+startOfMonthDate+", end date: "+endOfMonthDate);
			Map<String, LocalDate> newStartEndDates = new HashMap<>();
			newStartEndDates.put("startDate",startOfMonthDate);
			newStartEndDates.put("endDate",endOfMonthDate);
			return newStartEndDates;
		}
		public Map<String, LocalDate> getFirstAndLastDateOfGivenWeekDate(LocalDate weekDate){
			LocalDate startOfWeek = weekDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

			// Get the end date of the week (Sunday)
			LocalDate endOfWeek = weekDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
			System.out.println("start date: "+startOfWeek+", end date: "+endOfWeek);
			Map<String, LocalDate> newStartEndDates = new HashMap<>();
			newStartEndDates.put("startDate",startOfWeek);
			newStartEndDates.put("endDate",endOfWeek);
			return newStartEndDates;
		}

		public int getThreeMonthQuarterOfDate(LocalDate dt){
			int quarter = (dt.getMonthValue() - 1) / 3 + 1;
			System.out.println("Quarter of given date: "+dt+", quarter: "+ quarter);
			return quarter;
		}
		public int getFourMonthQuarterOfDate(LocalDate dt){
			int quarter = (dt.getMonthValue() - 1) / 4 + 1;
			System.out.println("Quarter of given date: "+dt+", quarter: "+ quarter);
			return quarter;
		}
		static Map<Integer, Map<String, LocalDate>> threeMonthQuarterCache = new HashMap<>();
		static {
			Map<String, LocalDate> q1 = new HashMap<>();
			q1.put("startDate", LocalDate.of(LocalDate.now().getYear(), 1, 1));
			q1.put("endDate",LocalDate.of(LocalDate.now().getYear(), 3, 31));
			threeMonthQuarterCache.put(1,q1);
			Map<String, LocalDate> q2 = new HashMap<>();
			q2.put("startDate",LocalDate.of(LocalDate.now().getYear(), 4, 1));
			q2.put("endDate",LocalDate.of(LocalDate.now().getYear(), 6, 30));
			threeMonthQuarterCache.put(2,q2);
			Map<String, LocalDate> q3 = new HashMap<>();
			q3.put("startDate",LocalDate.of(LocalDate.now().getYear(), 7, 1));
			q3.put("endDate",LocalDate.of(LocalDate.now().getYear(), 9, 30));
			threeMonthQuarterCache.put(3,q3);
			Map<String, LocalDate> q4 = new HashMap<>();
			q4.put("startDate",LocalDate.of(LocalDate.now().getYear(), 10, 1));
			q4.put("endDate",LocalDate.of(LocalDate.now().getYear(), 12, 31));
			threeMonthQuarterCache.put(4,q4);
		}
		public Map<String, LocalDate> getDatesOfCurrentYearQuarters(int quarter){
			Map dateMap = threeMonthQuarterCache.get(quarter);
			System.out.println("current year's quarter dates: "+dateMap);
			return dateMap;
		}
		public LocalDate getNewDateByAddingOrSubtractingMonth(int month){
			LocalDate currentDate = LocalDate.now();
			if(month < 0){
				LocalDate newDate = currentDate.minusMonths(Math.abs(month));
				System.out.println("Current Date: " + currentDate);
				System.out.println("New Date after subtracting " + month + " months: " + newDate);
				return newDate;
			}
			LocalDate newDate = currentDate.plusMonths(month);
			System.out.println("Current Date: " + currentDate);
			System.out.println("New Date after adding " + month + " months: " + newDate);
			return newDate;
		}

		public LocalDate getNewDateByAddingOrSubtractingYear(int year){
			LocalDate currentDate = LocalDate.now();
			if(year < 0){
				LocalDate newDate = currentDate.minusYears(Math.abs(year));
				System.out.println("Current Date: " + currentDate);
				System.out.println("New Date after subtracting " + year + " years: " + newDate);
				return newDate;
			}
			LocalDate newDate = currentDate.plusYears(year);
			System.out.println("Current Date: " + currentDate);
			System.out.println("New Date after adding " + year + " years: " + newDate);
			return newDate;
		}

		public LocalDate getNewDateByAddingOrSubtractingDay(int day){
			LocalDate currentDate = LocalDate.now();
			if(day < 0){
				LocalDate newDate = currentDate.minusDays(Math.abs(day));
				System.out.println("Current Date: " + currentDate);
				System.out.println("New Date after subtracting " + day + " days: " + newDate);
				return newDate;
			}
			LocalDate newDate = currentDate.plusDays(day);
			System.out.println("Current Date: " + currentDate);
			System.out.println("New Date after adding " + day + " days: " + newDate);
			return newDate;
		}
		public LocalDate getNewDateByAddingOrSubtractingWeek(int week){
			LocalDate currentDate = LocalDate.now();
			if(week < 0){
				LocalDate newDate = currentDate.minusWeeks(Math.abs(week));
				System.out.println("Current Date: " + currentDate);
				System.out.println("New Date after subtracting " + week + " weeks: " + newDate);
				return newDate;
			}
			LocalDate newDate = currentDate.plusWeeks(week);
			System.out.println("Current Date: " + currentDate);
			System.out.println("New Date after adding " + week + " weeks: " + newDate);
			return newDate;
		}
		public int getYearByAddOrSubtractQuarter(int year, int quarter){
			// Adjust year if quarter is negative
			int nYear = year;
			if (quarter < 0) {
				nYear -= Math.abs(quarter) / 4; // Assuming 4 quarters in a year
				if (Math.abs(quarter) % 4 != 0) {
					nYear--; // Adjust year if the negative quarter is not perfectly divisible by 4
				}
				System.out.println("new year after subtracting quarters: " + nYear);
				return nYear;
			}
			nYear += (quarter - 1) / 4;
			System.out.println("new year after adding quarters: " + nYear);
			return nYear;
		}
		public Map<String, LocalDate> getDateRangeOfGivenQuarter(int quarter){
			int months = quarter * 3; // consider 4 quarters in a year
			LocalDate newDate = getNewDateByAddingOrSubtractingMonth(months);
			System.out.println("new date: "+newDate);
			int newQ = getThreeMonthQuarterOfDate(newDate);
			System.out.println("new quarter: "+newQ);
			Map<String, LocalDate> dateMap = getDatesOfCurrentYearQuarters(newQ);
			LocalDate startDate = LocalDate.of(newDate.getYear(),dateMap.get("startDate").getMonth(), dateMap.get("startDate").getDayOfMonth());
			System.out.println("start date: "+startDate);
			LocalDate endDate = LocalDate.of(newDate.getYear(),dateMap.get("endDate").getMonth(), dateMap.get("endDate").getDayOfMonth());
			System.out.println("end date: "+endDate);
			Map<String, LocalDate> newStartEndDates = new HashMap<>();
			newStartEndDates.put("startDate",startDate);
			newStartEndDates.put("endDate",endDate);
			return newStartEndDates;
		}
		public Map<String, LocalDate> getDateRangeOfGivenMonth(int month){
			LocalDate newDate = getNewDateByAddingOrSubtractingMonth(month);
			Map<String, LocalDate> newStartEndDates= getFirstAndLastDateOfYearMonth(newDate.getYear(), newDate.getMonthValue());
			return newStartEndDates;
		}
		public Map<String, LocalDate> getDateRangeOfGivenWeek(int week){
			LocalDate newDate = getNewDateByAddingOrSubtractingWeek(week);
			Map<String, LocalDate> newStartEndDates= getFirstAndLastDateOfGivenWeekDate(newDate);
			return newStartEndDates;
		}

		public Map<String, LocalDate> getDateRangeOfGivenDay(int day){
			LocalDate newDate = getNewDateByAddingOrSubtractingDay(day);
			Map<String, LocalDate> newStartEndDates = new HashMap<>();
			newStartEndDates.put("startDate",newDate);
			newStartEndDates.put("endDate",newDate);
			return newStartEndDates;
		}
		public Map<String, LocalDate> getDateRangeOfGivenYear(int year){
			LocalDate newDate = getNewDateByAddingOrSubtractingYear(year);
			Map<String, LocalDate> newStartEndDates = new HashMap<>();
			LocalDate startDate = LocalDate.of(newDate.getYear(), 1,1);
			newStartEndDates.put("startDate",startDate);
			newStartEndDates.put("endDate",newDate);
			return newStartEndDates;
		}

		public static void main(String a[]) throws Exception {
//			new DateFormatUtil().getYearByAddOrSubtractQuarter(2024, -1);
//			Map<String, LocalDate> mp = new DateFormatUtil().getNewDateRange(-2);
//			System.out.println("new date range: "+mp);

	//		String dt = changeDateFormat(CmsConstants.DATE_FORMAT_dd_MM_yyyy, "dd/MM/yyyy", "29/04/2019");
	//		Date d = getUtilDate(CmsConstants.DATE_FORMAT_dd_MM_yyyy,dt);
	//		System.out.println(d);
	//		LocalDate date = convertStringToLocalDate("08"+"/"+"09"+"/"+"2019","MM/dd/yyyy"); //LocalDate.now();
	//		System.out.println("date to be formated : "+date);
	//		String dt = changeLocalDateFormat(date, CmsConstants.DATE_FORMAT_MM_dd_yyyy);
	//		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(CmsConstants.DATE_FORMAT_MM_dd_yyyy);
	//		System.out.println("local date after format change : "+formatters.format(date));

	//		convertStringToInstant();
	//		testFindDatesBetween("2023-07-01","2023-07-31");
	//		find24HoursOfDay("2023-07-11");
	//		randomHourJsonArray ("2023-07-11");
//			generateTestCostData("2023-01-01","2024-04-30");
	//		weekOfMonthExample ();
	//		weekOfMonthExampleWithStartAndEndDate();
	//		bb();

	//		AddMinutesToInstant();

	//		testSessionTimeout();
//			getFirstAndLastDateOfYearMonth(2024, 2);
//			new DateFormatUtil().getThreeMonthQuarterOfDate(LocalDate.of(2024, 12, 7));
//			getFourMonthQuarterOfDate(LocalDate.of(2024, 12, 7));

//			Map<String, LocalDate> dateRange = new DateFormatUtil().getDateRangeOfGivenQuarter(-1);
//			Map<String, LocalDate> dateRange = new DateFormatUtil().getDateRangeOfGivenMonth(1);
//			Map<String, LocalDate> dateRange = new DateFormatUtil().getDateRangeOfGivenDay(-4);
//			Map<String, LocalDate> dateRange = new DateFormatUtil().getDateRangeOfGivenWeek(-2);
			Map<String, LocalDate> dateRange = new DateFormatUtil().getDateRangeOfGivenYear(-2);
			System.out.println("start date : **************************** "+dateRange.get("startDate").toString());
			System.out.println("end date : **************************** "+dateRange.get("endDate").toString());

		}

}

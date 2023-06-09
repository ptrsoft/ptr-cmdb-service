package com.synectiks.asset.service;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.synectiks.asset.util.DateFormatUtil;
import com.synectiks.asset.util.RandomUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;



public class JsonReaderService {

	public static JSONArray createBulkAnalyticData(String startDateTime, String endDateTime, String sourceJsonFile) throws ParseException {
		Date startDate = DateFormatUtil.convertStringToUtilDate("MM-dd-yyyy hh:mm", startDateTime);
		Date endDate = DateFormatUtil.convertStringToUtilDate("MM-dd-yyyy hh:mm", endDateTime);
		String FILE_PATH = sourceJsonFile;
		JSONParser parser = new JSONParser();
		JSONArray ary = new JSONArray();
		try {
			Object obj = parser.parse(new FileReader(FILE_PATH));
	        JSONObject jsonObject = (JSONObject)obj;
	        JSONArray services = (JSONArray)jsonObject.get("services");
	        Iterator<JSONObject> iterator = services.iterator();
	        while (iterator.hasNext()) {
	        	JSONObject jobj = iterator.next();
	        	jobj.remove("stats");
				long pause = 600000L;
				long endTime = endDate.getTime();
				long startTime = startDate.getTime();
				float prevCpuUse = 0;
		        float prevMemUse = 0;
		        float prevHeapUse = 0;
		        float prevNonHeapUse = 0;
		        float prevNoOfInstance = 0;
		        float prevJvmHeap = 0;
		        float prevJvmNonHeap = 0;
		        float prevJvmTotal = 0;
		        float prevJvmNative = 0;
		        float prevAvgRespTime= 0;
		        float prevRequestRate= 0;
		        float prevGcCollection= 0;
		        float prevPauseDu= 0;
		        float prevGcAllocated= 0;
		        float prevDataRead= 0;
		        float prevDataWrite= 0;
		        float prevDataLatency= 0;
		        float prevDataCon= 0;
		        
		        
				while(startTime <= endTime) {
		        	 float cpuUse = RandomUtil.getRandom(10, 12);
		        	 float memUse = RandomUtil.getRandom(4, 6);
		        	 float heapUse = RandomUtil.getRandom(1, 2);
		        	 float nonHeapUse = RandomUtil.getRandom(3, 5);
		        	 float noOfInstance= RandomUtil.getRandom(3, 4);
		        	 float jvmHeap= RandomUtil.getRandom(1, 2);
		        	 float jvmNonHeap= RandomUtil.getRandom(1, 2);
		        	 float jvmTotal= RandomUtil.getRandom(4, 6);
		        	 float jvmNative= RandomUtil.getRandom(1, 2);
		        	 float avgRespTime= RandomUtil.getRandom(1, 2);
		        	 float requestRate= RandomUtil.getRandom(5000, 7500);
		        	 float gcCollection= RandomUtil.getRandom(30, 33);
		        	 float pauseDu= RandomUtil.getRandom(3, 30);
		        	 float gcAllocated= RandomUtil.getRandom(50, 55);
		        	 float dataRead= RandomUtil.getRandom(50000, 55000);
		        	 float dataWrite= RandomUtil.getRandom(10000, 15000);
		        	 float dataLatency= RandomUtil.getRandom(500, 600);
		        	 float dataCon= RandomUtil.getRandom(30, 36);
		        	 
		        	 float cpuDif = cpuUse-prevCpuUse;
		        	 float memDif = memUse-prevMemUse;
		        	 float heapDif = heapUse-prevHeapUse;
		        	 float nonHeapDif = nonHeapUse-prevNonHeapUse;
		        	 float noOfInstanceDif = noOfInstance-prevNoOfInstance;
		        	 float jvmHeapDif = jvmHeap-prevJvmHeap;
		        	 float jvmNonHeapDif = jvmNonHeap-prevJvmNonHeap;
		        	 float jvmTotalDif = jvmTotal-prevJvmTotal;
		        	 float jvmNativeDif = jvmNative-prevJvmNative;
		        	 float avgRespTimeDif = avgRespTime-prevAvgRespTime;
		        	 float requestRateDif = requestRate-prevRequestRate;
		        	 float gcCollectionDif = gcCollection-prevGcCollection;
		        	 float pauseDuDif = pauseDu-prevPauseDu;
		        	 float gcAllocatedDif = gcAllocated-prevGcAllocated;
		        	 float dataReadDif = dataRead-prevDataRead;
		        	 float dataWriteDif = dataWrite-prevDataWrite;
		        	 float dataLatencyDif = dataLatency-prevDataLatency;
		        	 float dataConDif = dataCon-prevDataCon;
		        	 
		        	 float rogCpu = ((cpuDif/cpuUse)*100) == 0 ? 10 : ((cpuDif/cpuUse)*100);
					 float rogMem = ((memDif/memUse)*100) == 0 ? 4 : ((memDif/memUse)*100);
					 float rogHeap = ((heapDif/heapUse)*100) == 0 ? 1 : ((heapDif/heapUse)*100);
					 float rogNonHeap = ((nonHeapDif/nonHeapUse)*100) == 0 ? 3 : ((nonHeapDif/nonHeapUse)*100);
					 float rogNoOfInstance = ((noOfInstanceDif/noOfInstance)*100) == 0 ? 3 : ((noOfInstanceDif/noOfInstance)*100);
					 float rogJvmHeap = ((jvmHeapDif/jvmHeap)*100) == 0 ? 1 : ((jvmHeapDif/jvmHeap)*100);
					 float rogJvmNonHeap = ((jvmNonHeapDif/jvmNonHeap)*100) == 0 ? 1 : ((jvmNonHeapDif/jvmNonHeap)*100);
					 float rogJvmTotal = ((jvmTotalDif/jvmTotal)*100) == 0 ? 4 : ((jvmTotalDif/jvmTotal)*100);
					 float rogJvmNative = ((jvmNativeDif/jvmNative)*100) == 0 ? 1 : ((jvmNativeDif/jvmNative)*100);
					 float rogAvgRespTime = ((avgRespTimeDif/avgRespTime)*100) == 0 ? 1 : ((avgRespTimeDif/avgRespTime)*100);
					 float rogRequestRate = ((requestRateDif/requestRate)*100) == 0 ? 5000 : ((requestRateDif/requestRate)*100);
					 float rogGcCollection = ((gcCollectionDif/gcCollection)*100) == 0 ? 30 : ((gcCollectionDif/gcCollection)*100);
					 float rogPauseDu = ((pauseDuDif/pauseDu)*100) == 0 ? 3 : ((pauseDuDif/pauseDu)*100);
					 float rogGcAllocated = ((gcAllocatedDif/gcAllocated)*100) == 0 ? 50 : ((gcAllocatedDif/gcAllocated)*100);
					 float rogDataRead = ((dataReadDif/dataRead)*100) == 0 ? 50000 : ((dataReadDif/dataRead)*100);
					 float rogDataWrite= ((dataWriteDif/dataWrite)*100) == 0 ? 10000 : ((dataWriteDif/dataWrite)*100);
					 float rogDataLatency= ((dataLatencyDif/dataLatency)*100) == 0 ? 500 : ((dataLatencyDif/dataLatency)*100);
					 float rogDataCon= ((dataConDif/dataCon)*100) == 0 ? 30 : ((dataConDif/dataCon)*100);
					 
					 JSONObject nobj = (JSONObject)jobj.clone();
					 
					 nobj.put("timeInterval", new Timestamp(startTime).toString());
					 nobj.put("perfCpuUsage", String.valueOf(Math.abs((int)rogCpu)));
					 nobj.put("perfMemUsage", String.valueOf(Math.abs((int)rogMem)));
					 nobj.put("perfHeapUsage", String.valueOf(Math.abs((int)rogHeap)));
					 nobj.put("perfNonHeapUsage", String.valueOf(Math.abs((int)rogNonHeap)));
					 nobj.put("perfNoOfInstances", String.valueOf(Math.abs((int)rogNoOfInstance)));
					 nobj.put("perfJvmHeap", String.valueOf(Math.abs((int)rogJvmHeap)));
					 nobj.put("perfJvmNonHeap", String.valueOf(Math.abs((int)rogJvmNonHeap)));
					 nobj.put("perfJvmTotal", String.valueOf(Math.abs((int)rogJvmTotal)));
					 nobj.put("perfJvmNative", String.valueOf(Math.abs((int)rogJvmNative)));
					 nobj.put("perfIoAvgRespTime", String.valueOf(Math.abs((int)rogAvgRespTime)));
					 nobj.put("perfIoRequestRate", String.valueOf(Math.abs((int)rogRequestRate)));
					 nobj.put("perfGcCollection", String.valueOf(Math.abs((int)rogGcCollection)));
					 nobj.put("perfGcPauseDuration", String.valueOf(Math.abs((int)rogPauseDu)));
					 nobj.put("perfGcAllocated", String.valueOf(Math.abs((int)rogGcAllocated)));
					 nobj.put("perfDataRead", String.valueOf(Math.abs((int)rogDataRead)));
					 nobj.put("perfDataWrite", String.valueOf(Math.abs((int)rogDataWrite)));
					 nobj.put("perfDataLatency", String.valueOf(Math.abs((int)rogDataLatency)));
					 nobj.put("perfDataConnection", String.valueOf(Math.abs((int)rogDataCon)));
					 
					 prevCpuUse = cpuUse;
		        	 prevMemUse = memUse;
		        	 prevHeapUse = heapUse;
		        	 prevNonHeapUse = nonHeapUse;
		        	 prevNoOfInstance=noOfInstance;
		        	 prevJvmHeap = jvmHeap;
		        	 prevJvmNonHeap = jvmNonHeap;
		        	 prevJvmTotal = jvmTotal;
		        	 prevJvmNative = jvmNative;
		        	 prevAvgRespTime = avgRespTime;
		        	 prevRequestRate=requestRate;
		        	 prevGcCollection=gcCollection;
		        	 prevPauseDu=pauseDu;
		        	 prevGcAllocated=gcAllocated;
		        	 prevDataRead=dataRead;
		        	 prevDataWrite=dataWrite;
		        	 prevDataLatency=dataLatency;
		        	 prevDataCon=dataCon;
		        	 
		        	 ary.add(nobj);
		        	 startTime = startTime + pause;
		        	 
				}
				System.out.println("Total Records : "+ary.size());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ary;
	}
	
	public static void main(String a[]) throws ParseException, IOException {
		String SOURCE_JSON_FILE = "D:\\mycode\\json-data\\services.json";
		String TARGET_CSV_FILE = "D:\\mycode\\json-data\\TEST_SERVICES.csv";
		System.out.println("Generating json data .......");
		JSONArray ary = createBulkAnalyticData("05-01-2022 01:00", "05-08-2022 01:00", SOURCE_JSON_FILE);
		System.out.println("Json data generated");
		System.out.println("Writing json data in CSV file.......");
		convertToCsv(ary, TARGET_CSV_FILE);
		System.out.println("Process completed. CSV file:  "+TARGET_CSV_FILE);
	}
	
	public static void convertToCsv(JSONArray ary, String targetCsvFile) throws IOException {
		String CSV_FILE_PATH = targetCsvFile;
		JsonNode jsonTree = new ObjectMapper().readTree(ary.toString());
		File csvFile = new File(CSV_FILE_PATH);
        Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
        CsvSchema csvSchema = csvSchemaBuilder
            .build()
            .withHeader();
        
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(Feature.IGNORE_UNKNOWN, true);
        csvMapper.writerFor(JsonNode.class)
            .with(csvSchema)
            .writeValue(csvFile, jsonTree);
		

	}
}

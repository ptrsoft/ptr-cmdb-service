// To use this code, add the following Maven dependency to your project:
//
//
//     com.fasterxml.jackson.core     : jackson-databind          : 2.9.0
//     com.fasterxml.jackson.datatype : jackson-datatype-jsr310   : 2.9.0
//
// Import this package:
//
//     import io.quicktype.Converter;
//
// Then you can deserialize a JSON string with
//
//     Welcome data = Converter.fromJsonString(jsonString);

package com.synectiks.asset.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Converter {
	// Date-time helpers

	private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendOptional(DateTimeFormatter.ISO_DATE_TIME).appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
			.appendOptional(DateTimeFormatter.ISO_INSTANT)
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX"))
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toFormatter().withZone(ZoneOffset.UTC);

	public static OffsetDateTime parseDateTimeString(String str) {
		return ZonedDateTime.from(Converter.DATE_TIME_FORMATTER.parse(str)).toOffsetDateTime();
	}

	private static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendOptional(DateTimeFormatter.ISO_TIME).appendOptional(DateTimeFormatter.ISO_OFFSET_TIME)
			.parseDefaulting(ChronoField.YEAR, 2020).parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
			.parseDefaulting(ChronoField.DAY_OF_MONTH, 1).toFormatter().withZone(ZoneOffset.UTC);

	public static OffsetTime parseTimeString(String str) {
		return ZonedDateTime.from(Converter.TIME_FORMATTER.parse(str)).toOffsetDateTime().toOffsetTime();
	}

	public static <T> T fromJsonString(String json, Class cls) throws IOException {
		return getObjectReader(cls).readValue(json);
	}

	public static String toJsonString(Object obj, Class cls) throws JsonProcessingException {
		return getObjectWriter(cls).writeValueAsString(obj);
	}

	public static String toPrettyJsonString(Object obj, Class cls) throws JsonProcessingException {
		return getObjectWriter(cls).with(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
	}

	private static ObjectReader reader;
	private static ObjectWriter writer;

	private static void instantiateMapper(Class cls) {
		ObjectMapper mapper = Constants.instantiateMapper();
		reader = mapper.readerFor(cls);
		writer = mapper.writerFor(cls);
	}

	private static ObjectReader getObjectReader(Class cls) {
		if (reader == null)
			instantiateMapper(cls);
		return reader;
	}

	private static ObjectWriter getObjectWriter(Class cls) {
		if (writer == null)
			instantiateMapper(cls);
		return writer;
	}
}

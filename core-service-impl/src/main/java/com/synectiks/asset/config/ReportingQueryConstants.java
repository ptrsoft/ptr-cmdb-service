package com.synectiks.asset.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

/**
 * Application ReportingQueryConstants.
 */
public final class ReportingQueryConstants {

    public static String SPEND_OVERVIEW="with s as (\t\n" +
            "WITH ServiceCosts AS (\n" +
            "   SELECT\n" +
            "       ce.service_category,\n" +
            "       SUM(CAST(jb.value AS int)) AS total,\n" +
            "       null as percentage\n" +
            "   FROM\n" +
            "       cloud_element ce,\n" +
            "       landingzone l,\n" +
            "       department d,\n" +
            "       organization o,\n" +
            "        jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)\n" +
            "   WHERE\n" +
            "       l.department_id = d.id\n" +
            "       AND d.organization_id = o.id\n" +
            "       AND ce.landingzone_id = l.id\n" +
            "       AND jb.key >= ? AND jb.key <= ? \n" +
            "       and upper(l.cloud) = upper(?)\n" +
            "       AND o.id = ? \n" +
            "       #DYNAMIC_CONDITION# \n" +
            "   GROUP BY\n" +
            "       ce.service_category\n" +
            ")\n" +
            "SELECT service_category, total, percentage FROM ServiceCosts),\n" +
            "ps as ( with psum as (select sum(total) as sumall from s)\tselect sumall from psum), \n" +
            "res as (select  s.service_category, s.total, ROUND((s.total * 100.0 / ps.sumall), 2) AS percentage from s, ps\n" +
            "union all\n" +
            "select  'Grand Total' as service_category, ps.sumall as total, null as percentage from ps)\n" +
            "select ROW_NUMBER() OVER () as id, service_category, total, percentage from res\n";
    private ReportingQueryConstants() {
    }
}

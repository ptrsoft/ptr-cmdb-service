package com.synectiks.asset.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.synectiks.asset.config.Constants;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonAndObjectConverterUtil {

    private ObjectReader getObjectReader(ObjectMapper mapper, Class cls) {
    	if(mapper != null) {
    		return mapper.readerFor(cls);
    	}
        return Constants.instantiateMapper().readerFor(cls);
    }

    private ObjectWriter getObjectWriter(ObjectMapper mapper, Class cls) {
    	if(mapper != null) {
    		return mapper.writerFor(cls);
    	}
        return Constants.instantiateMapper().writerFor(cls);
    }

    /**
     *
     * @param json Source json string
     * @param cls Class type of target java object
     * @return
     * @param <T>
     * @throws IOException
     * It converts json string to java object
     *
     */
    public <T> T convertJsonStringToObject(ObjectMapper mapper, String json, Class cls) throws IOException {
        return getObjectReader(mapper, cls).readValue(json);
    }

    /**
     *
     * @param sourceObject
     * @param sourceObjectClass
     * @return
     * @throws JsonProcessingException
     * It converts java object to json string without indentation (e.g convertObjectToJsonString(car, Car.class))
     * It takes object and its class if Car referect is passed in source, target will be Car.class
     */
    public String convertObjectToJsonString(ObjectMapper mapper, Object sourceObject, Class sourceObjectClass) throws JsonProcessingException {
        return getObjectWriter(mapper, sourceObjectClass).writeValueAsString(sourceObject);
    }

    /**
     *
     * @param obj
     * @param cls
     * @return
     * @throws JsonProcessingException
     * It converts java object to json string with indentation
     */
    public String convertObjectToPrettyJsonString(ObjectMapper mapper, Object obj, Class cls) throws JsonProcessingException {
        return getObjectWriter(mapper, cls).with(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
    }

    /**
     *
     * @param source java object
     * @param target Class type of target java object (e.g. String.class)
     * @return
     * @param <T>
     * @throws IOException
     * It convert source java object into target java object
     */
    public <T> T convertSourceObjectToTarget(ObjectMapper mapper, Object source, Class target) throws IOException {
    	String strJson = null;
    	if(mapper != null) {
        	strJson = mapper.writeValueAsString(source);
        }else {
        	strJson = Constants.instantiateMapper().writeValueAsString(source);
        }
        return convertJsonStringToObject(mapper, strJson, target);
    }
}

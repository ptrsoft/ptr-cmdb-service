package com.synectiks.asset.business.service;

import com.google.gson.Gson;
import com.synectiks.asset.response.catalogue.CatalogueResponse;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CatalogueConverter implements AttributeConverter<CatalogueResponse, String> {

	  private final static Gson GSON = new Gson();

	  @Override
	  public String convertToDatabaseColumn(CatalogueResponse mjo) {
	    return GSON.toJson(mjo);
	  }

	  @Override
	  public CatalogueResponse convertToEntityAttribute(String dbData) {
	    return GSON.fromJson(dbData, CatalogueResponse.class);
	  }
}
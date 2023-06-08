package com.synectiks.asset.repository;

import com.synectiks.asset.business.domain.Catalogue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CatalogueRepositoryCustomImpl implements CatalogueRepositoryCustom {
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogueRepositoryCustomImpl.class);
			
	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Catalogue> findCatalogue(String whereCondition){
		String QUERY = "SELECT * FROM catalogue ";
		if(!StringUtils.isBlank(whereCondition)) {
			QUERY = QUERY + " WHERE details @> '"+whereCondition+"'";
		}
		logger.debug("Jsonb catalogue query: {} ",QUERY);
		Query query = entityManager.createNativeQuery(QUERY, Catalogue.class);
		return query.getResultList();
	}
	
}

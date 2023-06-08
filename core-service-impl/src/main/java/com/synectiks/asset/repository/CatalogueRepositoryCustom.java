package com.synectiks.asset.repository;

import com.synectiks.asset.business.domain.Catalogue;

import java.util.List;

public interface CatalogueRepositoryCustom {
	List<Catalogue> findCatalogue(String whereCondition);
}

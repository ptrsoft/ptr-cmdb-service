package com.synectiks.asset.business.service;

import com.google.gson.Gson;
import com.synectiks.asset.business.domain.Catalogue;
import com.synectiks.asset.repository.CatalogueRepository;
import com.synectiks.asset.response.catalogue.CloudDashboard;
import com.synectiks.asset.response.catalogue.Datasource;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogueService {
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogueService.class);
		
	@Autowired
	CatalogueRepository catalogueRepository;
	
	public Optional<Catalogue> getCatalogue(Long id) {
		logger.info("Get catalogue by id: {}", id);
		return catalogueRepository.findById(id);
	}
	
	public List<Catalogue> getAllCatalogue() {
		logger.info("Get all catalogue");
		return catalogueRepository.findAll(Sort.by(Direction.DESC, "id"));
	}
	
	public Optional<Catalogue> deleteCatalogue(Long id) {
		logger.info("Delete catalogue by id: {}", id);
		Optional<Catalogue> oObj = getCatalogue(id);
		if(!oObj.isPresent()) {
			logger.warn("Id {} not found. Catalogue cannot be deleted", id);
			return oObj;
		}
		catalogueRepository.deleteById(id);
		return oObj;
	}
	
	public Catalogue createCatalogue(Catalogue obj){
		logger.info("Create new catalogue");
		return catalogueRepository.save(obj);
	}
	
	public Catalogue updateCatalogue(Catalogue obj){
		logger.info("Update catalogue. Id: {}", obj.getId());
		if(!catalogueRepository.existsById(obj.getId())) {
			throw new BadRequestAlertException("Entity not found", "Catalogue", "idnotfound");
		}
		return catalogueRepository.save(obj);
	}
	
	public Optional<Catalogue> partialUpdateCatalogue(Catalogue obj){
		logger.info("Update catalogue partialy. Id: {}", obj.getId());
		if(!catalogueRepository.existsById(obj.getId())) {
			throw new BadRequestAlertException("Entity not found", "Catalogue", "idnotfound");
		}
		
		Optional<Catalogue> result = catalogueRepository.findById(obj.getId())
			.map(existingObj ->{
				return existingObj;
			})
			.map(catalogueRepository::save);
		return result;
	}
	
//	public Catalogue searchCatalogue(Map<String, String> obj) {
//		logger.info("Search catalogue");
//		Gson gson = new Gson(); 
//		String json = gson.toJson(obj); 
//		List<Catalogue> list = catalogueRepository.findCatalogue(json);
//		if(list.size() > 0) {
//			return list.get(0);
//		}
//		return null;
//	}

	public List<Catalogue> searchAllCatalogue(Map<String, String> obj) {
		logger.info("Search catalogue");
		Gson gson = new Gson(); 
		String json = gson.toJson(obj); 
		List<Catalogue> list = catalogueRepository.findCatalogue(json);
		return list;
	}
	
	public Catalogue searchCatalogue(Map<String, String> obj) {
		logger.info("Search catalogue");
		List<Catalogue> catalogueList = getAllCatalogue();
		if(catalogueList == null || (catalogueList != null && catalogueList.size() ==0)) {
			return Catalogue.builder().build();
		}
		Catalogue catalogue = catalogueList.get(0);
		List<CloudDashboard> cloudDashBoards = catalogue.getDetails().getOps().getCloudDashBoards();
		List<CloudDashboard> list2 = cloudDashBoards;
		if(obj.containsKey("id")) {
			int id = Integer.parseInt(obj.get("id"));
			list2 = list2.stream()
					.filter(cd -> (cd.getId() == id))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("name")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getName()).equals(obj.get("name")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("description")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getDescription()).equals(obj.get("description")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedDataSourceType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedDataSourceType()).equals(obj.get("associatedDataSourceType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedDataType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedDataType()).equals(obj.get("associatedDataType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedSLAType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedSLAType()).equals(obj.get("associatedSLAType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedCloud")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedCloud()).equals(obj.get("associatedCloud")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedCloudElementType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedCloudElementType()).equals(obj.get("associatedCloudElementType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("jsonLocation")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getJsonLocation()).equals(obj.get("jsonLocation")))
					.collect(Collectors.toList());
		}
		catalogue.getDetails().getOps().setCloudDashBoards(list2);
		return catalogue;
	}
	
	public List<CloudDashboard> searchCatalogue(Map<String, String> obj, List<CloudDashboard> cloudDashBoards) {
		logger.info("Search catalogue from provided list of dashboards");
//		List<Catalogue> catalogueList = getAllCatalogue();
//		if(catalogueList == null || (catalogueList != null && catalogueList.size() ==0)) {
//			return Catalogue.builder().build();
//		}
//		Catalogue catalogue = catalogueList.get(0);
//		List<CloudDashboard> cloudDashBoards = catalogue.getDetails().getOps().getCloudDashBoards();
		List<CloudDashboard> list2 = cloudDashBoards;
		if(obj.containsKey("id")) {
			int id = Integer.parseInt(obj.get("id"));
			list2 = list2.stream()
					.filter(cd -> (cd.getId() == id))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("name")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getName()).equals(obj.get("name")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("description")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getDescription()).equals(obj.get("description")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedDataSourceType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedDataSourceType()).equals(obj.get("associatedDataSourceType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedDataType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedDataType()).equals(obj.get("associatedDataType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedSLAType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedSLAType()).equals(obj.get("associatedSLAType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedCloud")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedCloud()).equals(obj.get("associatedCloud")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("associatedCloudElementType")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getAssociatedCloudElementType()).equals(obj.get("associatedCloudElementType")))
					.collect(Collectors.toList());
		}
		if(obj.containsKey("jsonLocation")) {
			list2 = list2.stream()
					.filter(cd -> (cd.getJsonLocation()).equals(obj.get("jsonLocation")))
					.collect(Collectors.toList());
		}
		
		return list2;
	}
	
	public Map<String, List<CloudDashboard>> filterCatalogueByDataSource(){
		Map<String, List<CloudDashboard>> result = new HashMap<>();
		List<Catalogue> catalogueList = getAllCatalogue();
		if(catalogueList == null || (catalogueList != null && catalogueList.size() ==0)) {
			return null;
		}
		Catalogue catalogue = catalogueList.get(0);
		List<Datasource> datasourceList = catalogue.getDetails().getOps().getDataSources();
		Map<String, String> searchMap = new HashMap<>();
		for(Datasource ds: datasourceList) {
			searchMap.clear();
			searchMap.put("associatedDataSourceType", ds.getName());
			Catalogue resultCatalogue = searchCatalogue(searchMap);
			result.put(ds.getName(), resultCatalogue.getDetails().getOps().getCloudDashBoards());
		}
		return result;
	}
}

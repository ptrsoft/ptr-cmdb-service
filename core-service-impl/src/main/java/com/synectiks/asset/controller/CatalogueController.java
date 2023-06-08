package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.Catalogue;
import com.synectiks.asset.business.service.CatalogueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CatalogueController {
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogueController.class);
	
	@Autowired
	CatalogueService catalogueService;
	
	@GetMapping("/catalogue/{id}")
	public ResponseEntity<Catalogue> getCatalogue(@PathVariable Long id) {
		logger.info("Request to get catalogue. Id: "+id);
		Optional<Catalogue> oObj = catalogueService.getCatalogue(id);
		if(oObj.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(oObj.get());
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@GetMapping("/catalogue")
	public ResponseEntity<List<Catalogue>> getAllCatalogue() {
		logger.info("Request to get catalogue");
		List<Catalogue> list = catalogueService.getAllCatalogue();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@DeleteMapping("/catalogue/{id}")
	public ResponseEntity<Optional<Catalogue>> deleteCatalogue(@PathVariable Long id) {
		logger.info("Request to delete catalogue by id: {}", id);
		Optional<Catalogue> oObj = catalogueService.deleteCatalogue(id);
		return ResponseEntity.status(HttpStatus.OK).body(oObj);
	}
	
	@PostMapping("/catalogue")
	public ResponseEntity<Catalogue> createCatalogue(@RequestBody Catalogue obj){
		logger.info("Request to create new catalogue");
		Catalogue spa = catalogueService.createCatalogue(obj);
		return ResponseEntity.status(HttpStatus.OK).body(spa);
	}
	
	@PutMapping("/catalogue")
	public ResponseEntity<Catalogue> updateCatalogue(@RequestBody Catalogue obj){
		logger.info("Request to update catalogue");
		Catalogue spa = catalogueService.updateCatalogue(obj);
		return ResponseEntity.status(HttpStatus.OK).body(spa);
	}
	
	@PatchMapping("/catalogue")
	public ResponseEntity<Optional<Catalogue>> partialUpdateCatalogue(@RequestBody Catalogue obj){
		logger.info("Request to partially update catalogue");
		Optional<Catalogue> oObj = catalogueService.partialUpdateCatalogue(obj);
		return ResponseEntity.status(HttpStatus.OK).body(oObj);
	}
	
	@GetMapping("/catalogue/search")
	public ResponseEntity<Catalogue> searchCatalogue(@RequestParam Map<String, String> obj){
		logger.info("Request to search catalogue");
		Catalogue catalogue = catalogueService.searchCatalogue(obj);
		return ResponseEntity.status(HttpStatus.OK).body(catalogue);
	}
	
	@GetMapping("/catalogue/search-all")
	public ResponseEntity<List<Catalogue>> searchAllCatalogue(@RequestParam Map<String, String> obj){
		logger.info("Request to search catalogue");
		List<Catalogue> list = catalogueService.searchAllCatalogue(obj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
}

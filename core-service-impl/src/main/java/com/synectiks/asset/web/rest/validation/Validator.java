package com.synectiks.asset.web.rest.validation;

import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class Validator {

    public void validateNotNull(Long id, String entityName) throws BadRequestAlertException {
        if(id != null){
            throw new BadRequestAlertException(String.format("A new %s cannot already have an ID", entityName), entityName, "idexists");
        }
    }

    public void validateNull(Object object, String entityName) throws BadRequestAlertException {
        if(object == null){
            throw new BadRequestAlertException(String.format("Null object", entityName), entityName, "null");
        }
    }

    public void validateNull(Long id, String entityName) throws BadRequestAlertException {
        if(id == null){
            throw new BadRequestAlertException(String.format("Null id", entityName), entityName, "idnull");
        }
    }

    public void validateEntityExistsInDb(Long id, String entityName, JpaRepository repository){
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", entityName, "idnotfound");
        }
    }
}

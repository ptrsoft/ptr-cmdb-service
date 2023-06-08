package com.synectiks.asset.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityMapper <D, E> {

    D toDto(E entity);

    E toEntity(D dto);

    default D entityToDto(E entity){
        return null;
    }

    default E dtoToEntity(D dto){
        return null;
    }

    default List <D> entityToDtoList(List<E> entityList) {
        return null;
    }

    default List <E> dtoToEntityList(List<D> dtoList) {
        return null;
    }

}

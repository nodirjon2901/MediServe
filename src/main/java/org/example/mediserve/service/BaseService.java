package org.example.mediserve.service;

import java.util.List;
import java.util.UUID;

/**
 * @author Nodir Tojiahmedov
 * @param <RD> Response DTO
 * @param <CD> Create DTO
 * */
public interface BaseService<RD, CD> {

    void save(CD cd);

    void deleteById(UUID id);

    RD update(CD cd, UUID id);

    RD findById(UUID id);

    List<RD> findAll();

}

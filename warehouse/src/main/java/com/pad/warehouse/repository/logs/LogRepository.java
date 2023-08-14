package com.pad.warehouse.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pad.warehouse.model.entity.LogEntity;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    
    
}

package com.pad.warehouse.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pad.warehouse.model.entity.ErrorLogEntity;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, Long> {

    
}

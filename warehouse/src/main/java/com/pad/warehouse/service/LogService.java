package com.pad.warehouse.service;

import org.springframework.stereotype.Service;

import com.pad.warehouse.exception.AbstractException;
import com.pad.warehouse.model.entity.ErrorLogEntity;
import com.pad.warehouse.model.entity.LogEntity;
import com.pad.warehouse.model.enums.ProductLogType;
import com.pad.warehouse.repository.logs.ErrorLogRepository;
import com.pad.warehouse.repository.logs.LogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
    
    private final ErrorLogRepository errorLogRepository;
    private final LogRepository logRepository;

    public void saveToErrorLog(AbstractException exception) {
        ErrorLogEntity errorLog = new ErrorLogEntity();
        errorLog.setErrorCode(String.valueOf(exception.getCode()));
        errorLog.setMessage(exception.getMessage());
        errorLog.setStackTrace(exception.getStackTrace().toString());
        errorLogRepository.save(errorLog);
    }

    public void saveToLog(Long entityId, ProductLogType type, String message) {
        System.out.println("this shit ok");
        LogEntity logEntity = new LogEntity();
        logEntity.setEntityId(entityId);
        logEntity.setMessage(message);
        logEntity.setType(type);
        logRepository.save(logEntity);
    }

    
}

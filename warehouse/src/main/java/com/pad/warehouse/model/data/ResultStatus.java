package com.pad.warehouse.model.data;

import java.util.HashMap;
import java.util.Map;

public class ResultStatus {
    
    private boolean isSuccess;

    private Map<String, String> errors;

    private Object result;

    public ResultStatus() {
        this.isSuccess = true;
        this.errors = new HashMap<>();
    }

}

package com.pad.warehouse.mappers;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.pad.warehouse.swagger.model.ResponseHeader;

public class ResponseHeaderMapper {
    

    public static ResponseHeader createResponseHeader(UUID requestId) {
        // TODO TEMP
        ResponseHeader header = new ResponseHeader();
        header.setRequestId(requestId);
        header.setTimestamp(OffsetDateTime.now());
        return header;
    }
}

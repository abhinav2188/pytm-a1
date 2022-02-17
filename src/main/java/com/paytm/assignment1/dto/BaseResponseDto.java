package com.paytm.assignment1.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class BaseResponseDto {
    private HttpStatus status;
    private Object data;
    private String errorMsg;
    private String msg;
}

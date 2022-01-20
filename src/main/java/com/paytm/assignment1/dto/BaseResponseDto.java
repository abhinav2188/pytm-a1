package com.paytm.assignment1.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BaseResponseDto {
    private HttpStatus status;
    private Object data;
    private String errorMsg;
}

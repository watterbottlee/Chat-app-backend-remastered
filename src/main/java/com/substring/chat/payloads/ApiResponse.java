package com.substring.chat.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String httpStatus;
    private LocalDateTime timeStamp ;
}

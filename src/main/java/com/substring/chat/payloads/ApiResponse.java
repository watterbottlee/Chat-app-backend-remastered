package com.substring.chat.payloads;

import com.substring.chat.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ApiResponse {
    private boolean success;
    private List<Message> Data;
    private String HttpStatus;
    private LocalDateTime localDateTime;
}

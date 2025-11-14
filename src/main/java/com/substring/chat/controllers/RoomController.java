package com.substring.chat.controllers;

import com.substring.chat.config.AppConstants;
import com.substring.chat.entities.Message;
import com.substring.chat.entities.Room;
import com.substring.chat.payloads.ApiResponse;
import com.substring.chat.payloads.RoomRequest;
import com.substring.chat.repositories.RoomRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/rooms")
@CrossOrigin(
        originPatterns = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository){
        this.roomRepository=roomRepository;
    }
    //create room
    @PostMapping("create-room")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomRequest){

        Room room = roomRepository.findByRoomId(roomRequest.getRoomId());

        if(room != null){
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(
                            false,
                            "room already exists",
                            null,
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    )
            );
        }
        if(roomRequest.getPassword().length()<4){
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(
                            false,
                            "password should be of 4 or more characters",
                            null,
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    )
            );

        }
        Room newRoom = new Room();
        newRoom.setRoomId(roomRequest.getRoomId());
        newRoom.setPassword(roomRequest.getPassword());
        Room savedRoom = roomRepository.save(newRoom);

        ApiResponse<Room> apiResponse = new ApiResponse<>(
                true,
                "room created successfully",
                savedRoom,
                HttpStatus.CREATED.toString(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @PostMapping("join-room")
    public ResponseEntity<?> joinRoom(@RequestBody RoomRequest roomRequest){
        log.info("someone called this method");
    	Room room = roomRepository.findByRoomId(roomRequest.getRoomId());
    	if(room==null) {
            log.info("room not found");
    		return ResponseEntity.badRequest().body(
                    new ApiResponse<>(
                            false,
                            "room does not exists",
                            null,
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    )
            );
    	}else{
            if(room.getPassword().equals(roomRequest.getPassword())){
                log.info("got the room , returned it");
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "room joined successfully",
                                null,
                                HttpStatus.OK.toString(),
                                LocalDateTime.now()
                        )
                );
            }else{
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(
                                false,
                                "wrong password",
                                null,
                                HttpStatus.BAD_REQUEST.toString(),
                                LocalDateTime.now()
                        )
                );
            }
        }
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable String roomId,
            @RequestParam(value="page", defaultValue="0", required=false) int page,
            @RequestParam(value="size", defaultValue="20", required=false) int size) {

        log.info("Fetching messages for room: {}, page: {}, size: {}", roomId, page, size);

        Room room = roomRepository.findByRoomId(roomId);
        if(room == null) {
            log.warn("Room not found: {}", roomId);
            return ResponseEntity.badRequest().build();
        }

        List<Message> messages = room.getMessages();
        if(messages == null || messages.isEmpty()) {
            log.info("No messages in room: {}", roomId);
            return ResponseEntity.ok(Collections.emptyList());
        }

        int totalMessages = messages.size();
        int start = Math.max(0, totalMessages - (page + 1) * size);
        int end = Math.max(0, totalMessages - page * size);

        List<Message> paginatedMessages = messages.subList(start, end);

        log.info("Returning {} messages for room: {}", paginatedMessages.size(), roomId);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "messages retrieved successfully",
                paginatedMessages,
                HttpStatus.OK.toString(),
                LocalDateTime.now()
        ));
    }
}

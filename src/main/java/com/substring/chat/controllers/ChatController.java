package com.substring.chat.controllers;

import java.time.LocalDateTime;

import com.mongodb.client.result.UpdateResult;
import com.substring.chat.config.AppConstants;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import com.substring.chat.entities.Message;
import com.substring.chat.entities.Room;
import com.substring.chat.payloads.MessageRequest;
import com.substring.chat.repositories.RoomRepository;

@Controller
@CrossOrigin(AppConstants.FRONT_END_BASE_URL)
public class ChatController {
    private final RoomRepository roomRepository;
    private final MongoTemplate mongoTemplate;

    public ChatController(RoomRepository roomRepository, MongoTemplate mongoTemplate, SimpMessagingTemplate messagingTemplate) {
        this.roomRepository = roomRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request) {

        System.out.println("=== MESSAGE RECEIVED ===");
        System.out.println("Room ID: " + roomId);
        System.out.println("Sender: " + request.getSender());
        System.out.println("Content: " + request.getContent());

        // Validate room exists FIRST
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            System.err.println("Room not found: " + roomId);
            throw new RuntimeException("Room not found: " + roomId);
        }

        // Create the message
        Message message = new Message();
        message.setContent(request.getContent().trim());
        message.setSender(request.getSender().trim());
        message.setTimestamp(LocalDateTime.now());

        // Atomic update - push message to the messages array
        Query query = new Query(Criteria.where("roomId").is(roomId));
        Update update = new Update().push("messages", message);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Room.class);

        if (result.getModifiedCount() == 0) {
            System.err.println("Failed to save message to room: " + roomId);
            throw new RuntimeException("Failed to save message");
        }

        System.out.println("Message saved and will be broadcast!");

        // Remove this line - @SendTo handles it automatically
        // messagingTemplate.convertAndSend("/topic/room/" + roomId, message);

        return message;  // This gets broadcast to /topic/room/{roomId}
    }
}
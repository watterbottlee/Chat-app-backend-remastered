# WebSocket Chat Application (A Tinkering Project)

A real-time chat application built with Spring Boot and WebSocket technology. Create password-protected chat rooms and communicate instantly with other users through a simple, efficient messaging system.

## Author notes
this repo is made just to explore websockets and thats the reason i havent implemented user login and sessions. this repo is to cater my experience in websockets and to make students / other newcommers a resource 
to learn websockets with spring boot in a simplest way possible. every block is as loosely coupled as possible. and this is only the signaling server for the sockets i will try to provide a simple minimalist react app to facilitate this socket channels and make a simple chat app. so stay tuned and follow me. i will add the react repo here later on. so if you are a dev like me feel free to fork, clone, pull and add features.

## Features

- **Real-time Messaging**: Instant message delivery using WebSocket protocol
- **Room-based Chat**: Create and join dedicated chat rooms
- **Password Protection**: Secure your rooms with password authentication
- **Message History**: Paginated message retrieval for chat history
- **REST + WebSocket**: Hybrid architecture combining REST APIs for room management and WebSocket for real-time chat

## sample api test results
https://github.com/user-attachments/assets/4c3b9f6e-8694-40c1-ab17-cdd4249975da

## 🏗️ Project Structure

```
chat-app-backend/
├── src/main/java/com/substring/chat/
│   ├── ChatAppBackendApplication.java    # Main application entry point
│   ├── config/
│   │   ├── AppConstants.java             # Application-wide constants
│   │   ├── CorsConfig.java               # CORS configuration
│   │   └── WebSocketConfig.java          # WebSocket setup
│   ├── controllers/
│   │   ├── ChatController.java           # WebSocket message handling
│   │   └── RoomController.java           # REST API for rooms
│   ├── entities/
│   │   ├── Message.java                  # Message entity
│   │   └── Room.java                     # Room entity
│   ├── payloads/
│   │   ├── ApiResponse.java              # Standard API response wrapper
│   │   ├── MessageRequest.java           # Message request DTO
│   │   └── RoomRequest.java              # Room request DTO
│   └── repositories/
│       └── RoomRepository.java           # Data access layer
└── src/main/resources/
    └── application.properties            # Application configuration
```


### Prerequisites

- Java 21
- Maven 3.6.3+
- MongoDB instance (local or cloud)

### Installation

1. Clone the repository
```bash
git clone https://github.com/watterbottlee/Chat-app-backend-remastered
cd chat-app-backend
```

2. Configure your database in `src/main/resources/application.properties`

3. Build the project
```bash
./mvnw clean install
```

4. Run the application
```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`

### Swagger UI

Once the application is running, you can access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## 📡 API Documentation

### REST Endpoints

#### Create Room
```http
POST /api/v1/rooms/create-room
Content-Type: application/json

{
  "roomId": "test-room",
  "password": "secret123"
}
```

#### Join Room
```http
POST /api/v1/rooms/join-room
Content-Type: application/json

{
  "roomId": "test-room",
  "password": "secret123"
}
```

#### Get Messages
```http
GET /api/v1/rooms/{roomId}/messages?page=0&size=20
```

**Query Parameters:**
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Messages per page

### WebSocket Connection

Connect to the WebSocket endpoint to start real-time messaging:

```
ws://localhost:8080/ws
```

Subscribe to a room's message channel:
```
/topic/room/{roomId}
```

Send messages to a room:
```
/app/chat/{roomId}
```

## 🔧 Technology Stack

- **Backend Framework**: Spring Boot 3.4.3
- **Real-time Communication**: Spring WebSocket + STOMP
- **Database**: MongoDB
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI 3.1 (Swagger UI)
- **Development Tools**: Spring DevTools, Lombok

## 💡 Usage Example

### Using REST Client (e.g., curl)

1. **Create a room:**
```bash
curl -X POST http://localhost:8080/api/v1/rooms/create-room \
  -H "Content-Type: application/json" \
  -d '{"roomId":"gaming","password":"play123"}'
```

2. **Join the room:**
```bash
curl -X POST http://localhost:8080/api/v1/rooms/join-room \
  -H "Content-Type: application/json" \
  -d '{"roomId":"gaming","password":"play123"}'
```

3. **Fetch message history:**
```bash
curl http://localhost:8080/api/v1/rooms/gaming/messages?page=0&size=20
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
---

⭐ Star this repo if you find it useful!

# Notely

Notely is a real-time collaborative note-taking application built with Spring Boot and WebSockets. It allows multiple users to edit a shared text pad simultaneously, with changes reflected instantly across all connected clients. The application features optimistic locking to handle version conflicts gracefully.

**Access the  Live Application:**
Open your web browser and navigate to:  https://notely-kh3m.onrender.com/

## Features

*   **Real-Time Collaboration**: Changes made by one user are broadcast to all other clients viewing the same pad in real-time.
*   **WebSocket Communication**: Utilizes STOMP over WebSockets for efficient, low-latency client-server communication.
*   **Version Conflict Handling**: Implements an optimistic locking mechanism to prevent lost updates. If a user tries to save an outdated version of a pad, they are notified of the conflict and prompted to load the latest version.
*   **Simple Pad Management**: Create, retrieve, and update pads using simple, shareable IDs.
*   **RESTful API**: Provides a clean REST API for managing pads.
*   **Persistent Storage**: Uses an Postgres file-based database to persist pad content.

## Tech Stack

*   **Backend**:
    *   Java 21
    *   Spring Boot
    *   Spring WebSocket (for STOMP messaging)
    *   Spring Data JPA
    *   Hibernate
*   **Database**:
    *   Postgres Database Engine
*   **Frontend**:
    *   HTML5
    *   CSS3
    *   Vanilla JavaScript with STOMP.js
*   **Build & Deployment**:
    *   Maven
    *   Docker
    *   GitHub Actions for CI

## Getting Started

### Prerequisites

*   Java JDK 21 or later
*   Maven
*   Docker (for containerized deployment)

### Running Locally

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/shikhar302001/Notely.git
    cd Notely
    ```

2.  **Run the application using the Maven wrapper:**
    *   On macOS/Linux:
        ```sh
        ./mvnw spring-boot:run
        ```
    *   On Windows:
        ```sh
        .\mvnw.cmd spring-boot:run
        ```

3.  **Access the application:**
    Open your web browser and navigate to `http://localhost:10000`.

### Running with Docker

1.  **Build the Docker image:**
    ```sh
    docker build -t notely .
    ```

2.  **Run the container:**
    ```sh
    docker run -p 10000:10000 notely
    ```

3.  **Access the application:**
    Open your web browser and navigate to `http://localhost:10000`.

## How to Use

1.  Open the application in your browser.
2.  Enter a unique identifier for your note in the "Enter PAD ID" field and click **Load Pad**. If a pad with that ID exists, its content will be loaded. Otherwise, a new, empty pad will be created.
3.  To collaborate, share the Pad ID with others. They can enter the same ID on their devices to access the shared pad.
4.  Type your notes in the text area.
5.  Click **Save Changes** to save your content. Your changes will be broadcast to all other users viewing the same pad.
6.  If another user saves their changes while you are editing, a version conflict will occur when you try to save. You will be alerted and given the option to load the latest version from the server, discarding your local changes.

## API Endpoints

### REST API

The following REST endpoints are available for managing pads.

| Method | Endpoint         | Description                                                  |
| :----- | :--------------- | :----------------------------------------------------------- |
| `GET`  | `/api/pads/{id}` | Retrieves the pad with the specified ID. If it doesn't exist, a new one is created. |
| `PUT`  | `/api/pads/{id}` | Updates the content of a pad. Requires `content` and `version` in the request body for optimistic locking. |
| `DELETE` | `/api/pads/{id}` | Deletes the pad with the specified ID.                       |

### WebSocket API

The application uses STOMP over WebSockets for real-time communication.

*   **Connection Endpoint**: `/ws`

*   **Destinations**:
    *   **Publish Updates**: Send messages to `/app/pads/{id}` to update a pad.
        *   **Message Body**: A JSON object with `content`, `version`, and a unique `userId`.
          ```json
          {
            "content": "This is the updated text.",
            "version": 1,
            "userId": "client-uuid-12345"
          }
          ```

    *   **Subscribe to Updates**: Subscribe to `/topic/pads/{id}` to receive live updates for a pad.
        *   **Received Message**: A `PadWebSocketResponse` object with `type`, `message`, and the updated `pad` entity.

    *   **Conflict Notifications**: The server sends conflict notifications to a user-specific queue at `/user/queue/pad-conflict`. The client automatically subscribes to this upon connection.

## Project Structure

```
.
├── Dockerfile              # Docker configuration for containerizing the application.
├── pom.xml                 # Maven project configuration.
├── src/main/java/          # Main application source code.
│   └── com/notely/
│       ├── controller/     # REST and WebSocket controllers.
│       ├── entity/         # JPA entity for the Pad.
│       ├── dto/            # Data Transfer Objects for API communication.
│       ├── service/        # Business logic for pad management.
│       ├── repository/     # Spring Data JPA repository.
│       └── config/         # WebSocket configuration.
└── src/main/resources/
    ├── application.properties # Spring Boot configuration.
    └── static/index1.html     # The single-page frontend application.


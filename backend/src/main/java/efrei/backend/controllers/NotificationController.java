package efrei.backend.controllers;

import efrei.backend.exceptions.BaseException;
import efrei.backend.models.notifications.NotificationEntity;
import efrei.backend.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management", description = "APIs for managing notifications for clients.")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Retrieves a list of notifications for a specific client.
     *
     * @param clientId The ID of the client for whom to fetch notifications.
     * @return A list of notifications associated with the client.
     */
    @Operation(
            summary = "Get notifications for a client",
            description = "Fetches a list of notifications for a specific client by their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationEntity.class))),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<NotificationEntity>> getNotificationsByClientId(
            @PathVariable String clientId) {
        List<NotificationEntity> notifications = notificationService.getNotificationsByClientId(clientId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notifications);
    }

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId The ID of the notification to mark as read.
     * @return A message indicating the success of the operation.
     */
    @Operation(
            summary = "Mark a notification as read",
            description = "Marks the specified notification as read by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully marked notification as read",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Success Message", value = "\"Notification with id 1234 read successfully.\"")
                    })),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> readNotification(
            @PathVariable String notificationId) throws BaseException {
        notificationService.readNotification(notificationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Notification with id " + notificationId + " read successfully.");
    }
}
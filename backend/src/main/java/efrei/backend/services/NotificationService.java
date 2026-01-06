package efrei.backend.services;

import efrei.backend.exceptions.ResourceNotFoundException;
import efrei.backend.models.clients.ClientEntity;
import efrei.backend.models.notifications.NotificationEntity;
import efrei.backend.models.notifications.NotificationType;
import efrei.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service layer for managing operations related to notifications.
 * This service encapsulates the business logic for creating, retrieving,
 * and updating notifications within the application.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Constructs an instance of NotificationService.
     *
     * @param notificationRepository The repository responsible for accessing and managing
     *                               notification data from the database.
     */
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Retrieves all notifications associated with a specific client.
     *
     * @param clientId The unique identifier of the client whose notifications are to be retrieved.
     * @return A list of {@link NotificationEntity} objects representing the notifications
     *         associated with the specified client.
     */
    @Transactional(readOnly = true)
    public List<NotificationEntity> getNotificationsByClientId(String clientId) {
        return notificationRepository.findAllByClient_Id(clientId);
    }

    /**
     * Sends a notification to a specific client.
     * This method creates a new notification with the specified message, client,
     * and type, and saves it to the database.
     *
     * @param message          The content of the notification to be sent.
     * @param client           The {@link ClientEntity} representing the recipient of the notification.
     * @param typeNotification The {@link NotificationType} indicating the type or category of the notification.
     */
    @Transactional
    public void sendNotification(String message, ClientEntity client, NotificationType typeNotification) {
        NotificationEntity notification = createNotification(message, client, typeNotification);
        notificationRepository.save(notification);
    }

    /**
     * Marks a notification as read.
     * Updates the 'read' status of the specified notification to true.
     *
     * @param notificationId The unique identifier of the notification to be marked as read.
     * @throws ResourceNotFoundException If no notification with the specified ID exists in the database.
     */
    @Transactional
    public void readNotification(String notificationId) throws ResourceNotFoundException {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("The Notification with ID '" + notificationId + "' not found."));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Creates a new {@link NotificationEntity} with the provided details.
     * This is a helper method used internally to construct a notification object.
     *
     * @param message The content of the notification.
     * @param client  The {@link ClientEntity} representing the recipient of the notification.
     * @param type    The {@link NotificationType} specifying the category of the notification.
     * @return A newly created {@link NotificationEntity} instance.
     */
    private NotificationEntity createNotification(String message, ClientEntity client, NotificationType type) {
        NotificationEntity notification = new NotificationEntity();
        notification.setClient(client);
        notification.setNotificationType(type);
        notification.setNotificationDate(new Date());
        notification.setMessage(message);
        notification.setRead(false);

        return notification;
    }
}
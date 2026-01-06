package efrei.backend.models.notifications;

import efrei.backend.models.clients.ClientEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "notifications")
public class NotificationEntity {
    @Id
    private String id;
    private ClientEntity client;
    private NotificationType notificationType;
    private String message;
    private Date notificationDate;
    private boolean read;
}
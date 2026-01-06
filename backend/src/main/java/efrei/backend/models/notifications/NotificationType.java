package efrei.backend.models.notifications;

import lombok.Getter;

@Getter
public enum NotificationType {
    OLD_PROPOSAL,
    NEW_PROPOSAL,
    REFUSED,
    ACCEPTED,
    REMINDER
}
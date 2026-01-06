package efrei.backend.repositories;

import efrei.backend.models.notifications.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByClient_Id(String clientId);
}
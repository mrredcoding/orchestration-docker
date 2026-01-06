package efrei.backend.repositories;

import efrei.backend.models.clients.ClientEntity;
import efrei.backend.models.clients.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, String> {
    Optional<ClientEntity> findByEmail(String clientEmail);
    List<ClientEntity> findAllByRoleIsLike(RoleType role);
}
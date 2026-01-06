package efrei.backend.repositories;

import efrei.backend.models.tools.ToolEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends MongoRepository<ToolEntity, String> {
    List<ToolEntity> findAllByActiveTrue();
    Optional<ToolEntity> findByTitle(String title);
}
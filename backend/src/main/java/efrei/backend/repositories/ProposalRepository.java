package efrei.backend.repositories;

import efrei.backend.models.proposals.ProposalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends MongoRepository<ProposalEntity, String> {
    Optional<ProposalEntity> findByClient_IdAndTool_Title(String clientId, String toolTitle);
    List<ProposalEntity> findAllByCreationDateBefore(Date date);
    List<ProposalEntity> findAllByCreationDateBetween(Date start, Date end);
}

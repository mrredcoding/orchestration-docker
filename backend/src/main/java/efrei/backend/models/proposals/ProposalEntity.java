package efrei.backend.models.proposals;

import efrei.backend.models.clients.ClientEntity;
import efrei.backend.models.tools.ToolEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Document(collection = "proposals")
public class ProposalEntity {

    @Id
    private String Id;
    private ToolEntity tool;
    private ClientEntity client;
    private Date creationDate;
}
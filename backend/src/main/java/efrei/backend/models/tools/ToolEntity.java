package efrei.backend.models.tools;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "tools")
public class ToolEntity {

    @Id
    private String id;
    private String title;
    private DomainType domainType;
    private String description;
    private String link;
    private List<Step> steps;
    private List<Feedback> feedbacks;
    private boolean active;
}
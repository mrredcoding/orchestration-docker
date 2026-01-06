package efrei.backend.contracts;

import efrei.backend.models.tools.DomainType;
import efrei.backend.models.tools.Step;

import java.util.List;

public record CreateProposal(String title, DomainType domainType, String description, String link, List<Step> steps) { }
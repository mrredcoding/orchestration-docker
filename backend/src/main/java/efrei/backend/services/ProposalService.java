package efrei.backend.services;

import efrei.backend.contracts.CreateProposal;
import efrei.backend.exceptions.BaseException;
import efrei.backend.exceptions.ResourceAlreadyExistsException;
import efrei.backend.exceptions.ResourceNotFoundException;
import efrei.backend.models.proposals.ProposalEntity;
import efrei.backend.models.clients.ClientEntity;
import efrei.backend.models.notifications.NotificationType;
import efrei.backend.models.tools.ToolEntity;
import efrei.backend.repositories.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing operations related to proposals.
 * Provides functionality to create, retrieve, update, and delete proposals,
 * as well as handle notifications and tool activations.
 */
@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final NotificationService notificationService;
    private final ClientService clientService;
    private final ToolService toolService;

    /**
     * Constructs a ProposalService instance with required dependencies.
     *
     * @param proposalRepository  The repository for accessing and managing proposal data.
     * @param notificationService The service for handling notifications.
     * @param clientService       The service for managing client operations.
     * @param toolService         The service for managing tool operations.
     */
    @Autowired
    public ProposalService(ProposalRepository proposalRepository,
                           NotificationService notificationService,
                           ClientService clientService,
                           ToolService toolService) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationService;
        this.clientService = clientService;
        this.toolService = toolService;
    }

    /**
     * Retrieves all proposals from the database.
     *
     * @return A list of all {@link ProposalEntity} objects.
     */
    @Transactional(readOnly = true)
    public List<ProposalEntity> getAllProposals() {
        return proposalRepository.findAll();
    }

    /**
     * Retrieves a proposal by its unique identifier.
     *
     * @param id The unique identifier of the proposal.
     * @return The {@link ProposalEntity} corresponding to the specified ID.
     * @throws ResourceNotFoundException If no proposal is found with the given ID.
     */
    @Transactional(readOnly = true)
    public ProposalEntity getProposalById(String id) throws ResourceNotFoundException {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal with ID '" + id + "' not found."));
    }

    /**
     * Creates a new proposal and saves it in the database.
     *
     * @param proposalContract The {@link CreateProposal} containing the details of the proposal to be created.
     * @return The newly created {@link ProposalEntity}.
     * @throws ResourceAlreadyExistsException If a proposal for the same tool by the same client already exists.
     * @throws ResourceNotFoundException      If the client or tool associated with the proposal does not exist.
     */
    @Transactional
    public ProposalEntity createProposal(CreateProposal proposalContract) throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientEntity client = (ClientEntity) authentication.getPrincipal();

        Optional<ProposalEntity> existingProposal = proposalRepository
                .findByClient_IdAndTool_Title(client.getId(), proposalContract.title());

        if (existingProposal.isPresent())
            throw new ResourceAlreadyExistsException("You have already proposed the Tool '" + proposalContract.title() + "'.");

        ToolEntity tool = new ToolEntity();
        tool.setTitle(proposalContract.title());
        tool.setDomainType(proposalContract.domainType());
        tool.setDescription(proposalContract.description());
        tool.setLink(proposalContract.link());
        tool.setSteps(proposalContract.steps());
        tool.setFeedbacks(null);
        tool.setActive(false);

        ProposalEntity newProposal = new ProposalEntity();
        newProposal.setClient(client);
        newProposal.setTool(tool);
        newProposal.setCreationDate(new Date());

        proposalRepository.save(newProposal);

        notifyAdmins(
                "New proposal for Tool '" + tool.getTitle() + "' created by Client ID '" + client.getId() + "'.",
                NotificationType.NEW_PROPOSAL
        );

        return newProposal;
    }

    /**
     * Accepts a proposal, activates the associated tool, and notifies the client.
     *
     * @param proposalId The unique identifier of the proposal to accept.
     * @throws ResourceNotFoundException If the proposal or associated tool does not exist.
     */
    @Transactional
    public void acceptProposal(String proposalId) throws ResourceNotFoundException {
        ProposalEntity proposal = getProposalById(proposalId);
        activateTool(proposal.getTool());
        notifyClient(proposal, NotificationType.ACCEPTED, "accepted");
        deleteProposal(proposal);
    }

    /**
     * Refuses a proposal, deletes the associated tool, and notifies the client.
     *
     * @param proposalId The unique identifier of the proposal to refuse.
     * @throws ResourceNotFoundException If the proposal or associated tool does not exist.
     */
    @Transactional
    public void refuseProposal(String proposalId) throws ResourceNotFoundException {
        ProposalEntity proposal = getProposalById(proposalId);
        toolService.deleteToolById(proposal.getTool().getId());
        notifyClient(proposal, NotificationType.REFUSED, "refused");
        deleteProposal(proposal);
    }

    /**
     * Deletes proposals older than 30 days and notifies the associated clients.
     */
    public void deleteOldProposals() {
        Date cutoffDate = getDateFromNow(-30);
        List<ProposalEntity> oldProposals = proposalRepository.findAllByCreationDateBefore(cutoffDate);

        oldProposals.forEach(proposal -> {
            notifyClient(proposal, NotificationType.OLD_PROPOSAL,
                    "deleted after 30 days without being processed");
            deleteProposal(proposal);
        });
    }

    /**
     * Sends reminders to administrators about pending proposals nearing their expiration date.
     */
    public void sendProposalRemindersToAdmins() {
        Date sevenDaysAgo = getDateFromNow(-7);
        Date thirtyDaysAgo = getDateFromNow(-30);

        List<ProposalEntity> pendingProposals = proposalRepository.findAllByCreationDateBetween(thirtyDaysAgo, sevenDaysAgo);

        pendingProposals.forEach(proposal -> {
            long remainingDays = calculateRemainingDays(proposal.getCreationDate());
            notifyAdmins(
                    "Proposal for Tool '" + proposal.getTool().getTitle() + "' by Client ID '" + proposal.getClient().getId()
                            + "' is pending with " + remainingDays + " days left before deletion.",
                    NotificationType.REMINDER
            );
        });
    }

    // --- Private Helper Methods ---

    private void activateTool(ToolEntity tool) throws ResourceNotFoundException {
        tool.setActive(true);
        toolService.updateToolById(tool.getId(), tool);
    }

    private void notifyClient(ProposalEntity proposal, NotificationType type, String action) {
        notificationService.sendNotification(
                "Your proposal for Tool '" + proposal.getTool().getTitle() + "' has been " + action + ".",
                proposal.getClient(),
                type
        );
    }

    private void deleteProposal(ProposalEntity proposal) {
        proposalRepository.delete(proposal);
    }

    private Date getDateFromNow(int daysOffset) {
        LocalDate date = LocalDate.now().plusDays(daysOffset);
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private long calculateRemainingDays(Date creationDate) {
        LocalDate creation = creationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return 30 - creation.until(now).getDays();
    }

    private void notifyAdmins(String message, NotificationType type) {
        clientService.getAllAdmins()
                .forEach(admin -> notificationService.sendNotification(message, admin, type));
    }
}
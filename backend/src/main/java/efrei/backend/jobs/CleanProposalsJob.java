package efrei.backend.jobs;

import efrei.backend.services.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job for cleaning up old proposals.
 * This job is executed daily at a specified time to delete proposals that are older than 30 days.
 *
 * The job relies on the {@link ProposalService} to perform the cleanup operation.
 */
@Component
public class CleanProposalsJob implements Job {

    private final ProposalService proposalService;

    /**
     * Constructor for CleanProposalsJob.
     * Initializes the job with the required {@link ProposalService}.
     *
     * @param proposalService The service responsible for managing proposal-related operations.
     */
    @Autowired
    public CleanProposalsJob(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    /**
     * Executes the scheduled cleanup task.
     * This method is triggered daily at 9:00 PM server time (21:00) as defined by the cron expression.
     *
     * The task deletes proposals that are older than 30 days by invoking
     * {@link ProposalService#deleteOldProposals()}.
     */
    @Override
    @Scheduled(cron = "0 0 21 * * *") // Runs daily at 9:00 PM
    public void execute() {
        proposalService.deleteOldProposals();
    }
}

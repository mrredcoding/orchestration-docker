package efrei.backend.jobs;

import efrei.backend.services.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job for sending reminders about pending proposals to administrators.
 *
 * This job is executed daily at a specified time to notify administrators about proposals
 * that are nearing their expiration date (e.g., proposals older than 7 days and not yet processed).
 *
 * The job uses the {@link ProposalService} to handle the reminder notification logic.
 */
@Component
public class ProposalReminderJob implements Job {

    private final ProposalService proposalService;

    /**
     * Constructor for ProposalReminderJob.
     * Initializes the job with the required {@link ProposalService}.
     *
     * @param proposalService The service responsible for managing proposal-related operations.
     */
    @Autowired
    public ProposalReminderJob(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    /**
     * Executes the scheduled reminder task.
     * This method is triggered daily at 10:00 AM server time as defined by the cron expression.
     *
     * The task identifies pending proposals nearing their expiration date
     * (e.g., older than 7 days) and sends reminder notifications to administrators
     * by invoking {@link ProposalService#sendProposalRemindersToAdmins()}.
     */
    @Override
    @Scheduled(cron = "0 0 10 * * *") // Runs daily at 10:00 AM
    public void execute() {
        proposalService.sendProposalRemindersToAdmins();
    }
}
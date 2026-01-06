package efrei.backend.controllers;

import efrei.backend.contracts.CreateProposal;
import efrei.backend.exceptions.BaseException;
import efrei.backend.models.proposals.ProposalEntity;
import efrei.backend.services.ProposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposals")
@Tag(name = "Proposal Management", description = "APIs for managing proposals.")
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @Operation(summary = "Get all proposals", description = "Fetches a list of all proposals.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proposals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProposalEntity.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProposalEntity>> getAllProposals() {
        List<ProposalEntity> proposals = proposalService.getAllProposals();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(proposals);
    }

    @Operation(summary = "Get proposal by ID", description = "Fetches a specific proposal by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proposal",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProposalEntity.class))),
            @ApiResponse(responseCode = "404", description = "Proposal not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Proposal not found\", \"status\": 404}")))
    })
    @GetMapping("/{proposalId}")
    public ResponseEntity<ProposalEntity> getProposal(@PathVariable String proposalId) throws BaseException {
        ProposalEntity proposal = proposalService.getProposalById(proposalId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(proposal);
    }

    @Operation(summary = "Create a new proposal", description = "Creates a new proposal based on the provided contract.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created proposal",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateProposal.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ProposalEntity> createProposal(@RequestBody CreateProposal proposalContract) throws BaseException {
        ProposalEntity proposal = proposalService.createProposal(proposalContract);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(proposal);
    }

    @Operation(summary = "Accept proposal", description = "Accepts the proposal with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully accepted proposal",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"Proposal accepted successfully.\""))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not authorized"),
            @ApiResponse(responseCode = "404", description = "Proposal not found")
    })
    @PatchMapping("/{proposalId}/accept")
    public ResponseEntity<String> acceptProposal(@PathVariable String proposalId) throws BaseException {
        proposalService.acceptProposal(proposalId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Proposal accepted successfully.");
    }

    @Operation(summary = "Refuse proposal", description = "Refuses the proposal with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refused proposal",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"Proposal refused successfully.\""))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not authorized"),
            @ApiResponse(responseCode = "404", description = "Proposal not found")
    })
    @PatchMapping("/{proposalId}/refuse")
    public ResponseEntity<String> refuseProposal(@PathVariable String proposalId) throws BaseException {
        proposalService.refuseProposal(proposalId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Proposal refused successfully.");
    }
}
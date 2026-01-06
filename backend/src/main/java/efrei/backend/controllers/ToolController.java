package efrei.backend.controllers;

import efrei.backend.exceptions.BaseException;
import efrei.backend.models.tools.ToolEntity;
import efrei.backend.services.ToolService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/tools")
@Tag(name = "Tool Management", description = "APIs for managing tools.")
public class ToolController {

    private final ToolService toolService;

    @Autowired
    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @Operation(summary = "Get all tools", description = "Fetches a list of all active tools.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tools",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ToolEntity.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<ToolEntity>> getAllTools() {
        List<ToolEntity> tools = toolService.getAllActiveTools();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tools);
    }

    @Operation(summary = "Get tool by ID", description = "Fetches a specific tool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tool",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ToolEntity.class))),
            @ApiResponse(responseCode = "404", description = "Tool not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Tool not found\", \"status\": 404}")))
    })
    @GetMapping("/{toolId}")
    public ResponseEntity<ToolEntity> getTool(@PathVariable String toolId) throws BaseException {
        ToolEntity tool = toolService.getToolById(toolId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tool);
    }

    @Operation(summary = "Update tool", description = "Updates the tool with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated tool",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"Tool with id '123' updated successfully.\""))),
            @ApiResponse(responseCode = "404", description = "Tool not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Tool not found\", \"status\": 404}")))
    })
    @PutMapping("/{toolId}/update")
    public ResponseEntity<Map<String, Object>> updateTool(@PathVariable String toolId, @RequestBody ToolEntity tool) throws BaseException {
        toolService.updateToolById(toolId, tool);
        Map<String, Object> response = Map.of(
                "message", "Tool with id '" + toolId + "' updated successfully.",
                "status", HttpStatus.OK
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete tool", description = "Deletes the tool with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted tool",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"Tool with id '123' deleted successfully.\""))),
            @ApiResponse(responseCode = "404", description = "Tool not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Tool not found\", \"status\": 404}")))
    })
    @DeleteMapping("/{toolId}/delete")
    public ResponseEntity<Map<String, Object>> deleteTool(@PathVariable String toolId) throws BaseException {
        toolService.deleteToolById(toolId);
        Map<String, Object> response = Map.of(
                "message", "Tool with id '" + toolId + "' deleted successfully.",
                "status", HttpStatus.OK
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
package efrei.backend.services;

import efrei.backend.exceptions.ResourceNotFoundException;
import efrei.backend.models.tools.ToolEntity;
import efrei.backend.repositories.ToolRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing operations related to tools.
 * Provides functionality for retrieving, updating, and deleting tools, as well as managing their active status.
 */
@Service
public class ToolService {

    private final ToolRepository toolRepository;

    /**
     * Constructs a ToolService instance with the required dependencies.
     *
     * @param toolRepository The repository for accessing and managing tool data.
     */
    @Autowired
    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    /**
     * Retrieves all tools that are marked as active.
     *
     * @return A list of active {@link ToolEntity} objects.
     */
    @Transactional(readOnly = true)
    public List<ToolEntity> getAllActiveTools() {
        return toolRepository.findAllByActiveTrue();
    }

    /**
     * Retrieves a tool by its unique identifier.
     *
     * @param toolId The unique identifier of the tool.
     * @return The {@link ToolEntity} corresponding to the specified ID.
     * @throws ResourceNotFoundException If no tool is found with the given ID.
     */
    @Transactional(readOnly = true)
    public ToolEntity getToolById(String toolId) throws ResourceNotFoundException {
        return toolRepository.findById(toolId)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with ID '" + toolId + "' not found."));
    }

    /**
     * Retrieves a tool by its title.
     *
     * @param title The title of the tool.
     * @return The {@link ToolEntity} corresponding to the specified title.
     * @throws ResourceNotFoundException If no tool is found with the given title.
     */
    @Transactional(readOnly = true)
    public ToolEntity getToolByTitle(String title) throws ResourceNotFoundException {
        return toolRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with title '" + title + "' not found."));
    }

    /**
     * Updates the details of an existing tool.
     * Only non-identifier fields will be updated; the tool's ID remains unchanged.
     *
     * @param toolId      The unique identifier of the tool to update.
     * @param updatedTool The updated {@link ToolEntity} object containing new details.
     * @throws ResourceNotFoundException If no tool is found with the given ID.
     */
    @Transactional
    public void updateToolById(String toolId, ToolEntity updatedTool) throws ResourceNotFoundException {
        ToolEntity existingTool = getToolById(toolId);

        // Copy non-ID properties from the updated tool to the existing tool.
        BeanUtils.copyProperties(updatedTool, existingTool, "id");

        toolRepository.save(existingTool);
    }

    /**
     * Deletes a tool by its unique identifier.
     *
     * @param toolId The unique identifier of the tool to delete.
     * @throws ResourceNotFoundException If no tool is found with the given ID.
     */
    @Transactional
    public void deleteToolById(String toolId) throws ResourceNotFoundException {
        ToolEntity toolEntity = getToolById(toolId);
        toolRepository.delete(toolEntity);
    }
}
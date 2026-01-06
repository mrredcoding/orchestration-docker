package efrei.backend.seeders;

import efrei.backend.models.tools.ToolEntity;
import efrei.backend.models.tools.DomainType;
import efrei.backend.models.tools.Step;
import efrei.backend.models.tools.Feedback;
import efrei.backend.repositories.ToolRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ToolSeeder is responsible for populating the database with predefined tool data.
 * This component reads data from an Excel file and converts it into a collection
 * of {@link ToolEntity} objects, which are then stored in the database.
 */
@Component
public class ToolSeeder implements CommandLineRunner {

    private final ToolRepository toolRepository;
    private final String HOPE_DATA = "HOPE_Excel.xlsx";

    /**
     * Constructs a new ToolSeeder.
     *
     * @param toolRepository the repository used for managing tool entities
     */
    @Autowired
    public ToolSeeder(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    /**
     * Runs the seeder logic to populate the database with initial tool data.
     * Deletes any existing tool records before creating new predefined ones from an Excel file.
     *
     * @param args command-line arguments passed to the application
     */
    @Override
    public void run(String... args) {
        try {
            toolRepository.deleteAll();

            Resource resource = new ClassPathResource(HOPE_DATA);
            InputStream fileInputStream = resource.getInputStream();

            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<ToolEntity> tools = parseSheet(sheet);

            toolRepository.saveAll(tools);

            System.out.println("Seeded initial tools data.");
        } catch (IOException e) {
            System.err.println("Failed to load the Excel file for seeding: " + e.getMessage());
        }
    }

    /**
     * Parses an Excel sheet and converts rows into a list of {@link ToolEntity}.
     *
     * @param sheet the Excel sheet containing tool data
     * @return a list of tool entities parsed from the sheet
     */
    private List<ToolEntity> parseSheet(Sheet sheet) {
        return IntStream.rangeClosed(1, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull)
                .map(this::createToolFromRow)
                .collect(Collectors.toList());
    }

    /**
     * Creates a {@link ToolEntity} from a row in the Excel sheet.
     *
     * @param row the row containing tool data
     * @return a tool entity populated with data from the row
     */
    private ToolEntity createToolFromRow(Row row) {
        ToolEntity tool = new ToolEntity();
        tool.setTitle(getCellStringValue(row, 0));
        tool.setDomainType(mapDomainType(getCellStringValue(row, 1)));
        tool.setDescription(formatDescription(
                getCellStringValue(row, 2),
                getCellStringValue(row, 3)
        ));
        tool.setLink(getCellStringValue(row, 4));
        tool.setSteps(parseSteps(getCellStringValue(row, 5)));
        tool.setFeedbacks(parseFeedback(getCellStringValue(row, 6)));
        tool.setActive(true);
        return tool;
    }

    /**
     * Formats the description by combining simple and detailed parts if present.
     *
     * @param simple  the simple description
     * @param detailed the detailed description
     * @return a formatted description combining both parts
     */
    private String formatDescription(String simple, String detailed) {
        return Stream.of(
                        simple.isEmpty() ? null : simple,
                        detailed.isEmpty() ? null : detailed
                ).filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Parses step data from a string and converts it into a list of {@link Step}.
     *
     * @param access the string containing step descriptions
     * @return a list of steps
     */
    private List<Step> parseSteps(String access) {
        if (access == null || access.isBlank())
            return new ArrayList<>();

        String[] lines = access.split("\\r?\\n");
        return IntStream.range(0, lines.length)
                .mapToObj(i -> {
                    Step step = new Step();
                    step.setOrder(i + 1);
                    step.setDescription(lines[i].trim());
                    return step;
                })
                .filter(step -> !step.getDescription().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Parses feedback data from a string and converts it into a list of {@link Feedback}.
     *
     * @param feedbackUsers the string containing feedback descriptions
     * @return a list of feedback objects
     */
    private List<Feedback> parseFeedback(String feedbackUsers) {
        if (feedbackUsers == null || feedbackUsers.isBlank())
            return new ArrayList<>();

        return Stream.of(feedbackUsers.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    Feedback feedback = new Feedback();
                    feedback.setOwner("anonymous");
                    feedback.setDescription(line);
                    return feedback;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a cell's string value from a row.
     *
     * @param row       the row containing the cell
     * @param cellIndex the index of the cell
     * @return the string value of the cell, or an empty string if the cell is null
     */
    private String getCellStringValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return cell == null ? "" : new DataFormatter().formatCellValue(cell).trim();
    }

    /**
     * Maps a domain description string to its corresponding {@link DomainType}.
     *
     * @param domainText the domain description string
     * @return the corresponding domain type
     * @throws IllegalStateException if the domain text does not match any known domain type
     */
    private DomainType mapDomainType(String domainText) {
        return switch (domainText.toLowerCase()) {
            case "bouquet de services" -> DomainType.SERVICES;
            case "codage / développement" -> DomainType.DEVELOPMENT;
            case "cyber sécurité" -> DomainType.CYBERSECURITY;
            case "cloud provider" -> DomainType.CLOUD;
            case "formations en ligne" -> DomainType.E_LEARNING;
            case "mathématiques" -> DomainType.MATHEMATICS;
            case "electronique" -> DomainType.ELECTRONICS;
            case "data science" -> DomainType.DATA_SCIENCE;
            case "gestion de projets et collaboration" -> DomainType.PROJECT_MANAGEMENT;
            case "génération de documents" -> DomainType.DOCUMENT_GENERATOR;
            case "réseaux" -> DomainType.NETWORK;
            case "bases de données" -> DomainType.DATABASE;
            case "réalité virtuelle / augmentée" -> DomainType.VIRTUAL_REALITY;
            case "containers" -> DomainType.DOCKER;
            case "hyperviseurs", "environnements virtuels" -> DomainType.VIRTUAL_ENVIRONMENT;
            default -> throw new IllegalStateException("Unexpected value: " + domainText.toLowerCase());
        };
    }
}

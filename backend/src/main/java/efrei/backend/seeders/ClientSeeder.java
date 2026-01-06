package efrei.backend.seeders;

import efrei.backend.models.clients.ClientEntity;
import efrei.backend.models.clients.RoleType;
import efrei.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ClientSeeder is responsible for populating the database with predefined client data.
 * It seeds the database with a set of predefined clients, including students, professors, and an administrator.
 * It converts it into a collection of {@link ClientEntity} objects, which are then stored in the database.
 */
@Component
public class ClientSeeder implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new ClientSeeder.
     *
     * @param clientRepository the repository used for managing client entities
     * @param passwordEncoder  the encoder used for securely storing passwords
     */
    @Autowired
    public ClientSeeder(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs the seeder logic to populate the database with initial client data.
     * Deletes any existing client records before creating new predefined ones.
     *
     * @param args command-line arguments passed to the application
     */
    @Override
    public void run(String... args) {
        clientRepository.deleteAll();
        createClient(
                "Alonso",
                "Cédric",
                "cedric.alonso@efrei.net",
                RoleType.STUDENT,
                "securePasswordStudent123*"
        );

        createClient(
                "Brinet",
                "Félix",
                "felix.brinet@efrei.net",
                RoleType.STUDENT,
                "securePasswordStudent123*"
        );

        createClient(
                "Hatoum",
                "Jade",
                "jade.hatoum@efrei.net",
                RoleType.STUDENT,
                "securePasswordStudent123*"
        );

        createClient(
                "Houhou",
                "Ahmad",
                "ahmad.houhou@efrei.net",
                RoleType.STUDENT,
                "securePasswordStudent123*"
        );

        createClient(
                "Renno",
                "Joseph",
                "joseph.renno@efrei.net",
                RoleType.STUDENT,
                "securePasswordStudent123*"
        );

        createClient(
                "Augustin",
                "Jacques",
                "jacques.augustin@efrei.net",
                RoleType.PROFESSOR,
                "securePasswordProfessor123*"
        );

        createClient(
                "Admin",
                "Hope",
                "admin.hope@efrei.net",
                RoleType.ADMIN,
                "securePasswordAdmin123*"
        );

        System.out.println("Seeded initial clients data.");
    }

    /**
     * Creates and persists a new client entity with the specified details.
     *
     * @param lastName  the last name of the client
     * @param firstName the first name of the client
     * @param email     the email address of the client
     * @param role      the role type of the client (e.g., STUDENT, PROFESSOR)
     * @param password  the plaintext password of the client, which will be encoded before storage
     */
    private void createClient(String lastName, String firstName, String email, RoleType role, String password) {
        ClientEntity client = new ClientEntity();
        client.setLastName(lastName.toUpperCase());
        client.setFirstName(firstName);
        client.setEmail(email);
        client.setRole(role);
        client.setPassword(passwordEncoder.encode(password));
        clientRepository.save(client);
    }
}
package efrei.backend.services;

import efrei.backend.contracts.LoginRequest;
import efrei.backend.contracts.RegisterClient;
import efrei.backend.exceptions.BaseException;
import efrei.backend.exceptions.InvalidPasswordOrEmailException;
import efrei.backend.exceptions.ResourceNotFoundException;
import efrei.backend.models.clients.ClientEntity;
import efrei.backend.models.clients.RoleType;
import efrei.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for handling client-related operations, such as authentication, registration,
 * and data retrieval. This class acts as a bridge between the controller and repository layers,
 * encapsulating business logic and data processing for clients.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs a new ClientService with the necessary dependencies.
     *
     * @param clientRepository    The repository for managing client data.
     * @param passwordEncoder     The password encoder for secure password hashing.
     * @param authenticationManager The manager for handling authentication processes.
     */
    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new client in the system.
     *
     * @param input The registration details for the client, encapsulated in a RegisterClient object.
     * @return The saved ClientEntity representing the newly registered client.
     */
    public ClientEntity signup(RegisterClient input) {
        ClientEntity client = new ClientEntity();

        client.setLastName(input.getLastName().toUpperCase());
        client.setFirstName(input.getFirstName());
        client.setEmail(input.getEmail());
        client.setRole(input.getRole());
        client.setPassword(passwordEncoder.encode(input.getPassword()));

        return clientRepository.save(client);
    }

    /**
     * Authenticates a client based on the provided login credentials.
     *
     * @param input The login request containing the client's email and password.
     * @return The authenticated ClientEntity.
     * @throws BaseException If authentication fails or the user is not found.
     */
    public ClientEntity authenticate(LoginRequest input) throws BaseException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.email(),
                            input.password()
                    )
            );
        } catch (Exception e) {
            throw new InvalidPasswordOrEmailException();
        }

        return clientRepository.findByEmail(input.email())
                .orElseThrow(InvalidPasswordOrEmailException::new);
    }

    /**
     * Retrieves all clients who have the role of ADMIN.
     *
     * @return A list of ClientEntity objects with the ADMIN role.
     */
    public List<ClientEntity> getAllAdmins() {
        return clientRepository.findAllByRoleIsLike(RoleType.ADMIN);
    }

    /**
     * Retrieves a specific client by their unique ID.
     *
     * @param clientId The unique ID of the client to be retrieved.
     * @return The corresponding ClientEntity.
     * @throws ResourceNotFoundException If no client with the provided ID is found in the system.
     */
    public ClientEntity getClientById(String clientId) throws ResourceNotFoundException {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID '" + clientId + "' not found."));
    }
}
package efrei.backend.controllers;

import efrei.backend.contracts.LoginRequest;
import efrei.backend.contracts.LoginResponse;
import efrei.backend.contracts.RegisterClient;
import efrei.backend.exceptions.BaseException;
import efrei.backend.models.clients.ClientEntity;
import efrei.backend.services.ClientService;
import efrei.backend.services.JwtService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Client Management", description = "APIs for managing client registration and authentication.")
public class ClientController {
    private final JwtService jwtService;
    private final ClientService clientService;

    @Autowired
    public ClientController(JwtService jwtService, ClientService clientService) {
        this.jwtService = jwtService;
        this.clientService = clientService;
    }

    @Operation(
            summary = "Register a new client",
            description = "Creates a new client account. Accessible to everyone."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/signup")
    public ResponseEntity<ClientEntity> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the client to register",
                    required = true,
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Admin Registration", value = """
                                    {
                                      "lastName": "Doe",
                                      "firstName": "John",
                                      "email": "john.doe@example.com",
                                      "role": "ADMIN",
                                      "password": "securePassword123"
                                    }
                                    """)
                    })
            )
            @RequestBody RegisterClient input
    ) {
        ClientEntity registeredUser = clientService.signup(input);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registeredUser);
    }

    @Operation(
            summary = "Authenticate a client",
            description = "Authenticates a client using their email and password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client authenticated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User Login", value = """
                                    {
                                      "email": "john.doe@example.com",
                                      "password": "securePassword123"
                                    }
                                    """)
                    })
            )
            @RequestBody LoginRequest loginRequest
    ) throws BaseException {
        ClientEntity authenticatedUser = clientService.authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }

    @Operation(
            summary = "Get authenticated user details",
            description = "Fetches the details of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<ClientEntity> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientEntity client = (ClientEntity) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(client);
    }
}
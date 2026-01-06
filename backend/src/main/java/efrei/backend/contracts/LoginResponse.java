package efrei.backend.contracts;

public record LoginResponse(String token, long expiresIn) { }

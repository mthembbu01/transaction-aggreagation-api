package za.co.capitec.loans.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s with %s: %s not found", resource, field, value));
    }
}


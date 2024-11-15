package com.Blogs.Backend.BlogsBackend.Security.exceptions;

public class NoContentException extends RuntimeException {
    // Constructor with a default message
    public NoContentException() {
        super("Product not found");
    }

    // Constructor with a custom message
    public NoContentException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public NoContentException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public NoContentException(Throwable cause) {
        super(cause);
    }
}

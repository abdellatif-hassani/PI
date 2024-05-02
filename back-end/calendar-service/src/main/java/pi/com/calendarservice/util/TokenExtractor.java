package pi.com.calendarservice.util;

public class TokenExtractor {

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        return null; // Invalid or missing token
    }
}


package backend.security;

import io.jsonwebtoken.*;

import java.util.Date;

public class Token {
    private static Token instance = null;
    public static synchronized Token getInstance() {
        if (instance == null) {
            instance = new Token();
        }
        return instance;
    }
    private static final String SECRET_KEY = "Y-CMPS-242";

    public String createToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();

        String result = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 3600000)) // 1 hour token validity
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).compact();
        return result;
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT");
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT");
        } catch (SignatureException e) {
            System.out.println("Invalid signature");
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument token");
        }
        return false;
    }
}

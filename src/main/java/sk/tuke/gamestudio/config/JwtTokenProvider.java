package sk.tuke.gamestudio.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secretKey = "cDNlNGRjZjNjNTBiYjE3YzBiNjJlYjIwOGVlMDYyYWRiMDMzYzRjZjc0NjNkNTc5YmQxNmE5NTNmZWVlZmI1ZDRhOGNkOWVhMTU4YWU2MjkzMjg2YWJiM2VkYWZhZDc0MWZkMDNmNjQ3MzQ3Y2YwYzI4YTQ5ZTNkY2Y2YWZlZWQxMDNkMDgyOTFmN2ZlMzY1ZjM5OGIyMjQ0NGY4NjAzOGE2ZTc0NjlkMDVhNTI1ZDVjNmI3MTZjODkzZWI1M2RiNDE2MDNkYmJlODQ4OGQ2NjBlNjYzNjBmMTM1OTQ0ZjUwOTA0NzRmZGUxOGNmMmRjZjA1N2JmMGQ2YzY1MDlhZWY5MDkzMTRiZTkwMjNlZThiOGJjYzBkNTNlNDdlMzBlYzlmNTIzZDY2Yjc3YzRjNzNjMjBlYmExYzk5Mjk3NDQxN2QyOTQ0YWQ2ZmFhNzI1YjkyMjYwODE2MGJlYWY4MGUwZmNiY2M5NGU1OGU1OTYxMzMxYmIzOWI0OWQ2NDBlYTM4MzcwNDRkYTRhNTNjYzViYjQ3M2VhNDQxZDU2OTFjMmVkMTEyNDMxODNjZDVhNzBjZDVlZDA2YzViODJkYWI2NjdmODNhY2VlYjQzMGZkZDU5ODliYWJkYjQwNzQ5NTVkNTk1MDlkYWJjNWI5ZjllYTM4YzU2Yzk0YWE1MzEwYWM2MzRkMjg5YTBlYjI4ZGFjMDZlODgwZTMyNDVlMmQxNTJkNGFkNjEzOGY0MTQwOGY1NjNjNDhkZWE4MzkyNmEyM2Y1MDFkOWQyNDY4ZWE1ZDlkZWYzMmNjYWIyNmVjNmRiYzY0YjhjMzJmYWU4OTUxNTBkOGE4OWI2NzM4MzIyYzc0Y2MwNGUxZDAwY2RjYWE1NDA1ZmVlYTQ2MDYyNzZmYjZlMzJhODNmNDc5ZTFiMzFmYzY2NTQ0ZmY5YThiZjZhZDJjZDEzY2IyNjdhMzI2MTJkZmU4YzMxNzQ4NWVjYjQ5Y2EyNjQ5OGI1YzgwOGQ0NzAwMzcxMWYwYWU1NGVkMTk0NWEwMTk4Yzk1NDllZWJiZTczZTU1MzRjNzM1MjY0YTU5NmFlZmUwMzk2MjAyZWYzZTU3OGM3M2M5MjRiODQyZDM5NDE2NzNjOTgyOTliN2MwMDlhNTIwZTYyMWYwMDczYjQxZDgxYjFjOWMwMGJhNTc4YTVkYmRiMDg2ODVmN2IwNWQxOTA4N2ZjYWYxNmYyMmE5NmI4NDczZmI4Njc5OWUwYWI1MWQ3OGE5NmYyODk1YzMxZjI3ZDg2OThjMmM0NmU0OTJmNWI0ZDM1NDA3N2Q4YmM2NGJiZDgwZTJlOWJhNTBiZTYxZmNkYjVlY2ZjOGZhZDYxNDQ3";

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000);  // 24 часа

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

package phuoc.vd.springloginexample.security;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import phuoc.vd.springloginexample.config.AppProperties;
import phuoc.vd.springloginexample.controller.response.JwtResponse;
import phuoc.vd.springloginexample.exception.BasicException;
import phuoc.vd.springloginexample.exception.StatusResponse;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.StringTokenizer;

@Service
@Slf4j
@AllArgsConstructor
public class TokenProvider {

    private AuthenticationManager authenticationManager;

    private AppProperties appProperties;


    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

//        if (userPrincipal.getRoleId() == null) return "Role User is not found!";

        if (!userPrincipal.isActive()) return "User don't active!";

        return Jwts.builder()
                .claim("uId", userPrincipal.getId())
                .claim("uNa", userPrincipal.getEmail())
                .claim("rol", userPrincipal.getRoleId())
                .claim("rolN", userPrincipal.getRoleName())
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public JwtResponse makeAccessToken(String authCredentials) {
        final String[] userAndPassword = parseCredentials(authCredentials);
        log.info("Found username=[{}]", userAndPassword[0]);

        JwtResponse msg = this.buildJwtResponse(
                userAndPassword[0],
                userAndPassword[1],
                null);

        if (msg == null) {
            throw new BasicException(StatusResponse.FORBIDDEN);
        }
        return msg;
    }

    private String[] parseCredentials(String authCredentials) {
        StringTokenizer tokenizer = new StringTokenizer(authCredentials, " ");
        if (!tokenizer.hasMoreTokens()) {
            throw new BasicException(StatusResponse.FORBIDDEN);
        }
        final String key = tokenizer.nextToken().toUpperCase();
        if (!tokenizer.hasMoreTokens()) {
            throw new BasicException(StatusResponse.FORBIDDEN);
        }
        final String value = tokenizer.nextToken();

        if (key.isEmpty() || value.isEmpty()) {
            throw new BasicException(StatusResponse.FORBIDDEN);
        }

//        String encodeToString = Base64.getUrlEncoder().encodeToString(value.getBytes());
        byte[] decodedBytes = Base64.getDecoder().decode(value);
        String usernameAndPassword = null;
        try {
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new BasicException(StatusResponse.FORBIDDEN);
        }
        tokenizer = new StringTokenizer(usernameAndPassword, ":");

        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        return new String[]{username, password};
    }

    public JwtResponse buildJwtResponse(
            String email,
            String password,
            String remoteIp) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            if (userPrincipal.getRoleId() == null) return new JwtResponse()
                    .setSuccess(false)
                    .setMessage("Role User is not found!");

            if (!userPrincipal.isActive()) return new JwtResponse()
                    .setSuccess(false)
                    .setMessage("User don't active!");

            String access_token = this.generateToken(authentication);

            return new JwtResponse()
                    .setAccess_token(access_token)
                    .setRefresh_token("")
                    .setScope(userPrincipal.getRoleName())
                    .setExpires_in(appProperties.getAuth().getTokenExpirationMsec())
                    .setToken_type("JWT")
                    .setMessage("+OK");
        } catch (Exception e) {
            throw new BasicException(StatusResponse.FORBIDDEN);
        }
    }

    public Claims validateToken(String authToken) {
        try {
            return Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken).getBody();
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return null;
    }

}

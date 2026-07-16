package com.iexpo.auth.service;

import com.iexpo.auth.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    private RSAKey rsaKey;

    public String generateToken(User user) {
        try {
            // Prepare JWT claims
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer("auth-service")
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                    .claim("roles", user.getRoles())
                    .claim("mobileNumber", user.getMobileNumber())
                    .claim("userId", user.getId())
                    .build();

            // Create RSA signer with private key
            JWSSigner signer = new RSASSASigner(rsaKey.toRSAPrivateKey());

            // Build header
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.getKeyID())
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            // Compute signature
            signedJWT.sign(signer);

            // Serialize to compact form
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public Map<String, Object> getJwk() {
        return rsaKey.toPublicJWK().toJSONObject();
    }
}

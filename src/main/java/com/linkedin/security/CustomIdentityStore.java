package com.linkedin.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;

@Named
@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        if (credential.compareTo("bohdan", "qwerty")) {
            return new CredentialValidationResult("bohdan",
                    new HashSet<>(Arrays.asList("admin", "basic")));
        }

        return CredentialValidationResult.INVALID_RESULT;
    }
}

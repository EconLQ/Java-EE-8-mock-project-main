package com.linkedin.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;

@Named
@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "local")    // local is the name on WF side
public class SecurityConfig {
}

package com.linkedin.security;

import javax.annotation.security.DeclareRoles;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@DeclareRoles({"admin", "basic"})
@ServletSecurity(@HttpConstraint(rolesAllowed = "admin"))  // limit who can access the servlet
@WebServlet(name = "SecureServlet", urlPatterns = "/secure")
public class SecureServlet extends HttpServlet {

    @Inject
    private SecurityContext securityContext;    // contains information about the user

    public SecureServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // after successful login redirects to catalog form
        response.sendRedirect(request.getContextPath() + "/form.xhtml");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}

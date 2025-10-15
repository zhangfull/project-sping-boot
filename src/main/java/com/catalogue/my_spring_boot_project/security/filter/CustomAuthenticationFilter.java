package com.catalogue.my_spring_boot_project.security.filter;

import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authManager) {
        super.setAuthenticationManager(authManager);
        super.setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
                 Log.info(getClass(), "验证身份：username-password验证");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(request.getInputStream(),
                    new TypeReference<Map<String, String>>() {
                    });
            String emailOrUid = map.get("emailOrUid");
            String password = map.get("password");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(emailOrUid,
                    password);
            return this.getAuthenticationManager().authenticate(authToken);
        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                throw (AuthenticationException) e;
            }
            throw new RuntimeException(e);
        }
    }

}

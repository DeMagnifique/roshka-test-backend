package com.roshkatest.controller;

import com.roshkatest.config.JwtTokenUtil;
import com.roshkatest.dto.LoginRequest;
import com.roshkatest.entity.User;
import com.roshkatest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, 
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Los campos de 'username' y 'password' son requeridos.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            final String token = jwtTokenUtil.generateToken(loginRequest.getUsername());
            
            Cookie jwtCookie = new Cookie("token", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(86400);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("type", "Bearer");
            responseBody.put("username", loginRequest.getUsername());

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales invalidas");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        
        User user = userOpt.get();
        String authHeader = request.getHeader("Authorization");
        String token = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("user", user.getUsername());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());
        profile.put("ip", getClientIpAddress(request));
        
        if (token != null) {
            long timeLeft = jwtTokenUtil.getTimeUntilExpiration(token);
            long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;
            
            profile.put("tokenValido", String.format("%02d:%02d:%02d", hours, minutes, seconds));
        }
        
        profile.put("timestamp", System.currentTimeMillis());
        profile.put("userAgent", request.getHeader("User-Agent"));
        
        return ResponseEntity.ok(profile);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0];
        }
    }
}

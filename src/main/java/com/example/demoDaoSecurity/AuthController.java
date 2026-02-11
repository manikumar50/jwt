package com.example.demoDaoSecurity;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController
{
    private PasswordEncoder encoder;
    private UserRepository repository;
    private AuthenticationManager authManager;
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestParam String username,@RequestParam String password)
    {
        User user = new User();
        user.setName(username);
        user.setPassword(encoder.encode(password));
        user.setRole("user".toUpperCase());

        repository.save(user);

        return "successfully registered...";
    }

    @PostMapping("/signin")
    public String login(@RequestParam String username,@RequestParam String password)
    {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if(authentication.isAuthenticated())
        {
            // generate token
            String role = authentication.getAuthorities().stream()
                    .map(x-> x.getAuthority()).findFirst()
                    .get().toUpperCase();
            String token = jwtUtil.generateToken(username,role);

            return token;
        }
        else{
            return "username and password is invalid";
        }
    }
}
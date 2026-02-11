package com.example.demoDaoSecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController
{
    @GetMapping("/admin")
    public String admin()
    {
        return "admin";
    }

    @GetMapping("/user")
    public String user()
    {
        return "user";
    }
}

package com.example.demo.web;

import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    @GetMapping("/api/v1/user")
    public SessionUser user(@LoginUser SessionUser user) {
        return user;
    }
}

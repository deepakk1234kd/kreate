package com.moneyclick.controller;

import com.moneyclick.dto.LoginRequest;
import com.moneyclick.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreController {

    @Autowired
    CoreService coreService;

    @GetMapping("/process")
    public HttpStatus process() {
        coreService.process();
        return HttpStatus.NO_CONTENT;
    }

    @PostMapping("/login")
    public HttpStatus login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername().equalsIgnoreCase("admin") &&
                loginRequest.getPassword().equalsIgnoreCase("password")){
            return HttpStatus.OK;
        } else
            return HttpStatus.UNAUTHORIZED;
    }

}

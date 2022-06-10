package com.practice.springbootrestapimarket.controller.sign;


import com.practice.springbootrestapimarket.dto.response.Response;
import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.service.sign.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SignController {

    @Autowired
    SignService signService;

    @PostMapping("/api/signIn")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest req) {
        return Response.success(signService.signIn(req));
    }

    @PostMapping("/api/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@Valid @RequestBody SignUpRequest req) {
        signService.signUp(req);
        return Response.success();
    }

    @PostMapping("/api/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestHeader (value = "Authorization")String refreshToken){
        return Response.success(signService.refreshToken(refreshToken));
    }
}

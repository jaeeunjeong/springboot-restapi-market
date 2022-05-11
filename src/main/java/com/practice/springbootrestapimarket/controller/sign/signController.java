package com.practice.springbootrestapimarket.controller.sign;


import com.practice.springbootrestapimarket.controller.response.Response;
import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.service.sign.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class signController {

    @Autowired
    SignService signService;

    @PostMapping("/api/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signIn(@Valid @RequestBody SignInRequest req) {
        signService.signIn(req);
        return Response.success();
    }

    @PostMapping("/api/signIn")
    @ResponseStatus(HttpStatus.OK)
    public Response signUp(@Valid @RequestBody SignUpRequest req) {
        signService.signUp(req);
        return Response.success();
    }


}
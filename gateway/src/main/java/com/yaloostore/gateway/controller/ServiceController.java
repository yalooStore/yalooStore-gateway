package com.yaloostore.gateway.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/service-test")
public class ServiceController {

    @Value("${server.port}")
    private String port;

    @GetMapping
    public String check(){
        return String.format("gateway server test - PORT " + port);
    }

    @Value("${yalooStore.server.front}")
    private String frontSever;

    @GetMapping("getFrontServer-test")
    public String getFrontSeverTest(){
        return String.format("server address : "+ frontSever);
    }


}

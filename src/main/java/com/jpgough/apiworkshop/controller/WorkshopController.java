package com.jpgough.apiworkshop.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkshopController {

    @GetMapping("/hello")
    @ApiOperation("Hello World, used in the bootstrap of the lab")
    public String helloWorld() {
        return "Hello World";
    }

}
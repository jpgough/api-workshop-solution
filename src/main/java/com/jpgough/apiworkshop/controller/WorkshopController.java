package com.jpgough.apiworkshop.controller;

import com.jpgough.apiworkshop.domain.Todo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WorkshopController {

    @GetMapping("/hello")
    @ResponseBody
    @ApiOperation("Hello World, used in the bootstrap of the lab")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/todos")
    @ResponseBody
    @ApiOperation("Returns the todos stored to the server")
    public List<Todo> todos() {
        return new ArrayList<>();
    }



}
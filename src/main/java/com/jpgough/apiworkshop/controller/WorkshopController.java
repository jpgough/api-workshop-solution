package com.jpgough.apiworkshop.controller;

import com.jpgough.apiworkshop.domain.Todo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
        //TODO implement
        return new ArrayList<>();
    }

    @PostMapping("/todos/{id}")
    @ApiOperation("Persist a new todo to the server")
    public void addTodo(@RequestAttribute("id") Integer id, @RequestBody Todo todo) {
        //TODO implement
    }

    @GetMapping("/todos/{id}")
    @ResponseBody
    @ApiOperation("Request a todo item by id")
    public Todo getTodo(@RequestAttribute("id") Integer id) {
        //TODO implement
        return new Todo();
    }

    @DeleteMapping("/todos/{id}")
    @ApiOperation("Delete a todo item by id")
    public void deleteTodo(@RequestAttribute("id") Integer id) {
        //TODO implement
    }

}
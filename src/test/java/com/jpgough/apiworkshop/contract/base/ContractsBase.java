package com.jpgough.apiworkshop.contract.base;

import com.jpgough.apiworkshop.controller.TodosController;
import com.jpgough.apiworkshop.domain.TodoStore;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;

import static java.nio.charset.StandardCharsets.UTF_8;


public abstract class ContractsBase {

    TodosController todoController = new TodosController(new TodoStore(), 8080);

    @BeforeEach
    public void setup() {
        //* FIX for https://github.com/spring-cloud/spring-cloud-contract/issues/1428
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(new EncoderConfig(UTF_8.name(), UTF_8.name()));
        //*
        RestAssuredMockMvc.standaloneSetup(this.todoController);
//        todoController.createNewTodo(new Todo(0, "Mkae the bed"));
    }
}

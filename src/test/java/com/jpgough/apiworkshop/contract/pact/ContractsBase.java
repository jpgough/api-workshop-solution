package com.jpgough.apiworkshop.contract.pact;

import com.jpgough.apiworkshop.controller.TodosController;
import com.jpgough.apiworkshop.domain.TodoStore;
import com.jpgough.apiworkshop.model.Todo;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;


public abstract class ContractsBase {

    TodosController todoController = new TodosController(new TodoStore(), 8080);

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(this.todoController);
        todoController.createNewTodo(new Todo(1, "Mkae the bed"));
    }
}

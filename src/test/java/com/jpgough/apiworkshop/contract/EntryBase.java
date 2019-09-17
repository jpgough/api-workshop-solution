package com.jpgough.apiworkshop.contract;

import com.jpgough.apiworkshop.controller.TodosController;
import com.jpgough.apiworkshop.domain.TodoStore;
import com.jpgough.apiworkshop.model.Todo;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;


public abstract class EntryBase {

    TodosController todoController = new TodosController(new TodoStore());

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(this.todoController);
        todoController.createNewTodo(new Todo("Mkae the bed"));
    }
}

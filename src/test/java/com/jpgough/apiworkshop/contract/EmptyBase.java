package com.jpgough.apiworkshop.contract;

import com.jpgough.apiworkshop.controller.TodosController;
import com.jpgough.apiworkshop.domain.TodoStore;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;



public abstract class EmptyBase {

    TodosController todoController = new TodosController(new TodoStore());

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(this.todoController);
    }
}

package com.jpgough.apiworkshop.contract;

import com.jpgough.apiworkshop.controller.TodosController;
import com.jpgough.apiworkshop.domain.TodoStore;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = EmptyBase.DefaultConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext
public abstract class EmptyBase {

    @Autowired
    WebApplicationContext context;

    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(this.context);
    }

    @EnableAutoConfiguration
    @Configuration
    static class DefaultConfig {

        @Bean
        public TodosController todosController() {
            return new TodosController(todoStore());
        }

        @Bean
        public TodoStore todoStore() {
            return new TodoStore();
        }
    }
}

package com.jpgough.apiworkshop.contract;

import com.jpgough.apiworkshop.controller.TodosController;
import com.jpgough.apiworkshop.domain.TodoStore;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static java.nio.charset.StandardCharsets.UTF_8;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = ContractsEmptyBase.DefaultConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext
public abstract class ContractsEmptyBase {

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    public void setup() {
        //* FIX for https://github.com/spring-cloud/spring-cloud-contract/issues/1428
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(new EncoderConfig(UTF_8.name(), UTF_8.name()));
        //*
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

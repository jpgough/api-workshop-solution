package com.jpgough.apiworkshop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpgough.apiworkshop.domain.InvalidTodoIdException;
import com.jpgough.apiworkshop.domain.TodoStore;
import com.jpgough.apiworkshop.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestTodosControllerShould.DefaultConfig.class
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestTodosControllerShould {

    @LocalServerPort
    String serverPort;

    @Autowired
    TodoStore todoStore;

    private WebTestClient webClient;

    @BeforeEach
    public void setUp() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + serverPort + "/")
                .build();
    }

    @Test
    public void return_an_ok_response_with_an_empty_store() {
        webClient.get()
                .uri("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("[]");
    }

    @Test
    public void return_an_ok_response_with_item_in_store() {
        final var returnedTodo = new ResultTodo(0, "Hello");

        preAddTodoToStore(0, returnedTodo.getMessage());

        Collection<ResultTodo> expectedResult = new ArrayList<>();
        expectedResult.add(returnedTodo);
        webClient.get()
                .uri("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Collection<ResultTodo>>() {
                })
                .isEqualTo(expectedResult);
    }

    @Test
    public void return_a_missing_resource_when_item_is_not_in_store() {
        webClient.get()
                .uri("/todos/0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void return_a_specific_todo_when_item_is_in_store() {
        final var returnedTodo = new ResultTodo(0, "Hello");

        preAddTodoToStore(0, "Hello");

        webClient.get()
                .uri("/todos/0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResultTodo.class)
                .isEqualTo(returnedTodo);
    }

    @Test
    public void created_todo_returns_a_created_status() {
        final var requestTodo = new ResultTodo("Hello");
        final var resultTodo = new ResultTodo(0, "Hello");
        webClient.post()
                .uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestTodo), ResultTodo.class)
                .exchange()
                .expectStatus()
                .isCreated();
        webClient.get()
                .uri("/todos/0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResultTodo.class)
                .isEqualTo(resultTodo);
    }

    @Test
    public void replaced_todo_updates_the_todo() throws InvalidTodoIdException {
        final var requestTodo = new ResultTodo("Hello");

        preAddTodoToStore(0, "hello");

        webClient.put()
                .uri("/todos/0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestTodo), ResultTodo.class)
                .exchange()
                .expectStatus()
                .isNoContent();
        assertEquals("Hello", todoStore.getTodo(0).getMessage());
    }

    @Test
    public void returns_a_missing_resource_when_trying_to_replace_item_not_in_the_store() {
        webClient.put()
                .uri("/todos/0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new ResultTodo("Hello")), ResultTodo.class)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void returns_a_missing_resource_when_trying_to_delete_item_not_in_the_store() {
        webClient.delete()
                .uri("/todos/0")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void returns_a_success_resource_when_deleting_item_in_the_store() {
        preAddTodoToStore(0, "Hello");
        webClient.delete()
                .uri("/todos/0")
                .exchange()
                .expectStatus()
                .isNoContent();
        assertEquals(0, todoStore.getTodos().size());

    }

    private void preAddTodoToStore(int i, String hello) {
        final var todo = new Todo(0, "Hello");
        todoStore.addTodo(todo);
    }


    @EnableAutoConfiguration
    @Configuration
    public static class DefaultConfig {

        @Bean
        public TodoStore todoStore() {
            return new TodoStore();
        }

        @Bean
        public TodosController todosController() {
            return new TodosController(todoStore());
        }
    }

    private static class ResultTodo {
        private Integer id;
        private String message;

        public ResultTodo() {
        }

        public ResultTodo(Integer id, String message) {
            this.message = message;
            this.id = id;
        }

        public ResultTodo(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResultTodo resultToDo = (ResultTodo) o;
            return Objects.equals(message, resultToDo.message) &&
                    Objects.equals(id, resultToDo.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, id);
        }

        public String getMessage() {
            return message;
        }

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        public Integer getId() {
            return id;
        }
    }
}
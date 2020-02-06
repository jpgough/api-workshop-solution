package com.jpgough.apiworkshop.controller;

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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
                .json("{}");
    }

    @Test
    public void return_an_ok_response_with_item_in_store() {
        final var helloTodo = new Todo("Hello");
        todoStore.addTodo(helloTodo);
        Map<Integer, Todo> expectedResult = new HashMap<>();
        expectedResult.put(0, helloTodo);
        webClient.get()
                .uri("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Map<Integer, Todo>>() {
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
        final var helloTodo = new Todo("Hello");
        todoStore.addTodo(helloTodo);
        Map<Integer, Todo> expectedResult = new HashMap<>();
        expectedResult.put(0, helloTodo);
        webClient.get()
                .uri("/todos/0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Todo.class)
                .isEqualTo(helloTodo);
    }

    @Test
    public void created_todo_returns_a_created_status() {
        final var helloTodo = new Todo("Hello");
        webClient.post()
                .uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(helloTodo), Todo.class)
                .exchange()
                .expectStatus()
                .isCreated();
        webClient.get()
                .uri("/todos/0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Todo.class)
                .isEqualTo(helloTodo);
    }

    @Test
    public void replaced_todo_updates_the_todo() throws InvalidTodoIdException {
        final var helloTodo = new Todo("hello");
        todoStore.addTodo(helloTodo);
        Map<Integer, Todo> expectedResult = new HashMap<>();
        expectedResult.put(0, helloTodo);
        webClient.put()
                .uri("/todos/0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Todo("Hello")), Todo.class)
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
                .body(Mono.just(new Todo("Hello")), Todo.class)
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
        final var helloTodo = new Todo("hello");
        todoStore.addTodo(helloTodo);
        Map<Integer, Todo> expectedResult = new HashMap<>();
        expectedResult.put(0, helloTodo);
        webClient.delete()
                .uri("/todos/0")
                .exchange()
                .expectStatus()
                .isNoContent();
        assertEquals(0, todoStore.getTodos().size());

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
}
package com.jpgough.apiworkshop.controller;

import com.jpgough.apiworkshop.domain.InvalidTodoIdException;
import com.jpgough.apiworkshop.domain.TodoStore;
import com.jpgough.apiworkshop.model.Todo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;

@RestController
public class TodosController {

    private final TodoStore todoStore;

    @Value("${server.port}")
    private int port;

    @Autowired
    public TodosController(TodoStore todoStore) {
        this.todoStore = todoStore;
    }

    public TodosController(TodoStore todoStore, int port) {
        this.todoStore = todoStore;
        this.port = port;
    }

    @GetMapping("/todos")
    @ApiOperation("Returns the todos stored to the server")
    public Collection<Todo> getAllTodos() {
        return todoStore.getTodos()
                .values();
    }

    @PostMapping(value = "/todos")
    @ApiOperation("Persist a new todo to the server")
    public ResponseEntity<Void> createNewTodo(@Validated @RequestBody Todo todo) {
        final var createdTodo = todoStore.addTodo(todo);
        final var uri = URI.create("http://localhost:" + port + "/todos/" + createdTodo.getId());
        return ResponseEntity
                .created(uri)
                .build();
    }

    @GetMapping("/todos/{id}")
    @ApiOperation("Request a todo item by id")
    public ResponseEntity<Todo> getTodo(@PathVariable(value = "id") Integer id) {
        try {
            return ResponseEntity.ok(todoStore.getTodo(id));
        } catch (InvalidTodoIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/todos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Update a todo item by id")
    public ResponseEntity<Void> updateTodo(@PathVariable(value = "id") Integer id, @RequestBody Todo todo) {
        try {
            todoStore.replace(new Todo(id, todo.getMessage()));
            return ResponseEntity.noContent().build();
        } catch (InvalidTodoIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/todos/{id}")
    @ApiOperation("Delete a todo item by id")
    public ResponseEntity<Void> deleteTodo(@PathVariable(value = "id") Integer id) {
        try {
            todoStore.removeTodo(id);
            return ResponseEntity.noContent().build();
        } catch (InvalidTodoIdException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
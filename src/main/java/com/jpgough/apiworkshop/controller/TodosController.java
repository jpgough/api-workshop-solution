package com.jpgough.apiworkshop.controller;

import com.jpgough.apiworkshop.domain.InvalidTodoIdException;
import com.jpgough.apiworkshop.domain.TodoStore;
import com.jpgough.apiworkshop.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
public class TodosController {

    @Autowired
    private final TodoStore todoStore;

    @Autowired
    public TodosController(TodoStore todoStore) {
        this.todoStore = todoStore;
    }

    @GetMapping("/todos")
    public Map<Integer, Todo> getAllTodos() {
        return todoStore.getTodos();
    }

    @PostMapping(value = "/todos")
    public ResponseEntity<Void> createNewTodo(@RequestBody Todo todo) {
        todoStore.addTodo(todo);
        final var uri = URI.create("http://localhost/todos/");
        return ResponseEntity
                .created(uri)
                .build();
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable(value = "id") Integer id) {
        try {
            return ResponseEntity.ok(todoStore.getTodo(id));
        } catch (InvalidTodoIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/todos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTodo(@PathVariable(value = "id") Integer id, @RequestBody Todo todo) {
        try {
            todoStore.replace(id, todo);
            return ResponseEntity.noContent().build();
        } catch (InvalidTodoIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable(value = "id") Integer id){
        if(!todoStore.getTodos().containsKey(id)){
            return ResponseEntity.notFound().build();
        }
        todoStore.removeTodo(id);
        return ResponseEntity.noContent().build();
    }
}
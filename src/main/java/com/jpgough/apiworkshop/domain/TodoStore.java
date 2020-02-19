package com.jpgough.apiworkshop.domain;

import com.jpgough.apiworkshop.model.Todo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TodoStore {

    AtomicInteger incrementKey = new AtomicInteger(0);
    Map<Integer, Todo> todos = new HashMap<>(20);

    public Map<Integer, Todo> getTodos() {
        return Collections.unmodifiableMap(todos);
    }

    public Todo getTodo(Integer id) throws InvalidTodoIdException {
        if (!todos.containsKey(id)) {
            throw new InvalidTodoIdException();
        }
        return todos.get(id);
    }

    public Todo addTodo(Todo todo) {
        Todo newTodo = new Todo(incrementKey.getAndAdd(1), todo.getMessage());
        todos.put(newTodo.getId(), newTodo);
        return newTodo;
    }

    public void replace(Todo todo) throws InvalidTodoIdException {
        if (!todos.containsKey(todo.getId())) {
            throw new InvalidTodoIdException();
        }
        todos.replace(todo.getId(), todo);
    }

    public void removeTodo(Integer id) throws InvalidTodoIdException {
        if (!todos.containsKey(id)) {
            throw new InvalidTodoIdException();
        }
        todos.remove(id);
    }
}

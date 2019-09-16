package com.jpgough.apiworkshop.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoStore {

    private Map<Integer, Todo> todos = new HashMap<>();

    public List<Todo> getTodos() {
        return new ArrayList<>(todos.values());
    }

    public void getTodo(Integer id) throws InvalidTodoIdException {
        throw new InvalidTodoIdException();
    }
}

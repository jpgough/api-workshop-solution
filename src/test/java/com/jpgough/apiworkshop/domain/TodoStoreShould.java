package com.jpgough.apiworkshop.domain;

import com.jpgough.apiworkshop.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TodoStoreShould {

    private TodoStore todoStore;

    @BeforeEach
    public void before() {
        todoStore = new TodoStore();
    }

    @Test
    public void start_with_an_empty_store() {
        assertNotNull(todoStore.getTodos());
        assertEquals(0, todoStore.getTodos().size());
    }

    @Test
    public void throw_a_invalid_todo_exception_for_missing_get_todo() throws InvalidTodoIdException {
        assertThrows(InvalidTodoIdException.class, () -> todoStore.getTodo(5));
    }

    @Test
    public void add_a_todo_to_the_store() throws InvalidTodoIdException {
        String message = "message";
        todoStore.addTodo(new Todo(0, message));
        assertEquals(1, todoStore.getTodos().size());
        assertEquals(message, todoStore.getTodo(0).getMessage());
    }

    @Test
    public void replace_a_todo_that_exists() throws InvalidTodoIdException {
        String message = "new message";
        String newMessage = "message";
        todoStore.addTodo(new Todo(0, message));
        assertEquals(1, todoStore.getTodos().size());
        todoStore.replace(new Todo(0, newMessage));
        assertEquals(1, todoStore.getTodos().size());
        assertEquals(newMessage, todoStore.getTodo(0).getMessage());
    }

    @Test
    public void throw_a_invalid_todo_exception_for_missing_replace_todo() throws InvalidTodoIdException {
        assertThrows(InvalidTodoIdException.class, () -> todoStore.replace(new Todo(2, "message")));
    }

    @Test
    public void throw_a_invalid_todo_exception_for_missing_remove_todo() throws InvalidTodoIdException {
        assertThrows(InvalidTodoIdException.class, () -> todoStore.removeTodo(2));
    }

    @Test
    public void remove_a_todo_that_exists_in_the_store() throws InvalidTodoIdException {
        String message1 = "message1";
        String message2 = "message2";
        todoStore.addTodo(new Todo(0, message1));
        todoStore.addTodo(new Todo(1, message2));
        assertEquals(2, todoStore.getTodos().size());
        todoStore.removeTodo(1);
        assertEquals(1, todoStore.getTodos().size());
        assertEquals(new Todo(0, message1), todoStore.getTodo(0));
    }
}

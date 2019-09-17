package com.jpgough.apiworkshop.domain;

import com.jpgough.apiworkshop.model.Todo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TodoStoreShould {

    private TodoStore todoStore;

    @Before
    public void before() {
        todoStore = new TodoStore();
    }

    @Test
    public void start_with_an_empty_store() {
        assertNotNull(todoStore.getTodos());
        assertEquals(0, todoStore.getTodos().size());
    }

    @Test(expected = InvalidTodoIdException.class)
    public void throw_a_invalid_todo_exception_for_missing_get_todo() throws InvalidTodoIdException {
        todoStore.getTodo(5);
    }

    @Test
    public void add_a_todo_to_the_store() throws InvalidTodoIdException {
        String message = "message";
        todoStore.addTodo(new Todo(message));
        assertEquals(1, todoStore.getTodos().size());
        assertEquals(message, todoStore.getTodo(0).getMessage());
    }

    @Test
    public void replace_a_todo_that_exists() throws InvalidTodoIdException {
        String message = "new message";
        String newMessage = "message";
        todoStore.addTodo(new Todo(message));
        assertEquals(1, todoStore.getTodos().size());
        todoStore.replace(0, new Todo(newMessage));
        assertEquals(1, todoStore.getTodos().size());
        assertEquals(newMessage, todoStore.getTodo(0).getMessage());
    }

    @Test(expected = InvalidTodoIdException.class)
    public void throw_a_invalid_todo_exception_for_missing_replace_todo() throws InvalidTodoIdException {
        todoStore.replace(2, new Todo("message"));
    }

    @Test(expected = InvalidTodoIdException.class)
    public void throw_a_invalid_todo_exception_for_missing_remove_todo() throws InvalidTodoIdException {
        todoStore.removeTodo(2);
    }

    @Test
    public void remove_a_todo_that_exists_in_the_store() throws InvalidTodoIdException {
        String message1 = "message1";
        String message2 = "message2";
        todoStore.addTodo(new Todo(message1));
        todoStore.addTodo(new Todo(message2));
        assertEquals(2, todoStore.getTodos().size());
        todoStore.removeTodo(1);
        assertEquals(1, todoStore.getTodos().size());
        assertEquals(new Todo(message1), todoStore.getTodo(1).getMessage());
    }
}

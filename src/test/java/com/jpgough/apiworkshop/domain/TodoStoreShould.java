package com.jpgough.apiworkshop.domain;

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
    public void throw_a_invalid_todo_exception_for_missing_todo() throws InvalidTodoIdException {
        todoStore.getTodo(5);
    }
}

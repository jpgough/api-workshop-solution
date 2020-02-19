package com.jpgough.apiworkshop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class Todo {

    private Integer id;

    @NotEmpty
    private String message;

    public Todo() {
        //Used for serialization
    }

    public Todo(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(message, todo.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }
}

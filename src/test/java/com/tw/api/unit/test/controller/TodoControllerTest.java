package com.tw.api.unit.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;
import com.tw.api.unit.test.services.ShowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired
    private TodoController todoController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;



    @Test
    void getAll() throws Exception {
        //given
        List<Todo> todos = new ArrayList<>();
        Todo todo = new Todo(1,"sample mo ako",false,2);
        todos.add(todo);
        when(todoRepository.getAll()).thenReturn(todos);
        //when
        ResultActions result = mvc.perform(get("/todos")) ;
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title").value("sample mo ako"));
    }

    @Test
    void getTodo() throws Exception {
        //given
        List<Todo> todos = new ArrayList<>();
        Todo todo = new Todo(0,"sample mo ako",false,2);
        todos.add(todo);
        //when
        when(todoRepository.findById(0)).thenReturn(todos.stream().findFirst());
        //then
        ResultActions result = mvc.perform(get("/todos/{todo-id}", 0L));
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.title").value("sample mo ako"));
    }

    @Test
    void saveTodo() throws Exception {
        Todo todo = new Todo(0,"sample mo ako",false,2);
        //when
        ResultActions result = mvc.perform(post("/todos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(todo)));
        //then
        result.andExpect(status().isCreated());
    }

//    @Test
//    void deleteOneTodo() {
//    }

    @Test
    void deleteOneTodo() throws Exception {
        //given
        List<Todo> todos = new ArrayList<>();
        Todo todo = new Todo(0, "sample mo ako", false, 2);
        todos.add(todo);
        //when
        when(todoRepository.findById(0)).thenReturn(todos.stream().findFirst());
        //then
        ResultActions result = mvc.perform(delete("/todos/{todo-id}", 0L));
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateTodo() throws Exception {
        List<Todo> todos = new ArrayList<>();
        //given
        Todo todo = new Todo(1,"sample mo ako", true,2);
        Todo todoNewValues = new Todo(1,"sample mo siya", true,2);
        todos.add(todo);
        when(todoRepository.findById(1)).thenReturn(todos.stream().findFirst());
        //when
        ResultActions result = mvc.perform(patch("/todos/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(todoNewValues)));
        //then
        result.andExpect(status().isOk());
//                .andDo(print());
    }
}
package org.example.daas.data.controller;

import org.assertj.core.util.Lists;
import org.example.daas.data.model.User;
import org.example.daas.data.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DaasController.class)
public class DaasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void controllerShouldNotReturnNothingWhenTheEndpointIsInvalid() throws Exception {
        List<User> mockUsers = Lists.newArrayList();

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/daas/dummy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUsers_shouldReturnsAnEmptyListOfUsersWhenThereAreNoUsers() throws Exception {
        List<User> mockUsers = Lists.newArrayList();

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/daas/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void getUsers_shouldReturnsListOfUsersWhenThereAreUsers() throws Exception {
        List<User> mockUsers = List.of(
                new User(1, "John"),
                new User(2, "Alice")
        );

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/daas/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Alice")));
    }
}
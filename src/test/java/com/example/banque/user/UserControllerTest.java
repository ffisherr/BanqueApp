package com.example.banque.user;

import com.example.banque.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends AbstractControllerTest {

    private static final String NAME = "John";

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setup() {
        this.localUri = "http://localhost:" + port + "/users";
    }

    @Test
    public void shouldCreateUser() {
        // given
        final var request = new UserCreateRequest(NAME, BigDecimal.ZERO);

        // when
        final var response = restTemplate.postForEntity(localUri, request, UserResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final var createdUser = response.getBody();
        assertThat(repository.existsById(createdUser.getId())).isEqualTo(true);
        assertThat(createdUser.getName()).isEqualTo(NAME);
        assertThat(createdUser.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void shouldGetUserDetails() {
        // given
        final var user = repository.save(UserEntity.builder().name(NAME).balance(BigDecimal.TEN).build());

        // when
        final var response = restTemplate.getForEntity(localUri + "/" + user.getId(), UserResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(user.getId());
        assertThat(body.getName()).isEqualTo(user.getName());
        assertThat(body.getBalance().compareTo(user.getBalance())).isZero();
    }
}

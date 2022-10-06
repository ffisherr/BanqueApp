package com.example.banque.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

    private static final String NAME = "John Smith";
    private final UserRepository repository = mock(UserRepository.class);
    private final UserService underTest = new UserServiceImpl(repository);

    @ParameterizedTest
    @CsvSource({"1.0", "0.0", "100.52"})
    public void shouldCreateUser_whenCorrectRequest() {
        // given
        final var balance = BigDecimal.ZERO;
        final var request = new UserCreateRequest(NAME, balance);
        final var entity = UserEntity.builder().name(NAME).balance(balance).build();
        given(repository.save(entity))
                .willReturn(new UserEntity(1L, NAME, balance));

        // when
        final var result = underTest.create(request);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getBalance()).isEqualTo(balance);
        then(repository).should().save(entity);
    }

    @Test
    public void shouldThrow_whenNegativeAmount() {
        // then
        assertThatThrownBy(() -> underTest.create(new UserCreateRequest(NAME, new BigDecimal("-1.0"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Initial amount should not be less then zero");
    }

    @Test
    public void shouldThrow_whenEmptyName() {
        // then
        assertThatThrownBy(() -> underTest.create(new UserCreateRequest(" ", BigDecimal.ZERO)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name should not be empty");
    }

    @Test
    public void shouldGetUserDetails() {
        // given
        final var userId = 1L;
        given(repository.findById(userId))
                .willReturn(Optional.of(UserEntity.builder().id(userId).name("Name").balance(BigDecimal.TEN).build()));

        // when
        final var user = underTest.userDetails(userId);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Name");
        assertThat(user.getBalance().compareTo(BigDecimal.TEN)).isZero();
    }

    @Test
    public void shouldThrow_whenUserNotExist() {
        // then
        assertThatThrownBy(() -> underTest.userDetails(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong id");
    }
}

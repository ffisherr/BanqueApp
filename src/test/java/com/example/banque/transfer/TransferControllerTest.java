package com.example.banque.transfer;

import com.example.banque.AbstractControllerTest;
import com.example.banque.user.UserEntity;
import com.example.banque.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setup() {
        this.localUri = "http://localhost:" + port + "/transfers";
    }

    @Test
    public void shouldPerformTransfer() {
        // given
        final var sender = aUser("John", BigDecimal.TEN);
        final var recipient = aUser("Stan", BigDecimal.ZERO);

        final var request = new TransferCreationRequest(sender.getId(), recipient.getId(), BigDecimal.TEN);

        // when
        restTemplate.postForLocation(localUri, request);

        // then
        assertThat(repository.findById(sender.getId()).get().getBalance().compareTo(BigDecimal.ZERO)).isZero();
        assertThat(repository.findById(recipient.getId()).get().getBalance().compareTo(BigDecimal.TEN)).isZero();
    }

    private UserEntity aUser(String name, BigDecimal amount) {
        return repository.save(UserEntity.builder().name(name).balance(amount).build());
    }
}

package com.example.banque.transfer;

import com.example.banque.user.UserEntity;
import com.example.banque.user.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class TransferServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final TransferService underTest = new TransferServiceImpl(userRepository);

    @ParameterizedTest
    @CsvSource({
            "100,0,10.5",
            "10,11,10"
    })
    public void shouldTransfer_whenCorrectRequest(BigDecimal senderAmount, BigDecimal recipientAmount,
                                                  BigDecimal transferAmount) {
        // given
        final var sender = aUser(1L, "John", senderAmount);
        final var recipient = aUser(2L, "Mike", recipientAmount);
        final var request = new TransferCreationRequest(sender.getId(), recipient.getId(), transferAmount);

        // when
        underTest.transfer(request);

        // then
        then(userRepository).should().saveAll(List.of(
                new UserEntity(1L, "John", senderAmount.subtract(transferAmount)),
                new UserEntity(2L, "Mike", recipientAmount.add(transferAmount))
        ));
    }

    private UserEntity aUser(Long id, String name, BigDecimal initialAmount) {
        final var user = UserEntity.builder().id(id).name(name).balance(initialAmount).build();
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        return user;
    }
}

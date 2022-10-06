package com.example.banque.transfer;

import com.example.banque.user.UserEntity;
import com.example.banque.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void transfer(TransferCreationRequest request) {
        final var sender = fetchUser(request.senderId());
        final var recipient = fetchUser(request.recipientId());
        validateTransfer(request, sender);
        sender.setBalance(sender.getBalance().subtract(request.amount()));
        recipient.setBalance(recipient.getBalance().add(request.amount()));
        userRepository.saveAll(List.of(sender, recipient));
    }

    private void validateTransfer(TransferCreationRequest request, UserEntity sender) {
        checkArgument(!request.senderId().equals(request.recipientId()), "Cannot transfer to yourself");
        checkArgument(request.amount().compareTo(BigDecimal.ZERO) > 0, // only for p2p, amount can be 0 for card auth
                "Transfer amount must be greater then zero");
        checkArgument(sender.getBalance().compareTo(request.amount()) >= 0, "Not enough money");
        // also possible to add validation on transfer in/out limits
    }

    private UserEntity fetchUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")); // message may expose IDOR
    }

    private void checkArgument(boolean isOk, String errorMessage) {
        if (!isOk)
            throw new IllegalArgumentException(errorMessage);
    }
}

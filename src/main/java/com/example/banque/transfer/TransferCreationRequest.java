package com.example.banque.transfer;

import java.math.BigDecimal;

public record TransferCreationRequest(Long senderId, Long recipientId, BigDecimal amount) {
}

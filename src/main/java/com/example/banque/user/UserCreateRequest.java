package com.example.banque.user;

import java.math.BigDecimal;

public record UserCreateRequest(String name, BigDecimal initialAmount) {
}

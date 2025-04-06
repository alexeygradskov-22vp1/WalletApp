package ru.gav.walletapp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class WalletDto {
    UUID walletId;
    BigDecimal balance;
}

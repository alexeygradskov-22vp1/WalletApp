package ru.gav.walletapp.dto;

import lombok.Value;
import ru.gav.walletapp.entity.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for {@link ru.gav.walletapp.entity.WalletOperation}
 */
@Value
public class WalletOperationDto {
    UUID operationId;
    OperationType operationType;
    UUID walletId;
    BigDecimal cost;
}
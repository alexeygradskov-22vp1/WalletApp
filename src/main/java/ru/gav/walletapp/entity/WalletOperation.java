package ru.gav.walletapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.gav.walletapp.entity.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "history_operations", schema = "wallet_app")
public class WalletOperation {
    @Id
    private UUID operationId;
    private OperationType operationType;
    private UUID walletId;
    private BigDecimal cost;
}

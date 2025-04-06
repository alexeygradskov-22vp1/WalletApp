package ru.gav.walletapp.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.gav.walletapp.entity.WalletOperation;

import java.util.Map;
import java.util.UUID;

public interface WalletOperationRepository extends R2dbcRepository<WalletOperation, UUID> {
    Flux<WalletOperation> findAllByWalletId(UUID walletId);
}

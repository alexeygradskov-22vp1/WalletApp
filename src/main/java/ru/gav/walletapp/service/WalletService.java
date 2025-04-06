package ru.gav.walletapp.service;

import reactor.core.publisher.Mono;
import ru.gav.walletapp.dto.WalletDto;
import ru.gav.walletapp.dto.WalletOperationDto;

import java.util.UUID;

public interface WalletService {
    Mono<WalletDto> postWalletOperation(WalletOperationDto walletOperationDto);

    Mono<WalletDto> getWallet(UUID walletId);

    Mono<WalletDto> postWallet();

}

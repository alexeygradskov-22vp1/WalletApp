package ru.gav.walletapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.gav.walletapp.dto.WalletDto;
import ru.gav.walletapp.dto.WalletOperationDto;
import ru.gav.walletapp.service.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/{walletId}")
    public Mono<WalletDto> getWallet(@PathVariable(name = "walletId") UUID walletId) {
        return walletService.getWallet(walletId);
    }

    @PostMapping("/operations")
    public Mono<WalletDto> postWalletOperation(@RequestBody WalletOperationDto walletOperationDto) {
        return walletService.postWalletOperation(walletOperationDto);
    }

    @PostMapping("")
    public Mono<WalletDto> postWallet() {
        return walletService.postWallet();
    }
}


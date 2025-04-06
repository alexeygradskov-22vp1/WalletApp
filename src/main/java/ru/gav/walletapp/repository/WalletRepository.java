package ru.gav.walletapp.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.gav.walletapp.dto.WalletDto;
import ru.gav.walletapp.entity.Wallet;

import java.util.UUID;

public interface WalletRepository extends R2dbcRepository<Wallet, UUID> {

}

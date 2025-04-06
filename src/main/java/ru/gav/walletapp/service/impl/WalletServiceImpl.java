package ru.gav.walletapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import ru.gav.walletapp.dto.WalletDto;
import ru.gav.walletapp.dto.WalletOperationDto;
import ru.gav.walletapp.entity.OperationType;
import ru.gav.walletapp.entity.Wallet;
import ru.gav.walletapp.entity.WalletOperation;
import ru.gav.walletapp.exception.supplier.ExceptionSupplier;
import ru.gav.walletapp.mapper.WalletMapper;
import ru.gav.walletapp.mapper.WalletOperationMapper;
import ru.gav.walletapp.repository.WalletOperationRepository;
import ru.gav.walletapp.repository.WalletRepository;
import ru.gav.walletapp.service.WalletService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletOperationRepository walletOperationRepository;
    private final WalletMapper walletMapper;
    private final WalletOperationMapper walletOperationMapper;
    private final ExceptionSupplier exceptionSupplier;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<WalletDto> getWallet(UUID walletId) {
        return walletRepository.findById(walletId).map(walletMapper::mapEntityToDto);
    }

    @Override
    public Mono<WalletDto> postWallet() {
        Wallet wallet = Wallet
                .builder()
                .build();
        return walletRepository.save(wallet).map(walletMapper::mapEntityToDto);
    }

    @Override
    @Transactional
    public Mono<WalletDto> postWalletOperation(WalletOperationDto walletOperationDto) {
        WalletOperation walletOperation = walletOperationMapper.mapDtoToEntity(walletOperationDto);
        return walletRepository.findById(walletOperationDto.getWalletId())
                .switchIfEmpty(Mono.error(exceptionSupplier.walletNotFoundExceptionSupplier(walletOperationDto.getWalletId())))
                .then(walletOperationRepository.save(walletOperation))
                .thenMany(walletOperationRepository.findAllByWalletId(walletOperationDto.getWalletId()))
                .groupBy(WalletOperation::getOperationType)
                .flatMap(group ->
                        group
                                .map(WalletOperation::getCost)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .map(sum -> Map.entry(group.key(), sum))
                )
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .map(map -> {
                    BigDecimal deposits = map.getOrDefault(OperationType.DEPOSIT, BigDecimal.ZERO);
                    BigDecimal withdrawals = map.getOrDefault(OperationType.WITHDRAW, BigDecimal.ZERO);
                    return deposits.subtract(withdrawals);
                })
                .map(balance -> balance.compareTo(BigDecimal.ZERO) < 0 ?
                        Mono.error(exceptionSupplier.balanceExceptionSupply(walletOperationDto.getWalletId())) :
                        Mono.just(balance))
                .flatMap(balanceMono -> balanceMono
                        .flatMap(balance -> {
                            return updateBalance(walletOperationDto.getWalletId(), (BigDecimal) balance);
                        })
                )
                .as(transactionalOperator::transactional)
                .map(walletMapper::mapEntityToDto);
    }

    private Mono<Wallet> updateBalance(UUID walletId, BigDecimal balance) {
        return walletRepository
                .findById(walletId)
                .flatMap(wallet -> {
                    wallet.setBalance(balance);
                    return  walletRepository.save(wallet);
                });
    }

}

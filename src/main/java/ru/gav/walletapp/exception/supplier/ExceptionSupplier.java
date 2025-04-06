package ru.gav.walletapp.exception.supplier;

import org.springframework.stereotype.Component;
import ru.gav.walletapp.exception.BalanceException;
import ru.gav.walletapp.exception.WalletNotFoundException;

import java.util.UUID;
import java.util.function.Supplier;

@Component
public class ExceptionSupplier {
    public Supplier<BalanceException> balanceExceptionSupply(UUID walletId){
        return ()->new BalanceException(String.format("Баланс не может быть отрицательным. Id кошелька: %s", walletId));
    }

    public Supplier<WalletNotFoundException> walletNotFoundExceptionSupplier(UUID walletId){
        return ()->new WalletNotFoundException(String.format("Кошелёк с id %s не найден.", walletId));
    }
}

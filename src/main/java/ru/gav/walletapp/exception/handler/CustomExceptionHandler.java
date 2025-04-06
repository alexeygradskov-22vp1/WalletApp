package ru.gav.walletapp.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gav.walletapp.exception.BalanceException;
import ru.gav.walletapp.exception.WalletNotFoundException;
import ru.gav.walletapp.exception.handler.model.ExceptionModel;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionModel notFoundHandler(WalletNotFoundException ex) {
        return new ExceptionModel(ex.getMessage());
    }

    @ExceptionHandler(BalanceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionModel balanceExceptionHandler(BalanceException ex) {
        return new ExceptionModel(ex.getMessage());
    }
}

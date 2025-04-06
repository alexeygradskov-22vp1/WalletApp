package ru.gav.walletapp.exception;

public class BalanceException extends RuntimeException {
  public BalanceException(String message) {
    super(message);
  }
}

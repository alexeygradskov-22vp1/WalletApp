package ru.gav.walletapp.mapper;

import org.mapstruct.Mapper;
import ru.gav.walletapp.dto.WalletDto;
import ru.gav.walletapp.entity.Wallet;

@Mapper(componentModel = "SPRING")
public interface WalletMapper {
    Wallet mapDtoToEntity(WalletDto walletDto);

    WalletDto mapEntityToDto(Wallet wallet);
}

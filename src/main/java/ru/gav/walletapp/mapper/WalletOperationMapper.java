package ru.gav.walletapp.mapper;

import org.mapstruct.Mapper;
import ru.gav.walletapp.dto.WalletOperationDto;
import ru.gav.walletapp.entity.WalletOperation;

@Mapper(componentModel = "SPRING")
public interface WalletOperationMapper {
    WalletOperation mapDtoToEntity(WalletOperationDto walletOperationDto);
}

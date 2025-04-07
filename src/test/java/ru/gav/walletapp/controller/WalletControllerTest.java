package ru.gav.walletapp.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import ru.gav.walletapp.dto.WalletOperationDto;
import ru.gav.walletapp.entity.enums.OperationType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
@Testcontainers
public class WalletControllerTest {
    private static Map<String, String> map = Map.of(
            "POSTGRES_PASSWORD", "root",
            "POSTGRES_USERNAME", "root",
            "POSTGRES_DB", "testdb");
    @Autowired
    private WalletController walletController;

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName(map.get("POSTGRES_DB"))
                    .withPassword(map.get("POSTGRES_PASSWORD"))
                    .withUsername(map.get("POSTGRES_USERNAME"));

    @DynamicPropertySource
    static void dataSourceProps(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.liquibase.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
        registry.add("spring.liquibase.password", postgreSQLContainer::getPassword);

        registry.add("spring.r2dbc.url", () -> String.format("r2dbc:postgresql://%s:%d/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getFirstMappedPort(),
                postgreSQLContainer.getDatabaseName()
        ));
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.change-log", () -> "db/changelog/liquibase-changelog.yaml");
        registry.add("preliquibase.sql-script-references", () -> "classpath:db/init/init-database-test.sql");
    }

    @Test
    public void postWalletTest() {
        StepVerifier
                .create(walletController.postWallet())
                .assertNext(walletDto -> {
                    Assertions.assertTrue(Objects.nonNull(walletDto));
                })
                .verifyComplete();
    }

    @Test
    public void getWalletTest() {
        StepVerifier
                .create(
                        walletController
                                .postWallet()
                                .zipWhen(walletDto ->
                                        walletController.getWallet(walletDto.getWalletId()), (posted, fetched) -> {
                                    Assertions.assertEquals(posted.getWalletId(), fetched.getWalletId());
                                    return fetched;
                                }))
                .verifyComplete();
    }

    @Test
    public void postWalletOperationTest() {
        StepVerifier
                .create(
                        walletController
                                .postWallet()
                                .zipWhen(walletDto -> {
                                    WalletOperationDto walletOperationDto = WalletOperationDto
                                            .builder()
                                            .walletId(walletDto.getWalletId())
                                            .operationType(OperationType.DEPOSIT)
                                            .cost(new BigDecimal(1000))
                                            .build();
                                    return walletController.postWalletOperation(walletOperationDto);
                                }, (posted, fetched) -> {
                                    Assertions.assertEquals(posted.getWalletId(), fetched.getWalletId());
                                    Assertions.assertEquals(posted.getBalance(), new BigDecimal(1000));
                                    return fetched;
                                }))
                .verifyComplete();
    }
}

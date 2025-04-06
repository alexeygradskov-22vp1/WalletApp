create table wallet_app.wallets(
    walletId uuid primary key,
    balance decimal check ( balance>0 )
)
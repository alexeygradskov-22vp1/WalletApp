create schema if not exists wallet_app;

select * from pg_extension;
create extension pgcrypto;

create table if not exists wallet_app.wallets(
    wallet_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    balance decimal check ( balance>0 )
);


create table if not exists wallet_app.history_operations(
    operation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    operation_type varchar,
    wallet_id uuid references wallet_app.wallets(wallet_id) on delete cascade,
    cost decimal default 0
)
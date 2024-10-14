CREATE TABLE exchange_rates
(
    id                      BIGSERIAL PRIMARY KEY,
    currency_code           VARCHAR(5) NOT NULL,
    exchange_rate_to_usd    DECIMAL(22, 8),
    created_at              TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

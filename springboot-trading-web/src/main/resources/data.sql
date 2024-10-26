-- Insert into ContractDbo table
INSERT INTO contract_dbo (
    id,
    contract_id,
    symbol,
    security_type,
    currency,
    exchange,
    last_trade_date,
    strike,
    trade_right,
    multiplier,
    local_symbol,
    trading_class,
    include_expired,
    combo_legs_description
) VALUES (
             NEXT VALUE FOR contract_id_sequence,             -- id
             null,         -- contract_id
             'SPX',         -- symbol (must match enum name exactly)
             'IND',         -- securityType (assuming "IND" exists in Types.SecType enum)
             'USD',         -- currency
             'CBOE',        -- exchange
             NULL,          -- last_trade_date
             NULL,          -- strike
             NULL,          -- trade_right
             NULL,          -- multiplier
             NULL,          -- local_symbol
             NULL,          -- trading_class
             false,         -- include_expired
             NULL           -- combo_legs_description
         );

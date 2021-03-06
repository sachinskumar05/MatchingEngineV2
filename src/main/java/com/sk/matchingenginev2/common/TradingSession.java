package com.sk.matchingenginev2.common;

public enum TradingSession {
    START_OF_TRADING,
    OPENING_AUCTION_CALL,
    CONTINUOUS_TRADING,
    FCO_AUCTION_CALL,
    VOLATILITY_AUCTION_CALL,
    INTRADAY_AUCTION_CALL,
    CLOSING_AUCTION_CALL,
    CLOSING_PRICE_PUBLICATION,
    CLOSING_PRICE_CROSS,
    POST_CLOSE,
    TRADE_REPORTING;
}

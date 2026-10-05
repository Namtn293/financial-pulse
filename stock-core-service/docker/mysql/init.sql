-- DDL for Stock Core Service Database Schema (MySQL)

CREATE DATABASE IF NOT EXISTS stock_core_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stock_core_db;

CREATE TABLE IF NOT EXISTS watch_list
(
    id         BINARY(16)   NOT NULL COMMENT 'Unique identifier of the watchlist (UUID)',
    user_id    BIGINT       NOT NULL COMMENT 'User ID owning this watchlist',
    name       VARCHAR(255) NOT NULL COMMENT 'Display name of the watchlist',
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp',
    PRIMARY KEY (id),
    UNIQUE KEY uq_watchlist_user_name (user_id, name),
    KEY idx_watchlist_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS watch_list_item
(
    id           BINARY(16)  NOT NULL COMMENT 'Unique identifier of the watchlist item (UUID)',
    watchlist_id BINARY(16)  NOT NULL COMMENT 'Watchlist ID (UUID) referencing watch_list table',
    symbol       VARCHAR(20) NOT NULL COMMENT 'Stock symbol / ticker code (e.g. VNM, VIC, FPT)',
    note         TEXT        DEFAULT NULL COMMENT 'User note for this stock symbol',
    is_pinned    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT 'Flag indicating if the item is pinned (1) or not (0)',
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp',
    PRIMARY KEY (id),
    KEY idx_watchlist_item_watchlist_id (watchlist_id),
    KEY idx_watchlist_item_symbol (symbol),
    CONSTRAINT fk_watchlist_item_watchlist FOREIGN KEY (watchlist_id) REFERENCES watch_list (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS shedlock
(
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP(6) NOT NULL,
    locked_at  TIMESTAMP(6) NOT NULL,
    locked_by  VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS stock
(
    symbol             VARCHAR(20)   NOT NULL COMMENT 'Stock symbol / ticker code (Primary Key)',
    company_name       VARCHAR(255)  NOT NULL COMMENT 'Full company name',
    exchange           VARCHAR(10)   NOT NULL COMMENT 'Stock exchange (HOSE, HNX, UPCOM)',
    industry           VARCHAR(100)  DEFAULT NULL COMMENT 'Industry / Sector',
    ceo_name           VARCHAR(100)  DEFAULT NULL COMMENT 'CEO name',
    established_year   INT           DEFAULT NULL COMMENT 'Established year',
    website            VARCHAR(255)  DEFAULT NULL COMMENT 'Company website',
    overview_summary   TEXT          DEFAULT NULL COMMENT 'Company overview summary',
    shares_outstanding BIGINT        DEFAULT NULL COMMENT 'Shares outstanding',
    pe                 DECIMAL(10,2) DEFAULT NULL COMMENT 'Price to Earnings ratio',
    pb                 DECIMAL(10,2) DEFAULT NULL COMMENT 'Price to Book ratio',
    roe                DECIMAL(10,2) DEFAULT NULL COMMENT 'Return on Equity (%)',
    rsi_14             DECIMAL(10,2) DEFAULT NULL COMMENT 'Relative Strength Index 14',
    ma_20              DECIMAL(15,2) DEFAULT NULL COMMENT 'Moving Average 20',
    ma_50              DECIMAL(15,2) DEFAULT NULL COMMENT 'Moving Average 50',
    avg_volume_20d     BIGINT        DEFAULT NULL COMMENT 'Average 20-day trading volume',
    hash_data          VARCHAR(64)   DEFAULT NULL COMMENT 'MD5 hash for change detection',
    created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (symbol),
    KEY idx_stock_exchange (exchange),
    KEY idx_stock_industry (industry)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;



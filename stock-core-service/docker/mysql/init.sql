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


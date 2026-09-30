package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository;

import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchListItemsJpaRepository extends JpaRepository<WatchListItemsEntity, UUID> {
}

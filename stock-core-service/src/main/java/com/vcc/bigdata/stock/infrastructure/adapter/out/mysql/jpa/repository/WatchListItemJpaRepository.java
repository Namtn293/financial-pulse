package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository;

import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchListItemJpaRepository extends JpaRepository<WatchListItemEntity, UUID> {
}

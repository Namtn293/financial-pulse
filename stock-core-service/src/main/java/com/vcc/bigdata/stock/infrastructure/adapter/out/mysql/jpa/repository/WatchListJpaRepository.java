package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository;

import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchListJpaRepository extends JpaRepository<WatchListEntity, UUID> {

    boolean existsByUserIdAndName(Long userId, String name);

    Page<WatchListEntity> getWatchListEntitiesByUserId(Long userId, Pageable pageable);
}

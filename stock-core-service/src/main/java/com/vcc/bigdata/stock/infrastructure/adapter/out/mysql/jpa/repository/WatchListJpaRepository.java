package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository;

import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListJpaRepository extends JpaRepository<WatchListEntity, Long> {
}

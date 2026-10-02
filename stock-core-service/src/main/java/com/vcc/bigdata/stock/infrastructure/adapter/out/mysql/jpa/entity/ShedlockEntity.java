package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "shedlock")
@Setter
@Getter

public class ShedlockEntity {

    @Id
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "lock_until", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant lockUntil;

    @Column(name = "locked_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant lockedAt;

    @Column(name = "locked_by", nullable = false)
    private String lockedBy;
}

package com.dragonvvcr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "complaints")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "target_player_id", nullable = false)
    private String targetPlayerId;

    @Column(name = "dungeon_name", nullable = false)
    private String dungeonName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
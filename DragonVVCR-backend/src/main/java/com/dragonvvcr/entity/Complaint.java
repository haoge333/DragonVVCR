package com.dragonvvcr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("complaints")
public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    // 保留user对象用于关联查询
    @TableField(exist = false)
    private User user;

    @TableField("target_player_id")
    private String targetPlayerId;

    @TableField("target_guild")
    private String targetGuild;

    @TableField("dungeon_type")
    private String dungeonType;

    @TableField(exist = false)
    private String dungeonName;

    @TableField("description")
    private String description;

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
package com.dragonvvcr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragonvvcr.dto.ComplaintCountDTO;
import com.dragonvvcr.entity.Complaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ComplaintMapper extends BaseMapper<Complaint> {

    @Select("SELECT * FROM complaints WHERE user_id = #{userId}")
    List<Complaint> findByUserId(Long userId);

    @Select("SELECT * FROM complaints WHERE target_player_id = #{targetPlayerId}")
    List<Complaint> findByTargetPlayerId(String targetPlayerId);

    @Select("SELECT * FROM complaints WHERE dungeon_name = #{dungeonName}")
    List<Complaint> findByDungeonName(String dungeonName);

    @Select("SELECT * FROM complaints WHERE user_id = #{userId} AND target_player_id = #{targetPlayerId}")
    List<Complaint> findByUserIdAndTargetPlayerId(@Param("userId") Long userId, @Param("targetPlayerId") String targetPlayerId);

    @Select("SELECT target_player_id as name, COUNT(*) as count FROM complaints GROUP BY target_player_id ORDER BY count DESC")
    List<ComplaintCountDTO> findMostComplainedPlayers();

    @Select("SELECT dungeon_name as name, COUNT(*) as count FROM complaints GROUP BY dungeon_name ORDER BY count DESC")
    List<ComplaintCountDTO> findMostComplainedDungeons();
}
package com.dragonvvcr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonvvcr.dto.ComplaintCountDTO;
import com.dragonvvcr.entity.Complaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ComplaintMapper extends BaseMapper<Complaint> {

    @Select("SELECT c.*, d.dict_name as dungeon_name FROM complaints c " +
            "LEFT JOIN dictionary d ON c.dungeon_type = d.dict_code AND d.dict_type = 'sys_nest_type' " +
            "WHERE c.user_id = #{userId}")
    List<Complaint> findByUserId(Long userId);

    @Select("SELECT c.*, d.dict_name as dungeon_name FROM complaints c " +
            "LEFT JOIN dictionary d ON c.dungeon_type = d.dict_code AND d.dict_type = 'sys_nest_type' " +
            "WHERE c.target_player_id = #{targetPlayerId}")
    List<Complaint> findByTargetPlayerId(String targetPlayerId);

    @Select("SELECT c.*, d.dict_name as dungeon_name FROM complaints c " +
            "LEFT JOIN dictionary d ON c.dungeon_type = d.dict_code AND d.dict_type = 'sys_nest_type' " +
            "WHERE c.dungeon_type = #{dungeonType}")
    List<Complaint> findByDungeonType(String dungeonType);

    @Select("SELECT c.*, d.dict_name as dungeon_name FROM complaints c " +
            "LEFT JOIN dictionary d ON c.dungeon_type = d.dict_code AND d.dict_type = 'sys_nest_type' " +
            "WHERE c.user_id = #{userId} AND c.target_player_id = #{targetPlayerId}")
    List<Complaint> findByUserIdAndTargetPlayerId(@Param("userId") Long userId, @Param("targetPlayerId") String targetPlayerId);

    @Select("SELECT target_player_id as name, COUNT(*) as count FROM complaints GROUP BY target_player_id ORDER BY count DESC")
    List<ComplaintCountDTO> findMostComplainedPlayers();

    @Select("SELECT COALESCE(d.dict_name, c.dungeon_type) as name, COUNT(*) as count FROM complaints c " +
            "LEFT JOIN dictionary d ON c.dungeon_type = d.dict_code AND d.dict_type = 'sys_nest_type' " +
            "GROUP BY c.dungeon_type ORDER BY count DESC")
    List<ComplaintCountDTO> findMostComplainedDungeons();

    /**
     * 分页查询所有吐槽，按创建时间降序排列
     * @param page 分页参数
     * @return 分页结果
     */
    @Select("SELECT c.*, d.dict_name as dungeon_name FROM complaints c " +
            "LEFT JOIN dictionary d ON c.dungeon_type = d.dict_code AND d.dict_type = 'sys_nest_type' " +
            "ORDER BY c.created_time DESC")
    IPage<Complaint> findAllComplaints(Page<Complaint> page);
}
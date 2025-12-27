package com.dragonvvcr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonvvcr.dto.ComplaintCountDTO;
import com.dragonvvcr.entity.Complaint;
import com.dragonvvcr.mapper.ComplaintMapper;
import com.dragonvvcr.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ComplaintService {
    
    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String COMPLAINT_KEY_PREFIX = "complaint:";
    private static final String USER_COMPLAINTS_KEY_PREFIX = "user:complaints:";
    private static final String PLAYER_COMPLAINTS_KEY_PREFIX = "player:complaints:";
    private static final String DUNGEON_COMPLAINTS_KEY_PREFIX = "dungeon:complaints:";

    public Complaint getComplaintById(Long id) {
        // 先从 Redis 缓存中获取
        String key = COMPLAINT_KEY_PREFIX + id;
        Complaint complaint = redisUtil.get(key, Complaint.class);

        if (complaint == null) {
            // 缓存中没有，从数据库获取
            complaint = complaintMapper.selectById(id);
            if (complaint != null) {
                // 将数据存入缓存，设置过期时间为 1 小时
                redisUtil.set(key, complaint, 1, TimeUnit.HOURS);
            }
        }

        return complaint;
    }

    public List<Complaint> getComplaintsByUserId(Long userId) {
        String key = USER_COMPLAINTS_KEY_PREFIX + userId;
        List<Complaint> complaints = redisUtil.getList(key, Complaint.class);

        if (complaints == null) {
            complaints = complaintMapper.findByUserId(userId);
            redisUtil.set(key, complaints, 30, TimeUnit.MINUTES);
        }

        return complaints;
    }

    public List<Complaint> getComplaintsByTargetPlayerId(String targetPlayerId) {
        String key = PLAYER_COMPLAINTS_KEY_PREFIX + targetPlayerId;
        List<Complaint> complaints = redisUtil.getList(key, Complaint.class);

        if (complaints == null) {
            complaints = complaintMapper.findByTargetPlayerId(targetPlayerId);
            redisUtil.set(key, complaints, 30, TimeUnit.MINUTES);
        }

        return complaints;
    }

    public List<Complaint> getComplaintsByDungeonType(String dungeonType) {
        String key = DUNGEON_COMPLAINTS_KEY_PREFIX + dungeonType;
        List<Complaint> complaints = redisUtil.getList(key, Complaint.class);

        if (complaints == null) {
            complaints = complaintMapper.findByDungeonType(dungeonType);
            redisUtil.set(key, complaints, 30, TimeUnit.MINUTES);
        }

        return complaints;
    }

    public Complaint createComplaint(Complaint complaint) {
        logger.debug("创建吐槽: 目标玩家={}, 副本类型={}", complaint.getTargetPlayerId(), complaint.getDungeonType());
        
        // 确保设置userId字段
        if (complaint.getUser() != null && complaint.getUser().getId() != null) {
            complaint.setUserId(complaint.getUser().getId());
        }

        complaintMapper.insert(complaint);
        Complaint savedComplaint = complaintMapper.selectById(complaint.getId());

        // 将新吐槽存入缓存
        String key = COMPLAINT_KEY_PREFIX + savedComplaint.getId();
        redisUtil.set(key, savedComplaint, 1, TimeUnit.HOURS);
        logger.debug("新吐槽已存入缓存: ID={}", savedComplaint.getId());

        // 清除相关缓存
        clearRelatedCache(savedComplaint);
        logger.info("吐槽创建成功: ID={}, 目标玩家={}, 副本类型={}", 
            savedComplaint.getId(), savedComplaint.getTargetPlayerId(), savedComplaint.getDungeonType());

        return savedComplaint;
    }

    public void deleteComplaint(Long id) {
        logger.debug("删除吐槽: ID={}", id);
        
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint != null) {
            complaintMapper.deleteById(id);
            // 删除缓存
            String key = COMPLAINT_KEY_PREFIX + id;
            redisUtil.delete(key);
            logger.debug("吐槽缓存已删除: ID={}", id);

            // 清除相关缓存
            clearRelatedCache(complaint);
            logger.info("吐槽删除成功: ID={}", id);
        } else {
            logger.warn("删除吐槽失败: 吐槽不存在, ID={}", id);
        }
    }

    public List<ComplaintCountDTO> getMostComplainedPlayers() {
        String key = "stats:most_complained_players";
        List<ComplaintCountDTO> result = redisUtil.getList(key, ComplaintCountDTO.class);

        if (result == null) {
            result = complaintMapper.findMostComplainedPlayers();
            redisUtil.set(key, result, 1, TimeUnit.HOURS);
        }

        return result;
    }

    public List<ComplaintCountDTO> getMostComplainedDungeons() {
        String key = "stats:most_complained_dungeons";
        List<ComplaintCountDTO> result = redisUtil.getList(key, ComplaintCountDTO.class);

        if (result == null) {
            result = complaintMapper.findMostComplainedDungeons();
            redisUtil.set(key, result, 1, TimeUnit.HOURS);
        }

        return result;
    }

    private void clearRelatedCache(Complaint complaint) {
        logger.debug("清除相关缓存: 用户ID={}, 玩家={}, 副本类型={}", 
            complaint.getUserId(), complaint.getTargetPlayerId(), complaint.getDungeonType());
            
        // 清除用户吐槽列表缓存
        String userComplaintsKey = USER_COMPLAINTS_KEY_PREFIX + complaint.getUserId();
        redisUtil.delete(userComplaintsKey);
        logger.debug("已清除用户吐槽列表缓存: {}", userComplaintsKey);

        // 清除被吐槽玩家吐槽列表缓存
        String playerComplaintsKey = PLAYER_COMPLAINTS_KEY_PREFIX + complaint.getTargetPlayerId();
        redisUtil.delete(playerComplaintsKey);
        logger.debug("已清除玩家吐槽列表缓存: {}", playerComplaintsKey);

        // 清除副本吐槽列表缓存
        String dungeonComplaintsKey = DUNGEON_COMPLAINTS_KEY_PREFIX + complaint.getDungeonType();
        redisUtil.delete(dungeonComplaintsKey);
        logger.debug("已清除副本吐槽列表缓存: {}", dungeonComplaintsKey);

        // 清除统计数据缓存
        redisUtil.delete("stats:most_complained_players");
        redisUtil.delete("stats:most_complained_dungeons");
        logger.debug("已清除统计数据缓存");
    }

    /**
     * 分页查询所有吐槽，按创建时间降序排列
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public IPage<Complaint> findAllComplaints(int pageNum, int pageSize) {
        logger.debug("分页查询所有吐槽: 页码={}, 每页大小={}", pageNum, pageSize);

        Page<Complaint> page = new Page<>(pageNum, pageSize);
        IPage<Complaint> result = complaintMapper.findAllComplaints(page);

        logger.debug("分页查询完成: 总记录数={}, 当前页记录数={}", result.getTotal(), result.getRecords().size());

        return result;
    }
}
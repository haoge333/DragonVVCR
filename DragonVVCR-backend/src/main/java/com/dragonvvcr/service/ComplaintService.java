package com.dragonvvcr.service;

import com.dragonvvcr.entity.Complaint;
import com.dragonvvcr.repository.ComplaintRepository;
import com.dragonvvcr.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

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
            complaint = complaintRepository.findById(id).orElse(null);
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
            complaints = complaintRepository.findByUserId(userId);
            redisUtil.set(key, complaints, 30, TimeUnit.MINUTES);
        }

        return complaints;
    }

    public List<Complaint> getComplaintsByTargetPlayerId(String targetPlayerId) {
        String key = PLAYER_COMPLAINTS_KEY_PREFIX + targetPlayerId;
        List<Complaint> complaints = redisUtil.getList(key, Complaint.class);

        if (complaints == null) {
            complaints = complaintRepository.findByTargetPlayerId(targetPlayerId);
            redisUtil.set(key, complaints, 30, TimeUnit.MINUTES);
        }

        return complaints;
    }

    public List<Complaint> getComplaintsByDungeonName(String dungeonName) {
        String key = DUNGEON_COMPLAINTS_KEY_PREFIX + dungeonName;
        List<Complaint> complaints = redisUtil.getList(key, Complaint.class);

        if (complaints == null) {
            complaints = complaintRepository.findByDungeonName(dungeonName);
            redisUtil.set(key, complaints, 30, TimeUnit.MINUTES);
        }

        return complaints;
    }

    public Complaint createComplaint(Complaint complaint) {
        Complaint savedComplaint = complaintRepository.save(complaint);

        // 将新吐槽存入缓存
        String key = COMPLAINT_KEY_PREFIX + savedComplaint.getId();
        redisUtil.set(key, savedComplaint, 1, TimeUnit.HOURS);

        // 清除相关缓存
        clearRelatedCache(savedComplaint);

        return savedComplaint;
    }

    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id).orElse(null);
        if (complaint != null) {
            complaintRepository.deleteById(id);
            // 删除缓存
            String key = COMPLAINT_KEY_PREFIX + id;
            redisUtil.delete(key);

            // 清除相关缓存
            clearRelatedCache(complaint);
        }
    }

    public List<Object[]> getMostComplainedPlayers() {
        String key = "stats:most_complained_players";
        List<Object[]> result = redisUtil.getList(key, Object[].class);

        if (result == null) {
            result = complaintRepository.findMostComplainedPlayers();
            redisUtil.set(key, result, 1, TimeUnit.HOURS);
        }

        return result;
    }

    public List<Object[]> getMostComplainedDungeons() {
        String key = "stats:most_complained_dungeons";
        List<Object[]> result = redisUtil.getList(key, Object[].class);

        if (result == null) {
            result = complaintRepository.findMostComplainedDungeons();
            redisUtil.set(key, result, 1, TimeUnit.HOURS);
        }

        return result;
    }

    private void clearRelatedCache(Complaint complaint) {
        // 清除用户吐槽列表缓存
        String userComplaintsKey = USER_COMPLAINTS_KEY_PREFIX + complaint.getUser().getId();
        redisUtil.delete(userComplaintsKey);

        // 清除被吐槽玩家吐槽列表缓存
        String playerComplaintsKey = PLAYER_COMPLAINTS_KEY_PREFIX + complaint.getTargetPlayerId();
        redisUtil.delete(playerComplaintsKey);

        // 清除副本吐槽列表缓存
        String dungeonComplaintsKey = DUNGEON_COMPLAINTS_KEY_PREFIX + complaint.getDungeonName();
        redisUtil.delete(dungeonComplaintsKey);

        // 清除统计数据缓存
        redisUtil.delete("stats:most_complained_players");
        redisUtil.delete("stats:most_complained_dungeons");
    }
}
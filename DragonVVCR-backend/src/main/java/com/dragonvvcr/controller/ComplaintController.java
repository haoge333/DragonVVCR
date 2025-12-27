package com.dragonvvcr.controller;

import com.dragonvvcr.dto.ComplaintCountDTO;
import com.dragonvvcr.entity.Complaint;
import com.dragonvvcr.entity.User;
import com.dragonvvcr.service.ComplaintService;
import com.dragonvvcr.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
    
    private static final Logger logger = LoggerFactory.getLogger(ComplaintController.class);

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaint(@PathVariable Long id) {
        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint != null) {
            return ResponseEntity.ok(complaint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint) {
        logger.info("创建吐槽请求: 目标玩家={}, 副本名称={}", complaint.getTargetPlayerId(), complaint.getDungeonName());
        
        // 验证用户是否存在
        User user = userService.getUserById(complaint.getUser().getId());
        if (user == null) {
            logger.warn("创建吐槽失败: 用户不存在, ID={}", complaint.getUser().getId());
            return ResponseEntity.badRequest().build();
        }

        Complaint createdComplaint = complaintService.createComplaint(complaint);
        logger.info("吐槽创建成功: ID={}", createdComplaint.getId());
        return ResponseEntity.ok(createdComplaint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteComplaint(@PathVariable Long id) {
        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null) {
            return ResponseEntity.notFound().build();
        }

        complaintService.deleteComplaint(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Complaint deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Complaint>> getComplaintsByUser(
            @PathVariable Long userId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        logger.info("获取用户吐槽列表: 用户ID={}", userId);
        
        try {
            // 如果提供了token，验证它
            if (token != null && !token.isEmpty()) {
                User user = userService.getUserByToken(token);
                if (user == null) {
                    logger.warn("获取用户吐槽列表失败: token无效");
                    return ResponseEntity.status(401).build();
                }
            }

            // 验证用户是否存在
            if (userService.getUserById(userId) == null) {
                logger.warn("获取用户吐槽列表失败: 用户不存在, ID={}", userId);
                return ResponseEntity.badRequest().build();
            }

            List<Complaint> complaints = complaintService.getComplaintsByUserId(userId);
            logger.info("成功获取用户吐槽列表: 用户ID={}, 吐槽数量={}", userId, complaints.size());
            return ResponseEntity.ok(complaints);
        } catch (Exception e) {
            logger.error("获取用户吐槽列表异常: 用户ID={}, 错误: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/player/{targetPlayerId}")
    public ResponseEntity<List<Complaint>> getComplaintsByTargetPlayer(@PathVariable String targetPlayerId) {
        List<Complaint> complaints = complaintService.getComplaintsByTargetPlayerId(targetPlayerId);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/dungeon/{dungeonName}")
    public ResponseEntity<List<Complaint>> getComplaintsByDungeon(@PathVariable String dungeonName) {
        List<Complaint> complaints = complaintService.getComplaintsByDungeonName(dungeonName);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/stats/most-complained-players")
    public ResponseEntity<List<ComplaintCountDTO>> getMostComplainedPlayers() {
        List<ComplaintCountDTO> stats = complaintService.getMostComplainedPlayers();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/most-complained-dungeons")
    public ResponseEntity<List<ComplaintCountDTO>> getMostComplainedDungeons() {
        List<ComplaintCountDTO> stats = complaintService.getMostComplainedDungeons();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/list")
    public ResponseEntity<com.baomidou.mybatisplus.core.metadata.IPage<Complaint>> getAllComplaints(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("获取所有吐槽列表: 页码={}, 每页大小={}", pageNum, pageSize);

        try {
            com.baomidou.mybatisplus.core.metadata.IPage<Complaint> complaints = 
                complaintService.findAllComplaints(pageNum, pageSize);
            logger.info("成功获取所有吐槽列表: 总记录数={}, 当前页记录数={}", 
                complaints.getTotal(), complaints.getRecords().size());
            return ResponseEntity.ok(complaints);
        } catch (Exception e) {
            logger.error("获取所有吐槽列表异常: 错误: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
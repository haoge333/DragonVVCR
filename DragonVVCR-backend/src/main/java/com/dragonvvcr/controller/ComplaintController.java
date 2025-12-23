package com.dragonvvcr.controller;

import com.dragonvvcr.entity.Complaint;
import com.dragonvvcr.entity.User;
import com.dragonvvcr.service.ComplaintService;
import com.dragonvvcr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

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
        // 验证用户是否存在
        User user = userService.getUserById(complaint.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Complaint createdComplaint = complaintService.createComplaint(complaint);
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
        try {
            // 如果提供了token，验证它
            if (token != null && !token.isEmpty()) {
                User user = userService.getUserByToken(token);
                if (user == null) {
                    return ResponseEntity.status(401).build();
                }
            }

            // 验证用户是否存在
            if (userService.getUserById(userId) == null) {
                return ResponseEntity.badRequest().build();
            }

            List<Complaint> complaints = complaintService.getComplaintsByUserId(userId);
            return ResponseEntity.ok(complaints);
        } catch (Exception e) {
            e.printStackTrace();
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
    public ResponseEntity<List<Object[]>> getMostComplainedPlayers() {
        List<Object[]> stats = complaintService.getMostComplainedPlayers();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/most-complained-dungeons")
    public ResponseEntity<List<Object[]>> getMostComplainedDungeons() {
        List<Object[]> stats = complaintService.getMostComplainedDungeons();
        return ResponseEntity.ok(stats);
    }
}
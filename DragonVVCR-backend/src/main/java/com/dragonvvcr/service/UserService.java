package com.dragonvvcr.service;

import com.dragonvvcr.entity.User;
import com.dragonvvcr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USER_KEY_PREFIX = "user:";
    private static final String TOKEN_KEY_PREFIX = "token:";

    public User getUserById(Long id) {
        // 先从 Redis 缓存中获取
        String key = USER_KEY_PREFIX + id;
        User user = (User) redisTemplate.opsForValue().get(key);

        if (user == null) {
            // 缓存中没有，从数据库获取
            user = userRepository.findById(id).orElse(null);
            if (user != null) {
                // 将数据存入缓存，设置过期时间为 1 小时
                redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);
            }
        }

        return user;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        // 检查用户名是否已存在
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        // 密码加密
        user.setPassword(encryptPassword(user.getPassword()));

        User savedUser = userRepository.save(user);
        // 将新用户存入缓存
        String key = USER_KEY_PREFIX + savedUser.getId();
        redisTemplate.opsForValue().set(key, savedUser, 1, TimeUnit.HOURS);
        return savedUser;
    }

    public User updateUser(User user) {
        User updatedUser = userRepository.save(user);
        // 更新缓存
        String key = USER_KEY_PREFIX + updatedUser.getId();
        redisTemplate.opsForValue().set(key, updatedUser, 1, TimeUnit.HOURS);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        // 删除缓存
        String key = USER_KEY_PREFIX + id;
        redisTemplate.delete(key);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 验证密码
        String encryptedPassword = encryptPassword(password);
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 生成token
        String token = UUID.randomUUID().toString();

        // 将token存入Redis，设置过期时间为24小时
        redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + token, user.getId(), 24, TimeUnit.HOURS);

        return token;
    }

    public User getUserByToken(String token) {
        Object userIdObj = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + token);
        if (userIdObj == null) {
            return null;
        }

        // 处理可能的Integer到Long的转换
        Long userId;
        if (userIdObj instanceof Integer) {
            userId = ((Integer) userIdObj).longValue();
        } else if (userIdObj instanceof Long) {
            userId = (Long) userIdObj;
        } else {
            // 如果是其他类型，尝试转换
            userId = Long.parseLong(userIdObj.toString());
        }

        return getUserById(userId);
    }

    public void logout(String token) {
        redisTemplate.delete(TOKEN_KEY_PREFIX + token);
    }

    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
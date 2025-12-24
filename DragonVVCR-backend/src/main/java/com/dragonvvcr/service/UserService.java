package com.dragonvvcr.service;

import com.dragonvvcr.entity.User;
import com.dragonvvcr.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

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
            user = userMapper.selectById(id);
            if (user != null) {
                // 将数据存入缓存，设置过期时间为 1 小时
                redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);
            }
        }

        return user;
    }

    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User createUser(User user) {
        logger.info("尝试创建新用户: {}", user.getUsername());
        
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            logger.warn("创建用户失败: 用户名已存在 - {}", user.getUsername());
            throw new RuntimeException("Username already exists");
        }

        // 密码加密
        user.setPassword(encryptPassword(user.getPassword()));

        userMapper.insert(user);
        User savedUser = userMapper.selectById(user.getId());
        // 将新用户存入缓存
        String key = USER_KEY_PREFIX + savedUser.getId();
        redisTemplate.opsForValue().set(key, savedUser, 1, TimeUnit.HOURS);
        logger.info("用户创建成功: ID={}, 用户名={}", savedUser.getId(), savedUser.getUsername());
        return savedUser;
    }

    public User updateUser(User user) {
        int result = userMapper.updateById(user);
        User updatedUser = userMapper.selectById(user.getId());
        // 更新缓存
        String key = USER_KEY_PREFIX + updatedUser.getId();
        redisTemplate.opsForValue().set(key, updatedUser, 1, TimeUnit.HOURS);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        userMapper.deleteById(id);
        // 删除缓存
        String key = USER_KEY_PREFIX + id;
        redisTemplate.delete(key);
    }

    public String login(String username, String password) {
        logger.debug("尝试登录用户: {}", username);
        
        User user = userMapper.findByUsername(username);
        if (user == null) {
            logger.warn("登录失败: 用户不存在 - {}", username);
            throw new RuntimeException("User not found");
        }

        // 验证密码
        String encryptedPassword = encryptPassword(password);
        if (!encryptedPassword.equals(user.getPassword())) {
            logger.warn("登录失败: 密码错误 - {}", username);
            throw new RuntimeException("Invalid password");
        }

        // 生成token
        String token = UUID.randomUUID().toString();
        logger.debug("为用户 {} 生成token: {}", username, token);

        // 将token存入Redis，设置过期时间为24小时
        redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + token, user.getId(), 24, TimeUnit.HOURS);
        logger.info("用户 {} 登录成功，token已存入Redis", username);

        return token;
    }

    public User getUserByToken(String token) {
        logger.debug("根据token获取用户信息: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
        
        Object userIdObj = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + token);
        if (userIdObj == null) {
            logger.warn("token无效或已过期: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
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
        
        logger.debug("根据token找到用户ID: {}", userId);
        return getUserById(userId);
    }

    public void logout(String token) {
        logger.debug("用户登出，删除token: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
        redisTemplate.delete(TOKEN_KEY_PREFIX + token);
        logger.info("用户登出成功，token已删除");
    }

    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
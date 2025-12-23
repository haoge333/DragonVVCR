import api from './api';

export default {
  // 用户登录
  login(username, password) {
    return api.post('/users/login', {
      username,
      password
    });
  },

  // 用户注册
  register(userData) {
    return api.post('/users/register', userData);
  },

  // 获取用户信息
  getProfile() {
    return api.get('/users/profile');
  },

  // 用户退出
  logout() {
    return api.post('/users/logout');
  }
};
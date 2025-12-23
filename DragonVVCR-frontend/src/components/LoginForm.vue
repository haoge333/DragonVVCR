<template>
  <form @submit.prevent="handleSubmit">
    <div class="mb-3">
      <label for="loginUsername" class="form-label">游戏名</label>
      <input type="text" class="form-control" id="loginUsername" v-model="username" required>
    </div>
    <div class="mb-3">
      <label for="loginPassword" class="form-label">密码</label>
      <input type="password" class="form-control" id="loginPassword" v-model="password" required>
    </div>
    <button type="submit" class="btn btn-primary w-100">登录</button>
  </form>
</template>

<script>
import { ref } from 'vue';
import userService from '../services/userService';

export default {
  name: 'LoginForm',
  emits: ['login-success'],
  setup(props, { emit }) {
    const username = ref('');
    const password = ref('');

    const handleSubmit = async () => {
      try {
        const response = await userService.login(username.value, password.value);
        if (response.data.success) {
          emit('login-success', response.data);
        } else {
          alert(response.data.message || '登录失败');
        }
      } catch (error) {
        console.error('登录错误:', error);
        alert('登录失败，请检查网络连接');
      }
    };

    return {
      username,
      password,
      handleSubmit
    };
  }
};
</script>
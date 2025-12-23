<template>
  <form @submit.prevent="handleSubmit">
    <div class="mb-3">
      <label for="registerUsername" class="form-label">游戏名</label>
      <input type="text" class="form-control" id="registerUsername" v-model="username" required>
    </div>
    <div class="mb-3">
      <label for="registerPassword" class="form-label">密码</label>
      <input type="password" class="form-control" id="registerPassword" v-model="password" required>
    </div>
    <div class="mb-3">
      <label for="registerRegion" class="form-label">大区</label>
      <input type="text" class="form-control" id="registerRegion" v-model="region" required>
    </div>
    <div class="mb-3">
      <label for="registerServer" class="form-label">服务器</label>
      <input type="text" class="form-control" id="registerServer" v-model="server" required>
    </div>
    <div class="mb-3">
      <label for="registerGameId" class="form-label">游戏ID</label>
      <input type="text" class="form-control" id="registerGameId" v-model="gameId" required>
    </div>
    <button type="submit" class="btn btn-primary w-100">注册</button>
  </form>
</template>

<script>
import { ref } from 'vue';
import userService from '../services/userService';

export default {
  name: 'RegisterForm',
  emits: ['register-success'],
  setup(props, { emit }) {
    const username = ref('');
    const password = ref('');
    const region = ref('');
    const server = ref('');
    const gameId = ref('');

    const handleSubmit = async () => {
      try {
        const response = await userService.register({
          username: username.value,
          password: password.value,
          region: region.value,
          server: server.value,
          gameId: gameId.value
        });

        if (response.data.success) {
          emit('register-success');
        } else {
          alert(response.data.message || '注册失败');
        }
      } catch (error) {
        console.error('注册错误:', error);
        alert('注册失败，请检查网络连接');
      }
    };

    return {
      username,
      password,
      region,
      server,
      gameId,
      handleSubmit
    };
  }
};
</script>
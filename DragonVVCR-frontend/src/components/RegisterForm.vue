<template>
  <form @submit.prevent="handleSubmit" class="register-form">
    <div class="mb-3">
      <label for="registerUsername" class="form-label">用户名</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
        <input type="text" class="form-control" id="registerUsername" v-model="username" placeholder="请输入用户名" required>
      </div>
    </div>
    <div class="mb-3">
      <label for="registerPassword" class="form-label">密码</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
        <input type="password" class="form-control" id="registerPassword" v-model="password" placeholder="请输入密码" required>
      </div>
    </div>
    <div class="mb-3">
      <label for="registerRegion" class="form-label">大区</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-geo-alt-fill"></i></span>
        <select class="form-control" id="registerRegion" v-model="region" required>
          <option value="" disabled>请选择大区</option>
          <option v-for="item in regionOptions" :key="item.id" :value="item.dictName">{{ item.dictName }}</option>
        </select>
      </div>
    </div>
    <div class="mb-3">
      <label for="registerServer" class="form-label">服务器</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-hdd-network-fill"></i></span>
        <select class="form-control" id="registerServer" v-model="server" required>
          <option value="" disabled>请选择服务器</option>
          <option v-for="item in serverOptions" :key="item.id" :value="item.dictName">{{ item.dictName }}</option>
        </select>
      </div>
    </div>
    <div class="mb-4">
      <label for="registerGameId" class="form-label">游戏ID</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-controller"></i></span>
        <input type="text" class="form-control" id="registerGameId" v-model="gameId" placeholder="请输入游戏ID" required>
      </div>
    </div>
    <div class="d-grid gap-2">
      <button type="submit" class="btn btn-primary">注册</button>
    </div>
  </form>
</template>

<style scoped>
.register-form {
  max-width: 450px;
  margin: 0 auto;
  padding: 25px;
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.form-label {
  font-weight: 600;
  color: #495057;
  margin-bottom: 0.5rem;
}

.input-group {
  border-radius: 6px;
  overflow: hidden;
}

.input-group-text {
  background-color: #e9ecef;
  border: 1px solid #ced4da;
  border-right: none;
  color: #495057;
  width: 45px;
  display: flex;
  justify-content: center;
}

.form-control {
  border-radius: 0 6px 6px 0;
  border: 1px solid #ced4da;
  border-left: none;
  padding: 0.75rem;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

.form-control:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
  padding: 0.75rem;
  font-weight: 500;
  transition: background-color 0.2s, border-color 0.2s;
}

.btn-primary:hover {
  background-color: #0069d9;
  border-color: #0062cc;
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .register-form {
    max-width: 100%;
    padding: 20px 15px;
    border-radius: 8px;
  }

  .form-control {
    font-size: 16px; /* 防止iOS缩放 */
    padding: 0.6rem 0.75rem;
  }

  .btn {
    padding: 0.6rem 1rem;
    font-size: 1rem;
  }

  .input-group-text {
    width: 40px;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .register-form {
    max-width: 90%;
  }
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .register-form {
    padding: 30px;
  }

  .form-control {
    padding: 0.8rem 1rem;
  }

  .btn {
    padding: 0.8rem 1rem;
    font-size: 1.05rem;
  }
}
</style>

<script>
import { ref, onMounted } from 'vue';
import userService from '../services/userService';
import dictionaryService from '../services/dictionaryService';

export default {
  name: 'RegisterForm',
  emits: ['register-success'],
  setup(props, { emit }) {
    const username = ref('');
    const password = ref('');
    const region = ref('');
    const server = ref('');
    const gameId = ref('');
    const regionOptions = ref([]);
    const serverOptions = ref([]);

    // 获取大区选项
    const fetchRegionOptions = async () => {
      try {
        const response = await dictionaryService.getDictionaryByType('sys_daqu_type');
        console.log('大区数据响应:', response); // 添加日志调试
        // 直接使用返回的数组数据
        if (response.data) {
          regionOptions.value = response.data;
          console.log('大区选项:', regionOptions.value); // 添加日志调试
        } else {
          console.error('获取大区选项失败，响应格式不正确');
        }
      } catch (error) {
        console.error('获取大区选项失败:', error);
      }
    };

    // 获取服务器选项
    const fetchServerOptions = async () => {
      try {
        const response = await dictionaryService.getDictionaryByType('sys_server_type');
        console.log('服务器数据响应:', response); // 添加日志调试
        // 直接使用返回的数组数据
        if (response.data) {
          serverOptions.value = response.data;
          console.log('服务器选项:', serverOptions.value); // 添加日志调试
        } else {
          console.error('获取服务器选项失败，响应格式不正确');
        }
      } catch (error) {
        console.error('获取服务器选项失败:', error);
      }
    };

    // 组件挂载时获取选项
    onMounted(() => {
      fetchRegionOptions();
      fetchServerOptions();
    });

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
      regionOptions,
      serverOptions,
      handleSubmit
    };
  }
};
</script>
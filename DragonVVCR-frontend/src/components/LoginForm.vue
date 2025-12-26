<template>
  <form @submit.prevent="handleSubmit" class="login-form">
    <div class="mb-4">
      <label for="loginUsername" class="form-label">游戏名</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
        <input type="text" class="form-control" id="loginUsername" v-model="username" placeholder="请输入游戏名" required>
      </div>
    </div>
    <div class="mb-4">
      <label for="loginPassword" class="form-label">密码</label>
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
        <input type="password" class="form-control" id="loginPassword" v-model="password" placeholder="请输入密码" required>
      </div>
    </div>
    <div class="d-grid gap-2">
      <button type="submit" class="btn btn-primary">登录</button>
    </div>
  </form>
</template>

<style scoped>
.login-form {
  max-width: 400px;
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
  .login-form {
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
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .login-form {
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
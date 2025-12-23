<template>
  <div class="container">
    <h1 class="text-center mb-4">龙之谷菜鸡排行榜</h1>

    <!-- 登录/注册表单 -->
    <div v-if="!isLoggedIn" class="auth-section">
      <ul class="nav nav-tabs" id="authTab" role="tablist">
        <li class="nav-item" role="presentation">
          <button class="nav-link active" id="login-tab" data-bs-toggle="tab" data-bs-target="#login" type="button" role="tab" aria-controls="login" aria-selected="true">登录</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="register-tab" data-bs-toggle="tab" data-bs-target="#register" type="button" role="tab" aria-controls="register" aria-selected="false">注册</button>
        </li>
      </ul>
      <div class="tab-content" id="authTabContent">
        <div class="tab-pane fade show active" id="login" role="tabpanel" aria-labelledby="login-tab">
          <LoginForm @login-success="handleLoginSuccess" />
        </div>
        <div class="tab-pane fade" id="register" role="tabpanel" aria-labelledby="register-tab">
          <RegisterForm @register-success="handleRegisterSuccess" />
        </div>
      </div>
    </div>

    <!-- 吐槽系统主界面 -->
    <div v-else class="complaint-section">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>欢迎, {{ currentUser.username }}</h2>
        <button @click="handleLogout" class="btn btn-outline-secondary">退出登录</button>
      </div>

      <ul class="nav nav-tabs" id="mainTab" role="tablist">
        <li class="nav-item" role="presentation">
          <button class="nav-link active" id="complain-tab" data-bs-toggle="tab" data-bs-target="#complain" type="button" role="tab" aria-controls="complain" aria-selected="true">我要吐槽</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="myComplaints-tab" data-bs-toggle="tab" data-bs-target="#myComplaints" type="button" role="tab" aria-controls="myComplaints" aria-selected="false">我的吐槽</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="stats-tab" data-bs-toggle="tab" data-bs-target="#stats" type="button" role="tab" aria-controls="stats" aria-selected="false">吐槽统计</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="search-tab" data-bs-toggle="tab" data-bs-target="#search" type="button" role="tab" aria-controls="search" aria-selected="false">搜索吐槽</button>
        </li>
      </ul>

      <div class="tab-content" id="mainTabContent">
        <!-- 我要吐槽 -->
        <div class="tab-pane fade show active" id="complain" role="tabpanel" aria-labelledby="complain-tab">
          <ComplaintForm :user-id="currentUser.id" @submit-success="handleComplaintSubmit" />
        </div>

        <!-- 我的吐槽 -->
        <div class="tab-pane fade" id="myComplaints" role="tabpanel" aria-labelledby="myComplaints-tab">
          <MyComplaints :user-id="currentUser.id" />
        </div>

        <!-- 吐槽统计 -->
        <div class="tab-pane fade" id="stats" role="tabpanel" aria-labelledby="stats-tab">
          <ComplaintStats />
        </div>

        <!-- 搜索吐槽 -->
        <div class="tab-pane fade" id="search" role="tabpanel" aria-labelledby="search-tab">
          <ComplaintSearch />
        </div>
      </div>
    </div>

    <!-- Toast 通知 -->
    <div class="toast-container">
      <div id="toast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <strong class="me-auto">系统提示</strong>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">{{ toastMessage }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import userService from '../services/userService';
import LoginForm from '../components/LoginForm.vue';
import RegisterForm from '../components/RegisterForm.vue';
import ComplaintForm from '../components/ComplaintForm.vue';
import MyComplaints from '../components/MyComplaints.vue';
import ComplaintStats from '../components/ComplaintStats.vue';
import ComplaintSearch from '../components/ComplaintSearch.vue';
import * as bootstrap from 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

export default {
  name: 'Home',
  components: {
    LoginForm,
    RegisterForm,
    ComplaintForm,
    MyComplaints,
    ComplaintStats,
    ComplaintSearch
  },
  setup() {
    const isLoggedIn = ref(false);
    const currentUser = ref({});
    const toastMessage = ref('');

    // 检查本地存储中是否有token
    const checkAuth = async () => {
      const token = localStorage.getItem('authToken');
      if (token) {
        try {
          const response = await userService.getProfile();
          currentUser.value = response.data;
          isLoggedIn.value = true;
        } catch (error) {
          localStorage.removeItem('authToken');
        }
      }
    };

    // 显示提示消息
    const showToast = (message, type = 'info') => {
      toastMessage.value = message;

      const toastElement = document.getElementById('toast');
      const toastBody = toastElement.querySelector('.toast-body');

      // 设置消息内容
      toastBody.textContent = message;

      // 设置样式类
      toastElement.className = `toast bg-${type === 'success' ? 'success' : type === 'danger' ? 'danger' : type === 'warning' ? 'warning' : 'info'} text-white`;

      // 显示toast
      const toast = new bootstrap.Toast(toastElement);
      toast.show();
    };

    // 处理登录成功
    const handleLoginSuccess = (userData) => {
      localStorage.setItem('authToken', userData.token);
      currentUser.value = userData.user;
      isLoggedIn.value = true;
      showToast('登录成功', 'success');
    };

    // 处理注册成功
    const handleRegisterSuccess = () => {
      showToast('注册成功，请登录', 'success');
      // 切换到登录标签页
      document.getElementById('login-tab').click();
    };

    // 处理投诉提交成功
    const handleComplaintSubmit = () => {
      showToast('吐槽提交成功', 'success');
    };

    // 处理退出登录
    const handleLogout = async () => {
      try {
        await userService.logout();
        localStorage.removeItem('authToken');
        isLoggedIn.value = false;
        currentUser.value = {};
        showToast('退出登录成功', 'success');
      } catch (error) {
        showToast('退出登录失败', 'danger');
      }
    };

    onMounted(() => {
      checkAuth();
    });

    return {
      isLoggedIn,
      currentUser,
      toastMessage,
      handleLoginSuccess,
      handleRegisterSuccess,
      handleComplaintSubmit,
      handleLogout
    };
  }
};
</script>
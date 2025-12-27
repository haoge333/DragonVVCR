<template>
  <div class="container">
    <div class="header-section">
      <h1 class="text-center mb-4">龙之谷怀旧服菜鸡排行榜</h1>
    </div>

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
      <div class="user-header d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">欢迎, {{ currentUser.username }}</h2>
        <button @click="handleLogout" class="btn btn-outline-secondary btn-sm">退出登录</button>
      </div>

      <ul class="nav nav-tabs" id="mainTab" role="tablist">
        <li class="nav-item" role="presentation">
          <button class="nav-link active" id="complain-tab" data-bs-toggle="tab" data-bs-target="#complain" type="button" role="tab" aria-controls="complain" aria-selected="true">我要吐槽</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="complaintList-tab" data-bs-toggle="tab" data-bs-target="#complaintList" type="button" role="tab" aria-controls="complaintList" aria-selected="false">吐槽列表</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="stats-tab" data-bs-toggle="tab" data-bs-target="#stats" type="button" role="tab" aria-controls="stats" aria-selected="false">菜鸡统计</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="search-tab" data-bs-toggle="tab" data-bs-target="#search" type="button" role="tab" aria-controls="search" aria-selected="false">谁是菜鸡</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="myComplaints-tab" data-bs-toggle="tab" data-bs-target="#myComplaints" type="button" role="tab" aria-controls="myComplaints" aria-selected="false">我的吐槽</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="dictionaryType-tab" data-bs-toggle="tab" data-bs-target="#dictionaryType" type="button" role="tab" aria-controls="dictionaryType" aria-selected="false">
            <i class="bi bi-book me-1"></i>字典类型管理
          </button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="dictionary-tab" data-bs-toggle="tab" data-bs-target="#dictionary" type="button" role="tab" aria-controls="dictionary" aria-selected="false">
            <i class="bi bi-book-half me-1"></i>字典管理
          </button>
        </li>
      </ul>

      <div class="tab-content" id="mainTabContent">
        <!-- 我要吐槽 -->
        <div class="tab-pane fade show active" id="complain" role="tabpanel" aria-labelledby="complain-tab">
          <ComplaintForm :user-id="currentUser.id" @submit-success="handleComplaintSubmit" />
        </div>

        <!-- 吐槽列表 -->
        <div class="tab-pane fade" id="complaintList" role="tabpanel" aria-labelledby="complaintList-tab">
          <ComplaintList />
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

        <!-- 字典类型管理 -->
        <div class="tab-pane fade" id="dictionaryType" role="tabpanel" aria-labelledby="dictionaryType-tab">
          <DictionaryTypeManagement />
        </div>

        <!-- 字典管理 -->
        <div class="tab-pane fade" id="dictionary" role="tabpanel" aria-labelledby="dictionary-tab">
          <DictionaryManagement />
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

<style scoped>
.header-section {
  padding: 0 15px;
  margin-bottom: 20px;
}

.header-section h1 {
  font-weight: 700;
  color: #2c3e50;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.user-header {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid #007bff;
}

.user-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2c3e50;
}

.auth-section, .complaint-section {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .header-section {
    padding: 0 10px;
    margin-bottom: 15px;
  }

  .header-section h1 {
    font-size: 1.5rem;
    margin-bottom: 1rem;
  }

  .user-header {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 10px;
    padding: 12px;
  }

  .user-header h2 {
    font-size: 1.2rem;
  }

  .btn-sm {
    font-size: 0.8rem;
    padding: 0.25rem 0.5rem;
  }

  .nav-tabs {
    margin-bottom: 15px;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .header-section h1 {
    font-size: 1.8rem;
  }

  .user-header {
    padding: 18px;
  }

  .user-header h2 {
    font-size: 1.4rem;
  }
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .header-section {
    padding: 0 20px;
    margin-bottom: 25px;
  }

  .header-section h1 {
    font-size: 2.2rem;
  }

  .user-header {
    padding: 20px;
  }

  .user-header h2 {
    font-size: 1.6rem;
  }
}
</style>

<script>
import { ref, onMounted } from 'vue';
import userService from '../services/userService';
import LoginForm from '../components/LoginForm.vue';
import RegisterForm from '../components/RegisterForm.vue';
import ComplaintForm from '../components/ComplaintForm.vue';
import ComplaintList from '../components/ComplaintList.vue';
import MyComplaints from '../components/MyComplaints.vue';
import ComplaintStats from '../components/ComplaintStats.vue';
import ComplaintSearch from '../components/ComplaintSearch.vue';
import DictionaryTypeManagement from '../views/DictionaryTypeManagement.vue';
import DictionaryManagement from '../views/DictionaryManagement.vue';
import * as bootstrap from 'bootstrap';

export default {
  name: 'Home',
  components: {
    LoginForm,
    RegisterForm,
    ComplaintForm,
    ComplaintList,
    MyComplaints,
    ComplaintStats,
    ComplaintSearch,
    DictionaryTypeManagement,
    DictionaryManagement
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
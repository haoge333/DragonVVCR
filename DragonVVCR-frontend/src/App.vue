<template>
  <div class="app-container">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
body {
  background-color: #f8f9fa;
  padding: 0;
  margin: 0;
  background-image: url('/assets/images/main-background-image.jpg');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  opacity: 0.8;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 1;
}

.container {
  max-width: 1200px;
  margin: 20px auto;
  background-color: rgba(255, 255, 255, 0.85);
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
  width: 100%;
  backdrop-filter: blur(3px);
  transition: all 0.3s ease;
}

.complaint-card {
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 15px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.complaint-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.stats-card {
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 15px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.nav-tabs {
  margin-bottom: 20px;
  overflow-x: auto;
  white-space: nowrap;
  border-bottom: 2px solid #dee2e6;
}

.nav-tabs .nav-link {
  padding: 0.5rem 0.8rem;
  font-size: 0.9rem;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
  margin-right: 5px;
  border-radius: 5px 5px 0 0;
  background-color: rgba(255, 255, 255, 0.7);
  border: 1px solid #dee2e6;
  border-bottom: none;
}

.nav-tabs .nav-link:hover {
  border-bottom-color: rgba(0, 123, 255, 0.25);
  background-color: rgba(255, 255, 255, 0.85);
  border-color: #b8daff;
  border-bottom: none;
}

.nav-tabs .nav-link.active {
  border-bottom-color: #007bff;
  font-weight: 500;
  background-color: #e7f3ff;
  border-color: #007bff;
  border-bottom: none;
  color: #0056b3;
}

.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1050;
  max-width: 350px;
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .container {
    margin: 10px 5px;
    padding: 15px;
    border-radius: 8px;
  }

  .toast-container {
    left: 10px;
    right: 10px;
    max-width: none;
  }

  .nav-tabs .nav-link {
    padding: 0.4rem 0.6rem;
    font-size: 0.8rem;
  }

  .complaint-card, .stats-card {
    padding: 12px;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .container {
    margin: 15px auto;
    padding: 20px;
  }

  .nav-tabs .nav-link {
    padding: 0.5rem 0.7rem;
    font-size: 0.85rem;
  }
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .container {
    padding: 30px;
    margin: 30px auto;
  }

  .nav-tabs .nav-link {
    padding: 0.6rem 1rem;
    font-size: 1rem;
  }
}

/* 大屏幕设备优化 */
@media (min-width: 1200px) {
  .container {
    max-width: 1140px;
  }
}
</style>

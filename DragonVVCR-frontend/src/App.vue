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
}

.app-container {
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin-top: 20px;
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.complaint-card {
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 15px;
}

.stats-card {
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 15px;
}

.nav-tabs {
  margin-bottom: 20px;
  overflow-x: auto;
  white-space: nowrap;
}

.nav-tabs .nav-link {
  padding: 0.5rem 0.8rem;
  font-size: 0.9rem;
}

.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1050;
  max-width: 350px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .container {
    margin-top: 10px;
    padding: 15px;
    border-radius: 0;
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
}

@media (min-width: 992px) {
  .container {
    padding: 30px;
  }
}
</style>

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
}

.app-container {
  min-height: 100vh;
}

.container {
  max-width: 800px;
  margin-top: 50px;
  background-color: white;
  padding: 30px;
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
}

.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1050;
}
</style>

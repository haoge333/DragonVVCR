<template>
  <div class="complaint-search">
    <div class="search-form">
      <div class="mb-3">
        <label for="searchType" class="form-label">搜索类型</label>
        <select class="form-select" id="searchType" v-model="searchType">
          <option value="player">菜鸡ID</option>
          <option value="dungeon">副本名称</option>
        </select>
      </div>
      <div class="mb-3">
        <label for="searchKeyword" class="form-label">关键词</label>
        <input type="text" class="form-control" id="searchKeyword" v-model="keyword">
      </div>
      <div class="d-grid gap-2">
        <button @click="handleSearch" class="btn btn-primary">搜索</button>
      </div>
    </div>

    <div v-if="loading" class="text-center py-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">搜索中...</span>
      </div>
    </div>

    <div v-else>
      <div v-if="searchResults.length === 0 && hasSearched" class="text-center py-4">
        <p class="empty-message">没有找到相关吐槽</p>
      </div>

      <div v-else-if="searchResults.length > 0" class="search-results">
        <div v-for="complaint in searchResults" :key="complaint.id" class="complaint-card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0">菜鸡ID: {{ complaint.targetPlayerId }}</h5>
            <small class="text-muted">{{ formatDate(complaint.createdTime) }}</small>
          </div>
          <div class="card-body">
            <p class="mb-2"><strong>副本:</strong> {{ complaint.dungeonName }}</p>
            <p><strong>菜鸡行为:</strong> {{ complaint.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.complaint-search {
  max-width: 800px;
  margin: 0 auto;
}

.search-form {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.search-results {
  margin-top: 15px;
}

.complaint-card {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  margin-bottom: 20px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.card-header {
  padding: 15px;
  border-bottom: 1px solid #e9ecef;
  background-color: #f8f9fa;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.card-body {
  padding: 15px;
}

.empty-message {
  color: #6c757d;
  font-size: 1.1rem;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .complaint-search {
    max-width: 100%;
  }

  .search-form {
    padding: 15px;
  }

  .form-control {
    font-size: 16px; /* 防止iOS缩放 */
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 8px;
  }

  .card-header h5 {
    font-size: 1rem;
  }

  .card-body {
    padding: 12px;
  }

  .card-body p {
    font-size: 0.9rem;
    margin-bottom: 0.5rem;
  }
}
</style>

<script>
import { ref } from 'vue';
import complaintService from '../services/complaintService';

export default {
  name: 'ComplaintSearch',
  setup() {
    const searchType = ref('player');
    const keyword = ref('');
    const searchResults = ref([]);
    const loading = ref(false);
    const hasSearched = ref(false);

    const handleSearch = async () => {
      if (!keyword.value.trim()) {
        alert('请输入搜索关键词');
        return;
      }

      loading.value = true;
      hasSearched.value = true;

      try {
        let response;
        if (searchType.value === 'player') {
          response = await complaintService.getComplaintsByPlayer(keyword.value);
        } else {
          response = await complaintService.getComplaintsByDungeon(keyword.value);
        }

        searchResults.value = response.data;
      } catch (error) {
        console.error('搜索错误:', error);
        alert('搜索失败，请检查网络连接');
      } finally {
        loading.value = false;
      }
    };

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleString();
    };

    return {
      searchType,
      keyword,
      searchResults,
      loading,
      hasSearched,
      handleSearch,
      formatDate
    };
  }
};
</script>
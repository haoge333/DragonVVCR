<template>
  <div>
    <div class="mb-3">
      <label for="searchType" class="form-label">搜索类型</label>
      <select class="form-select" id="searchType" v-model="searchType">
        <option value="player">玩家</option>
        <option value="dungeon">副本</option>
      </select>
    </div>
    <div class="mb-3">
      <label for="searchKeyword" class="form-label">关键词</label>
      <input type="text" class="form-control" id="searchKeyword" v-model="keyword">
    </div>
    <button @click="handleSearch" class="btn btn-primary mb-3">搜索</button>

    <div v-if="loading" class="text-center">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">搜索中...</span>
      </div>
    </div>

    <div v-else>
      <div v-if="searchResults.length === 0 && hasSearched" class="text-center">
        <p>没有找到相关吐槽</p>
      </div>

      <div v-else-if="searchResults.length > 0">
        <div v-for="complaint in searchResults" :key="complaint.id" class="complaint-card">
          <div class="d-flex justify-content-between">
            <h5>菜鸡ID: {{ complaint.targetPlayerId }}</h5>
            <small>{{ formatDate(complaint.createdTime) }}</small>
          </div>
          <p class="mt-2"><strong>副本:</strong> {{ complaint.dungeonName }}</p>
          <p><strong>菜鸡行为:</strong> {{ complaint.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

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
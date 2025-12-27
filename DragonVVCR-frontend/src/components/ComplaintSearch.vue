<template>
  <div class="complaint-search">
    <div class="search-form">
      <div class="mb-3">
        <label for="searchType" class="form-label">搜索类型</label>
        <select class="form-select" id="searchType" v-model="searchType">
          <option value="player">菜鸡ID</option>
          <option value="dungeon">副本类型</option>
        </select>
      </div>
      <div class="mb-3">
        <label :for="searchType === 'player' ? 'playerName' : 'dungeonType'" class="form-label">
          {{ searchType === 'player' ? '菜鸡名称' : '副本类型' }}
        </label>
        <input v-if="searchType === 'player'" type="text" class="form-control" id="playerName" v-model="keyword">
        <select v-else class="form-select" id="dungeonType" v-model="keyword">
          <option v-for="item in dungeonTypes" :key="item.dictCode" :value="item.dictCode">
            {{ item.dictName }}
          </option>
        </select>
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
            <p class="mb-2"><strong>副本名称:</strong> {{ complaint.dungeonName }}</p>
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
  border-radius: 10px;
  margin-bottom: 25px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-results {
  margin-top: 20px;
}

.complaint-card {
  border: 1px solid #e9ecef;
  border-radius: 10px;
  margin-bottom: 20px;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s, box-shadow 0.2s;
  overflow: hidden;
}

.complaint-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.card-header {
  padding: 18px 20px;
  border-bottom: 1px solid #e9ecef;
  background-color: #f8f9fa;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h5 {
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.card-body {
  padding: 20px;
}

.card-body p {
  margin-bottom: 12px;
  line-height: 1.6;
}

.card-body strong {
  color: #495057;
  font-weight: 600;
}

.empty-message {
  color: #6c757d;
  font-size: 1.1rem;
  padding: 30px 0;
  text-align: center;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-top: 20px;
}

.spinner-border {
  width: 2rem;
  height: 2rem;
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .complaint-search {
    max-width: 100%;
  }

  .search-form {
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 15px;
  }

  .form-control {
    font-size: 16px; /* 防止iOS缩放 */
    padding: 0.6rem 0.75rem;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 10px;
    padding: 15px;
  }

  .card-header h5 {
    font-size: 1rem;
  }

  .card-body {
    padding: 15px;
  }

  .card-body p {
    font-size: 0.9rem;
    margin-bottom: 0.8rem;
  }

  .empty-message {
    padding: 20px 15px;
    font-size: 1rem;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .complaint-search {
    max-width: 90%;
  }

  .search-form {
    padding: 18px;
  }

  .card-header {
    padding: 16px 18px;
  }

  .card-body {
    padding: 18px;
  }
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .search-form {
    padding: 25px;
  }

  .card-header {
    padding: 20px 25px;
  }

  .card-body {
    padding: 25px;
  }
}
</style>

<script>
import { ref, onMounted, watch } from 'vue';
import complaintService from '../services/complaintService';
import dictionaryService from '../services/dictionaryService';

export default {
  name: 'ComplaintSearch',
  setup() {
    const searchType = ref('player');
    const keyword = ref('');
    const searchResults = ref([]);
    const loading = ref(false);
    const hasSearched = ref(false);
    const dungeonTypes = ref([]);

    // 获取副本类型字典
    const getDungeonTypes = async () => {
      try {
        const response = await dictionaryService.getDictionaryByType('sys_nest_type');
        if (response.data) {
          dungeonTypes.value = response.data;
          // 初始加载时不需要设置默认值，因为默认搜索类型是玩家ID
        }
      } catch (error) {
        console.error('获取副本类型失败:', error);
      }
    };

    const handleSearch = async () => {
      if (!keyword.value.trim()) {
        alert(searchType.value === 'player' ? '请输入菜鸡名称' : '请选择副本类型');
        return;
      }

      loading.value = true;
      hasSearched.value = true;

      try {
        let response;
        if (searchType.value === 'player') {
          response = await complaintService.getComplaintsByPlayer(keyword.value);
        } else {
          // 确保提交的是 dictCode 而不是 dictName
          console.log('搜索副本类型(dictCode):', keyword.value);
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

    // 监听搜索类型变化
    watch(searchType, (newType, oldType) => {
      if (newType === 'dungeon' && oldType === 'player' && dungeonTypes.value.length > 0) {
        // 从玩家ID切换到副本类型时，设置默认值为第一个副本类型
        keyword.value = dungeonTypes.value[0].dictCode;
      } else if (newType === 'player') {
        // 切换到玩家ID时，清空关键字
        keyword.value = '';
      }
    });

    // 组件挂载时获取字典数据
    onMounted(() => {
      getDungeonTypes();
    });

    return {
      searchType,
      keyword,
      searchResults,
      loading,
      hasSearched,
      dungeonTypes,
      handleSearch,
      formatDate
    };
  }
};
</script>
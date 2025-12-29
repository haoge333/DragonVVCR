<template>
  <div class="complaint-search">
    <div class="search-form">
      <div class="mb-3">
        <label for="searchType" class="form-label">搜索类型</label>
        <select class="form-select" id="searchType" v-model="searchType">
          <option value="player">菜鸡ID</option>
          <option value="guild">菜鸡工会</option>
          <option value="dungeon">副本类型</option>
        </select>
      </div>
      <div class="mb-3">
        <label :for="searchType === 'player' ? 'playerName' : (searchType === 'guild' ? 'guildName' : 'dungeonType')" class="form-label">
          {{ searchType === 'player' ? '菜鸡名称' : (searchType === 'guild' ? '工会名称' : '副本类型') }}
        </label>
        <div v-if="searchType === 'player'" class="input-wrapper">
          <input type="text" class="form-control" id="playerName" v-model="keyword">
        </div>
        <div v-else-if="searchType === 'guild'" class="dropdown-wrapper">
          <input 
            type="text" 
            class="form-control" 
            id="guildName" 
            v-model="keyword" 
            @focus="showGuildDropdown = true" 
            @input="filterGuilds" 
            placeholder="请选择或输入工会名称"
          >
          <div class="dropdown-menu" v-if="showGuildDropdown && filteredGuilds.length > 0">
            <div 
              v-for="guild in filteredGuilds" 
              :key="guild" 
              class="dropdown-item" 
              @click="selectGuild(guild)"
            >
              {{ guild }}
            </div>
          </div>
        </div>
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
            <div class="behavior-section">
              <h6 class="behavior-title">菜鸡行为:</h6>
              <div class="behavior-content" :class="{ 'expanded': isExpanded[complaint.id] }">
                {{ complaint.description }}
              </div>
              <button v-if="shouldShowExpandButton(complaint.description)" 
                      class="expand-btn" 
                      @click="toggleExpand(complaint.id)">
                {{ isExpanded[complaint.id] ? '收起' : '详情' }}
              </button>
            </div>
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
  padding: 12px 20px;
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

.behavior-section {
  margin-bottom: 15px;
}

.behavior-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.behavior-title::after {
  content: "";
  flex-grow: 1;
  height: 1px;
  background: #e9ecef;
  margin-left: 10px;
}

.behavior-content {
  background-color: #f8f9fa;
  border-left: 3px solid #007bff;
  padding: 12px 15px;
  border-radius: 0 6px 6px 0;
  font-size: 0.95rem;
  line-height: 1.6;
  word-wrap: break-word;
  word-break: break-word;
  white-space: pre-wrap;
  max-height: 100px;
  overflow: hidden;
  position: relative;
  transition: max-height 0.3s ease;
}

.behavior-content.expanded {
  max-height: 500px;
  overflow-y: auto;
}

.behavior-content::-webkit-scrollbar {
  width: 6px;
}

.behavior-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.behavior-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.behavior-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.expand-btn {
  background: none;
  border: none;
  color: #007bff;
  font-size: 0.85rem;
  padding: 5px 0;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  margin-top: 5px;
  transition: color 0.2s;
}

.expand-btn:hover {
  color: #0056b3;
  text-decoration: underline;
}

.expand-btn::before {
  content: "";
  display: inline-block;
  width: 0;
  height: 0;
  margin-right: 5px;
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 4px solid currentColor;
  transition: transform 0.2s;
}

.expand-btn:hover::before {
  transform: translateY(1px);
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
    padding: 10px;
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
    padding: 12px 18px;
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
    padding: 15px 25px;
  }

  .card-body {
    padding: 25px;
  }
}
</style>

<script>
import { ref, onMounted, onUnmounted, watch } from 'vue';
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
    const isExpanded = ref({});
    const guilds = ref([]);
    const filteredGuilds = ref([]);
    const showGuildDropdown = ref(false);

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
        alert(searchType.value === 'player' ? '请输入菜鸡名称' : (searchType.value === 'guild' ? '请输入工会名称' : '请选择副本类型'));
        return;
      }

      loading.value = true;
      hasSearched.value = true;

      try {
        let response;
        if (searchType.value === 'player') {
          response = await complaintService.getComplaintsByPlayer(keyword.value);
        } else if (searchType.value === 'guild') {
          response = await complaintService.getComplaintsByGuild(keyword.value);
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

    // 判断是否需要显示展开按钮
    const shouldShowExpandButton = (text) => {
      // 如果文本长度超过100个字符，则显示展开按钮
      return text && text.length > 100;
    };

    // 切换展开/收起状态
    const toggleExpand = (id) => {
      isExpanded.value[id] = !isExpanded.value[id];
    };

    // 获取所有工会
    const getAllGuilds = async () => {
      try {
        const response = await complaintService.getAllGuilds();
        guilds.value = response.data || [];
        filteredGuilds.value = guilds.value;
      } catch (error) {
        console.error('获取工会列表失败:', error);
      }
    };
    
    // 过滤工会列表
    const filterGuilds = () => {
      if (!keyword.value) {
        filteredGuilds.value = guilds.value;
      } else {
        filteredGuilds.value = guilds.value.filter(guild => 
          guild.toLowerCase().includes(keyword.value.toLowerCase())
        );
      }
    };
    
    // 选择工会
    const selectGuild = (guild) => {
      keyword.value = guild;
      showGuildDropdown.value = false;
    };
    
    // 点击外部关闭下拉框
    const handleClickOutside = (event) => {
      const dropdown = document.querySelector('.dropdown-wrapper');
      if (dropdown && !dropdown.contains(event.target)) {
        showGuildDropdown.value = false;
      }
    };
    
    // 监听搜索类型变化
    watch(searchType, (newType, oldType) => {
      if (newType === 'dungeon' && oldType === 'player' && dungeonTypes.value.length > 0) {
        // 从玩家ID切换到副本类型时，设置默认值为第一个副本类型
        keyword.value = dungeonTypes.value[0].dictCode;
      } else if (newType === 'player') {
        // 切换到玩家ID时，清空关键字
        keyword.value = '';
      } else if (newType === 'guild') {
        // 切换到工会时，清空关键字
        keyword.value = '';
      }
    });

    // 组件挂载时获取字典数据
    onMounted(() => {
      getDungeonTypes();
      getAllGuilds();
      document.addEventListener('click', handleClickOutside);
    });
    
    // 组件卸载时移除事件监听
    onUnmounted(() => {
      document.removeEventListener('click', handleClickOutside);
    });

    return {
      searchType,
      keyword,
      searchResults,
      loading,
      hasSearched,
      dungeonTypes,
      isExpanded,
      guilds,
      filteredGuilds,
      showGuildDropdown,
      filterGuilds,
      selectGuild,
      handleSearch,
      formatDate,
      shouldShowExpandButton,
      toggleExpand
    };
  }
};
</script>
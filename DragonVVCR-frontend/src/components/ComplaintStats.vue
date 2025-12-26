<template>
  <div class="complaint-stats">
    <div class="stats-container">
      <div class="stats-card">
        <h5 class="card-title">菜鸡排行榜</h5>
        <div v-if="playersLoading" class="text-center py-3">
          <div class="spinner-border spinner-border-sm" role="status">
            <span class="visually-hidden">加载中...</span>
          </div>
        </div>
        <div v-else>
          <div v-if="mostComplainedPlayers.length === 0" class="text-center py-3">
            <p class="empty-message">暂无数据</p>
          </div>
          <div v-else class="stats-list">
            <div v-for="(player, index) in mostComplainedPlayers" :key="index" class="stats-item">
              <span class="rank">{{ index + 1 }}</span>
              <span class="name">{{ player.name }}</span>
              <span class="count">{{ player.count }} 次吐槽</span>
            </div>
          </div>
        </div>
      </div>

      <div class="stats-card">
        <h5 class="card-title">菜鸡数量排行榜</h5>
        <div v-if="dungeonsLoading" class="text-center py-3">
          <div class="spinner-border spinner-border-sm" role="status">
            <span class="visually-hidden">加载中...</span>
          </div>
        </div>
        <div v-else>
          <div v-if="mostComplainedDungeons.length === 0" class="text-center py-3">
            <p class="empty-message">暂无数据</p>
          </div>
          <div v-else class="stats-list">
            <div v-for="(dungeon, index) in mostComplainedDungeons" :key="index" class="stats-item">
              <span class="rank">{{ index + 1 }}</span>
              <span class="name">{{ dungeon.name }}</span>
              <span class="count">{{ dungeon.count }} 次吐槽</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.complaint-stats {
  max-width: 900px;
  margin: 0 auto;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.stats-card {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 20px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  height: 100%;
}

.card-title {
  margin-bottom: 15px;
  color: #495057;
  font-weight: 600;
}

.stats-list {
  margin-top: 10px;
}

.stats-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f1f1f1;
}

.stats-item:last-child {
  border-bottom: none;
}

.rank {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 28px;
  height: 28px;
  background-color: #f8f9fa;
  border-radius: 50%;
  margin-right: 12px;
  font-weight: 600;
  font-size: 0.9rem;
}

.name {
  flex: 1;
  font-weight: 500;
}

.count {
  color: #6c757d;
  font-size: 0.9rem;
}

.empty-message {
  color: #6c757d;
  font-style: italic;
}

@media (max-width: 768px) {
  .complaint-stats {
    max-width: 100%;
  }

  .stats-container {
    grid-template-columns: 1fr;
  }

  .stats-card {
    padding: 15px;
  }

  .stats-item {
    padding: 8px 0;
  }

  .rank {
    width: 24px;
    height: 24px;
    font-size: 0.8rem;
    margin-right: 10px;
  }

  .name {
    font-size: 0.9rem;
  }

  .count {
    font-size: 0.8rem;
  }
}
</style>

<script>
import { ref, onMounted } from 'vue';
import complaintService from '../services/complaintService';

export default {
  name: 'ComplaintStats',
  setup() {
    const mostComplainedPlayers = ref([]);
    const mostComplainedDungeons = ref([]);
    const playersLoading = ref(false);
    const dungeonsLoading = ref(false);

    const loadMostComplainedPlayers = async () => {
      playersLoading.value = true;
      try {
        const response = await complaintService.getMostComplainedPlayers();
        mostComplainedPlayers.value = response.data;
      } catch (error) {
        console.error('加载玩家统计错误:', error);
        alert('加载统计数据失败，请检查网络连接');
      } finally {
        playersLoading.value = false;
      }
    };

    const loadMostComplainedDungeons = async () => {
      dungeonsLoading.value = true;
      try {
        const response = await complaintService.getMostComplainedDungeons();
        mostComplainedDungeons.value = response.data;
      } catch (error) {
        console.error('加载副本统计错误:', error);
        alert('加载统计数据失败，请检查网络连接');
      } finally {
        dungeonsLoading.value = false;
      }
    };

    const loadStats = () => {
      loadMostComplainedPlayers();
      loadMostComplainedDungeons();
    };

    // 监听标签页显示事件，加载数据
    onMounted(() => {
      const tab = document.getElementById('stats-tab');
      if (tab) {
        tab.addEventListener('shown.bs.tab', loadStats);
      }
    });

    return {
      mostComplainedPlayers,
      mostComplainedDungeons,
      playersLoading,
      dungeonsLoading
    };
  }
};
</script>
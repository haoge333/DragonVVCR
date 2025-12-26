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
  gap: 25px;
}

.stats-card {
  border: 1px solid #e9ecef;
  border-radius: 10px;
  padding: 25px;
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  height: 100%;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stats-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.card-title {
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 1.2rem;
  padding-bottom: 10px;
  border-bottom: 2px solid #f1f1f1;
}

.stats-list {
  margin-top: 15px;
}

.stats-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f1f1f1;
  transition: background-color 0.2s;
}

.stats-item:hover {
  background-color: rgba(0, 0, 0, 0.02);
  border-radius: 6px;
  padding-left: 8px;
  padding-right: 8px;
}

.stats-item:last-child {
  border-bottom: none;
}

.rank {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 32px;
  height: 32px;
  background-color: #f8f9fa;
  border-radius: 50%;
  margin-right: 15px;
  font-weight: 600;
  font-size: 0.95rem;
  color: #495057;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

/* 第一名 - 金色 */
.stats-item:nth-child(1) .rank {
  background: linear-gradient(135deg, #FFD700, #FFC107);
  color: #212529;
  box-shadow: 0 2px 6px rgba(255, 215, 0, 0.4);
}

/* 第二名 - 银色 */
.stats-item:nth-child(2) .rank {
  background: linear-gradient(135deg, #E5E5E5, #C0C0C0);
  color: #212529;
  box-shadow: 0 2px 6px rgba(192, 192, 192, 0.4);
}

/* 第三名 - 铜色 */
.stats-item:nth-child(3) .rank {
  background: linear-gradient(135deg, #CD7F32, #B87333);
  color: white;
  box-shadow: 0 2px 6px rgba(184, 115, 51, 0.4);
}

/* 其他排名 - 统一颜色 */
.stats-item:nth-child(n+4) .rank {
  background-color: #e7f3ff;
  color: #495057;
  border: 1px solid #b8daff;
}

.name {
  flex: 1;
  font-weight: 500;
  color: #343a40;
  font-size: 1rem;
}

.count {
  color: #6c757d;
  font-size: 0.9rem;
  background-color: #f8f9fa;
  padding: 4px 8px;
  border-radius: 4px;
}

.empty-message {
  color: #6c757d;
  font-style: italic;
  padding: 20px;
  text-align: center;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.spinner-border-sm {
  width: 1.2rem;
  height: 1.2rem;
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .complaint-stats {
    max-width: 100%;
  }

  .stats-container {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .stats-card {
    padding: 18px;
  }

  .card-title {
    font-size: 1.1rem;
    margin-bottom: 15px;
  }

  .stats-item {
    padding: 10px 0;
  }

  .rank {
    width: 28px;
    height: 28px;
    font-size: 0.85rem;
    margin-right: 12px;
  }

  .name {
    font-size: 0.95rem;
  }

  .count {
    font-size: 0.85rem;
    padding: 3px 6px;
  }

  .empty-message {
    padding: 15px;
    font-size: 0.95rem;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .stats-container {
    gap: 20px;
  }

  .stats-card {
    padding: 22px;
  }
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .stats-container {
    gap: 30px;
  }

  .stats-card {
    padding: 30px;
  }

  .card-title {
    font-size: 1.3rem;
  }

  .stats-item {
    padding: 14px 0;
  }

  .rank {
    width: 36px;
    height: 36px;
    font-size: 1rem;
    margin-right: 18px;
  }

  .name {
    font-size: 1.05rem;
  }

  .count {
    font-size: 0.95rem;
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
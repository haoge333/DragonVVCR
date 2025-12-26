<template>
  <div class="row">
    <div class="col-md-6">
      <div class="stats-card">
        <h5>菜鸡排行榜</h5>
        <div v-if="playersLoading" class="text-center">
          <div class="spinner-border spinner-border-sm" role="status">
            <span class="visually-hidden">加载中...</span>
          </div>
        </div>
        <div v-else>
          <div v-if="mostComplainedPlayers.length === 0" class="text-center">
            <p>暂无数据</p>
          </div>
          <div v-else>
            <div v-for="(player, index) in mostComplainedPlayers" :key="index" class="mb-2">
              {{ index + 1 }}. {{ player.name }} - {{ player.count }} 次吐槽
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="stats-card">
        <h5>副本菜鸡数量排行榜</h5>
        <div v-if="dungeonsLoading" class="text-center">
          <div class="spinner-border spinner-border-sm" role="status">
            <span class="visually-hidden">加载中...</span>
          </div>
        </div>
        <div v-else>
          <div v-if="mostComplainedDungeons.length === 0" class="text-center">
            <p>暂无数据</p>
          </div>
          <div v-else>
            <div v-for="(dungeon, index) in mostComplainedDungeons" :key="index" class="mb-2">
              {{ index + 1 }}. {{ dungeon.name }} - {{ dungeon.count }} 次吐槽
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

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
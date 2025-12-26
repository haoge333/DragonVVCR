<template>
  <div>
    <div v-if="loading" class="text-center">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">加载中...</span>
      </div>
    </div>

    <div v-else>
      <div v-if="complaints.length === 0" class="text-center">
        <p>您还没有发表任何吐槽</p>
      </div>

      <div v-else>
        <div v-for="complaint in complaints" :key="complaint.id" class="complaint-card">
          <div class="d-flex justify-content-between">
            <h5>菜鸡ID: {{ complaint.targetPlayerId }}</h5>
            <small>{{ formatDate(complaint.createdTime) }}</small>
          </div>
          <p class="mt-2"><strong>副本:</strong> {{ complaint.dungeonName }}</p>
          <p><strong>菜鸡行为:</strong> {{ complaint.description }}</p>
          <button class="btn btn-sm btn-outline-danger" @click="deleteComplaint(complaint.id)">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import complaintService from '../services/complaintService';

export default {
  name: 'MyComplaints',
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  setup(props) {
    const complaints = ref([]);
    const loading = ref(false);

    const loadComplaints = async () => {
      loading.value = true;
      try {
        const response = await complaintService.getUserComplaints(props.userId);
        complaints.value = response.data;
      } catch (error) {
        console.error('加载投诉错误:', error);
        alert('加载吐槽失败，请检查网络连接');
      } finally {
        loading.value = false;
      }
    };

    const deleteComplaint = async (id) => {
      if (!confirm('确定要删除这条吐槽吗？')) {
        return;
      }

      try {
        await complaintService.deleteComplaint(id);
        // 重新加载投诉列表
        loadComplaints();
        alert('删除成功');
      } catch (error) {
        console.error('删除投诉错误:', error);
        alert('删除失败，请检查网络连接');
      }
    };

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleString();
    };

    // 监听标签页显示事件，加载数据
    onMounted(() => {
      // 使用事件监听器，当标签页显示时加载数据
      const tab = document.getElementById('myComplaints-tab');
      if (tab) {
        tab.addEventListener('shown.bs.tab', loadComplaints);
      }
    });

    return {
      complaints,
      loading,
      deleteComplaint,
      formatDate
    };
  }
};
</script>
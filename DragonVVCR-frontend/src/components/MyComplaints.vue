<template>
  <div class="my-complaints">
    <div v-if="loading" class="text-center py-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">加载中...</span>
      </div>
    </div>

    <div v-else>
      <div v-if="complaints.length === 0" class="text-center py-4">
        <p class="empty-message">您还没有发表任何吐槽</p>
      </div>

      <div v-else class="complaints-list">
        <div v-for="complaint in complaints" :key="complaint.id" class="complaint-card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0">菜鸡ID: {{ complaint.targetPlayerId }}</h5>
            <small class="text-muted">{{ formatDate(complaint.createdTime) }}</small>
          </div>
          <div class="card-body">
            <p class="mb-2"><strong>副本:</strong> {{ complaint.dungeonName }}</p>
            <p class="mb-3"><strong>菜鸡行为:</strong> {{ complaint.description }}</p>
            <div class="text-end">
              <button class="btn btn-sm btn-outline-danger" @click="deleteComplaint(complaint.id)">删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.my-complaints {
  max-width: 800px;
  margin: 0 auto;
}

.complaints-list {
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
  .my-complaints {
    max-width: 100%;
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
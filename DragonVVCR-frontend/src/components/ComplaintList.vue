<template>
  <div class="complaint-list">
    <div v-if="loading" class="text-center py-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">加载中...</span>
      </div>
    </div>

    <div v-else>
      <div v-if="complaints.length === 0" class="text-center py-4">
        <p class="empty-message">暂无吐槽记录</p>
      </div>

      <div v-else class="complaints-list">
        <div v-for="complaint in complaints" :key="complaint.id" class="complaint-card">
          <div class="card-header">
            <div class="d-flex justify-content-between align-items-center mb-1">
              <div class="d-flex flex-wrap align-items-center">
                <span class="me-3"><strong>菜鸡ID:</strong> {{ complaint.targetPlayerId }}</span>
                <span><strong>副本:</strong> {{ complaint.dungeonName }}</span>
              </div>
              <small class="text-muted ms-3">{{ formatDate(complaint.createdTime) }}</small>
            </div>
          </div>
          <div class="card-body">
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
.complaint-list {
  max-width: 800px;
  margin: 0 auto;
}

.complaints-list {
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

.btn-outline-primary {
  border-color: #007bff;
  color: #007bff;
  transition: all 0.2s;
}

.btn-outline-primary:hover {
  background-color: #007bff;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 123, 255, 0.3);
}

.spinner-border {
  width: 2rem;
  height: 2rem;
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .complaint-list {
    max-width: 100%;
  }

  .complaints-list {
    margin-top: 15px;
  }

  .card-header {
    padding: 10px;
  }

  .card-header .d-flex {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 8px;
  }

  .card-header .d-flex > div {
    display: flex;
    flex-direction: column;
    gap: 5px;
  }

  .card-header .ms-3 {
    margin-left: 0 !important;
    margin-top: 5px;
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

  .btn-sm {
    padding: 0.4rem 0.8rem;
    font-size: 0.8rem;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .complaint-list {
    max-width: 90%;
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
  .card-header {
    padding: 15px 25px;
  }

  .card-body {
    padding: 25px;
  }
}
</style>

<script>
import { ref, onMounted } from 'vue';
import complaintService from '../services/complaintService';

export default {
  name: 'ComplaintList',
  setup() {
    const complaints = ref([]);
    const loading = ref(false);
    const isExpanded = ref({});

    const loadComplaints = async () => {
      loading.value = true;
      try {
        const response = await complaintService.getAllComplaints();
        complaints.value = response.data.records;
      } catch (error) {
        console.error('加载投诉错误:', error);
        alert('加载吐槽失败，请检查网络连接');
      } finally {
        loading.value = false;
      }
    };

    const viewDetails = (complaint) => {
      alert(`查看详情: ${complaint.targetPlayerId} - ${complaint.description}`);
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

    // 监听标签页显示事件，加载数据
    onMounted(() => {
      // 使用事件监听器，当标签页显示时加载数据
      const tab = document.getElementById('complaintList-tab');
      if (tab) {
        tab.addEventListener('shown.bs.tab', loadComplaints);
      }
    });

    return {
      complaints,
      loading,
      isExpanded,
      viewDetails,
      formatDate,
      shouldShowExpandButton,
      toggleExpand
    };
  }
};
</script>

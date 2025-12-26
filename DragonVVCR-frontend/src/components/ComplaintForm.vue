<template>
  <form @submit.prevent="handleSubmit" class="complaint-form">
    <div class="mb-3">
      <label for="targetPlayerId" class="form-label">菜鸡ID</label>
      <input type="text" class="form-control" id="targetPlayerId" v-model="targetPlayerId" required>
    </div>
    <div class="mb-3">
      <label for="dungeonName" class="form-label">副本名称</label>
      <input type="text" class="form-control" id="dungeonName" v-model="dungeonName" required>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label">菜鸡行为</label>
      <textarea class="form-control" id="description" rows="5" v-model="description" required></textarea>
    </div>
    <div class="d-grid gap-2">
      <button type="submit" class="btn btn-primary">提交吐槽</button>
    </div>
  </form>
</template>

<style scoped>
.complaint-form {
  max-width: 600px;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .complaint-form {
    max-width: 100%;
  }

  .form-control {
    font-size: 16px; /* 防止iOS缩放 */
  }

  textarea {
    min-height: 120px;
  }
}
</style>

<script>
import { ref } from 'vue';
import complaintService from '../services/complaintService';

export default {
  name: 'ComplaintForm',
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  emits: ['submit-success'],
  setup(props, { emit }) {
    const targetPlayerId = ref('');
    const dungeonName = ref('');
    const description = ref('');

    const handleSubmit = async () => {
      try {
        const response = await complaintService.createComplaint({
          user: {
            id: props.userId
          },
          targetPlayerId: targetPlayerId.value,
          dungeonName: dungeonName.value,
          description: description.value
        });

        if (response.data.id) {
          // 清空表单
          targetPlayerId.value = '';
          dungeonName.value = '';
          description.value = '';

          emit('submit-success');
        } else {
          alert('吐槽提交失败');
        }
      } catch (error) {
        console.error('提交投诉错误:', error);
        alert('吐槽提交失败，请检查网络连接');
      }
    };

    return {
      targetPlayerId,
      dungeonName,
      description,
      handleSubmit
    };
  }
};
</script>
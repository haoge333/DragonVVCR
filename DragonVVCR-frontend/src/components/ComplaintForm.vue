<template>
  <form @submit.prevent="handleSubmit">
    <div class="mb-3">
      <label for="targetPlayerId" class="form-label">被吐槽玩家游戏ID</label>
      <input type="text" class="form-control" id="targetPlayerId" v-model="targetPlayerId" required>
    </div>
    <div class="mb-3">
      <label for="dungeonName" class="form-label">副本名称</label>
      <input type="text" class="form-control" id="dungeonName" v-model="dungeonName" required>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label">吐槽内容</label>
      <textarea class="form-control" id="description" rows="5" v-model="description" required></textarea>
    </div>
    <button type="submit" class="btn btn-primary w-100">提交吐槽</button>
  </form>
</template>

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
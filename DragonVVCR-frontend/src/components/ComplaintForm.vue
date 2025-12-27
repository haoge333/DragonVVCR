<template>
  <form @submit.prevent="handleSubmit" class="complaint-form">
    <div class="mb-3">
      <label for="targetPlayerId" class="form-label">菜鸡ID</label>
      <input type="text" class="form-control" id="targetPlayerId" v-model="targetPlayerId" required>
    </div>
    <div class="mb-3">
      <label for="dungeonType" class="form-label">副本类型</label>
      <select class="form-select" id="dungeonType" v-model="dungeonType" required>
        <option v-for="item in dungeonTypes" :key="item.dictCode" :value="item.dictCode">
          {{ item.dictName }}
        </option>
      </select>
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
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.form-label {
  font-weight: 600;
  color: #495057;
  margin-bottom: 0.5rem;
}

.form-control {
  border-radius: 6px;
  border: 1px solid #ced4da;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

.form-control:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
  padding: 0.5rem 1rem;
  font-weight: 500;
  transition: background-color 0.2s, border-color 0.2s;
}

.btn-primary:hover {
  background-color: #0069d9;
  border-color: #0062cc;
}

/* 移动设备优化 */
@media (max-width: 576px) {
  .complaint-form {
    max-width: 100%;
    padding: 15px;
    border-radius: 8px;
  }

  .form-control {
    font-size: 16px; /* 防止iOS缩放 */
    padding: 0.6rem 0.75rem;
  }

  textarea {
    min-height: 120px;
  }

  .btn {
    padding: 0.6rem 1rem;
    font-size: 1rem;
  }
}

/* 平板设备优化 */
@media (min-width: 577px) and (max-width: 991px) {
  .complaint-form {
    max-width: 90%;
    padding: 18px;
  }
}

/* 桌面设备优化 */
@media (min-width: 992px) {
  .complaint-form {
    padding: 25px;
  }

  .form-control {
    padding: 0.75rem 1rem;
  }

  textarea {
    min-height: 150px;
  }
}
</style>

<script>
import { ref, onMounted } from 'vue';
import complaintService from '../services/complaintService';
import dictionaryService from '../services/dictionaryService';

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
    const dungeonType = ref('');
    const description = ref('');
    const dungeonTypes = ref([]);

    // 获取副本类型字典
    const getDungeonTypes = async () => {
      try {
        const response = await dictionaryService.getDictionaryByType('sys_nest_type');
        if (response.data) {
          dungeonTypes.value = response.data;
          // 设置默认选中第一个副本类型
          if (dungeonTypes.value.length > 0) {
            dungeonType.value = dungeonTypes.value[0].dictCode;
          }
        }
      } catch (error) {
        console.error('获取副本类型失败:', error);
      }
    };

    const handleSubmit = async () => {
      try {
        // 确保提交的是 dictCode 而不是 dictName
        console.log('提交的 dungeonType 值(dictCode):', dungeonType.value);
        const response = await complaintService.createComplaint({
          user: {
            id: props.userId
          },
          targetPlayerId: targetPlayerId.value,
          dungeonType: dungeonType.value,
          description: description.value
        });

        if (response.data.id) {
          // 清空表单
          targetPlayerId.value = '';
          dungeonType.value = '';
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

    // 组件挂载时获取字典数据
    onMounted(() => {
      getDungeonTypes();
    });

    return {
      targetPlayerId,
      dungeonType,
      description,
      dungeonTypes,
      handleSubmit
    };
  }
};
</script>
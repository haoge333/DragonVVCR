import api from './api';

export default {
  // 提交投诉
  createComplaint(complaintData) {
    return api.post('/complaints', complaintData);
  },

  // 获取用户的投诉列表
  getUserComplaints(userId) {
    return api.get(`/complaints/user/${userId}`);
  },

  // 删除投诉
  deleteComplaint(id) {
    return api.delete(`/complaints/${id}`);
  },

  // 根据玩家ID搜索投诉
  getComplaintsByPlayer(playerId) {
    return api.get(`/complaints/player/${playerId}`);
  },

  // 根据副本名称搜索投诉
  getComplaintsByDungeon(dungeonName) {
    return api.get(`/complaints/dungeon/${dungeonName}`);
  },

  // 获取最常被投诉的玩家
  getMostComplainedPlayers() {
    return api.get('/complaints/stats/most-complained-players');
  },

  // 获取最常被投诉的副本
  getMostComplainedDungeons() {
    return api.get('/complaints/stats/most-complained-dungeons');
  }
};
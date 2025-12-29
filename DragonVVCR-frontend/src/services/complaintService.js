import api from './api';

export default {
  // 获取所有投诉列表
  getAllComplaints() {
    return api.get('/complaints/list');
  },
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
  
  // 根据工会名称搜索投诉
  getComplaintsByGuild(guildName) {
    return api.get(`/complaints/guild/${guildName}`);
  },

  // 获取最常被投诉的玩家
  getMostComplainedPlayers() {
    return api.get('/complaints/stats/most-complained-players');
  },

  // 获取最常被投诉的副本
  getMostComplainedDungeons() {
    return api.get('/complaints/stats/most-complained-dungeons');
  },
  
  // 获取最常被投诉的工会
  getMostComplainedGuilds() {
    return api.get('/complaints/stats/most-complained-guilds');
  },
  
  // 获取所有工会列表
  getAllGuilds() {
    return api.get('/complaints/guilds');
  }
};
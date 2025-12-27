import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/dictionary-type',
    name: 'DictionaryTypeManagement',
    component: () => import('../views/DictionaryTypeManagement.vue')
  },
  {
    path: '/dictionary',
    name: 'DictionaryManagement',
    component: () => import('../views/DictionaryManagement.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
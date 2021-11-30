import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About'
import AdminEbook from '../views/admin/admin-ebook'
import AdminCategory from '../views/admin/category-ebook'
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About //这里的About是上面引入的
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

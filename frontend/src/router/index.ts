import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import AboutView from '../views/AboutView.vue';
import ResearcheView from '../views/ResearcheView.vue';
import ShopView from '../views/ShopView.vue';
import ProgrammeView from '../views/ProgrammeView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView 
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
  },
  {
    path: '/researche',
    name: 'researche',
    component: ResearcheView
  },
  {
    path: '/shop',
    name: 'shop',
    component: ShopView
  },
  {
    path: '/programme',
    name: 'programme',
    component: ProgrammeView
  },
  {
    path: '/:catchAll(.*)',  
    redirect: '/'
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;

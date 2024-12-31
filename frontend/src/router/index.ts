import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import AboutView from '../views/AboutView.vue';
import ResearcheView from '../views/ResearcheView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView // C'est la vue qui sera affichée par défaut
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
    path: '/:catchAll(.*)',  // Pour gérer les routes non trouvées
    redirect: '/' // Redirection vers la page d'accueil si aucune autre route n'est trouvée
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;

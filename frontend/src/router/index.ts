import { createRouter, createWebHistory } from 'vue-router';

import HomeView from '../views/HomeView.vue';
import AboutView from '../views/AboutView.vue';
import ResearcheView from '../views/ResearcheView.vue';
import ShopView from '../views/ShopView.vue';
import ProgrammeView from '../views/ProgrammeView.vue';
import ProgrammeDetails from "../views/Detail/ProgrammeDetail.vue";
import LanceurView from '../views/LanceurView.vue';
import ErgolView from '../views/ErgolView.vue';
import FuseeDetails from '../views/Detail/Fusee/FuseeDetail.vue';
import BoosterDetails from '../views/Detail/Fusee/LanceurDetail.vue';
import RessourceHumaines from '../views/RessourceHumaines/RessourcesHumaineView.vue';
import MarcheEmploie from '../views/RessourceHumaines/MarcheEmploieView.vue';
import Lancement from '../views/LancementView.vue';
import formulaireBoosterView from "../views/Formulaire/FormulaireBoosterView.vue";
import marketBatimentView from "../views/Batiments/BatimentMarketView.vue";


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
    path: '/marketBatiment',
    name: 'marketBatiment',
    component: marketBatimentView
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
    path: '/lanceur',
    name: 'lanceur',
    component: LanceurView
  },
  {
    path: '/ergol',
    name: 'ergol',
    component: ErgolView
  },
  {
    path: '/formulaireBooster',
    name: 'formulaireBooster',
    component: formulaireBoosterView
  },
  {
    path: '/programme',
    name: 'programme',
    component: ProgrammeView
  },
  {
    path: '/fusee-details',
    name: 'fuseeDetails',
    component: FuseeDetails
  },
  {
    path: '/booster-details',
    name: 'boosterDetails',
    component: BoosterDetails
  },
  {
    path: '/Ressource-Humaines',
    name: '/RessourceHumaines',
    component: RessourceHumaines
  },
  {
    path: "/programme-details",
    name: "ProgrammeDetails",
    component: ProgrammeDetails,
  },
  {
    path: "/MarcheEmploie",
    name: "MarcheEmploie",
    component: MarcheEmploie,
  },
  {
    path: "/lancement",
    name: "Lancement",
    component: Lancement,
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
import { createRouter, createWebHistory } from 'vue-router'

import store from '@/store'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/home/index.vue'),
    },
    {
      path: '/city',
      name: 'city',
      component: () => import('@/views/city/index.vue'),
    },
    {
      path: '/city-manage',
      name: 'city-manage',
      component: () => import('@/views/city-manage/index.vue'),
    },
    {
      path: '/my',
      name:'my',
      component: () => import('@/views/my/index.vue'),
    },
    {
      path: '/aboutme',
      name: 'aboutme',
      component: () => import('@/views/my/aboutme/index.vue')
    },
    {
      path: '/collection',
      name: 'collection',
      component: () => import('@/views/my/collection/index.vue')
    },
    {
      path: '/setting',
      name:'setting',
      component: () => import('@/views/my/setting/index.vue')
    },
    {

      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue'),
    },
    {
      path: '/register',
      name:'register',
      component: () => import('@/views/register/index.vue'),
    },
  ],
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = store.getters.token;

    if (!token) {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    } else {
      try {
        // 解析 token 以获取过期时间
        const decodedToken = parse(token);
        const currentTime = Date.now() / 1000;

        if (decodedToken.exp < currentTime) {
          // Token 过期，重定向到登录页面
          next({
            path: '/login',
            query: { redirect: to.fullPath }
          });
        } else {
          // Token 有效，继续
          next();
        }
      } catch (error) {
        // 解析 token 失败，重定向到登录页面
        next({
          path: '/login',
          query: { redirect: to.fullPath }
        });
      }
    }
  } else {
    next();
  }
});

export default router

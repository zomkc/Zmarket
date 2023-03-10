import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path: '/',
        redirect: '/index'
    },
    {
        path:'/index',
        name:'Index',
        component: ()=>import('../pages/Index.vue')
    },
]

const router = createRouter({
    history: createWebHashHistory(), // 路径格式
    routes: routes, // 路由数组
})

export default router
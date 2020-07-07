import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'home',
      component: () => import('@/views/home/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },

  {
    path: '/log',
    component: Layout,
    redirect: '/log/allLog',
    name: 'log',
    meta: { title: '访问日志', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'allLog',
        name: 'allLog',
        component: () => import('@/views/log/log/index'),
        meta: { title: '日志浏览', icon: 'documentation' }
      },
      {
        path: 'analyze',
        name: 'Tree',
        component: () => import('@/views/log/analyze/index'),
        meta: { title: '日志分析', icon: 'el-icon-s-marketing' }
      }
    ]
  },

  {
    path: '/article',
    component: Layout,
    redirect: '/article/query',
    name: 'Article',
    meta: {
      title: '博文管理',
      icon: 'nested'
    },
    children: [
      {
        path: 'query',
        component: () => import('@/views/article/query/index'), // Parent router-view
        name: 'Query',
        meta: { title: '历史文章', icon: 'el-icon-folder-opened' }
      },
      {
        path: 'add',
        component: () => import('@/views/article/add/index'),
        name: 'AddArticle',
        meta: { title: '新增博文', icon: 'edit' }
      },
      {
        path: 'category',
        component: () => import('@/views/article/category/index'), // Parent router-view
        name: 'Category',
        meta: { title: '分类管理', icon: 'component' }
      },
      {
        path: 'comment',
        component: () => import('@/views/article/comment/index'), // Parent router-view
        name: 'Comment',
        meta: { title: '评论管理', icon: 'el-icon-s-comment' }
      }
    ]
  },

  {
    path: '/friends',
    component: Layout,
    redirect: '/friends',
    children: [{
      path: 'friends',
      name: 'friends',
      component: () => import('@/views/friends/index'),
      meta: { title: '友链管理', icon: 'peoples' }
    }]
  },

  {
    path: '/information',
    component: Layout,
    redirect: '/information',
    children: [{
      path: 'information',
      name: 'information',
      component: () => import('@/views/information/index'),
      meta: { title: '个人信息', icon: 'user' }
    }]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://www.vergessen.top',
        meta: { title: '博客前台', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router

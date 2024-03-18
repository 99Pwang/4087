import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front'),
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('../views/front/Home.vue')
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import('../views/front/Password.vue')
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import('../views/front/Person.vue')
      },
      {path: 'person', name: 'FPerson', component: () => import('../views/front/Person.vue')},
      {path: 'detail', name: 'FDetail', component: () => import('../views/front/Detail.vue')},
      {path: 'collect', name: 'Collect', component: () => import('../views/front/Collect.vue')},
      {path: 'applys', name: 'Applys', component: () => import('../views/front/Applys.vue')},
      {path: 'paper', name: 'Paper', component: () => import('../views/front/Paper.vue')},

    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

// reset router
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    routes
  })
}


export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
        { path: 'person', name: 'Personal information', component: () => import('../views/Person.vue')},
        { path: 'password', name: 'Password change', component: () => import('../views/Password.vue')},
        { path: 'paperDetail', name: 'Resume details', component: () => import('../views/PaperDetail.vue')},
      ] }
    const menus = JSON.parse(storeMenus)
    menus.forEach(item => {
      if (item.path) {
        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
        manageRoute.children.push(itemMenu)
      } else if(item.children.length) {
        item.children.forEach(item => {
          if (item.path) {
            let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
            manageRoute.children.push(itemMenu)
          }
        })
      }
    })


    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      router.addRoute(manageRoute)
    }
  }
}

// reset the route when refreshing the page
setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // set the current route name
  store.commit("setPath")
  if (!to.matched.length) {
    const menus = localStorage.getItem("menus")
    if (!menus) {
      next("/login")
    } else {
      next("/404")
    }
  } else {
    next()
  }
})

export default router

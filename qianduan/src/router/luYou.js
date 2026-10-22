// 作者：肖沐樑 QQ：3387432690
import { createRouter, createWebHistory } from 'vue-router'
import DengLu from '@/views/DengLu.vue'
import ShouYe from '@/views/ShouYe.vue'
import ShangPinXiangQing from '@/views/ShangPinXiangQing.vue'
import FaBuShangPin from '@/views/FaBuShangPin.vue'
import WoDeDingDan from '@/views/WoDeDingDan.vue'
import GeRenZhongXin from '@/views/GeRenZhongXin.vue'
import GuanLiDengLu from '@/views/GuanLiDengLu.vue'
import GuanLi from '@/views/GuanLi.vue'

const luYou = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/shouye' },
    { path: '/denglu', component: DengLu },
    { path: '/shouye', component: ShouYe },
    { path: '/shangpin/:id', component: ShangPinXiangQing },
    { path: '/fabu', component: FaBuShangPin },
    { path: '/dingdan', component: WoDeDingDan },
    { path: '/geren', component: GeRenZhongXin },
    { path: '/admin/login', component: GuanLiDengLu },
    { path: '/admin', component: GuanLi }
  ]
})

luYou.beforeEach((to, from, next) => {
  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    if (!localStorage.getItem('guanLiYuan')) {
      next('/admin/login')
      return
    }
  }
  if (to.path === '/admin/login' && localStorage.getItem('guanLiYuan')) {
    next('/admin')
    return
  }
  next()
})

export default luYou

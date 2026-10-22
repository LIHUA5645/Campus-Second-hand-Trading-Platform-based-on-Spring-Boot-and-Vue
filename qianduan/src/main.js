// 作者：肖沐樑 QQ：3387432690
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import luYou from '@/router/luYou'
import '@/styles/quanJu.css'

const yingYong = createApp(App)
for (const [mingCheng, zuJian] of Object.entries(ElementPlusIconsVue)) {
  yingYong.component(mingCheng, zuJian)
}
yingYong.use(ElementPlus)
yingYong.use(luYou)
yingYong.mount('#app')

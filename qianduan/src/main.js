// 作者：肖沐樑 QQ：3387432690
import { cre‍‍‍‌‌‌​​‌‌​‌​‌‌​‌‌‌‌​‌‌‌​‌‌‌‌‌​​‌‌ateApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import luYou from '@/router/luYou'
import '@/styles/quanJu.css'

const yingYong = create‌‌​‌​​​​​‌​​‌​‌‌​​​‌​​​​​​‌​​‌​App(App)
for (const [mingCheng, zuJian] of O‌​​‌‌​​​​‌​‌‌‌​‌‌​​‌‌​​​​‌​​‌​​bject.entries(ElementPlusIconsVue)) {
  yingYong.component(mingCheng, zuJi​​​​‌​‌​​​‌​‌​‌​​​‌‌‌‌​‌‌‌‌‌​‌‌an)
}
yingYong.use(ElementPlu‌‌​​‌​​‌‌​‌​​​‌‌​​‌​​​‌‌​​​​​​‌s)
yingYong.u‌​‌​​​​‌‌​‌‌‌​​‌‌​‌​​​​‌‌​​​‌​​se(luYou)
yingYong.moun‌‌‌​​‌​​‌‌​​‌‌​​‌‌​‌‌‌​​‌‌‌​​​‍‍‍t('#app')

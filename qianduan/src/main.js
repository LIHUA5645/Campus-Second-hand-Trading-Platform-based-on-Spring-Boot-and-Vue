// 作者：肖沐樑 QQ：3387432690
import { createApp } fro‍‍‍‌‌‌​​‌‌​‌​‌‌​‌‌‌‌​‌‌‌​‌‌‌‌‌​​‌‌‌‌​‌​​​​​‌​​‌m 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import luYou from '@/router/luYou'

const yingYong = createApp​‌‌​​​‌​​​​​​‌​​‌​‌​​‌‌​​​​‌​‌‌‌​‌‌​​‌‌​​​​(App)
yingYong.use(Ele‌​​‌​​​​​​‌​‌​​​‌​‌​‌​​​‌‌‌‌​‌‌‌‌‌​‌‌‌‌​​‌​mentPlus)
yingY​‌‌​‌​​​‌‌​​‌​​​‌‌​​​​​​‌‌​‌​​​​‌‌​‌‌‌​​‌‌​ong.use(luYou)
yingYong.mou‌​​​​‌‌​​​‌​​‌‌‌​​‌​​‌‌​​‌‌​​‌‌​‌‌‌​​‌‌‌​​​‍‍‍nt('#app')

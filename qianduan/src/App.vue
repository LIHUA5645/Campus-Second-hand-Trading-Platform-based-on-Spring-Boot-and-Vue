<!-- 作者：肖沐樑 QQ：2047419378 -->
<template>
  <div id="app">
    <DingBu v-if="!shiHouTai" />
    <router-view />
    <DaShang v-if="!shiHouTai" />
    <AiRuKou />
    <footer v-if="!shiHouTai" class="yeJiao">
      <div class="rongQi">
        <p class="shouQuan">版权作者（软著）：肖沐樑 QQ：2047419378 ｜ 项目所有者：肖沐樑 QQ：2047419378</p>
        <p>技术交流 / 肖沐樑：QQ 2047419378 ｜ 商务合作：19877380122</p>
        <p>如果觉得不错，欢迎
          <a href="https://gitee.com/zhang-san-zhangshan/esjypt" target="_blank" rel="noopener">Gitee Star ⭐</a>
          ｜
          <a href="https://github.com/LIHUA5645/Campus-Second-hand-Trading-Platform-based-on-Spring-Boot-and-Vue" target="_blank" rel="noopener">GitHub Star ⭐</a>
          支持作者
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import DingBu from '@/components/DingBu.vue'
import DaShang from '@/components/DaShang.vue'
import AiRuKou from '@/components/AiRuKou.vue'

const luYouXin = useRoute()
// 后台（含管理员登录页）不显示前台顶栏与页脚
const shiHouTai = computed(() => luYouXin.path.startsWith('/admin') || luYouXin.path === '/denglu')
</script>

<style>
#app { min-height: 100vh; display: flex; flex-direction: column; }
.yeJiao { margin-top: 40px; padding: 28px 0; text-align: center; color: var(--wenZiSan); font-size: 13px; background: #fff; border-top: 1px solid var(--bianKuang); line-height: 2; }
.yeJiao a { color: var(--zhuSe); text-decoration: none; font-weight: 600; }
</style>

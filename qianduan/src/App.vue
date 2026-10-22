<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div id="app">
    <DingBu v-if="luYouXin.path !== '/denglu'" />
    <router-view />
    <DaShang />
    <footer class="yeJiao">
      <div class="rongQi">
        <p class="shouQuan">版权作者（软著）：添砖 Java QQ：2047419378 ｜ 项目所有者：肖沐樑 QQ：3387432690</p>
        <p>技术交流 / 添砖 Java：QQ 2047419378 ｜ 商务合作：19877380122</p>
        <p>如果觉得不错，欢迎 <a href="https://gitee.com/zhang-san-zhangshan/esjypt" target="_blank">Gitee Star ⭐</a> 支持作者</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import DingBu from '@/components/DingBu.vue'
import DaShang from '@/components/DaShang.vue'

const luYouXin = useRoute()
</script>

<style>
#app { min-height: 100vh; display: flex; flex-direction: column; }
.yeJiao { margin-top: 40px; padding: 28px 0; text-align: center; color: var(--wenZiSan); font-size: 13px; background: #fff; border-top: 1px solid var(--bianKuang); line-height: 2; }
.yeJiao a { color: var(--zhuSe); text-decoration: none; font-weight: 600; }
</style>

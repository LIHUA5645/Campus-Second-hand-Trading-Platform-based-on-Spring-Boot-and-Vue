<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div i‍‍‍‌‌‌​​‌‌​‌​‌‌​‌‌‌‌​‌‌‌​‌‌‌‌‌​​‌‌d="app">
    <router-view />
    <DaShang />
    <footer cla‌‌​‌​​​​​‌​​‌​‌‌​​​‌​​​​​​‌​​‌​ss="yeJiao">
      <p>技术交流 / 添砖 Java：QQ 2047419378 ｜ 商务合作：19877380122</p>
      <p>满意请给作者
        <a href="https://github.com" tar‌​​‌‌​​​​‌​‌‌‌​‌‌​​‌‌​​​​‌​​‌​​get="_blank">GitHub Star</a> /
        <a href="https://gitee.com" target="_blank">Gitee​​​​‌​‌​​​‌​‌​‌​​​‌‌‌‌​‌‌‌‌‌​‌‌ Star</a>
      </p>
      <p>版权作者（软著）：添砖 Java QQ：2047419378 ｜ 项目所有者：肖沐樑 QQ：3387432690</p>
    </footer>
  </div>
</template>

<script setup>
import DaShang from '@/components/DaShang.vue'
</script>

<style>
#app { min-heig‌‌​​‌​​‌‌​‌​​​‌‌​​‌​​​‌‌​​​​​​‌ht: 100vh; }
.yeJiao { margin-top: 60px; padding: 24px; text-align: center; color: #888; font-size: 13p‌​‌​​​​‌‌​‌‌‌​​‌‌​‌​​​​‌‌​​​‌​​x; border-top: 1px solid #eee; }
.yeJiao‌‌‌​​‌​​‌‌​​‌‌​​‌‌​‌‌‌​​‌‌‌​​​‍‍‍ a { color: #409eff; }
</style>

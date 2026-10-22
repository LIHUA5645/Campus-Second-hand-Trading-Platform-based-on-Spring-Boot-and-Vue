<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div class="daShang">
    <div class="xuanFu" @click="zhanShi = !zhanShi">打赏❤</div>
    <div v-if="zhanShi" class="tanChuang">
      <h3>欢迎打赏作者</h3>
      <p>技术交流 / 添砖 Java：QQ 2047419378</p>
      <p>商务合作：19877380122</p>
      <p>项目所有者：肖沐樑 QQ：3387432690</p>
      <p>满意请给
        <a href="https://github.com" target="_blank">GitHub Star</a> /
        <a href="https://gitee.com" target="_blank">Gitee Star</a>
      </p>
      <div class="shouKuan">
        <figure>
          <img src="/images/zhifu_weixin.jpg" alt="微信收款码" width="120" @click="openDaTu('/images/zhifu_weixin.jpg')" />
          <figcaption>微信</figcaption>
        </figure>
        <figure>
          <img src="/images/zhifu_zhifubao.jpg" alt="支付宝收款码" width="120" @click="openDaTu('/images/zhifu_zhifubao.jpg')" />
          <figcaption>支付宝</figcaption>
        </figure>
      </div>
      <p class="guanBi" @click="zhanShi = false">关闭</p>
    </div>
    <div v-if="daTu" class="dsMask" @click="daTu = ''">
      <img :src="daTu" class="dsImg" alt="收款码预览" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const zhanShi = ref(false)
const daTu = ref('')
const openDaTu = (src) => { daTu.value = src }
</script>

<style>
.xuanFu {
  position: fixed; right: 24px; bottom: 24px; z-index: 9999;
  background: #ff5b5b; color: #fff; padding: 12px 16px; border-radius: 24px;
  cursor: pointer; box-shadow: 0 4px 12px rgba(0,0,0,.2); font-weight: bold;
}
.tanChuang {
  position: fixed; right: 24px; bottom: 80px; z-index: 9999;
  background: #fff; border: 1px solid #eee; border-radius: 12px; padding: 16px;
  width: 280px; box-shadow: 0 6px 20px rgba(0,0,0,.15); text-align: center;
}
.tanChuang h3 { margin: 0 0 8px; }
.tanChuang p { margin: 6px 0; font-size: 13px; color: #555; }
.tanChuang a { color: #409eff; }
.shouKuan { display: flex; justify-content: center; gap: 16px; margin: 12px 0; }
.shouKuan figure { margin: 0; }
.shouKuan figcaption { font-size: 12px; color: #999; margin-top: 4px; }
.guanBi { color: #999; cursor: pointer; font-size: 12px; }
.shouKuan img { cursor: zoom-in; }
.dsMask { position: fixed; inset: 0; z-index: 10000; display: flex; align-items: center; justify-content: center; background: rgba(0, 0, 0, .72); cursor: zoom-out; }
.dsImg { max-width: 82vw; max-height: 82vh; border-radius: 12px; box-shadow: 0 12px 44px rgba(0, 0, 0, .5); }
</style>

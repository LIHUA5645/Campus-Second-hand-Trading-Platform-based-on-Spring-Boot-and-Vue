<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <header class="dingBu">
    <div class="rongQi neiBu">
      <div class="pinPai" @click="luYou.push('/shouye')">
        <img src="/logo.png" class="logo" alt="logo" />
        <span class="pinPaiMing">校园二手</span>
      </div>

      <div class="souSuo">
        <el-input v-model="guanJianCi" placeholder="搜索教材、数码、生活用品…" clearable @keyup.enter="souSuo">
          <template #prefix><el-icon><Search /></el-icon></template>
          <template #append><el-button @click="souSuo">搜索</el-button></template>
        </el-input>
      </div>

      <nav class="daoHang">
        <el-button text @click="luYou.push('/shouye')"><el-icon><HomeFilled /></el-icon> 首页</el-button>
        <el-button text class="aiRuKou" @click="aiDianJi">
          <img v-if="aiTouXiang" :src="aiTouXiang" alt="AI" class="aiTouXiang" />
          <el-icon v-else><MagicStick /></el-icon>
          AI 助手
        </el-button>
        <template v-if="yongHu">
          <el-button text @click="luYou.push('/fabu')"><el-icon><Plus /></el-icon> 发布</el-button>
          <el-button text @click="luYou.push('/dingdan')"><el-icon><List /></el-icon> 订单</el-button>
          <el-dropdown @command="chuLi">
            <span class="yongHu">
              <img v-if="yongHu.touXiang" :src="yongHu.touXiang" alt="头像" class="touXiangTu" />
              <el-avatar v-else :size="32" class="touXiang">{{ (yongHu.xingMing || yongHu.yongHuMing || '我').slice(0, 1) }}</el-avatar>
              <span class="niChen">{{ yongHu.xingMing || yongHu.yongHuMing }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="geren"><el-icon><User /></el-icon> 个人中心</el-dropdown-item>
                <el-dropdown-item command="fabu"><el-icon><Plus /></el-icon> 发布商品</el-dropdown-item>
                <el-dropdown-item command="dingdan"><el-icon><List /></el-icon> 我的订单</el-dropdown-item>
                <el-dropdown-item command="tuiChu" divided><el-icon><SwitchButton /></el-icon> 退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <el-button v-else type="primary" round @click="luYou.push('/denglu')">登录 / 注册</el-button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'

const luYou = useRouter()
const luYouXin = useRoute()
const yongHu = ref(null)
const guanJianCi = ref('')
const aiTouXiang = ref('')

const aiDianJi = () => {
  luYou.push('/ai')
}

const huoQu = () => {
  const y = localStorage.getItem('yongHu')
  yongHu.value = y ? JSON.parse(y) : null
}

const souSuo = () => {
  luYou.push({ path: '/shouye', query: { kw: guanJianCi.value || undefined } })
}

const chuLi = (mingLing) => {
  if (mingLing === 'tuiChu') {
    localStorage.removeItem('yongHu')
    yongHu.value = null
    luYou.push('/shouye')
  } else {
    luYou.push('/' + mingLing)
  }
}

onMounted(async () => {
  huoQu()
  guanJianCi.value = luYouXin.query.kw || ''
  window.addEventListener('storage', huoQu)
  try {
    const res = await jieKou.aiPeiZhi()
    if (res.data.chengGong && res.data.aiTouXiang) aiTouXiang.value = res.data.aiTouXiang
  } catch (e) { /* 头像获取失败仍显示图标 */ }
})
</script>

<style scoped>
.dingBu { position: sticky; top: 0; z-index: 100; background: rgba(255, 255, 255, .92); backdrop-filter: blur(12px); border-bottom: 1px solid var(--bianKuang); }
.neiBu { display: flex; align-items: center; gap: 24px; height: 64px; }
.pinPai { display: flex; align-items: center; gap: 10px; cursor: pointer; }
.logo { width: 34px; height: 34px; border-radius: 9px; }
.pinPaiMing { font-size: 19px; font-weight: 800; background: var(--zhuTiDu); -webkit-background-clip: text; background-clip: text; color: transparent; white-space: nowrap; }
.souSuo { flex: 1; max-width: 460px; }
.daoHang { display: flex; align-items: center; gap: 4px; }
.aiRuKou { color: var(--zhuSe) !important; font-weight: 600; }
.aiTouXiang {
  width: 24px;
  height: 24px;
  border-radius: 8px;
  object-fit: cover;
  display: block;
  box-shadow: 0 2px 6px rgba(47, 107, 255, .3);
}
.yongHu { display: flex; align-items: center; gap: 6px; cursor: pointer; outline: none; color: var(--wenZi); margin-left: 6px; }
.touXiang { background: var(--zhuTiDu); color: #fff; font-weight: 700; }
.touXiangTu {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  display: block;
  border: 2px solid #fff;
  box-shadow: 0 3px 8px rgba(47, 107, 255, .25);
}
.niChen { font-size: 14px; max-width: 90px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>

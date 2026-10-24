<template>
  <div class="adminDengLu">
    <div class="kaPian">
      <div class="biaoTi">管理员登录</div>
      <p class="fuBiaoTi">校园二手交易平台 · 后台</p>
      <el-form @submit.prevent>
        <el-form-item>
          <el-input v-model="yongHuMing" size="large" placeholder="管理员用户名">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="miMa" type="password" size="large" show-password placeholder="密码" @keyup.enter="dengLu">
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-button type="primary" size="large" class="dengLuAnNiu" :loading="dengLuZhong" @click="dengLu">登 录</el-button>
      </el-form>
      <p class="tiaoZhuan">普通用户？<span @click="luYou.push('/denglu')">前往前台登录</span></p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const yongHuMing = ref('')
const miMa = ref('')
const dengLuZhong = ref(false)

const dengLu = async () => {
  if (!yongHuMing.value || !miMa.value) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  dengLuZhong.value = true
  try {
    const res = await jieKou.adminDengLu(yongHuMing.value, miMa.value)
    if (res.data.chengGong) {
      localStorage.setItem('guanLiYuan', JSON.stringify(res.data.guanLiYuan))
      ElMessage.success('登录成功')
      luYou.push('/admin')
    } else {
      ElMessage.error(res.data.xinXi || '登录失败')
    }
  } finally {
    dengLuZhong.value = false
  }
}
</script>

<style scoped>
.adminDengLu {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-image:
    linear-gradient(120deg, rgba(15, 23, 42, .45), rgba(37, 99, 235, .32)),
    url('/images/houtai-denglu.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}
.kaPian {
  width: 380px;
  background: rgba(255, 255, 255, .94);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 22px;
  padding: 40px 34px 34px;
  box-shadow: 0 24px 70px rgba(0, 0, 0, .30);
  border: 1px solid rgba(255, 255, 255, .6);
  animation: houTaiRu .5s ease both;
}
@keyframes houTaiRu { from { opacity: 0; transform: translateY(18px); } to { opacity: 1; transform: translateY(0); } }
.biaoTi {
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
  text-align: center;
  letter-spacing: 1px;
  position: relative;
  padding-bottom: 14px;
}
.biaoTi::after {
  content: '';
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  width: 46px;
  height: 4px;
  border-radius: 4px;
  background: linear-gradient(90deg, #2563eb, #15c5b8);
}
.fuBiaoTi { text-align: center; color: #64748b; font-size: 13.5px; margin: 16px 0 26px; }
.kaPian :deep(.el-input__wrapper) {
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
  transition: box-shadow .2s ease;
}
.kaPian :deep(.el-input__wrapper:hover) { box-shadow: 0 0 0 1px #cbd5e1 inset; }
.kaPian :deep(.el-input__wrapper.is-focus) { box-shadow: 0 0 0 2px #2563eb inset; }
.kaPian :deep(.el-input__inner) { height: 42px; }
.dengLuAnNiu {
  width: 100%;
  margin-top: 6px;
  height: 46px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 2px;
  background: linear-gradient(90deg, #2563eb, #15c5b8);
  color: #fff;
  transition: filter .2s ease, transform .12s ease;
}
.dengLuAnNiu:hover { filter: brightness(1.06); }
.dengLuAnNiu:active { transform: scale(.99); }
.tiaoZhuan { text-align: center; font-size: 12.5px; color: #94a3b8; margin: 18px 0 0; }
.tiaoZhuan span { color: #2563eb; cursor: pointer; }
</style>

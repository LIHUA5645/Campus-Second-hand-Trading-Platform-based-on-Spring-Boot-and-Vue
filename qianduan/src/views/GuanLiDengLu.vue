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
  background: linear-gradient(120deg, #2f6bff 0%, #2b8bf0 48%, #15c5b8 100%);
  padding: 20px;
}
.kaPian {
  width: 360px;
  background: #fff;
  border-radius: 20px;
  padding: 34px 30px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, .18);
}
.biaoTi { font-size: 24px; font-weight: 800; color: #1f2733; text-align: center; }
.fuBiaoTi { text-align: center; color: #98a2b3; font-size: 13px; margin: 6px 0 24px; }
.dengLuAnNiu { width: 100%; margin-top: 4px; }
.tiaoZhuan { text-align: center; font-size: 12.5px; color: #98a2b3; margin: 16px 0 0; }
.tiaoZhuan span { color: #2f6bff; cursor: pointer; }
</style>

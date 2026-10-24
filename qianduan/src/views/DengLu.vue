<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div class="dengLu">
    <!-- 左侧品牌面板 -->
    <div class="zuo">
      <span class="wangGe"></span>

      <!-- 会看鼠标的笑脸 -->
      <div class="lian" ref="lianEl" :class="{ blink: blinkV, saJiao: saJiaoV }">
        <span class="hong hongL"></span>
        <span class="hong hongR"></span>
        <span class="yan yanL"><i class="tong" :style="tongStyle"></i></span>
        <span class="yan yanR"><i class="tong" :style="tongStyle"></i></span>
        <span class="zui"></span>
      </div>

      <div class="zuoNei">
        <div class="logoBox">
          <img src="/logo.png" class="logo" alt="logo" />
        </div>
        <h1>校园二手交易平台</h1>
        <p class="miaoShu">让闲置流动，让好物新生。<br />教材、数码、生活用品，一键发布、极速交易。</p>

        <ul class="teDian">
          <li>
            <span class="ico"><el-icon><CircleCheck /></el-icon></span>
            <div>
              <b>注册即发布</b>
              <small>闲置随手挂，几秒完成上架</small>
            </div>
          </li>
          <li>
            <span class="ico"><el-icon><Search /></el-icon></span>
            <div>
              <b>分类好检索</b>
              <small>教材 / 数码 / 生活，一搜即达</small>
            </div>
          </li>
          <li>
            <span class="ico"><el-icon><Van /></el-icon></span>
            <div>
              <b>交易可追踪</b>
              <small>下单到收货，状态全程可见</small>
            </div>
          </li>
        </ul>

        <div class="shuJu">
          <div><strong>{{ shu3 }}</strong><span>大类目</span></div>
          <div class="line"></div>
          <div><strong>秒级</strong><span>发布上架</span></div>
          <div class="line"></div>
          <div><strong>全程</strong><span>订单追踪</span></div>
        </div>
      </div>
    </div>

    <!-- 右侧表单（扁平卡片） -->
    <div class="you">
      <div class="kaPian">
        <div class="kaTou">
          <h2>欢迎回来</h2>
          <p>登录你的校园二手账号，继续淘好物</p>
        </div>

        <el-tabs v-model="huoYue" class="shouQian">
          <el-tab-pane label="登录" name="denglu">
            <el-form :model="dengLuForm" label-position="top" class="biaoDan">
              <el-form-item label="用户名">
                <el-input v-model="dengLuForm.yongHuMing" size="large" placeholder="请输入用户名">
                  <template #prefix><el-icon><User /></el-icon></template>
                </el-input>
              </el-form-item>
              <el-form-item label="密码">
                <el-input v-model="dengLuForm.miMa" type="password" size="large" show-password placeholder="请输入密码">
                  <template #prefix><el-icon><Lock /></el-icon></template>
                </el-input>
              </el-form-item>
              <div class="wangJiHang"><span class="lk" @click="daKaiZhaoHui">忘记密码？</span></div>
              <el-button type="primary" size="large" class="tiJiao" @click="dengLu">登 录</el-button>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="注册" name="zhuce">
            <el-form :model="zhuceForm" label-position="top" class="biaoDan">
              <el-form-item label="用户名">
                <el-input v-model="zhuceForm.yongHuMing" size="large" placeholder="用于登录的账号">
                  <template #prefix><el-icon><User /></el-icon></template>
                </el-input>
              </el-form-item>
              <el-form-item label="密码">
                <el-input v-model="zhuceForm.miMa" type="password" size="large" show-password placeholder="请设置密码">
                  <template #prefix><el-icon><Lock /></el-icon></template>
                </el-input>
              </el-form-item>
              <el-form-item label="昵称">
                <el-input v-model="zhuceForm.xingMing" size="large" placeholder="展示名称">
                  <template #prefix><el-icon><Avatar /></el-icon></template>
                </el-input>
              </el-form-item>
              <el-form-item label="手机">
                <el-input v-model="zhuceForm.shouJi" size="large" placeholder="联系电话">
                  <template #prefix><el-icon><Iphone /></el-icon></template>
                </el-input>
              </el-form-item>
              <el-form-item label="学号">
                <el-input v-model="zhuceForm.xueHao" size="large" placeholder="校园学号">
                  <template #prefix><el-icon><Reading /></el-icon></template>
                </el-input>
              </el-form-item>
              <el-button type="success" size="large" class="tiJiao" @click="zhuce">注 册</el-button>
            </el-form>
          </el-tab-pane>
        </el-tabs>

        <p class="xieYi">登录即代表同意
          <span class="lk" @click="openXy('yh')">《用户协议》</span> 与
          <span class="lk" @click="openXy('ys')">《隐私政策》</span>
        </p>
      </div>
    </div>

    <!-- 找回密码 -->
    <el-dialog v-model="zhaoHuiXian" title="找回密码" width="420px" align-center class="zhaoHuiDuiHua">
      <p class="zhaoHuiTiShi">输入注册时的用户名、手机号和学号，校验通过后即可设置新密码。</p>
      <el-form label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="zhaoHui.yongHuMing" size="large" placeholder="请输入用户名">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="zhaoHui.shouJi" size="large" placeholder="注册时填写的手机号">
            <template #prefix><el-icon><Iphone /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="zhaoHui.xueHao" size="large" placeholder="注册时填写的学号">
            <template #prefix><el-icon><Reading /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="zhaoHui.xinMiMa" type="password" size="large" show-password placeholder="设置新的登录密码">
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="zhaoHuiXian = false">取消</el-button>
        <el-button type="primary" @click="tiJiaoZhaoHui">确认重置</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="xyShow" :title="xyTitle" width="560px" append-to-body class="xyDialog">
      <div class="xyBody">
        <p v-for="(p, i) in xyContent" :key="i" v-html="p"></p>
      </div>
      <template #footer>
        <el-button class="shangBtn" @click="xyShow = false; shangShow = true">♥ 打赏作者</el-button>
        <el-button @click="xyShow = false">关闭</el-button>
        <el-button type="primary" @click="xyShow = false">我已阅读并同意</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="shangShow" title="打赏作者" width="420px" append-to-body class="shangDialog">
      <div class="shangBody">
        <div class="shangIcon">♥</div>
        <p>如果这个项目帮到了你，欢迎打赏支持开发者持续维护与迭代～</p>
        <div class="shouKuan">
          <figure>
            <img src="/images/zhifu_weixin.jpg" alt="微信收款码" @click="openDaTu('/images/zhifu_weixin.jpg')" />
            <figcaption>微信</figcaption>
          </figure>
          <figure>
            <img src="/images/zhifu_zhifubao.jpg" alt="支付宝收款码" @click="openDaTu('/images/zhifu_zhifubao.jpg')" />
            <figcaption>支付宝</figcaption>
          </figure>
        </div>
        <p class="shangInfo">长按或扫码即可打赏；所有打赏均出于自愿，不打赏也完全可以使用本平台的全部功能。</p>
      </div>
      <template #footer>
        <el-button type="primary" @click="shangShow = false">已打赏 / 关闭</el-button>
      </template>
    </el-dialog>

    <div v-if="daTu" class="daTuMask" @click="daTu = ''">
      <img :src="daTu" class="daTuImg" alt="收款码预览" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const huoYue = ref('denglu')
const dengLuForm = ref({ yongHuMing: '', miMa: '' })
const zhuceForm = ref({ yongHuMing: '', miMa: '', xingMing: '', shouJi: '', xueHao: '' })

// 用户协议 / 隐私政策 弹窗内容
const xyShow = ref(false)
const xyTitle = ref('')
const xyContent = ref([])
const shangShow = ref(false)
const daTu = ref('')
const openDaTu = (src) => { daTu.value = src }
const yhXy = [
  '欢迎使用校园二手交易平台（以下简称"本平台"）。在使用本服务前，请仔细阅读以下条款。注册或使用本平台即表示您已理解并同意本协议全部内容。',
  '一、服务说明：本平台为在校师生提供闲置物品（教材、数码、生活用品等）的发布、浏览与交易便利服务。平台仅提供信息展示与沟通渠道，具体交易由买卖双方自行完成。',
  '二、账号与责任：您应对本人账号下的所有行为负责，须提供真实、准确的注册信息，妥善保管账号与密码，不得转让、出借或冒用他人账号。',
  '三、交易规则：买卖双方应遵循诚实信用原则，如实描述商品成色、价格与交易方式；货款、物流与售后由双方自行约定并承担相应责任。',
  '四、禁止行为：不得发布违法违规、侵权、虚假或违背校园公序良俗的商品与信息；不得利用本平台从事诈骗、洗钱等任何违法活动。',
  '五、知识产权：平台界面、代码与文字内容的知识产权归开发者所有（开源代码遵循相应开源许可）；用户发布内容的著作权归发布者所有。',
  '六、免责声明：平台已尽合理努力保障服务稳定，但对因网络、第三方或不可抗力导致的服务中断、交易纠纷等，平台不承担责任。',
  '七、协议变更：平台有权根据法律法规与运营需要修订本协议，修订内容于平台公示后即生效。',
  '八、联系我们：如对本协议有任何疑问，可通过平台公布的渠道与开发者联系。',
  '九、开源与打赏：本项目的全部源代码已在 <a href="https://gitee.com/zhang-san-zhangshan/esjypt" target="_blank" rel="noopener" style="color:#2563eb;font-weight:600;text-decoration:underline;">Gitee</a> / <a href="https://github.com/LIHUA5645/Campus-Second-hand-Trading-Platform-based-on-Spring-Boot-and-Vue" target="_blank" rel="noopener" style="color:#2563eb;font-weight:600;text-decoration:underline;">GitHub</a> 以开源许可协议公开发布，任何人可免费学习、使用与二次开发。开发者坚持开源，是希望这套校园闲置流转的方案能被更多同学用上、被更多新手当作实战项目参考，也让代码在公开审视中变得更好。若本项目对你有帮助，欢迎通过下方打赏方式支持开发者——你的每一份鼓励，都是这个小而美的项目持续维护与迭代的动力。',
  '打赏信息：可通过下方弹窗中的微信 / 支付宝收款码支持开发者。所有打赏均出于自愿，不打赏也完全可以使用本平台的全部功能。'
]
const ysXy = [
  '本隐私政策说明校园二手交易平台如何收集、使用与保护您的个人信息。使用本平台即表示您已理解并同意本政策。',
  '一、我们收集的信息：注册时您提供的用户名、昵称、手机号、学号等资料；交易过程中产生的商品、订单与沟通记录；以及必要的访问日志等技术信息。',
  '二、信息的使用：用于账号管理、交易撮合、客户服务和平台安全风控；我们不会将您的个人信息用于上述目的之外的用途。',
  '三、信息的保护：我们采取合理的加密与访问控制措施保护您的信息，仅限必要人员在授权范围内访问。',
  '四、信息共享：除法律法规要求、司法机关依法调取，或为完成交易所必需（如向交易对方展示必要联系方式）外，我们不会向第三方披露您的个人信息。',
  '五、Cookie 与技术：我们使用必要的本地存储以维持登录态与改善体验，您可在浏览器中自行管理相关设置。',
  '六、您的权利：您有权查询、更正自己的信息，或在注销账号后要求我们删除您的个人数据。',
  '七、未成年人：本平台主要面向在校师生，未成年人应在监护人指导下使用。',
  '八、联系我们：关于隐私保护的任何疑问，可通过平台公布的渠道与开发者联系。',
  '九、开源说明：本项目以开源方式发布，完整源代码可在公开的代码仓库中查看与获取；开源部分不包含任何强制收集的个人数据。如你对开源与隐私的结合有任何疑问，欢迎通过上述渠道联系开发者。'
]
const openXy = (t) => {
  if (t === 'yh') { xyTitle.value = '用户协议'; xyContent.value = yhXy }
  else { xyTitle.value = '隐私政策'; xyContent.value = ysXy }
  xyShow.value = true
}

// 笑脸：瞳孔跟随 + 发呆撒娇
const lianEl = ref(null)
const pupil = ref({ x: 0, y: 0 })
const tongStyle = ref({ transform: 'translate(0px,0px)' })
const blinkV = ref(false)
const saJiaoV = ref(false)
let lastMove = 0

const onMove = (e) => {
  lastMove = Date.now()
  saJiaoV.value = false
  const el = lianEl.value
  if (!el) return
  const r = el.getBoundingClientRect()
  const cx = r.left + r.width / 2
  const cy = r.top + r.height / 2
  const dx = e.clientX - cx
  const dy = e.clientY - cy
  const dist = Math.hypot(dx, dy) || 1
  const max = 3.5
  const x = (dx / dist) * max
  const y = (dy / dist) * max
  pupil.value = { x, y }
  tongStyle.value = { transform: `translate(${x}px,${y}px)` }
}

let idleTimer = null
onMounted(() => {
  window.addEventListener('mousemove', onMove)
  const t = setInterval(() => {
    if (shu3.value < 3) shu3.value++
    else clearInterval(t)
  }, 260)
  idleTimer = setInterval(() => {
    if (Date.now() - lastMove < 1500 || !lianEl.value) return
    const r = Math.random()
    if (r < 0.3) {
      blinkV.value = true
      setTimeout(() => (blinkV.value = false), 160)
    } else if (r < 0.65) {
      const a = Math.random() * Math.PI * 2
      const x = Math.cos(a) * 3.5
      const y = Math.sin(a) * 3.5
      pupil.value = { x, y }
      tongStyle.value = { transform: `translate(${x}px,${y}px)` }
    } else {
      saJiaoV.value = true
      setTimeout(() => (saJiaoV.value = false), 700)
    }
  }, 1300)
})
onUnmounted(() => {
  window.removeEventListener('mousemove', onMove)
  if (idleTimer) clearInterval(idleTimer)
})

const shu3 = ref(0)

const dengLu = async () => {
  if (!dengLuForm.value.yongHuMing || !dengLuForm.value.miMa) {
    ElMessage.warning('请填写用户名和密码')
    return
  }
  const res = await jieKou.denglu(dengLuForm.value.yongHuMing, dengLuForm.value.miMa)
  if (res.data.chengGong) {
    localStorage.setItem('yongHu', JSON.stringify(res.data.yongHu))
    ElMessage.success('登录成功')
    luYou.push('/shouye')
  } else {
    ElMessage.error(res.data.xinXi || '登录失败')
  }
}

const zhuce = async () => {
  const res = await jieKou.zhuce(zhuceForm.value)
  if (res.data.chengGong) {
    ElMessage.success('注册成功，请登录')
    huoYue.value = 'denglu'
    dengLuForm.value.yongHuMing = zhuceForm.value.yongHuMing
    dengLuForm.value.miMa = zhuceForm.value.miMa
  } else {
    ElMessage.error(res.data.xinXi || '注册失败')
  }
}

// 找回密码
const zhaoHuiXian = ref(false)
const zhaoHui = ref({ yongHuMing: '', shouJi: '', xueHao: '', xinMiMa: '' })
const daKaiZhaoHui = () => {
  zhaoHui.value.yongHuMing = dengLuForm.value.yongHuMing
  zhaoHui.value.shouJi = ''
  zhaoHui.value.xueHao = ''
  zhaoHui.value.xinMiMa = ''
  zhaoHuiXian.value = true
}
const tiJiaoZhaoHui = async () => {
  const z = zhaoHui.value
  if (!z.yongHuMing || !z.shouJi || !z.xueHao || !z.xinMiMa) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (z.xinMiMa.length < 6) {
    ElMessage.warning('新密码至少 6 位')
    return
  }
  const res = await jieKou.zhaohuiMiMa(z.yongHuMing, z.shouJi, z.xueHao, z.xinMiMa)
  if (res.data.chengGong) {
    ElMessage.success('密码已重置，请用新密码登录')
    zhaoHuiXian.value = false
    huoYue.value = 'denglu'
    dengLuForm.value.yongHuMing = z.yongHuMing
    dengLuForm.value.miMa = ''
  } else {
    ElMessage.error(res.data.xinXi || '找回失败')
  }
}
</script>

<style scoped>
.dengLu { display: flex; min-height: 100vh; background: #f5f7fa; }

/* ===== 左侧 ===== */
.zuo {
  position: relative;
  flex: 1.15;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 64px;
  color: #1e293b;
  background: #eaf1ff;
}
.wangGe {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(#c7d6ff 1.4px, transparent 1.4px);
  background-size: 22px 22px;
  opacity: .5;
  pointer-events: none;
}

/* 笑脸 */
.lian {
  position: absolute;
  bottom: 30px;
  right: 30px;
  width: 84px;
  height: 84px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid #c7d6ff;
  box-shadow: 0 10px 22px rgba(37, 99, 235, .18);
  z-index: 3;
}
.lian .hong { position: absolute; width: 13px; height: 8px; border-radius: 50%; background: #fca5a5; top: 47px; }
.lian .hongL { left: 15px; }
.lian .hongR { right: 15px; }
.lian .yan {
  position: absolute;
  top: 30px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #0f172a;
  transition: transform .12s ease;
}
.lian .yanL { left: 22px; }
.lian .yanR { right: 22px; }
.lian .tong {
  position: absolute;
  top: 4px;
  left: 4px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #fff;
  transition: transform .08s linear;
}
.lian .zui {
  position: absolute;
  bottom: 19px;
  left: 50%;
  width: 30px;
  height: 15px;
  border: 2px solid #0f172a;
  border-top: none;
  border-radius: 0 0 30px 30px;
  transform: translateX(-50%);
}
.lian.blink .yan { transform: scaleY(.12); }
.lian.saJiao { animation: sajiao .7s ease; }
@keyframes sajiao {
  0%, 100% { transform: rotate(0) translateY(0); }
  25% { transform: rotate(-9deg) translateY(-3px); }
  50% { transform: rotate(8deg); }
  75% { transform: rotate(-5deg) translateY(-2px); }
}

.zuoNei { position: relative; z-index: 2; width: 100%; max-width: 440px; animation: ru .55s ease both; }
@keyframes ru { from { opacity: 0; transform: translateY(16px); } to { opacity: 1; transform: translateY(0); } }

.logoBox {
  display: inline-flex;
  padding: 12px;
  border-radius: 16px;
  background: #2563eb;
  margin-bottom: 26px;
}
.logo { width: 56px; height: 56px; border-radius: 10px; display: block; object-fit: cover; }
.zuoNei h1 { font-size: 40px; line-height: 1.2; letter-spacing: 1px; margin: 0 0 16px; font-weight: 800; color: #0f172a; }
.miaoShu { font-size: 15.5px; line-height: 1.75; color: #64748b; margin: 0 0 30px; }

.teDian { list-style: none; padding: 0; margin: 0 0 28px; display: flex; flex-direction: column; gap: 12px; }
.teDian li {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  border-radius: 12px;
  background: #fff;
  border: 1px solid #dbe4ff;
  transition: border-color .25s ease, transform .25s ease, box-shadow .25s ease;
}
.teDian li:hover {
  border-color: #2563eb;
  transform: translateX(6px);
  box-shadow: 0 10px 22px rgba(37, 99, 235, .14);
}
.teDian .ico {
  flex: none;
  display: grid;
  place-items: center;
  width: 38px;
  height: 38px;
  border-radius: 10px;
  background: #2563eb;
  color: #fff;
  font-size: 19px;
}
.teDian b { display: block; font-size: 15px; font-weight: 700; color: #0f172a; }
.teDian small { display: block; font-size: 12.5px; color: #64748b; margin-top: 2px; }

.shuJu { display: flex; align-items: center; gap: 22px; }
.shuJu > div:not(.line) { display: flex; flex-direction: column; }
.shuJu strong { font-size: 22px; font-weight: 800; color: #1d4ed8; }
.shuJu span { font-size: 12.5px; color: #64748b; margin-top: 2px; }
.shuJu .line { width: 1px; height: 30px; background: #c7d6ff; }

/* ===== 右侧表单（扁平卡片） ===== */
.you { flex: 1; display: flex; align-items: center; justify-content: center; padding: 40px; background: #f5f7fa; }
.kaPian {
  width: 100%;
  max-width: 430px;
  padding: 38px 36px 30px;
  border-radius: 16px;
  background: #fff;
  border: 1px solid #e5e7eb;
  animation: ru .45s ease both;
}
.kaTou { margin-bottom: 6px; }
.kaTou h2 { font-size: 26px; font-weight: 800; color: #0f172a; margin: 0 0 6px; letter-spacing: .5px; }
.kaTou p { font-size: 14px; color: #64748b; margin: 0; }

.shouQian :deep(.el-tabs__header) { margin: 18px 0 22px; }
.shouQian :deep(.el-tabs__nav-wrap::after) { background: #e5e7eb; height: 1px; }
.shouQian :deep(.el-tabs__item) { font-size: 15px; font-weight: 600; color: #64748b; padding: 0 22px !important; transition: color .25s ease; }
.shouQian :deep(.el-tabs__item.is-active) { color: #2563eb; }
.shouQian :deep(.el-tabs__active-bar) { background: #2563eb; height: 3px; border-radius: 3px; transition: transform .3s ease; }

.biaoDan { margin-top: 4px; }
.biaoDan :deep(.el-form-item__label) { color: #334155; font-weight: 600; padding-bottom: 6px; }
.biaoDan :deep(.el-form-item) { margin-bottom: 18px; }
.biaoDan :deep(.el-input__wrapper) {
  border-radius: 10px;
  padding: 4px 12px;
  background: #fff;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
  transition: box-shadow .2s ease;
}
.biaoDan :deep(.el-input__wrapper:hover) { box-shadow: 0 0 0 1px #cbd5e1 inset; }
.biaoDan :deep(.el-input__wrapper.is-focus) { box-shadow: 0 0 0 2px #2563eb inset; }
.biaoDan :deep(.el-input__inner) { height: 40px; }
.biaoDan :deep(.el-input__prefix) { color: #94a3b8; }

.tiJiao {
  width: 100%;
  height: 48px;
  margin-top: 6px;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 2px;
  background: #2563eb;
  color: #fff;
  transition: background .25s ease, transform .12s ease;
}
.tiJiao:hover { background: #1d4ed8; }
.tiJiao:active { transform: scale(.99); }

.wangJiHang { display: flex; justify-content: flex-end; margin: -6px 0 10px; }
.wangJiHang .lk { font-size: 12.5px; color: #2563eb; cursor: pointer; }
.wangJiHang .lk:hover { text-decoration: underline; }

.zhaoHuiTiShi { margin: 0 0 14px; font-size: 13px; color: #64748b; line-height: 1.7; }
.zhaoHuiDuiHua :deep(.el-form-item__label) { color: #334155; font-weight: 600; padding-bottom: 4px; }
.zhaoHuiDuiHua :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
}
.zhaoHuiDuiHua :deep(.el-input__wrapper.is-focus) { box-shadow: 0 0 0 2px #2563eb inset; }
.zhaoHuiDuiHua :deep(.el-input__inner) { height: 38px; }

.xieYi { margin: 20px 0 0; text-align: center; font-size: 12px; color: #94a3b8; }
.xieYi .lk { color: #2563eb; cursor: pointer; }
.xieYi .lk:hover { text-decoration: underline; }

.xyBody p { margin: 0 0 12px; font-size: 13.5px; line-height: 1.85; color: #475569; text-align: justify; }
.xyBody p:last-child { margin-bottom: 0; }

.shangBtn { background: #2563eb; border-color: #2563eb; color: #fff; border-radius: 10px; font-weight: 600; transition: background .25s ease, border-color .25s ease; }
.shangBtn:hover { background: #1d4ed8; border-color: #1d4ed8; }
.shangBody { text-align: center; padding: 4px 2px; }
.shangIcon { font-size: 34px; color: #ef4444; margin-bottom: 10px; }
.shangBody p { font-size: 13.5px; line-height: 1.8; color: #475569; margin: 0 0 10px; }
.shangBody .shangInfo { font-size: 12.5px; color: #94a3b8; text-align: left; }

.shouKuan { display: flex; justify-content: center; gap: 18px; margin: 14px 0 10px; }
.shouKuan figure { margin: 0; }
.shouKuan img { width: 120px; height: 120px; object-fit: cover; border: 1px solid #eef2f7; border-radius: 10px; background: #fff; }
.shouKuan img { cursor: zoom-in; transition: transform .2s ease; }
.shouKuan img:hover { transform: scale(1.04); }
.shouKuan figcaption { font-size: 12.5px; color: #64748b; margin-top: 6px; }

.daTuMask { position: fixed; inset: 0; z-index: 5000; display: flex; align-items: center; justify-content: center; background: rgba(0, 0, 0, .72); cursor: zoom-out; }
.daTuImg { max-width: 82vw; max-height: 82vh; border-radius: 12px; box-shadow: 0 12px 44px rgba(0, 0, 0, .5); }

@media (max-width: 900px) {
  .dengLu { flex-direction: column; }
  .zuo { padding: 44px 28px; }
  .zuoNei h1 { font-size: 30px; }
  .you { padding: 28px 18px 44px; }
  .kaPian { padding: 30px 24px 24px; }
}
</style>

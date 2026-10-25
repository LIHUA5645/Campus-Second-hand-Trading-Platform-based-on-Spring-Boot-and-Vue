<!-- 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24 -->
<template>
  <!-- 悬浮球 -->
  <div v-if="xianShi && !daKai" class="aiFuDong" @click="daKai = true" title="AI 智能导购">
    <span class="guangQuan"></span>
    <img v-if="aiXingXiang.aiTouXiang" :src="aiXingXiang.aiTouXiang" alt="AI" class="fuDongTuPian" />
    <el-icon v-else class="fuDongTuBiao"><MagicStick /></el-icon>
    <span class="fuDongWen">AI 帮我挑</span>
  </div>

  <!-- 聊天弹窗 -->
  <transition name="tanChu">
    <div v-if="xianShi && daKai" class="aiTanChuang">
      <div class="tanTou">
        <div class="touXiang">
          <img v-if="aiXingXiang.aiTouXiang" :src="aiXingXiang.aiTouXiang" alt="AI 头像" class="touXiangTu" />
          <el-icon v-else><MagicStick /></el-icon>
        </div>
        <div class="tanTouXinXi">
          <strong>{{ aiXingXiang.aiMingCheng }} · 智能导购</strong>
          <span>{{ zhuangTai.keYong ? 'AI 已接入' : '未配置密钥 · 关键词兜底' }}{{ zhuangTai.shiTuKeYong ? ' · 支持识图' : '' }}</span>
        </div>
        <el-tooltip content="打开完整页面" placement="top">
          <button type="button" class="touAnNiu" @click="quAi"><el-icon><FullScreen /></el-icon></button>
        </el-tooltip>
        <el-tooltip content="收起" placement="top">
          <button type="button" class="touAnNiu" @click="daKai = false"><el-icon><Close /></el-icon></button>
        </el-tooltip>
      </div>

      <div v-if="!dangQianYongHu" class="dengLuTiShi">
        <span>登录后即可让小淘帮你挑闲置好物</span>
        <button type="button" class="quDengLu" @click="quDengLu">去登录</button>
      </div>

      <div class="liaoTianQu" ref="liaoTianQu">
        <div v-if="!xiaoXiLie.length" class="kongBai">
          <div class="xiaoXi aiDe">
            <div class="touXiang xiao">
              <img v-if="aiXingXiang.aiTouXiang" :src="aiXingXiang.aiTouXiang" alt="AI 头像" class="touXiangTu" />
              <el-icon v-else><MagicStick /></el-icon>
            </div>
            <div class="pao huanYingPao">
              <strong class="huanYingMing">{{ aiXingXiang.aiMingCheng }} 来啦～</strong>
              <div class="paoWenZi">{{ aiXingXiang.aiJieShao || '说出你的需求，或上传一张图片，我帮你在全校闲置里挑出最合适的那几件。' }}</div>
            </div>
          </div>
          <div class="kuaiJieLie">
            <span v-for="w in kuaiJieWen" :key="w" class="kuaiJie" @click="yongKuaiJie(w)">
              <el-icon><ChatLineRound /></el-icon>{{ w }}
            </span>
          </div>
        </div>

        <div v-for="(x, i) in xiaoXiLie" :key="i" :class="['xiaoXi', x.jueSe === 'wo' ? 'woDe' : 'aiDe']">
          <div v-if="x.jueSe === 'ai'" class="touXiang xiao">
            <img v-if="aiXingXiang.aiTouXiang" :src="aiXingXiang.aiTouXiang" alt="AI 头像" class="touXiangTu" />
            <el-icon v-else><MagicStick /></el-icon>
          </div>
          <div v-if="x.jueSe === 'wo'" class="touXiang xiao woDeTouXiang">
            {{ (dangQianYongHu && (dangQianYongHu.xingMing || dangQianYongHu.yongHuMing) || '我').slice(0, 1) }}
          </div>
          <div class="pao">
            <div v-if="x.tuPian" class="paoTuPian"><img :src="x.tuPian" alt="上传的图片" /></div>
            <div v-if="x.wenZi" class="paoWenZi">{{ x.wenZi }}</div>

            <div v-if="x.jueSe === 'ai' && (x.lieBiao || []).length" class="tuiJianQu">
              <div class="tuiJianTou">
                <span>为你挑了 {{ x.lieBiao.length }} 件</span>
                <span class="moShiBiaoQian">{{ x.moShi }}</span>
              </div>
              <div class="tuiJianLie">
                <div v-for="sp in x.lieBiao" :key="sp.id" class="tuiJianKa" @click="kanXiangQing(sp.id)">
                  <div class="kaTu">
                    <img v-if="sp.tuPian" :src="sp.tuPian" :alt="sp.biaoTi" />
                    <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
                  </div>
                  <div class="kaXinXi">
                    <div class="kaBiaoTi">{{ sp.biaoTi }}</div>
                    <div class="kaDiBu">
                      <span class="kaJiaGe">￥{{ sp.jiaGe }}</span>
                      <span v-if="sp.liYou" class="kaLiYou">{{ sp.liYou }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="x.jiangJi && x.yuanYin" class="jiangJiTiShi">大模型未接通（{{ x.yuanYin }}），以上为本地匹配结果</div>
          </div>
        </div>

        <div v-if="dengDai" class="xiaoXi aiDe">
          <div class="touXiang xiao">
            <img v-if="aiXingXiang.aiTouXiang" :src="aiXingXiang.aiTouXiang" alt="AI 头像" class="touXiangTu" />
            <el-icon v-else><MagicStick /></el-icon>
          </div>
          <div class="pao">
            <div class="paoWenZi shuRuZhong">
              <span class="dian"></span><span class="dian"></span><span class="dian"></span>
              正在为你挑选好物…
            </div>
          </div>
        </div>
      </div>

      <div class="shuRuQu">
        <div v-if="daiFaTu" class="yuLanHang">
          <div class="yuLanTu">
            <img :src="daiFaTu" alt="待发送图片" />
            <span class="shanTu" @click="daiFaTu = ''"><el-icon><Close /></el-icon></span>
          </div>
          <span class="yuLanWen">发送后 AI 会先识图再找同款</span>
        </div>
        <div class="shuRuHang">
          <button type="button" class="tuBiaoAnNiu" @click="xuanTu"><el-icon><Picture /></el-icon></button>
          <input
            v-model="shuRu"
            class="shuRuKuang"
            type="text"
            placeholder="想找什么？直接说～"
            @keyup.enter="faSong"
          />
          <button type="button" class="faSongAnNiu" :disabled="dengDai" @click="faSong">
            <el-icon><Promotion /></el-icon>
          </button>
        </div>
        <input ref="wenJianShuRu" type="file" accept="image/png,image/jpeg,image/webp,image/gif" class="yinCangWenJian" @change="duTuPian" />
      </div>
    </div>
  </transition>
</template>

<script setup>
import { computed, ref, nextTick, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const luYouXin = useRoute()

const daKai = ref(false)
const shuRu = ref('')
const dengDai = ref(false)
const daiFaTu = ref('')
const xiaoXiLie = ref([])
const liaoTianQu = ref(null)
const wenJianShuRu = ref(null)
const zhuangTai = ref({ keYong: false, shiTuKeYong: false, qiYongShu: 0 })
const dangQianYongHu = ref(null)
const aiXingXiang = ref({ aiTouXiang: '', aiMingCheng: '小淘', aiJieShao: '' })

const kuaiJieWen = [
  '预算 200 以内的考研教材',
  '能打游戏的二手笔记本',
  '宿舍小电器，便宜点',
  '九成新的自行车'
]

// 悬浮入口对所有人可见（含游客）；登录页 / 后台 / AI 完整页本身不再重复显示
const xianShi = computed(() => {
  const luJing = luYouXin.path || ''
  return luJing !== '/denglu' && luJing !== '/ai' && !luJing.startsWith('/admin')
})

const quAi = () => {
  daKai.value = false
  luYou.push('/ai')
}

const quDengLu = () => {
  daKai.value = false
  luYou.push('/denglu')
}

const kanXiangQing = (id) => {
  if (id) luYou.push('/shangpin/' + id)
}

const gunDaoDi = async () => {
  await nextTick()
  if (liaoTianQu.value) liaoTianQu.value.scrollTop = liaoTianQu.value.scrollHeight
}

const yongKuaiJie = (w) => {
  shuRu.value = w
  faSong()
}

const xuanTu = () => {
  if (wenJianShuRu.value) {
    wenJianShuRu.value.value = ''
    wenJianShuRu.value.click()
  }
}

// 压缩后再转 base64，避免图片过大拖慢识别
const yaSuo = (wenJian) => new Promise((resolve, reject) => {
  const du = new FileReader()
  du.onload = () => {
    const tu = new Image()
    tu.onload = () => {
      const zuiDa = 1024
      const bi = Math.min(1, zuiDa / Math.max(tu.naturalWidth, tu.naturalHeight))
      const kuan = Math.round(tu.naturalWidth * bi)
      const gao = Math.round(tu.naturalHeight * bi)
      const huaBu = document.createElement('canvas')
      huaBu.width = kuan
      huaBu.height = gao
      const ctx = huaBu.getContext('2d')
      ctx.fillStyle = '#ffffff'
      ctx.fillRect(0, 0, kuan, gao)
      ctx.drawImage(tu, 0, 0, kuan, gao)
      resolve(huaBu.toDataURL('image/jpeg', 0.85))
    }
    tu.onerror = () => reject(new Error('图片读取失败'))
    tu.src = du.result
  }
  du.onerror = () => reject(new Error('图片读取失败'))
  du.readAsDataURL(wenJian)
})

const duTuPian = async (e) => {
  const wenJian = e.target.files && e.target.files[0]
  if (!wenJian) return
  if (wenJian.size > 8 * 1024 * 1024) {
    ElMessage.warning('图片不能超过 8MB')
    return
  }
  try {
    daiFaTu.value = await yaSuo(wenJian)
  } catch (err) {
    ElMessage.error('图片读取失败，换一张试试')
  }
}

const faSong = async () => {
  const wen = shuRu.value.trim()
  const tu = daiFaTu.value
  if (!wen && !tu) {
    ElMessage.warning('说点什么，或者上传一张图片')
    return
  }
  xiaoXiLie.value.push({ jueSe: 'wo', wenZi: wen, tuPian: tu })
  shuRu.value = ''
  daiFaTu.value = ''
  dengDai.value = true
  await gunDaoDi()
  try {
    const res = await jieKou.aiWenDa({ wenTi: wen, tuPian: tu, shu: 6 })
    const shuJu = res.data
    if (shuJu.chengGong) {
      xiaoXiLie.value.push({
        jueSe: 'ai',
        wenZi: shuJu.huiDa,
        lieBiao: shuJu.lieBiao || [],
        moShi: shuJu.moShi,
        jiangJi: !!shuJu.jiangJi,
        yuanYin: shuJu.yuanYin
      })
    } else {
      if (shuJu.weiDengLu) {
        xiaoXiLie.value.pop()
        ElMessage.warning('请先登录后再使用 AI 助手')
        luYou.push('/denglu')
        return
      }
      xiaoXiLie.value.push({ jueSe: 'ai', wenZi: shuJu.xinXi || '暂时没能回答，稍后再试～', lieBiao: [] })
    }
  } catch (err) {
    xiaoXiLie.value.push({ jueSe: 'ai', wenZi: 'AI 服务暂时不可用，请确认后台已配置密钥且网络可访问。', lieBiao: [] })
  } finally {
    dengDai.value = false
    await gunDaoDi()
  }
}

// 用户切换或退出登录时，清空聊天记录与未发送内容，避免上一账号的会话残留
const shuaXinYongHu = () => {
  let xin = null
  try {
    const y = localStorage.getItem('yongHu')
    xin = y ? JSON.parse(y) : null
  } catch (e) { xin = null }
  const jiuId = dangQianYongHu.value ? dangQianYongHu.value.id : null
  const xinId = xin ? xin.id : null
  if (jiuId !== xinId) {
    xiaoXiLie.value = []
    shuRu.value = ''
    daiFaTu.value = ''
    daKai.value = false
  }
  dangQianYongHu.value = xin
}
watch(() => luYouXin.path, shuaXinYongHu)

onMounted(async () => {
  const yongHu = localStorage.getItem('yongHu')
  dangQianYongHu.value = yongHu ? JSON.parse(yongHu) : null
  window.addEventListener('storage', shuaXinYongHu)
  try {
    const res = await jieKou.aiZhuangTai()
    if (res.data.chengGong) zhuangTai.value = res.data
  } catch (e) { /* 状态查询失败不影响使用 */ }
  try {
    const xing = await jieKou.aiPeiZhi()
    if (xing.data.chengGong) aiXingXiang.value = xing.data
  } catch (e) { /* 头像查询失败不影响使用 */ }
})
</script>

<style scoped>
/* ===== 悬浮球 ===== */
.aiFuDong {
  position: fixed;
  right: 28px;
  bottom: 92px;
  z-index: 900;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 18px;
  border-radius: 999px;
  background: var(--zhuTiDu);
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 12px 28px rgba(47, 107, 255, .34);
  transition: transform .18s ease, box-shadow .18s ease;
}
.aiFuDong:hover { transform: translateY(-3px); box-shadow: 0 16px 34px rgba(47, 107, 255, .42); }
.fuDongTuBiao { font-size: 18px; }
.fuDongTuPian { width: 22px; height: 22px; border-radius: 50%; object-fit: cover; display: block; }
.fuDongWen { white-space: nowrap; }
.guangQuan {
  position: absolute;
  inset: 0;
  border-radius: 999px;
  border: 2px solid rgba(255, 255, 255, .6);
  opacity: 0;
  animation: kuangSan 2.4s infinite ease-out;
}
@keyframes kuangSan {
  0% { transform: scale(1); opacity: .55; }
  70% { transform: scale(1.18); opacity: 0; }
  100% { opacity: 0; }
}

/* ===== 弹窗 ===== */
.aiTanChuang {
  position: fixed;
  right: 28px;
  bottom: 92px;
  z-index: 910;
  width: 400px;
  max-width: calc(100vw - 24px);
  height: 620px;
  max-height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: 20px;
  box-shadow: 0 24px 64px rgba(23, 43, 99, .28);
  overflow: hidden;
}
.tanChu-enter-active, .tanChu-leave-active { transition: opacity .18s ease, transform .18s ease; }
.tanChu-enter-from, .tanChu-leave-to { opacity: 0; transform: translateY(14px) scale(.97); }

.tanTou {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 14px;
  background: linear-gradient(135deg, #2f6bff 0%, #15c5b8 100%);
  color: #fff;
  flex-shrink: 0;
}
.tanTouXinXi { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 2px; }
.tanTouXinXi strong { font-size: 14.5px; font-weight: 800; }
.tanTouXinXi span { font-size: 11.5px; opacity: .85; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.touAnNiu {
  width: 30px;
  height: 30px;
  border-radius: 9px;
  border: none;
  background: rgba(255, 255, 255, .18);
  color: #fff;
  font-size: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background .15s ease;
}
.touAnNiu:hover { background: rgba(255, 255, 255, .32); }

.touXiang {
  width: 40px;
  height: 40px;
  border-radius: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, .22);
  color: #fff;
  font-size: 19px;
  flex-shrink: 0;
}
.touXiang.xiao { width: 30px; height: 30px; border-radius: 10px; font-size: 14px; }
.tanTou .touXiang { border: 2px solid rgba(255, 255, 255, .55); box-shadow: 0 4px 12px rgba(0, 0, 0, .18); }
.touXiangTu { width: 100%; height: 100%; object-fit: cover; border-radius: inherit; display: block; }
.woDeTouXiang {
  background: linear-gradient(135deg, #7c5cff 0%, #2f6bff 100%);
  font-size: 13px;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 3px 9px rgba(47, 107, 255, .28);
}

.liaoTianQu {
  position: relative;
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  scroll-behavior: smooth;
  background: linear-gradient(180deg, #f4f8ff 0%, #fdfeff 55%, #f7fbff 100%);
}
.liaoTianQu::before {
  content: '';
  position: absolute;
  top: -70px;
  right: -70px;
  width: 190px;
  height: 190px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(47, 107, 255, .12), transparent 70%);
  pointer-events: none;
}
.liaoTianQu::after {
  content: '';
  position: absolute;
  bottom: -80px;
  left: -80px;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(21, 197, 184, .10), transparent 70%);
  pointer-events: none;
}
.kongBai { position: relative; z-index: 1; padding-top: 12px; }
.huanYingPao {
  background: #fff;
  border: 1px solid #e3ecff;
  box-shadow: 0 8px 20px rgba(47, 107, 255, .10);
}
.huanYingMing {
  display: block;
  margin-bottom: 4px;
  font-size: 13.5px;
  font-weight: 800;
  background: var(--zhuTiDu);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.kuaiJieLie { position: relative; z-index: 1; display: flex; flex-wrap: wrap; gap: 8px; margin-top: 14px; }
.kuaiJie {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 7px 12px;
  border-radius: 999px;
  background: #fff;
  border: 1px solid #dde7ff;
  color: var(--zhuSe);
  font-size: 12.5px;
  cursor: pointer;
  transition: transform .15s ease, box-shadow .15s ease, border-color .15s ease;
}
.kuaiJie .el-icon { font-size: 13px; }
.kuaiJie:hover { transform: translateY(-2px); border-color: #b9d0ff; box-shadow: 0 8px 18px rgba(47, 107, 255, .16); }
.kuaiJie:hover { transform: translateY(-2px); box-shadow: 0 8px 18px rgba(47, 107, 255, .16); }

.xiaoXi { position: relative; z-index: 1; display: flex; gap: 8px; margin-bottom: 14px; align-items: flex-start; }
.xiaoXi.woDe { flex-direction: row-reverse; }
.pao {
  max-width: 80%;
  background: #f4f6fb;
  border-radius: 14px 14px 14px 4px;
  padding: 10px 13px;
  font-size: 13.5px;
  line-height: 1.65;
  color: var(--wenZi);
}
.woDe .pao {
  background: linear-gradient(135deg, #2f6bff 0%, #3f8cff 100%);
  color: #fff;
  border-radius: 14px 14px 4px 14px;
  box-shadow: 0 6px 16px rgba(47, 107, 255, .22);
}
.paoWenZi { white-space: pre-wrap; word-break: break-word; }
.paoTuPian {
  width: 130px;
  border-radius: 9px;
  overflow: hidden;
  margin-bottom: 7px;
  border: 1px solid rgba(0, 0, 0, .06);
}
.paoTuPian img { width: 100%; display: block; }

.shuRuZhong { display: flex; align-items: center; gap: 6px; color: var(--wenZiSan); }
.dian {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--zhuSe);
  animation: tiao 1.2s infinite ease-in-out;
}
.dian:nth-child(2) { animation-delay: .2s; }
.dian:nth-child(3) { animation-delay: .4s; }
@keyframes tiao {
  0%, 80%, 100% { transform: translateY(0); opacity: .35; }
  40% { transform: translateY(-4px); opacity: 1; }
}

.tuiJianQu { margin-top: 10px; }
.tuiJianTou {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  color: var(--wenZiSan);
  margin-bottom: 7px;
}
.moShiBiaoQian {
  background: #eef3ff;
  color: var(--zhuSe);
  border-radius: 999px;
  padding: 2px 9px;
  font-size: 11.5px;
}
.tuiJianLie { display: flex; flex-direction: column; gap: 8px; }
.tuiJianKa {
  display: flex;
  gap: 9px;
  padding: 8px;
  border: 1px solid var(--bianKuang);
  border-radius: 11px;
  background: #fff;
  cursor: pointer;
  transition: transform .15s ease, box-shadow .15s ease, border-color .15s ease;
}
.tuiJianKa:hover { transform: translateY(-1px); border-color: #bcd2ff; box-shadow: 0 8px 18px rgba(47, 107, 255, .13); }
.kaTu { width: 52px; height: 52px; border-radius: 9px; overflow: hidden; flex-shrink: 0; }
.kaTu img { width: 100%; height: 100%; object-fit: cover; display: block; }
.kaTu .tuPianZhanWei { width: 100%; height: 100%; font-size: 17px; }
.kaXinXi { min-width: 0; display: flex; flex-direction: column; gap: 3px; }
.kaBiaoTi {
  font-size: 13px;
  font-weight: 600;
  color: var(--wenZi);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.kaDiBu { display: flex; align-items: center; gap: 8px; min-width: 0; }
.kaJiaGe { color: var(--hongSe); font-weight: 800; font-size: 13px; flex-shrink: 0; }
.kaLiYou {
  font-size: 11.5px;
  color: var(--wenZiSan);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.jiangJiTiShi { margin-top: 8px; font-size: 11.5px; color: #e6a23c; }

.dengLuTiShi {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin: 0 2px 10px;
  padding: 10px 14px;
  border-radius: 12px;
  background: linear-gradient(135deg, #eef2ff, #f5f3ff);
  border: 1px solid #e1e7ff;
  font-size: 12.5px;
  color: #475569;
}
.dengLuTiShi .quDengLu {
  flex: none;
  border: none;
  cursor: pointer;
  padding: 6px 14px;
  border-radius: 8px;
  font-size: 12.5px;
  font-weight: 600;
  color: #fff;
  background: #2563eb;
  transition: background .2s ease, transform .12s ease;
}
.dengLuTiShi .quDengLu:hover { background: #1d4ed8; }
.dengLuTiShi .quDengLu:active { transform: scale(.97); }

.shuRuQu {
  padding: 10px 12px 12px;
  border-top: 1px solid var(--bianKuang);
  background: #fff;
  flex-shrink: 0;
}
.yuLanHang { display: flex; align-items: center; gap: 10px; margin-bottom: 9px; }
.yuLanTu { position: relative; width: 54px; height: 54px; }
.yuLanTu img { width: 54px; height: 54px; object-fit: cover; border-radius: 9px; border: 1px solid var(--bianKuang); display: block; }
.shanTu {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: var(--hongSe);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  cursor: pointer;
}
.yuLanWen { font-size: 12px; color: var(--wenZiSan); }

.shuRuHang { display: flex; align-items: center; gap: 8px; }
.tuBiaoAnNiu {
  width: 38px;
  height: 38px;
  border-radius: 11px;
  border: 1px solid var(--bianKuang);
  background: #f7f9fc;
  color: var(--zhuSe);
  font-size: 17px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color .15s ease;
}
.tuBiaoAnNiu:hover { border-color: var(--zhuSe); }
.shuRuKuang {
  flex: 1;
  height: 38px;
  border-radius: 11px;
  border: 1px solid var(--bianKuang);
  background: #f7f9fc;
  padding: 0 14px;
  font-size: 13.5px;
  color: var(--wenZi);
  outline: none;
  min-width: 0;
}
.shuRuKuang:focus { border-color: var(--zhuSe); background: #fff; }
.faSongAnNiu {
  width: 38px;
  height: 38px;
  border-radius: 11px;
  border: none;
  background: var(--zhuTiDu);
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 5px 14px rgba(47, 107, 255, .26);
}
.faSongAnNiu:disabled { opacity: .6; cursor: not-allowed; }
.yinCangWenJian { display: none; }

@media (max-width: 700px) {
  .aiFuDong { right: 16px; bottom: 76px; padding: 11px 14px; }
  .fuDongWen { display: none; }
  .aiTanChuang { right: 12px; bottom: 76px; height: calc(100vh - 120px); }
}
</style>

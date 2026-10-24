<!-- 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24 -->
<template>
  <div class="aiZhuShou">
    <div v-if="yiDengLu" class="rongQi neiRong">
      <!-- 头部 -->
      <section class="touBu">
        <div class="touBuZuo">
          <div class="touXiang">
            <img v-if="aiXingXiang.aiTouXiang" :src="aiXingXiang.aiTouXiang" alt="AI 头像" class="touXiangTu" />
            <el-icon v-else><MagicStick /></el-icon>
          </div>
          <div>
            <h2><span class="zhuTiWenZi">{{ aiXingXiang.aiMingCheng }}</span> · 智能导购</h2>
            <p>{{ aiXingXiang.aiJieShao || '说出你的需求，或上传一张图片，我帮你在全校闲置里挑出最合适的那几件。' }}</p>
          </div>
        </div>
        <div class="zhuangTaiLie">
          <el-tag v-if="zhuangTai.keYong" type="success" effect="dark" round>
            <el-icon><CircleCheck /></el-icon> AI 已接入（{{ zhuangTai.qiYongShu }} 个密钥）
          </el-tag>
          <el-tag v-else type="info" effect="plain" round>未配置密钥 · 关键词匹配兜底</el-tag>
          <el-tag :type="zhuangTai.shiTuKeYong ? 'success' : 'info'" effect="plain" round>
            {{ zhuangTai.shiTuKeYong ? '支持识图' : '暂不支持识图' }}
          </el-tag>
        </div>
      </section>

      <!-- 聊天区 -->
      <section class="liaoTianQu" ref="liaoTianQu">
        <div v-if="!xiaoXiLie.length" class="kongBai">
          <div class="kongTu"><el-icon><ChatDotRound /></el-icon></div>
          <h3>你想找点什么？</h3>
          <p>可以用大白话描述，也可以直接发图让我识图找同款。</p>
          <div class="kuaiJieLie">
            <span v-for="w in kuaiJieWen" :key="w" class="kuaiJie" @click="yongKuaiJie(w)">{{ w }}</span>
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
            <div v-if="x.tuPian" class="paoTuPian">
              <img :src="x.tuPian" alt="上传的图片" />
            </div>
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
                    <div class="kaJiaGe">￥{{ sp.jiaGe }}</div>
                    <div v-if="sp.liYou" class="kaLiYou">{{ sp.liYou }}</div>
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
      </section>

      <!-- 输入区 -->
      <section class="shuRuQu">
        <div v-if="daiFaTu" class="yuLanHang">
          <div class="yuLanTu">
            <img :src="daiFaTu" alt="待发送图片" />
            <span class="shanTu" @click="shanTu"><el-icon><Close /></el-icon></span>
          </div>
          <span class="yuLanWen">已选择图片，发送后 AI 会先识别它是什麼再找同款</span>
        </div>
        <div class="shuRuHang">
          <el-tooltip content="上传图片，让 AI 识图找同款" placement="top">
            <button type="button" class="tuBiaoAnNiu" @click="xuanTu">
              <el-icon><Picture /></el-icon>
            </button>
          </el-tooltip>
          <input
            v-model="shuRu"
            class="shuRuKuang"
            type="text"
            placeholder="例如：预算 300 左右的二手平板，主要用来看网课"
            @keyup.enter="faSong"
          />
          <button type="button" class="faSongAnNiu" :disabled="dengDai" @click="faSong">
            <el-icon><Promotion /></el-icon><span>{{ dengDai ? '思考中' : '发送' }}</span>
          </button>
        </div>
        <input ref="wenJianShuRu" type="file" accept="image/png,image/jpeg,image/webp,image/gif" class="yinCangWenJian" @change="duTuPian" />
        <p class="zhuShi">AI 只从平台在售商品里挑选，不会编造商品；结果仅供参考，下单前请和卖家确认成色。</p>
      </section>
    </div>

    <div v-else class="weiDengLu">
      <div class="weiDengLuTu"><el-icon><Lock /></el-icon></div>
      <h2>登录后即可使用 AI 助手</h2>
      <p>小淘能根据你的需求，从全校闲置里挑出最合适的几件好物。<br />登录账号后，随时找我帮你淘好物～</p>
      <el-button type="primary" size="large" @click="quDengLu">去登录</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const shuRu = ref('')
const dengDai = ref(false)
const daiFaTu = ref('')
const xiaoXiLie = ref([])
const liaoTianQu = ref(null)
const wenJianShuRu = ref(null)
const zhuangTai = ref({ keYong: false, shiTuKeYong: false, qiYongShu: 0 })
const dangQianYongHu = ref(null)
const aiXingXiang = ref({ aiTouXiang: '', aiMingCheng: '小淘', aiJieShao: '' })

const yiDengLu = computed(() => !!dangQianYongHu.value)
const quDengLu = () => {
  luYou.push('/denglu')
}

const kuaiJieWen = [
  '预算 200 以内的考研教材',
  '想要一台能打游戏的二手笔记本',
  '宿舍用的小电器，便宜点',
  '九成新的自行车有没有',
  '推荐几件适合新生入手的好物'
]

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

const shanTu = () => {
  daiFaTu.value = ''
}

const faSong = async () => {
  if (!dangQianYongHu.value) {
    ElMessage.warning('请先登录后再使用 AI 助手')
    luYou.push('/denglu')
    return
  }
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
      xiaoXiLie.value.push({ jueSe: 'ai', wenZi: shuJu.xinXi || '暂时没能回答，稍后再试～', lieBiao: [] })
    }
  } catch (err) {
    xiaoXiLie.value.push({ jueSe: 'ai', wenZi: 'AI 服务暂时不可用，请确认后台已配置密钥且网络可访问。', lieBiao: [] })
  } finally {
    dengDai.value = false
    await gunDaoDi()
  }
}

const kanXiangQing = (id) => {
  if (id) luYou.push('/shangpin/' + id)
}

onMounted(async () => {
  const yongHu = localStorage.getItem('yongHu')
  dangQianYongHu.value = yongHu ? JSON.parse(yongHu) : null
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
.aiZhuShou { padding: 28px 0 40px; }
.neiRong { display: flex; flex-direction: column; gap: 18px; }

.weiDengLu {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  gap: 14px;
  padding: 80px 24px;
  min-height: 60vh;
}
.weiDengLuTu {
  display: grid;
  place-items: center;
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, #eef2ff, #f5f3ff);
  color: #2563eb;
  font-size: 32px;
  margin-bottom: 4px;
  box-shadow: 0 10px 26px rgba(37, 99, 235, .18);
}
.weiDengLu h2 { margin: 0; font-size: 22px; font-weight: 800; color: #0f172a; }
.weiDengLu p { margin: 0; font-size: 14px; line-height: 1.8; color: #64748b; }
.weiDengLu .el-button { margin-top: 8px; padding: 0 28px; height: 46px; border-radius: 10px; font-size: 15px; font-weight: 600; }

.touBu {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: var(--yuanJiao);
  box-shadow: var(--banYing);
  padding: 20px 24px;
}
.touBuZuo { display: flex; align-items: center; gap: 16px; }
.touBu h2 { margin: 0 0 6px; font-size: 20px; font-weight: 800; }
.touBu p { margin: 0; font-size: 13px; color: var(--wenZiEr); }
.zhuangTaiLie { display: flex; gap: 8px; flex-wrap: wrap; }

.touXiang {
  width: 52px;
  height: 52px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--zhuTiDu);
  color: #fff;
  font-size: 24px;
  font-weight: 700;
  flex-shrink: 0;
}
.touXiang.xiao { width: 34px; height: 34px; border-radius: 11px; font-size: 16px; }
.touXiangTu { width: 100%; height: 100%; object-fit: cover; border-radius: inherit; display: block; }
.woDeTouXiang {
  background: linear-gradient(135deg, #7c5cff 0%, #2f6bff 100%);
  font-size: 14px;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 3px 9px rgba(47, 107, 255, .28);
}

.liaoTianQu {
  height: 460px;
  overflow-y: auto;
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: var(--yuanJiao);
  box-shadow: var(--banYing);
  padding: 22px;
  scroll-behavior: smooth;
}

.kongBai { text-align: center; padding: 70px 20px; color: var(--wenZiSan); }
.kongTu { font-size: 46px; color: #c3d0e8; margin-bottom: 12px; }
.kongBai h3 { margin: 0 0 6px; font-size: 17px; color: var(--wenZi); }
.kongBai p { margin: 0 0 18px; font-size: 13px; }
.kuaiJieLie { display: flex; flex-wrap: wrap; gap: 10px; justify-content: center; }
.kuaiJie {
  padding: 8px 14px;
  border-radius: 999px;
  background: #f2f6ff;
  border: 1px solid #dde7ff;
  color: var(--zhuSe);
  font-size: 13px;
  cursor: pointer;
  transition: transform .15s ease, box-shadow .15s ease;
}
.kuaiJie:hover { transform: translateY(-2px); box-shadow: 0 8px 18px rgba(47, 107, 255, .16); }

.xiaoXi { display: flex; gap: 10px; margin-bottom: 18px; align-items: flex-start; }
.xiaoXi.woDe { flex-direction: row-reverse; }
.pao {
  max-width: 76%;
  background: #f4f6fb;
  border-radius: 14px 14px 14px 4px;
  padding: 12px 16px;
  font-size: 14px;
  line-height: 1.7;
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
  width: 160px;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 8px;
  border: 1px solid rgba(0, 0, 0, .06);
}
.paoTuPian img { width: 100%; display: block; }

.shuRuZhong { display: flex; align-items: center; gap: 6px; color: var(--wenZiSan); }
.dian {
  width: 6px;
  height: 6px;
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

.tuiJianQu { margin-top: 12px; }
.tuiJianTou {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12.5px;
  color: var(--wenZiSan);
  margin-bottom: 8px;
}
.moShiBiaoQian {
  background: #eef3ff;
  color: var(--zhuSe);
  border-radius: 999px;
  padding: 2px 10px;
  font-size: 12px;
}
.tuiJianLie { display: grid; grid-template-columns: repeat(auto-fill, minmax(230px, 1fr)); gap: 10px; }
.tuiJianKa {
  display: flex;
  gap: 10px;
  padding: 10px;
  border: 1px solid var(--bianKuang);
  border-radius: 12px;
  background: #fff;
  cursor: pointer;
  transition: transform .15s ease, box-shadow .15s ease, border-color .15s ease;
}
.tuiJianKa:hover { transform: translateY(-2px); border-color: #bcd2ff; box-shadow: 0 10px 22px rgba(47, 107, 255, .14); }
.kaTu {
  width: 66px;
  height: 66px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}
.kaTu img { width: 100%; height: 100%; object-fit: cover; display: block; }
.kaTu .tuPianZhanWei { width: 100%; height: 100%; font-size: 20px; }
.kaXinXi { min-width: 0; display: flex; flex-direction: column; gap: 4px; }
.kaBiaoTi {
  font-size: 13.5px;
  font-weight: 600;
  color: var(--wenZi);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.kaJiaGe { color: var(--hongSe); font-weight: 800; font-size: 14px; }
.kaLiYou { font-size: 12px; color: var(--wenZiSan); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

.jiangJiTiShi { margin-top: 10px; font-size: 12px; color: #e6a23c; }

.shuRuQu {
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: var(--yuanJiao);
  box-shadow: var(--banYing);
  padding: 16px;
}
.yuLanHang { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.yuLanTu { position: relative; width: 72px; height: 72px; }
.yuLanTu img { width: 72px; height: 72px; object-fit: cover; border-radius: 10px; border: 1px solid var(--bianKuang); display: block; }
.shanTu {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: var(--hongSe);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  cursor: pointer;
}
.yuLanWen { font-size: 12.5px; color: var(--wenZiSan); }

.shuRuHang { display: flex; align-items: center; gap: 10px; }
.tuBiaoAnNiu {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  border: 1px solid var(--bianKuang);
  background: #f7f9fc;
  color: var(--zhuSe);
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color .15s ease, color .15s ease;
}
.tuBiaoAnNiu:hover { border-color: var(--zhuSe); }
.shuRuKuang {
  flex: 1;
  height: 42px;
  border-radius: 12px;
  border: 1px solid var(--bianKuang);
  background: #f7f9fc;
  padding: 0 16px;
  font-size: 14px;
  color: var(--wenZi);
  outline: none;
}
.shuRuKuang:focus { border-color: var(--zhuSe); background: #fff; }
.faSongAnNiu {
  height: 42px;
  padding: 0 20px;
  border-radius: 12px;
  border: none;
  background: var(--zhuTiDu);
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 6px 16px rgba(47, 107, 255, .26);
}
.faSongAnNiu:disabled { opacity: .6; cursor: not-allowed; }
.zhuShi { margin: 10px 0 0; font-size: 12px; color: var(--wenZiSan); }
.yinCangWenJian { display: none; }
</style>

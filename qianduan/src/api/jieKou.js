// 作者：肖沐樑 QQ：3387432690
import axios from 'axios'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000
})

http.interceptors.request.use((config) => {
  const yongHu = localStorage.getItem('yongHu')
  if (yongHu) {
    try {
      config.headers['YongHuId'] = JSON.parse(yongHu).id
    } catch (e) {
      // 忽略解析异常
    }
  }
  // 后台管理请求统一携带管理员身份，供后端拦截器鉴权
  const guanLiYuan = localStorage.getItem('guanLiYuan')
  if (guanLiYuan) {
    try {
      config.headers['GuanLiYuanId'] = JSON.parse(guanLiYuan).id
    } catch (e) {
      // 忽略解析异常
    }
  }
  return config
})

export default {
  zhuce: (data) => http.post('/yonghu/zhuce', data),
  denglu: (yongHuMing, miMa) => http.post('/yonghu/denglu?yongHuMing=' + yongHuMing + '&miMa=' + miMa),
  geren: (id) => http.get('/yonghu/geren?id=' + id),
  xiugaiYongHu: (data) => http.put('/yonghu/xiugai', data),
  zhaohuiMiMa: (yongHuMing, shouJi, xueHao, xinMiMa) =>
    http.post('/yonghu/zhaohui?yongHuMing=' + encodeURIComponent(yongHuMing)
      + '&shouJi=' + encodeURIComponent(shouJi)
      + '&xueHao=' + encodeURIComponent(xueHao)
      + '&xinMiMa=' + encodeURIComponent(xinMiMa)),
  huoQuShouYeZhuangXiu: () => http.get('/peizhi/shouye'),
  baoCunShouYeZhuangXiu: (data) => http.post('/admin/peizhi/shouye', data),

  fabu: (data) => http.post('/shangpin/fabu', data),
  liebiao: (params) => http.get('/shangpin/liebiao', { params }),
  xiangqing: (id) => http.get('/shangpin/xiangqing?id=' + id),
  xiugaiShangPin: (data) => http.put('/shangpin/xiugai', data),
  xiajia: (id) => http.post('/shangpin/xiajia?id=' + id),
  woDeShangPin: (yongHuId) => http.get('/shangpin/wode?yongHuId=' + yongHuId),

  // 个性化推荐（基于物品的协同过滤）
  tuiJianLieBiao: (yongHuId, shu = 8) => http.get('/shangpin/tuijian', {
    params: { yongHuId: yongHuId || undefined, shu }
  }),
  xiangGuanShangPin: (id, shu = 4) => http.get('/shangpin/xiangguan', {
    params: { shangPinId: id, shu }
  }),

  fenLeiLieBiao: () => http.get('/fenlei/liebiao'),

  xiadan: (data) => http.post('/dingdan/xiadan', data),
  woDeDingDan: (yongHuId, jiaoSe) => http.get('/dingdan/wode?yongHuId=' + yongHuId + '&jiaoSe=' + jiaoSe),
  dingDanZhuangTai: (id, zhuangTai) => http.put('/dingdan/zhuangtai?id=' + id + '&zhuangTai=' + zhuangTai),

  // 管理员
  adminDengLu: (yongHuMing, miMa) => http.post('/admin/denglu?yongHuMing=' + yongHuMing + '&miMa=' + miMa),
  adminTongJi: () => http.get('/admin/tongji'),
  adminYongHu: (params) => http.get('/admin/yongHu', { params }),
  adminShanChuYongHu: (id) => http.post('/admin/yongHu/shanchu?id=' + id),
  adminShangPin: (params) => http.get('/admin/shangPin', { params }),
  adminShanChuShangPin: (id) => http.post('/admin/shangPin/shanchu?id=' + id),
  adminXiaJiaShangPin: (id) => http.post('/admin/shangPin/xiajia?id=' + id),
  adminDingDan: (params) => http.get('/admin/dingDan', { params }),
  adminDingDanZhuangTai: (id, zhuangTai) => http.put('/admin/dingDan/zhuangtai?id=' + id + '&zhuangTai=' + zhuangTai),
  adminFenLei: () => http.get('/admin/fenlei'),
  adminTianJiaFenLei: (mingCheng) => http.post('/admin/fenlei/tianjia', { mingCheng }),
  adminShanChuFenLei: (id) => http.post('/admin/fenlei/shanchu?id=' + id),

  // 站点公开：轮播图 / 公告
  zhanDianLunBo: () => http.get('/zhandian/lunbo'),
  zhanDianGongGao: () => http.get('/zhandian/gonggao'),

  // 管理员：轮播图
  adminLunBo: () => http.get('/admin/lunbo'),
  adminBaoCunLunBo: (data) => http.post('/admin/lunbo/baocun', data),
  adminShanChuLunBo: (id) => http.post('/admin/lunbo/shanchu?id=' + id),
  adminLunBoZhuangTai: (id, zhuangTai) => http.post('/admin/lunbo/zhuangtai?id=' + id + '&zhuangTai=' + zhuangTai),

  // 管理员：公告
  adminGongGao: () => http.get('/admin/gonggao'),
  adminBaoCunGongGao: (data) => http.post('/admin/gonggao/baocun', data),
  adminShanChuGongGao: (id) => http.post('/admin/gonggao/shanchu?id=' + id),
  adminGongGaoZhuangTai: (id, zhuangTai) => http.post('/admin/gonggao/zhuangtai?id=' + id + '&zhuangTai=' + zhuangTai),

  // 管理员：权限 / 商品 / 订单
  adminSheZhiGuanLiYuan: (id, shi) => http.post('/admin/yongHu/guanliyuan?id=' + id + '&shi=' + shi),
  adminBianJiYongHu: (canShu) => http.post('/admin/yongHu/bianji', null, { params: canShu }),
  shangChuanYongHuTouXiang: (biaoDan) => http.post('/upload/tuxiang?leiBie=touxiang', biaoDan, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id
    },
    timeout: 30000
  }),
  adminShangJiaShangPin: (id) => http.post('/admin/shangPin/shangjia?id=' + id),
  adminShanChuDingDan: (id) => http.post('/admin/dingDan/shanchu?id=' + id),

  // 管理员：发布商品（可代指定用户上架）
  adminFaBuShangPin: (data) => http.post('/admin/shangPin/fabu', data, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  // 管理员：编辑商品（标题/描述/价格/图片/分类）
  adminBianJiShangPin: (data) => http.post('/admin/shangPin/xiugai', data, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),

  // 图片上传（后台选图）
  shangChuanTuPian: (biaoDan) => http.post('/upload/tuxiang', biaoDan, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id
    },
    timeout: 30000
  }),
  shangChuanShangPinTu: (biaoDan) => http.post('/upload/tuxiang?leiBie=shangpin', biaoDan, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id
    },
    timeout: 30000
  }),

  // AI 智能导购：文字提问 / 识图推荐
  aiZhuangTai: () => http.get('/ai/zhuangtai'),
  aiWenDa: (data) => http.post('/ai/wenda', data, { timeout: 120000 }),

  // 管理员：AI 接口配置（可一次添加多个密钥）
  adminAiLieBiao: () => http.get('/admin/ai/liebiao', {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  adminAiBaoCun: (data) => http.post('/admin/ai/baocun', data, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  adminAiPiLiang: (data) => http.post('/admin/ai/piliang', data, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  adminAiZhuangTai: (id, zhuangTai) => http.post('/admin/ai/zhuangtai?id=' + id + '&zhuangTai=' + zhuangTai, null, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  adminAiShanChu: (id) => http.post('/admin/ai/shanchu?id=' + id, null, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  adminAiCeShi: (id) => http.post('/admin/ai/ceshi?id=' + id, null, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id },
    timeout: 120000
  }),

  // AI 助手形象（头像 / 名称 / 介绍）
  aiPeiZhi: () => http.get('/peizhi/ai'),
  adminAiPeiZhi: (data) => http.post('/admin/peizhi/ai', data, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  }),
  shangChuanAiTouXiang: (biaoDan) => http.post('/upload/tuxiang?leiBie=ai', biaoDan, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id
    },
    timeout: 30000
  }),

  // 个人头像（登录用户上传，YongHuId 由拦截器统一携带）
  shangChuanGeRenTouXiang: (biaoDan) => http.post('/upload/tuxiang?leiBie=geren', biaoDan, {
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 30000
  }),
  adminChongZhiTouXiang: (id) => http.post('/admin/yongHu/chongzhiTouXiang?id=' + id, null, {
    headers: { 'GuanLiYuanId': (JSON.parse(localStorage.getItem('guanLiYuan') || '{}') || {}).id }
  })
}

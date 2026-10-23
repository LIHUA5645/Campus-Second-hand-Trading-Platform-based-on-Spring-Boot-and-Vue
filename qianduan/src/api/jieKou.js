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
  return config
})

export default {
  zhuce: (data) => http.post('/yonghu/zhuce', data),
  denglu: (yongHuMing, miMa) => http.post('/yonghu/denglu?yongHuMing=' + yongHuMing + '&miMa=' + miMa),
  geren: (id) => http.get('/yonghu/geren?id=' + id),
  xiugaiYongHu: (data) => http.put('/yonghu/xiugai', data),

  fabu: (data) => http.post('/shangpin/fabu', data),
  liebiao: (params) => http.get('/shangpin/liebiao', { params }),
  xiangqing: (id) => http.get('/shangpin/xiangqing?id=' + id),
  xiugaiShangPin: (data) => http.put('/shangpin/xiugai', data),
  xiajia: (id) => http.post('/shangpin/xiajia?id=' + id),
  woDeShangPin: (yongHuId) => http.get('/shangpin/wode?yongHuId=' + yongHuId),

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
  adminShangJiaShangPin: (id) => http.post('/admin/shangPin/shangjia?id=' + id),
  adminShanChuDingDan: (id) => http.post('/admin/dingDan/shanchu?id=' + id)
}

> 版权作者（软著）：添砖 Java QQ：2047419378
> 项目所有者：肖沐樑 QQ：3387432690

# 校园二手交易平台（Spring Boot + Vue）

大家好，我是**肖沐樑**，这是我自己动手做的基于 Spring Boot + Vue 的**校园二手交易平台**，
也是一套开源的毕业设计模板。前后端我已经完整打通，克隆下来清掉水印就能跑，方便大家二次开发或做毕设演示。

## 关于作者与版权

- 我是本项目的所有者：**肖沐樑（QQ：3387432690）**。
- 本项目的软件著作权登记在 **添砖 Java（QQ：2047419378）** 名下。
- 技术交流 / 添砖 Java：QQ 2047419378 ｜ 商务合作：19877380122。

## 技术栈

| 层 | 技术 | 版本 |
|----|------|------|
| 后端 | Spring Boot | 3.4.5 |
| 后端 | Java | 21（JDK 21+ 均可编译运行，我用 JDK 25 也跑通了） |
| 后端 | 持久化 | Spring Data JPA + MySQL |
| 前端 | Vue | 3.4 |
| 前端 | Vite | 6 |
| 前端 | Element Plus | 2.6 |
| 命名 | 变量 / 包名 | 全部使用拼音 |

## 目录结构

- `houduan/`：Spring Boot 后端（Maven 工程）
  - `src/main/java/com/ershoujiaoyi/`：启动类、controller、entity、repository、config
  - `src/main/resources/application.yml`：数据库连接配置
- `qianduan/`：Vue3 前端（Vite 工程）
  - `src/views/`：登录、首页、商品详情、发布、订单、个人中心
  - `src/components/DaShang.vue`：右下角悬浮打赏
  - `src/api/jieKou.js`：后端接口封装
  - `public/images/`：两张收款码（我自己的真实收款码）
- `images/`：两张收款码原图（和前端引用一致）
- `shui_yin.py`：零宽字符隐形水印工具（打水印 / 校验 / 清除）
- `开发计划.md` / `修复记录文档.md`：开发过程与改动留痕

## 环境要求

- JDK 21 或更高（我用 JDK 25 验证过）
- Node.js 18 或更高（我用 Node 24 验证 `npm run build` 通过）
- Maven 3.6+
- MySQL 8（本机用用户 `root`、密码 `sjk123456`，库名 `ershoupingtai`，已开启 `createDatabaseIfNotExist` 自动建库）

## 怎么跑起来（重要）

我在源码里按《开源规则说明》打了**零宽字符隐形水印**（署名：添砖 Java）。
水印会改变代码里的标识符，所以**没清除水印的源码不能直接编译运行**——这是为了防止代码外流被白嫖。
你自己要跑的话，先清掉水印（仓库根目录自带 `shui_yin.py`）：

```bash
python shui_yin.py qing houduan/src
python shui_yin.py qing qianduan/src
```

然后：

1. 后端：进 `houduan`，确认本机 MySQL 已启动（用户 root、密码 sjk123456），
   执行 `mvn spring-boot:run`（默认 8080，首次启动自动建库并写入默认分类）。
2. 前端：进 `qianduan`，`npm install` 后 `npm run dev`（默认 5173，已配 `/api` 代理到后端 8080）。

> 重新对外分发前，可再用 `python shui_yin.py jia houduan/src` 和 `jia qianduan/src` 把水印打回去。

## 功能一览

- 用户：注册、登录、查看个人信息
- 商品：发布、列表（关键词 / 分类 / 分页）、详情、编辑、下架、我的商品
- 分类：首次启动自动写入默认分类、分类列表
- 订单：下单、我的订单（我买的 / 我卖出的）、状态流转（待发货 → 待收货 → 已完成 / 取消）

## 如果对你有帮助

- 欢迎在 GitHub / Gitee 给个 Star ⭐
- 也欢迎打赏支持我，请喝杯奶茶：

### 微信收款码
![微信收款码](images/zhifu_weixin.jpg)

### 支付宝收款码
![支付宝收款码](images/zhifu_zhifubao.jpg)

> 版权作者（软著）：添砖 Java QQ：2047419378 ｜ 项目所有者：肖沐樑 QQ：3387432690

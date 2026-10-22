<div align="center">

<img src="docs/logo.png" width="118" alt="校园二手交易平台 Logo" />

# 校园二手交易平台

**让校园里的闲置，流动起来**

*基于 Spring Boot 3 + Vue 3 的校园二手交易平台 · 开源毕业设计模板*

<br/>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?logo=springboot&logoColor=white&style=flat-square)
![Vue](https://img.shields.io/badge/Vue-3.4-42B883?logo=vuedotjs&logoColor=white&style=flat-square)
![Vite](https://img.shields.io/badge/Vite-6-646CFF?logo=vite&logoColor=white&style=flat-square)
![Element Plus](https://img.shields.io/badge/Element%20Plus-2.6-409EFF?logo=element&logoColor=white&style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white&style=flat-square)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white&style=flat-square)
![License](https://img.shields.io/badge/License-MIT-2ea44f?style=flat-square)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen?style=flat-square)
[![Star on Gitee](https://gitee.com/zhang-san-zhangshan/esjypt/badge/star.svg?theme=dark)](https://gitee.com/zhang-san-zhangshan/esjypt)

</div>

---

## 💡 为什么做它

校园里每年都有大量教材、数码、生活用品在角落吃灰，而另一边又有同学正需要它们。
于是我把它做成了一个**真正能跑起来的完整项目**：登录注册、发布商品、分类检索、下单交易、
订单状态流转，前后端全部打通，可以直接用来做毕设演示，也可以作为你二次开发的地基。

## ✨ 项目亮点

| 能力 | 说明 |
| --- | --- |
| 🔐 账户体系 | 注册 / 登录 / 个人资料，登录态前端持久化 |
| 🛍️ 商品市场 | 发布、关键词搜索、分类筛选、分页浏览、详情、编辑、下架 |
| 🏷️ 分类管理 | 首次启动自动写入默认分类，后续可自由扩展 |
| 📦 订单交易 | 一键下单、我的订单（我买的 / 我卖出的）、状态机流转 |
| 🎨 精致 UI | Element Plus 组件 + 卡片式布局 + 右下角悬浮打赏 |
| 🧬 工程规范 | 全拼音命名、JPA 自动建表、开箱即跑 |

## 🚀 快速开始

> ✅ 源码**不含任何隐形水印**，克隆下来即可直接编译运行，无需任何预处理步骤。

### ① 启动后端

```bash
cd houduan
mvn spring-boot:run
# 默认 http://localhost:8080，首次启动自动建库并写入默认分类
```

### ② 启动前端

```bash
cd qianduan
npm install
npm run dev
# 默认 http://localhost:5173，已配置 /api 代理到后端 8080
```

## 🗄️ 技术栈

| 层 | 技术 | 版本 |
| --- | --- | --- |
| 后端框架 | Spring Boot | 3.4.5 |
| 运行环境 | Java | 21+（JDK 25 亦可） |
| 持久化 | Spring Data JPA + MySQL | 8 |
| 前端框架 | Vue | 3.4 |
| 构建工具 | Vite | 6 |
| UI 组件库 | Element Plus | 2.6 |

## 📦 目录结构

```text
.
├── houduan/                 # Spring Boot 后端
│   └── src/main/
│       ├── java/com/ershoujiaoyi/
│       │   ├── controller/  # 用户 / 商品 / 分类 / 订单
│       │   ├── entity/      # YongHu / ShangPin / FenLei / DingDan
│       │   ├── repository/  # JPA 仓库
│       │   └── config/      # 跨域 / 数据初始化
│       └── resources/application.yml
├── qianduan/                # Vue3 前端
│   ├── src/views/           # 登录 / 首页 / 详情 / 发布 / 订单 / 我的
│   ├── src/components/      # 悬浮打赏组件
│   └── public/images/       # 收款码
├── images/                  # 收款码原图
└── docs/logo.png            # 项目 Logo
```

## 🧩 业务流程

```text
用户 ─▶ 登录/注册 ─▶ 发布商品 ─▶ 浏览/搜索 ─▶ 下单 ─▶ 订单状态流转
                                     │
                                     └─▶ 我的订单 / 我的商品 / 下架
```

## 🖼️ 界面预览

> 运行后即可看到：首页商品瀑布流、分类导航、商品详情、下单弹窗、订单中心、个人中心，
> 右下角还有悬浮打赏入口。

## ☕ 赞助作者

如果这个项目帮到了你，欢迎请我喝杯奶茶 👇

| 微信 | 支付宝 |
| :---: | :---: |
| <img src="images/zhifu_weixin.jpg" width="160" /> | <img src="images/zhifu_zhifubao.jpg" width="160" /> |

如果觉得这个项目帮到了你，欢迎点个 ⭐ Star 支持一下：

👉 [在 Gitee 上 Star](https://gitee.com/zhang-san-zhangshan/esjypt)

> 如果你也在 GitHub 上托管了本项目，把仓库地址发我，我顺手把 GitHub Star 链接也加上。

## 📄 开源许可

本项目基于 **MIT License** 开源，可自由用于学习与二次开发。

> 项目所有者：**肖沐樑** QQ：3387432690
> 版权作者（软著）：**添砖 Java** QQ：2047419378

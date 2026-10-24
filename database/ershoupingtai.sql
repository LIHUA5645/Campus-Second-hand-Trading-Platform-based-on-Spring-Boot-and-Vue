-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: ershoupingtai
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ai_pei_zhi`
--

DROP TABLE IF EXISTS `ai_pei_zhi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_pei_zhi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bei_zhu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chuang_jian_shi_jian` datetime(6) DEFAULT NULL,
  `di_zhi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fu_wu_shang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mi_yao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ming_cheng` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mo_xing` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pai_xu` int DEFAULT NULL,
  `shi_tu` bit(1) DEFAULT NULL,
  `zhuang_tai` int DEFAULT NULL,
  `zui_jin_cuo_wu` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zui_jin_shi_yong_shi_jian` datetime(6) DEFAULT NULL,
  `zui_jin_zhuang_tai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_pei_zhi`
--

LOCK TABLES `ai_pei_zhi` WRITE;
/*!40000 ALTER TABLE `ai_pei_zhi` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_pei_zhi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ding_dan`
--

DROP TABLE IF EXISTS `ding_dan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ding_dan` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `di_zhi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `jia_ge` double DEFAULT NULL,
  `liu_yan` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mai_jia_id` bigint DEFAULT NULL,
  `mai_jia_ming` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mai_jia_shou_ji` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shang_pin_biao_ti` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shang_pin_id` bigint DEFAULT NULL,
  `tu_pian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `xia_dan_shi_jian` datetime(6) DEFAULT NULL,
  `zhuang_tai` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ding_dan`
--

LOCK TABLES `ding_dan` WRITE;
/*!40000 ALTER TABLE `ding_dan` DISABLE KEYS */;
/*!40000 ALTER TABLE `ding_dan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fen_lei`
--

DROP TABLE IF EXISTS `fen_lei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fen_lei` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ming_cheng` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fen_lei`
--

LOCK TABLES `fen_lei` WRITE;
/*!40000 ALTER TABLE `fen_lei` DISABLE KEYS */;
INSERT INTO `fen_lei` VALUES (1,'教材书籍'),(2,'数码电子'),(3,'生活用品'),(4,'服饰鞋包'),(5,'运动器材'),(6,'寝室好物'),(7,'其他');
/*!40000 ALTER TABLE `fen_lei` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gong_gao`
--

DROP TABLE IF EXISTS `gong_gao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gong_gao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `biao_ti` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chuang_jian_shi_jian` datetime(6) DEFAULT NULL,
  `nei_rong` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zhuang_tai` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gong_gao`
--

LOCK TABLES `gong_gao` WRITE;
/*!40000 ALTER TABLE `gong_gao` DISABLE KEYS */;
INSERT INTO `gong_gao` VALUES (1,'平台使用须知','2026-10-23 10:27:34.835000','发布闲置请如实描述成色与瑕疵，交易建议在校内当面验货；如遇纠纷可联系管理员协助处理。',0),(2,'开学季活动','2026-10-23 10:27:34.840000','开学季发布闲置免手续费，欢迎同学们把用不上的好物分享给需要的人～',0),(3,'平台规则','2026-10-24 00:59:47.343000','还没写',0);
/*!40000 ALTER TABLE `gong_gao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lun_bo_tu`
--

DROP TABLE IF EXISTS `lun_bo_tu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lun_bo_tu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `biao_ti` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chuang_jian_shi_jian` datetime(6) DEFAULT NULL,
  `fu_biao_ti` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lian_jie` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pai_xu` int DEFAULT NULL,
  `tu_pian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zhuang_tai` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lun_bo_tu`
--

LOCK TABLES `lun_bo_tu` WRITE;
/*!40000 ALTER TABLE `lun_bo_tu` DISABLE KEYS */;
INSERT INTO `lun_bo_tu` VALUES (1,'校园闲置 · 安心流转','2026-10-23 10:27:34.783000','教材 / 数码 / 好物，低价淘到学长学姐的宝贝','/',1,'/images/lunbo/banner1.jpg',0),(2,'毕业季清仓特惠','2026-10-23 10:27:34.819000','带走需要的，留下回忆，闲置好物统统低价转','/',2,'/images/lunbo/banner2.jpg',0),(3,'数码好物直降','2026-10-23 10:27:34.824000','手机 / 平板 / 耳机，成色好、价格香','/',3,'/images/lunbo/banner3.jpg',0);
/*!40000 ALTER TABLE `lun_bo_tu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shang_pin`
--

DROP TABLE IF EXISTS `shang_pin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shang_pin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `biao_ti` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fabu_shi_jian` datetime(6) DEFAULT NULL,
  `fen_lei_id` bigint DEFAULT NULL,
  `jia_ge` double DEFAULT NULL,
  `miao_shu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tu_pian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `yong_hu_id` bigint DEFAULT NULL,
  `yong_hu_ming` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zhuang_tai` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shang_pin`
--

LOCK TABLES `shang_pin` WRITE;
/*!40000 ALTER TABLE `shang_pin` DISABLE KEYS */;
INSERT INTO `shang_pin` VALUES (1,'曲面屏安卓手机 蓝紫渐变 无磕碰','2026-10-24 00:10:08.751000',2,8909,'去年双十一买的，一直带壳贴膜，屏幕无划痕无暗病，功能全部正常。换 iPhone 了所以出掉，屏是真的漂亮，自拍看着显白。','/images/shangping/shouji1.jpg',2,'系统管理员',0),(2,'黑色旗舰机 后置三摄 256G 大内存','2026-10-24 00:10:08.774000',2,1199,'8+256 版本，照片、课件随便存。电池正常使用一天，学生党拍作业、开黑都不卡。同城可取，当面验机更放心。','/images/shangping/shouji2.jpg',2,'系统管理员',0),(3,'粉色小屏手机 女生自用 拍照好看','2026-10-24 00:10:08.789000',2,799,'女生自用一手机器，外观几乎没有使用痕迹，配了粉色保护壳一起送。屏幕比较小，单手打字很舒服，适合当备用机。','/images/shangping/shouji3.jpg',2,'系统管理员',0),(4,'折叠屏手机 展开几乎无痕 12+256','2026-10-24 00:10:08.806000',2,2999,'开合顺滑，折痕在正面几乎看不出来，铰链没有异响。看 PDF、做表格一屏顶两屏，毕业设计期间帮了大忙，现在实习补贴到了换新。','/images/shangping/shouji4.jpg',2,'系统管理员',0),(5,'旧手机四台打包 金/银/蓝/黑','2026-10-24 00:10:08.818000',2,199,'抽屉里翻出来的旧机，都是能开机的老型号，屏幕有使用痕迹。适合做开发测试机、闹钟机、车机导航，打包一起出不拆卖。','/images/shangping/shouji5.jpg',2,'系统管理员',0),(6,'黑色全面屏手机 64G 全原装无拆修','2026-10-24 00:10:08.830000',2,1099,'全原装无拆无修，人脸识别正常，充电口不松。买新手机用不上两个号了，诚心要价格可以小刀，校内当面验机。','/images/shangping/shouji6.jpg',2,'系统管理员',0),(7,'透明探索版手机 8+256G 双面玻璃','2026-10-24 00:10:08.841000',2,1499,'透明后盖颜值党首选，戴透明壳还能看清内部结构。跑分日常够用，游戏画质中档稳定。带原装充电头和两根数据线。','/images/shangping/shouji7.jpg',2,'系统管理员',0),(8,'电竞游戏手机 机甲风散热 12+256G','2026-10-24 00:10:08.851000',2,1399,'自带散热风扇和肩键，原神、王者长时间不烫手，帧率很稳。毕业备战考研，收心了，连游戏手柄一起出。','/images/shangping/shouji8.jpg',2,'系统管理员',0),(9,'老人机 + 备用安卓机 两台一起出','2026-10-24 00:10:08.862000',2,149,'一台大字老人机，声音洪亮，适合给家里长辈；另一台安卓备用机 WiFi 正常。两台一起出，每样配件都在。','/images/shangping/shouji9.jpg',2,'系统管理员',0),(10,'全面屏智能手机 银色 6+128G','2026-10-24 00:10:08.871000',2,699,'日常用非常流畅，看网课、刷视频没问题。左下角边框有轻微磨损，不影响使用，价格也相应便宜。','/images/shangping/shouji10.jpg',2,'系统管理员',0),(11,'14 英寸轻薄本 i5 / 16G / 512G 深色机身','2026-10-24 00:10:08.882000',2,2299,'上专业课做 PPT、剪 vlog 都够用，带得动 PR 剪辑。电池健康 92%，键盘无油光，附赠内胆包和鼠标。','/images/shangping/diannao1.jpg',2,'系统管理员',0),(12,'15.6 英寸办公本 i5 / 16G / 1T 银色','2026-10-24 00:10:08.892000',2,1999,'屏幕大，写论文、看文献很舒服，机械硬盘 1T 能塞下所有资料。风扇清过灰，温度正常，校内当面验机。','/images/shangping/diannao2.jpg',2,'系统管理员',0),(13,'14 英寸锐龙本 R7 / 16G / 512G 深空灰','2026-10-24 00:10:08.905000',2,2599,'处理器性能强，编译、跑虚拟机都不虚。接口齐全，不用随身带拓展坞。实习配了新机器，这台就转给学弟学妹。','/images/shangping/diannao3.jpg',2,'系统管理员',0),(14,'13.3 英寸轻薄本 i5 / 8G / 256G 银色','2026-10-24 00:10:08.916000',2,1699,'重量轻，背去图书馆一天不累。适合文科类专业听课、写文档，玩大型游戏不太够用。充电器齐全。','/images/shangping/diannao4.jpg',2,'系统管理员',0),(15,'入门级单反相机 套机带 18-55 镜头','2026-10-24 00:10:08.923000',2,1699,'快门数不到一万，成色很新，拍社团活动、毕业照都够用。送 32G 卡、备用电池和清洁套装。','/images/shangping/xiangji1.jpg',3,'测试一号',0),(16,'复古数码相机 银黑机身 配件齐全','2026-10-24 00:10:08.932000',2,899,'外观复古有质感，出片偏胶片味，朋友圈点赞神器。支持 WiFi 传手机，配了两张存储卡。','/images/shangping/xiangji2.jpg',4,'测试二号',0),(17,'卡片数码相机 5 倍变焦 出门随手拍','2026-10-24 00:10:08.942000',2,599,'巴掌大小，塞口袋就走，旅游、演唱会都能带。屏幕没有划痕，配充电线和相机包。','/images/shangping/xiangji3.jpg',3,'测试一号',0),(18,'一次成像相机 带两盒相纸','2026-10-24 00:10:08.951000',2,499,'毕业季拍照刚需，按下出片很有仪式感。机身全新几乎没用过，含两盒原装相纸，介意者勿拍。','/images/shangping/xiangji4.jpg',4,'测试二号',0),(19,'《高等数学》同济第七版 上下册 笔记干净','2026-10-24 00:10:08.960000',1,25,'必修教材，配课程 PPT 和学习笔记，封面有轻微折痕。学弟学妹需要自取，图书馆门口交易。','/images/shangping/shuben1.jpg',3,'测试一号',0),(20,'考研英语词汇 全新未拆封','2026-10-24 00:10:08.968000',1,35,'买多了用不上，塑封完整，单词按单元分类好背。比书店便宜一半，考研的同学抓紧。','/images/shangping/shuben2.jpg',4,'测试二号',0),(21,'《计算机网络》教材 + 习题册','2026-10-24 00:10:08.976000',1,30,'专业课正版教材，附赠课后答案和期末复习提纲，划了重点，考试周复习很好用。','/images/shangping/shuben3.jpg',3,'测试一号',0),(22,'课外读物四本打包 小说散文合集','2026-10-24 00:10:08.985000',1,18,'宿舍书架空了，四本一起出，有小说也有散文，睡前翻着看很舒服，单本不拆。','/images/shangping/shuben4.jpg',4,'测试二号',0),(23,'陶瓷马克杯 宿舍办公通用','2026-10-24 00:10:08.993000',3,9,'杯口圆润，手握舒服，泡咖啡冲牛奶都合适。闲置一个，洗净消毒过的，直接能用。','/images/shangping/beizi1.jpg',3,'测试一号',0),(24,'不锈钢保温杯 500ml 保冷保热','2026-10-24 00:10:09.002000',3,35,'冬天装热水六小时还是温的，夏天装冰饮不结露。在家里又买一个同款，这个就闲置了。','/images/shangping/beizi2.jpg',4,'测试二号',0),(25,'玻璃随行杯 带硅胶保护套','2026-10-24 00:10:09.011000',3,22,'带硅胶套防烫防摔，透明玻璃看着干净。杯盖密封不漏，带去自习室很方便。','/images/shangping/beizi3.jpg',3,'测试一号',0),(26,'带吸管水杯 夏天喝冰饮必备','2026-10-24 00:10:09.020000',3,15,'大容量一杯喝半天，配粗吸管，喝酸奶、果茶都没问题。洗干净了，介意外观的勿拍。','/images/shangping/beizi4.jpg',4,'测试二号',0),(27,'宿舍小功率吹风机 不断电','2026-10-24 00:10:09.029000',3,45,'宿舍限电也能用，不会跳闸，风力足够吹干中长发。暑假带回家了，开学用不上了。','/images/shangping/chuifengji1.jpg',3,'测试一号',0),(28,'负离子吹风机 三档风温 低噪','2026-10-24 00:10:09.038000',3,89,'吹完头发不毛躁，三档可调，夜里用也不吵室友。原价两百多买的，诚心要可以小刀。','/images/shangping/chuifengji2.jpg',4,'测试二号',0),(29,'折叠旅行吹风机 便于收纳','2026-10-24 00:10:09.047000',3,39,'折叠后巴掌大，出门旅行、实习租房带着不占行李。风嘴可拆卸，配件齐全。','/images/shangping/chuifengji3.jpg',3,'测试一号',0),(30,'家用吹风机 带集风嘴 冷热风切换','2026-10-24 00:10:09.057000',3,55,'冷热风一键切换，夏天用冷风不流汗。线长一米七，宿舍上铺也够得着插座。','/images/shangping/chuifengji4.jpg',4,'测试二号',0),(31,'闲置女包 小号款 通勤百搭','2026-10-24 00:10:09.066000',4,69,'背了不到一个学期，内里干净没污渍，拉链顺滑。放手机、口红、学生证刚刚好，配春夏穿搭很清爽。','/images/shangping/bao1.jpg',3,'测试一号',0),(32,'闲置女包 手提款 几乎全新','2026-10-24 00:10:09.075000',4,88,'朋友送的礼物，风格不太适合我，标签都还在。版型挺括不会塌，毕业答辩、面试都能背。','/images/shangping/bao2.jpg',4,'测试二号',0),(33,'大容量通勤包 上课也合适','2026-10-24 00:10:09.083000',4,99,'能装下 14 寸本子和一沓教材，内部分层多，找东西不翻箱倒柜。肩带加宽不勒肩，实物比图片更有质感。','/images/shangping/bao3.jpg',3,'测试一号',0),(34,'冲锋衣 防风防水 户外徒步外套','2026-10-24 00:10:09.093000',4,159,'周末爬山越野 club 买的，只穿过两次，防水拉链全好使。170 码，里面加抓绒衣冬天照样扛。','/images/shangping/chongfengyi1.jpg',4,'测试二号',0),(35,'改良刺绣旗袍 文艺演出穿过一次','2026-10-24 00:10:09.101000',4,129,'社团演出穿过一次，干洗后收起来了，刺绣工整没脱线。S 码，拍照非常出片，毕业照、晚会都合适。','/images/shangping/qipao1.jpg',3,'测试一号',0),(36,'格子衬衫 男生百搭款 M 码','2026-10-24 00:10:09.109000',4,45,'秋天的实用款，单穿内搭都行，洗过几次没起球。男生柜子里永远缺一件格子衬衫，便宜出。','/images/shangping/chenshan1.jpg',4,'测试二号',0),(37,'红色气质连衣裙 S 码','2026-10-24 00:10:09.117000',4,89,'毕业晚会买来拍照的，红色特别上镜，只穿了一次。裙摆有内衬不透，170 以下都好驾驭。','/images/shangping/lianyiqun1.jpg',3,'测试一号',0),(38,'学院风毛衣 M 码 不起球','2026-10-24 00:10:09.125000',4,59,'秋冬基础款，配牛仔裤、百褶裙都好看。手洗过两次，没有起球变形，衣柜放不下了。','/images/shangping/maoyi1.jpg',4,'测试二号',0),(39,'冬季加厚棉服 L 码 保暖不钻风','2026-10-24 00:10:09.134000',4,129,'在北方读书必备，零下十几度也没觉得冷，帽子可拆。寒假南方佬用不上，留给需要的同学。','/images/shangping/mianyi1.jpg',3,'测试一号',0),(40,'牛仔外套 经典水洗蓝 M 码','2026-10-24 00:10:09.142000',4,99,'百搭神衣，四季都能穿，越穿越贴身。纽扣完好，袖口没有磨白，搭配白 T 就很清爽。','/images/shangping/niuzaijia1.jpg',4,'测试二号',0),(41,'机车风皮夹克 复古做旧 L 码','2026-10-24 00:10:09.150000',4,199,'拍照很有型的一件外套，皮质柔软不硬挺，内衬干净。男生女生都能穿，冬天配高领毛衣绝配。','/images/shangping/pijiake1.jpg',3,'测试一号',0),(42,'西装外套一套 答辩面试穿 170 码','2026-10-24 00:10:09.160000',4,168,'面试 Moss 买的，毕了以后基本闲置。版型修身，配了同色裤子，答辩、面试、实习报道都穿得上。','/images/shangping/xizhuang1.jpg',4,'测试二号',0),(43,'925 银项链 简约款 带原盒','2026-10-24 00:10:09.169000',4,69,'过敏体质也能戴，没有发黑，原装盒子还在。日常百搭，配毛衣尤其显气质。','/images/shangping/xianglian1.jpg',3,'测试一号',0),(44,'珍珠项链 复古百搭','2026-10-24 00:10:09.178000',4,99,'珍珠光泽很好，没有划痕，参加毕业典礼刚好用得上。送人也很有面子，自带礼盒。','/images/shangping/xianglian2.jpg',4,'测试二号',0),(45,'钛钢项链 不掉色 男女都能戴','2026-10-24 00:10:09.187000',4,39,'洗澡也不用摘，戴了一年还是亮的。简单款式不出错，夏天配白 T 很好看。','/images/shangping/xianglian3.jpg',3,'测试一号',0),(46,'字母定制项链 生日礼物首选','2026-10-24 00:10:09.195000',4,79,'可以刻名字或字母，去年生日收到的礼物。全新没戴过，转给需要的同学，价格随便刀。','/images/shangping/xianglian4.jpg',4,'测试二号',0),(47,'运动跑步鞋 42 码 鞋底磨损轻微','2026-10-24 00:10:09.205000',4,129,'体测训练穿的，鞋面完好，鞋底只有轻微磨损。跑鞋别买太贵，学生党练体育课刚好。','/images/shangping/xie1.jpg',3,'测试一号',0),(48,'白色板鞋 41 码 百搭通勤','2026-10-24 00:10:09.213000',4,99,'洗得很干净，几乎看不出穿过。配牛仔裤、休闲裤都不出错，四季能穿的基础款。','/images/shangping/xie2.jpg',4,'测试二号',0),(49,'马丁靴 38 码 秋冬必备','2026-10-24 00:10:09.222000',4,149,'秋冬搭配神器，皮面有自然褶皱更有味道。鞋底很厚，下雨天踩水不心疼，已做清洁保养。','/images/shangping/xie3.jpg',3,'测试一号',0),(50,'高跟单鞋 37 码 答辩面试穿','2026-10-24 00:10:09.230000',4,79,'正式场合穿过两次，鞋跟稳不打滑。放宿舍占地方，需要的姐妹拿走，校园自提。','/images/shangping/xie4.jpg',4,'测试二号',0),(51,'纯棉短袖 T 恤 L 码','2026-10-24 00:10:09.243000',4,39,'社团活动发的文化衫，纯棉透气，夏天穿着不闷。全新没剪吊牌，oversize 版型。','/images/shangping/txu1.jpg',3,'测试一号',0),(52,'乒乓球拍一支 + 训练球三只','2026-10-24 00:10:09.251000',5,25,'体育课自己买的，用了一学期，胶皮还很有粘性。宿舍楼下打两把非常方便，附带球三只。','/images/shangping/pingpangqiu1.jpg',4,'测试二号',0),(53,'乒乓球训练球一盒','2026-10-24 00:10:09.260000',5,12,'新材料 40+ 有缝球，弹性正常不瘪。练发球、陪练都够用，丢了也不心疼。','/images/shangping/pingpangqiu2.jpg',3,'测试一号',0),(54,'碳纤维羽毛球拍 已穿线 4U','2026-10-24 00:10:09.269000',5,120,'轻量化球拍，女生也能轻松挥动，刚换的新手胶。社团训练退役装备，手感依然在线。','/images/shangping/yumaoqiupai1.jpg',4,'测试二号',0),(55,'羽毛球拍双拍套装 带球包','2026-10-24 00:10:09.276000',5,158,'和室友合买的一套，现在都考研没时间打。含两支拍、球包、手胶，约球直接背着走。','/images/shangping/yumaoqiupai2.jpg',3,'测试一号',0),(56,'5 号足球 学院联赛用球','2026-10-24 00:10:09.286000',5,45,'联赛用过的备球，表皮磨损但不漏气，踢野场练球很合适。便宜出，别指望全新成色。','/images/shangping/zuqiu1.jpg',4,'测试二号',0),(57,'足球 + 打气筒 + 收纳网兜','2026-10-24 00:10:09.298000',5,65,'一套配齐，拿到球场就能踢。球的气密性很好，放了一个月都没瘪，入门练球性价比之选。','/images/shangping/zuqiu2.jpg',3,'测试一号',0),(58,'宿舍遮光窗帘 免打孔安装','2026-10-24 00:10:09.306000',6,49,'免打孔不伤墙，宿管阿姨也不说。遮光度很高，午休能睡踏实，毕业季搬走方便拆卸。','/images/shangping/chuanglian1.jpg',4,'测试二号',0),(59,'一体式床帘 遮光 + 蚊帐','2026-10-24 00:10:09.315000',6,89,'上下铺都能装，遮光 + 防蚊二合一，自己的小空间立马有了。夏天宿舍神器，全部配件都在。','/images/shangping/chuanglian2.jpg',3,'测试一号',0),(60,'北欧风布艺窗帘 带同色绑带','2026-10-24 00:10:09.324000',6,45,'租房到期带不走，质量很好，垂感自然。家里用了两年，颜色没有明显褪色。','/images/shangping/chuanglian3.jpg',4,'测试二号',0),(61,'笔记本增高支架 铝合金散热款','2026-10-24 00:10:09.333000',6,59,'铝合金实心很稳，不晃不塌，抬高之后脖子轻松多了。散热镂空设计，剪辑渲染也不发烫。','/images/shangping/diannaozhijia1.jpg',3,'测试一号',0),(62,'桌面电脑托架 高度可调可折叠','2026-10-24 00:10:09.341000',6,39,'六档调节，站着办公也能用，收起来不占桌面。实习买了升降桌，这个就用不上了。','/images/shangping/diannaozhijia2.jpg',4,'测试二号',0),(63,'床上懒人电脑桌 带小风扇','2026-10-24 00:10:09.349000',6,79,'冬天赖床写作业的快乐源泉，桌面够放 15.6 寸本 + 水杯。侧面带小灯和 USB 风扇，配件齐全。','/images/shangping/diannaozhijia3.jpg',3,'测试一号',0),(64,'宿舍折叠方桌 免安装','2026-10-24 00:10:09.357000',6,45,'打开就能用，收起来塞床底不占地方。吃外卖、复习刷题都靠它，桌面防水好擦。','/images/shangping/zhuozi1.jpg',4,'测试二号',0),(65,'床上折叠小书桌 带杯托','2026-10-24 00:10:09.366000',6,69,'上铺也能用，合起来放墙角。杯托和小抽屉很实用，期末在床上复习的标配。','/images/shangping/zhuozi2.jpg',3,'测试一号',0),(66,'冰红茶整箱 12 瓶 宿舍囤货','2026-10-24 00:10:09.374000',7,35,'整箱囤多了喝不完，保质期到明年夏天。运动后、打球回来来一瓶很爽，校内自提方便搬运。','/images/shangping/binghongcha1.jpg',4,'测试二号',0),(67,'柠檬味冰红茶一箱 临期特价','2026-10-24 00:10:09.383000',7,25,'保质期还有一个月，价格已经打到骨折。请在保质期内喝完，介意日期的同学请勿下单。','/images/shangping/binghongcha2.jpg',3,'测试一号',0),(68,'无糖冰红茶两箱 24 瓶','2026-10-24 00:10:09.391000',7,55,'控糖星人的最爱，0 糖版本。两箱一起出，价格和食堂买水差不多，宿舍几人拼更划算。','/images/shangping/binghongcha3.jpg',4,'测试二号',0),(69,'冰红茶 + 绿茶混合箱 24 瓶','2026-10-24 00:10:09.399000',7,45,'两种口味各半，宿舍四个人分刚好。包装完好没有破损，实习搬校区带不走。','/images/shangping/binghongcha4.jpg',3,'测试一号',0),(70,'内蒙古牛肉干 250g 香辣味 未拆封','2026-10-24 00:10:09.408000',7,49,'亲戚寄来的特产，一箱吃不完。独立小包装，晚自习加餐、追剧都很合适，日期新鲜。','/images/shangping/niurougan1.jpg',4,'测试二号',0),(71,'手撕牛肉干 500g 大包装','2026-10-24 00:10:09.417000',7,79,'真肉手撕纹理清晰，越嚼越香。一大袋吃到爽，宿舍聚餐分着吃刚好，包装完好无漏气。','/images/shangping/niurougan2.jpg',3,'测试一号',0),(72,'风干牛肉干两袋 原味 + 孜然','2026-10-24 00:10:09.425000',7,45,'两种口味各一袋，孜然味特别上头。低脂高蛋白，健身、减脂期解馋不错，生产日期很新。','/images/shangping/niurougan3.jpg',4,'测试二号',0),(73,'牛肉干礼盒装 送礼自用都合适','2026-10-24 00:10:09.433000',7,99,'包装很体面，教师节送导师也没压力。里面是四种口味小包装，搭配得很用心。','/images/shangping/niurougan4.jpg',3,'测试一号',0),(74,'小罐茶礼盒 20 罐 送礼有面','2026-10-24 00:10:09.442000',7,199,'过年收到的礼物，家里不喝茶。铝罐密封好，每罐都是一次的量，送长辈、辅导员很有面子。','/images/shangping/xiaoguancha1.jpg',4,'测试二号',0),(75,'铁观音小罐茶 10 罐','2026-10-24 00:10:09.451000',7,69,'清香型铁观音，回甘明显，办公室泡一杯很提神。十罐装体积小，抽屉一塞不占地。','/images/shangping/xiaoguancha2.jpg',3,'测试一号',0),(76,'金骏眉小罐茶 30g 自饮优选','2026-10-24 00:10:09.458000',7,89,'茶汤橙亮、口感顺滑，冬天热泡尤其舒服。一小盒慢慢喝，比喝奶茶健康多了。','/images/shangping/xiaoguancha3.jpg',4,'测试二号',0),(77,'小罐茶 + 简易茶具组合','2026-10-24 00:10:09.468000',7,159,'含茶滤壶和两个小杯，泡茶新手也能上手。茶具几乎全新，只泡过两三次，收纳方便。','/images/shangping/xiaoguancha4.jpg',3,'测试一号',0);
/*!40000 ALTER TABLE `shang_pin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xi_tong_peizhi`
--

DROP TABLE IF EXISTS `xi_tong_peizhi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xi_tong_peizhi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ai_jie_shao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ai_ming_cheng` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ai_tou_xiang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_biao_ti1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_biao_ti2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_gao_liang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_hui_biao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_miao_shu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_tong_ji1biao_qian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_tong_ji2biao_qian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_tong_ji3biao_qian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zx_tong_ji3zhi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xi_tong_peizhi`
--

LOCK TABLES `xi_tong_peizhi` WRITE;
/*!40000 ALTER TABLE `xi_tong_peizhi` DISABLE KEYS */;
INSERT INTO `xi_tong_peizhi` VALUES (1,'你的校园闲置导购小助手，说出需求或发图，帮你挑最合适的闲置～','小淘','/uploads/ai/c5e5a134dbab4ce6a577c08c5c748566.png','让校园里的','流动起来','闲置','校园闲置 · 安心流转','教材、数码、好物低价流转','在售好物','商品分类','极速交易','24h');
/*!40000 ALTER TABLE `xi_tong_peizhi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yong_hu`
--

DROP TABLE IF EXISTS `yong_hu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `yong_hu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mi_ma` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shou_ji` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tou_xiang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `xing_ming` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `xue_hao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `yong_hu_ming` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `zhu_ce_shi_jian` datetime(6) DEFAULT NULL,
  `guan_li_yuan` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk9rj7pnj11oettwsubc550i0l` (`yong_hu_ming`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yong_hu`
--

LOCK TABLES `yong_hu` WRITE;
/*!40000 ALTER TABLE `yong_hu` DISABLE KEYS */;
INSERT INTO `yong_hu` VALUES (2,'0192023a7bbd73250516f069df18b500',NULL,NULL,'系统管理员',NULL,'admin','2026-10-22 23:23:34.832000',_binary ''),(3,'e10adc3949ba59abbe56e057f20f883e','','/uploads/touxiang/70a2962d589e4ea0b0f9ad2470a06b22.png','测试一号','','cheshi1','2026-10-24 00:10:08.640000',_binary '\0'),(4,'e10adc3949ba59abbe56e057f20f883e','13800002222',NULL,'测试二号','20230002','cheshi2','2026-10-24 00:10:08.720000',_binary '\0');
/*!40000 ALTER TABLE `yong_hu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'ershoupingtai'
--

--
-- Dumping routines for database 'ershoupingtai'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-24 17:22:19

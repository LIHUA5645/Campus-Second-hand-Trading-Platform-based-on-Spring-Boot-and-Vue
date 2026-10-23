// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.GongGao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GongGaoRepository extends JpaRepository<GongGao, Long> {

    /** 后台：全部公告，最新的在前 */
    List<GongGao> findAllByOrderByChuangJianShiJianDescIdDesc();

    /** 前台：最新 5 条显示中的公告 */
    List<GongGao> findTop5ByZhuangTaiOrderByChuangJianShiJianDesc(Integer zhuangTai);
}

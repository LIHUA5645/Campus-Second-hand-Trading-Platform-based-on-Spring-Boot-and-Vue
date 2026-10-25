// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.YuEJiLu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YuEJiLuRepository extends JpaRepository<YuEJiLu, Long> {

    List<YuEJiLu> findByYongHuIdOrderByShiJianDescIdDesc(Long yongHuId);

    /** 按订单查收入流水，用于防止同一订单重复入账 */
    List<YuEJiLu> findByDingDanIdAndLeiXing(Long dingDanId, String leiXing);
}

// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.XiaoXi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface XiaoXiRepository extends JpaRepository<XiaoXi, Long> {

    // 与我有关的全部消息，按时间倒序，用于聚合会话列表
    List<XiaoXi> findByFaSongRenIdOrJieShouRenIdOrderByFaSongShiJianDesc(Long faSongRenId, Long jieShouRenId);

    // 某个商品下两个人的完整聊天记录
    @Query("select x from XiaoXi x where x.shangPinId = :shangPinId "
            + "and ((x.faSongRenId = :wo and x.jieShouRenId = :duiFang) "
            + "or (x.faSongRenId = :duiFang and x.jieShouRenId = :wo)) "
            + "order by x.faSongShiJian asc, x.id asc")
    List<XiaoXi> zhaoHuiHua(@Param("shangPinId") Long shangPinId,
                            @Param("wo") Long wo,
                            @Param("duiFang") Long duiFang);

    // 我的全部未读消息数
    long countByJieShouRenIdAndYiDuFalse(Long jieShouRenId);

    // 打开会话时把对方发来的消息标记为已读
    @Modifying
    @Transactional
    @Query("update XiaoXi x set x.yiDu = true where x.jieShouRenId = :wo "
            + "and x.faSongRenId = :duiFang and x.shangPinId = :shangPinId and x.yiDu = false")
    int biaoJiYiDu(@Param("wo") Long wo,
                   @Param("duiFang") Long duiFang,
                   @Param("shangPinId") Long shangPinId);
}

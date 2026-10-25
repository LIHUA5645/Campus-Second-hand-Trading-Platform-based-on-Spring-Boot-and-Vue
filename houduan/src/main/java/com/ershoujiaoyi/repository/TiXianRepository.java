// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.TiXian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TiXianRepository extends JpaRepository<TiXian, Long> {

    List<TiXian> findByYongHuIdOrderByShenQingShiJianDescIdDesc(Long yongHuId);

    List<TiXian> findByZhuangTaiOrderByShenQingShiJianDescIdDesc(Integer zhuangTai);

    List<TiXian> findAllByOrderByShenQingShiJianDescIdDesc();

    /** 统计待审核的提现笔数，后台菜单角标用 */
    long countByZhuangTai(Integer zhuangTai);
}

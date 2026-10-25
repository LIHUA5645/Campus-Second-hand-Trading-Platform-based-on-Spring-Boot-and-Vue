// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.ZhiFuPeiZhi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZhiFuPeiZhiRepository extends JpaRepository<ZhiFuPeiZhi, Long> {

    /** 取当前启用的配置，按顺序取第一条 */
    List<ZhiFuPeiZhi> findByZhuangTaiOrderByIdAsc(Integer zhuangTai);
}

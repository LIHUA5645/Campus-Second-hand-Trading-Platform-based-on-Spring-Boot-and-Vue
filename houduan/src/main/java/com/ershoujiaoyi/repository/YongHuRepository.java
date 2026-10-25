// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.YongHu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YongHuRepository extends JpaRepository<YongHu, Long>, JpaSpecificationExecutor<YongHu> {
    YongHu findByYongHuMing(String yongHuMing);
}

// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.ShangPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ShangPinRepository extends JpaRepository<ShangPin, Long>, JpaSpecificationExecutor<ShangPin> {
    List<ShangPin> findByYongHuId(Long yongHuId);
    long countByZhuangTai(Integer zhuangTai);
}

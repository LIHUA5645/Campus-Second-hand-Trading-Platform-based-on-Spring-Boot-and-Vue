// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.DingDan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface DingDanRepository extends JpaRepository<DingDan, Long>, JpaSpecificationExecutor<DingDan> {
    List<DingDan> findByMaiJiaId(Long maiJiaId);
    List<DingDan> findByShangPinId(Long shangPinId);
}

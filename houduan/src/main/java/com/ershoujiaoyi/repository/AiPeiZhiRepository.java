// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.AiPeiZhi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiPeiZhiRepository extends JpaRepository<AiPeiZhi, Long> {
    List<AiPeiZhi> findAllByOrderByPaiXuAscIdAsc();
}

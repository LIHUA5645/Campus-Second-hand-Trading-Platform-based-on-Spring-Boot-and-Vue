// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.LunBoTu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LunBoTuRepository extends JpaRepository<LunBoTu, Long> {

    /** 后台：全部轮播图，按排序升序 */
    List<LunBoTu> findAllByOrderByPaiXuAscIdAsc();

    /** 前台：仅启用的轮播图，按排序升序 */
    List<LunBoTu> findByZhuangTaiOrderByPaiXuAscIdAsc(Integer zhuangTai);
}

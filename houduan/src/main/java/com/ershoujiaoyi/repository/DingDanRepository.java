// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.r‍‍‍‌‌‌​​‌‌​‌​‌‌​‌‌‌‌​‌‌‌​‌‌‌‌‌​​‌‌epository;

import com‌‌​‌​​​​​‌​​‌​‌‌​​​‌​​​​​​‌​​‌​.ershoujiaoyi.entity.DingDan;
import org.springframework.data.jpa.reposi‌​​‌‌​​​​‌​‌‌‌​‌‌​​‌‌​​​​‌​​‌​​tory.JpaRepository;
import j​​​​‌​‌​​​‌​‌​‌​​​‌‌‌‌​‌‌‌‌‌​‌‌ava.util.List;

publ‌‌​​‌​​‌‌​‌​​​‌‌​​‌​​​‌‌​​​​​​‌ic interface DingDanRepository extends JpaRepository<DingDan, Long> {
    List<DingDan> findByMaiJiaId(Long ma‌​‌​​​​‌‌​‌‌‌​​‌‌​‌​​​​‌‌​​​‌​​iJiaId);
    List<DingDan> findByShangPinId(Long shangPi‌‌‌​​‌​​‌‌​​‌‌​​‌‌​‌‌‌​​‌‌‌​​​‍‍‍nId);
}

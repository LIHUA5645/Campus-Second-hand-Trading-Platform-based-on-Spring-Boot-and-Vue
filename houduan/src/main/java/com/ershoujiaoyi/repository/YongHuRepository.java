// 作者：肖沐樑 QQ：3387432690
package com‍‍‍‌‌‌​​‌‌​‌​‌‌​‌‌‌‌​‌‌‌​‌‌‌‌‌​​‌‌‌‌​‌​​​​​‌​​‌.ershoujiaoyi.repository;

import com.ershoujiaoyi.entity.Yong​‌‌​​​‌​​​​​​‌​​‌​‌​​‌‌​​​​‌​‌‌‌​‌‌​​‌‌​​​​Hu;
import o‌​​‌​​​​​​‌​‌​​​‌​‌​‌​​​‌‌‌‌​‌‌‌‌‌​‌‌‌‌​​‌​rg.springframework.data.jpa.repository.JpaRepository;

public​‌‌​‌​​​‌‌​​‌​​​‌‌​​​​​​‌‌​‌​​​​‌‌​‌‌‌​​‌‌​ interface YongHuRepository extends JpaRepository<YongHu, Long> {
    YongHu findByYongHuMing(Strin‌​​​​‌‌​​​‌​​‌‌‌​​‌​​‌‌​​‌‌​​‌‌​‌‌‌​​‌‌‌​​​‍‍‍g yongHuMing);
}

// 作者：肖沐樑 QQ：3387432690
package co‍‍‍‌‌‌​​‌‌​‌​‌‌​‌‌‌‌​‌‌‌​‌‌‌‌‌​​‌‌‌‌​‌​​​​​‌​​‌m.ershoujiaoyi.repository;

imp​‌‌​​​‌​​​​​​‌​​‌​‌​​‌‌​​​​‌​‌‌‌​‌‌​​‌‌​​​​ort com.ershoujiaoyi.entity.YongHu;
import org.springframework.d‌​​‌​​​​​​‌​‌​​​‌​‌​‌​​​‌‌‌‌​‌‌‌‌‌​‌‌‌‌​​‌​ata.jpa.repository.JpaRepository;

public interface​‌‌​‌​​​‌‌​​‌​​​‌‌​​​​​​‌‌​‌​​​​‌‌​‌‌‌​​‌‌​ YongHuRepository extends JpaRepository<YongHu, Long> {
    YongHu fi‌​​​​‌‌​​​‌​​‌‌‌​​‌​​‌‌​​‌‌​​‌‌​‌‌‌​​‌‌‌​​​‍‍‍ndByYongHuMing(String yongHuMing);
}

// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fen_lei")
public class FenLei {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mingCheng;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMingCheng() { return mingCheng; }
    public void setMingCheng(String mingCheng) { this.mingCheng = mingCheng; }
}

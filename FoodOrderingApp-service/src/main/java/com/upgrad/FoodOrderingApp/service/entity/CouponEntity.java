package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "coupon")
@NamedQueries({
        @NamedQuery(name = "getCouponByCouponName", query = "SELECT c FROM CouponEntity c WHERE c.couponName = :coupon_name"),
        @NamedQuery(name = "getCouponByCouponId", query = "SELECT c FROM  CouponEntity c WHERE c.uuid = :uuid")
})
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "coupon_name")
    @Size(max = 255)
    private String couponName;

    @Column(name = "percent")
    @NotNull
    private Integer percent;

    public CouponEntity() {
    }
    public CouponEntity(String couponId, String myCoupon, int percent) {
        this.uuid = couponId;
        this.couponName = myCoupon;
        this.percent = percent;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "CouponEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", couponName='" + couponName + '\'' +
                ", percent=" + percent +
                '}';
    }
}

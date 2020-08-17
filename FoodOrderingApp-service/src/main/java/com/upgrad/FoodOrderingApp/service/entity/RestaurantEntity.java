package com.upgrad.FoodOrderingApp.service.entity;


import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Table(name="restaurant")
@NamedQueries({
        @NamedQuery(name="getAllRestaurantsByRating",query = "SELECT r FROM RestaurantEntity r ORDER BY r.customerrating DESC"),
        @NamedQuery(name = "getRestaurantByUuid",query = "SELECT r FROM RestaurantEntity r WHERE r.uuid = :uuid"),
        @NamedQuery(name = "getRestaurantsByName",query = "SELECT r FROM  RestaurantEntity r WHERE LOWER(r.restaurantname) LIKE :restaurantName")
})
public class RestaurantEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "restaurant_name")
    @NotNull
    @Size(max = 50)
    private String restaurantname;

    @Column(name = "photo_url")
    @Size(max = 255)
    private String photourl;

    @Column(name = "customer_rating")
    @NotNull
    private double customerrating;

    @Column(name = "average_price_for_two")
    @NotNull
    private Integer avgprice;

    @Column(name = "number_of_customers_rated")
    @NotNull
    private Integer numberofcustomersrated;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;

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

    public String getRestaurantName() {
        return restaurantname;
    }

    public void setRestaurantName(String restaurantname) {
        this.restaurantname = restaurantname;
    }

    public String getPhotoUrl() {
        return photourl;
    }

    public void setPhotoUrl(String photourl) {
        this.photourl = photourl;
    }

    public double getCustomerRating() {
        return customerrating;
    }

    public void setCustomerRating(double customerrating) {
        this.customerrating = customerrating;
    }

    public Integer getAvgprice() {
        return avgprice;
    }

    public void setAvgPrice(Integer avgprice) {
        this.avgprice = avgprice;
    }

    public Integer getNumberCustomersRated() {
        return numberofcustomersrated;
    }

    public void setNumberCustomersRated(Integer numberofcustomersrated) {
        this.numberofcustomersrated = numberofcustomersrated;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }
}

package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "address")
@NamedQueries({
        @NamedQuery(name = "getAddressByUuid", query = "SELECT a from AddressEntity a where a.uuid = :uuid")
})

public class AddressEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "flat_buil_number")
    @Size(max = 255)
    private String flatBuilNumber;

    @Column(name = "locality")
    @Size(max = 255)
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    private String city;

    @Column(name = "pincode")
    @Size(max = 30)
    private String pinCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StateEntity state;

    @Column(name = "active")
    private int active;

    public AddressEntity() {
        active = 1;
    }

    public AddressEntity(@NotNull @Size(max = 200) String uuid, @Size(max = 255) String flatBuilNumber, @Size(max = 255) String locality, @Size(max = 30) String city, @Size(max = 30) String pinCode, StateEntity state, int active) {
        this.uuid = uuid;
        this.flatBuilNumber = flatBuilNumber;
        this.locality = locality;
        this.city = city;
        this.pinCode = pinCode;
        this.state = state;
        this.active = active;
    }

    public AddressEntity(String addressId, String s, String someLocality, String someCity, String s1, StateEntity stateEntity) {
        this.locality = someLocality;
        this.city = someCity;
        this.state = stateEntity;

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

    public String getFlatBuilNo() {
        return flatBuilNumber;
    }


    public void setFlatBuilNo(String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pinCode;
    }

    public void setPincode(String pincode) {
        this.pinCode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}

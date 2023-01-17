package com.business.api.dob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Transactional
@Table(name = "businesses")
public class Business {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "business_id")
    private Long businessId;

    @Column(unique = true)
    private String businessName;

    @Column(unique = true)
    private String businessEmail;


    private String businessPhone;
    private Double latitude;
    private Double longitude;

    public Business() {
    }

    public Business(String businessName, String businessEmail){
        this.businessName   = businessName;
        this.businessEmail  = businessEmail;
    }

    public Business(String businessName, String businessEmail,  String businessPhone){
        this.businessName   = businessName;
        this.businessEmail  = businessEmail;
        this.businessPhone  = businessPhone;
    }

    public Business(String businessName, String businessEmail,  String businessPhone, Double latitude, Double longitude){
        this.businessName   = businessName;
        this.businessEmail  = businessEmail;
        this.businessPhone  = businessPhone;
        this.latitude       = latitude;
        this.longitude      = longitude;
    }

    @JsonIgnore
    public boolean isValid(){
        return businessName != null && businessEmail != null;
    }
}

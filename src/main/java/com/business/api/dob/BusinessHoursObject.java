package com.business.api.dob;

import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Transactional
@Table(name = "schedules")
public class BusinessHoursObject {

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "business_hours_join_id")
    private Business business;

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "hours_id")
    private Long hoursId;

    private Integer dayOfWeek; //! day of week 0 = Monday -> 6 = Sunday;
    private Time openingHours;
    private Time closingHours;

    public BusinessHoursObject() {
    }

    public BusinessHoursObject(Integer dayOfWeek, Time openingHours, Time closingHours) {
        this.dayOfWeek = dayOfWeek;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
    }

    @JsonIgnore
    public boolean isValid(){
        return dayOfWeek != null && openingHours != null && closingHours != null;
    }
}

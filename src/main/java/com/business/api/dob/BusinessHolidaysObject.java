package com.business.api.dob;

import java.time.LocalDate;

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
@Table(name = "holidays")
public class BusinessHolidaysObject {

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "business_holidays_join_id")
    private Business business;

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "holidays_id")
    private Long holidaysId;

    private LocalDate calendarHolidayStart;
    private LocalDate calendarHolidayEnd;

    public BusinessHolidaysObject() {
    }

    public BusinessHolidaysObject(LocalDate calendarHolidayStart, LocalDate calendarHolidayEnd) {
        this.calendarHolidayStart = calendarHolidayStart;
        this.calendarHolidayEnd = calendarHolidayEnd;
    }

    @JsonIgnore
    public boolean isValid(){
        return calendarHolidayStart != null && calendarHolidayEnd != null;
    }

}

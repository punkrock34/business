package com.business.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.business.api.dob.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM Business AS b WHERE b.businessEmail = :businessEmail AND b.businessName = :businessName")
    Business getBusinessByEmailAndName(String businessName, String businessEmail);

    @Query("SELECT b FROM Business AS b WHERE b.latitude = :businessLatitude And b.longitude = :businessLongitude")
    Business findByLatLng(Double businessLatitude, Double businessLongitude);
}

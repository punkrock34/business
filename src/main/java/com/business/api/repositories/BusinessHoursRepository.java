package com.business.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.business.api.dob.BusinessHoursObject;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHoursObject, Long> {

    @Query("SELECT bh FROM BusinessHoursObject AS bh WHERE bh.business.businessEmail = :businessEmail AND bh.business.businessName = :businessName")
    List<BusinessHoursObject> findScheduleFromBusinessEmailAndName(String businessName, String businessEmail);

    @Query("SELECT COUNT(bh) FROM BusinessHoursObject as bh WHERE bh.business.businessEmail = :businessEmail AND bh.business.businessName = :businessName")
    Integer findByBusinessEmailAndName(String businessName, String businessEmail);

    @Query("SELECT bh FROM BusinessHoursObject as bh WHERE bh.business.businessEmail = :businessEmail AND bh.business.businessName = :businessName AND bh.dayOfWeek = :dayOfWeek")
    Optional<BusinessHoursObject> findByBusinessEmailAndNameAndDayOfWeek(String businessName, String businessEmail, Integer dayOfWeek);

    @Modifying
    @Query("DELETE FROM BusinessHoursObject bh WHERE bh.business.businessId = :businessId")
    void deleteAllByBusinessId(Long businessId);
}

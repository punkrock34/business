package com.business.api.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.business.api.dob.BusinessHolidaysObject;

@Repository
public interface BusinessHolidaysRepository extends JpaRepository<BusinessHolidaysObject, Long> {

    @Query("SELECT bh FROM BusinessHolidaysObject AS bh WHERE bh.business.businessEmail = :businessEmail AND bh.business.businessName = :businessName")
    List<BusinessHolidaysObject> findHolidaysFromBusinessEmailAndName(String businessName, String businessEmail);

    @Query("SELECT bh FROM BusinessHolidaysObject AS bh WHERE bh.business.businessEmail = :businessEmail AND bh.business.businessName = :businessName AND bh.calendarHolidayStart = :calendarHolidayStart AND bh.calendarHolidayEnd = :calendarHolidayEnd")
    Optional<BusinessHolidaysObject> checkIfExistsByBusinessEmailAndNameAndDate(String businessName, String businessEmail, LocalDate calendarHolidayStart, LocalDate calendarHolidayEnd);

    @Modifying
    @Query("DELETE FROM BusinessHolidaysObject bh WHERE bh.business.businessId = :businessId")
    void deleteAllByBusinessId(Long businessId);
}

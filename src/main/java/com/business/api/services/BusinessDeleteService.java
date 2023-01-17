package com.business.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.business.api.ErrorMessages;
import com.business.api.dob.Business;
import com.business.api.dob.BusinessHolidaysObject;
import com.business.api.dob.BusinessHoursObject;
import com.business.api.repositories.BusinessHolidaysRepository;
import com.business.api.repositories.BusinessHoursRepository;
import com.business.api.repositories.BusinessRepository;

import jakarta.transaction.Transactional;


@Service
public class BusinessDeleteService {
    private final BusinessRepository businessRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BusinessHolidaysRepository businessHolidaysRepository;

    @Autowired
    public BusinessDeleteService(BusinessRepository businessRepository, BusinessHoursRepository businessHoursRepository, BusinessHolidaysRepository businessHolidaysRepository) {
        this.businessRepository = businessRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.businessHolidaysRepository = businessHolidaysRepository;
    }

    @Transactional
    public void deleteBusiness(String businessName, String businessEmail, Business business) {
        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business businessToDelete = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(businessToDelete == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_NOT_FOUND);
        }

        if(businessToDelete.getBusinessId() != business.getBusinessId()){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        }

        businessHoursRepository.deleteAllByBusinessId(business.getBusinessId());
        businessHolidaysRepository.deleteAllByBusinessId(business.getBusinessId());
        businessRepository.delete(business);
    }

    @Transactional
    public void deleteBusinessSchedule(String businessName, String businessEmail, BusinessHoursObject businessHoursObject) {
        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business businessToDeleteScheduleFrom = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(businessToDeleteScheduleFrom == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_NOT_FOUND);
        }

        BusinessHoursObject businessScheduleToDelete = businessHoursRepository.getReferenceById(businessHoursObject.getHoursId());

        if(businessScheduleToDelete.getDayOfWeek() != businessHoursObject.getDayOfWeek()){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        }

        businessHoursRepository.deleteById(businessScheduleToDelete.getHoursId());

    }

    @Transactional
    public void deleteBusinessHoliday(String businessName, String businessEmail,BusinessHolidaysObject businessHolidaysObject) {
        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business businessToDeleteHolidayFrom = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(businessToDeleteHolidayFrom == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_NOT_FOUND);
        }

        BusinessHolidaysObject businessHolidayToDelete = businessHolidaysRepository.getReferenceById(businessHolidaysObject.getHolidaysId());

        if(!businessHolidayToDelete.getCalendarHolidayStart().isEqual(businessHolidaysObject.getCalendarHolidayStart()) && !businessHolidayToDelete.getCalendarHolidayEnd().isEqual(businessHolidaysObject.getCalendarHolidayEnd())){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        }

        businessHolidaysRepository.deleteById(businessHolidayToDelete.getHolidaysId());
    }


}

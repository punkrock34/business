package com.business.api.services;

import java.util.List;

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

@Service
public class BusinessGetService {

    private final BusinessRepository businessRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BusinessHolidaysRepository businessHolidaysRepository;

    @Autowired
    public BusinessGetService(BusinessRepository businessRepository, BusinessHoursRepository businessHoursRepository, BusinessHolidaysRepository businessHolidaysRepository) {
        this.businessRepository = businessRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.businessHolidaysRepository = businessHolidaysRepository;
    }

    //GET FUNCTIONS DOWN BELOW
    public Business getBusiness(String businessName, String businessEmail) {

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business business = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(business == null || !business.isValid())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(ErrorMessages.BUSINESS_NOT_FOUND, businessName, businessEmail));

        return business;
    }

    public List<BusinessHoursObject> getBusinessHours(String businessName, String businessEmail){

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        List<BusinessHoursObject> businessHours = businessHoursRepository.findScheduleFromBusinessEmailAndName(businessName, businessEmail);

        if(businessHours == null || businessHours.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(ErrorMessages.SCHEDULE_NOT_FOUND, businessName, businessEmail));

        return businessHours;
    }

    public List<BusinessHolidaysObject> getBusinessHolidays(String businessName, String businessEmail){

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        List<BusinessHolidaysObject> businessHolidays = businessHolidaysRepository.findHolidaysFromBusinessEmailAndName(businessName, businessEmail);

        if(businessHolidays == null || businessHolidays.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(ErrorMessages.HOLIDAYS_NOT_FOUND, businessName, businessEmail));

        return businessHolidays;
    }

}

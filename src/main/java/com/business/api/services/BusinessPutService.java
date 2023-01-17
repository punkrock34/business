package com.business.api.services;

import java.util.Optional;

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
public class BusinessPutService {
    private final BusinessRepository businessRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BusinessHolidaysRepository businessHolidaysRepository;

    @Autowired
    public BusinessPutService(BusinessRepository businessRepository, BusinessHoursRepository businessHoursRepository, BusinessHolidaysRepository businessHolidaysRepository) {
        this.businessRepository = businessRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.businessHolidaysRepository = businessHolidaysRepository;
    }

    @Transactional
    public void updateBusinessInformation(String businessName, String businessEmail, Business business) {

        Double businessLatitude  = business.getLatitude();
        Double businessLongitude = business.getLongitude();

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business businessToUpdate = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(businessToUpdate == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_NOT_FOUND);
        }


        Business businessOptional = businessRepository.findByLatLng(businessLatitude,businessLongitude);
        if(businessOptional != null && businessOptional.getBusinessId() != businessToUpdate.getBusinessId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_ALREADY_EXISTS_WITH_LAT_LNG);
        }

        Business checkIfBusinessExistsWithNewNameAndEmail = businessRepository.getBusinessByEmailAndName(business.getBusinessName(), business.getBusinessEmail());

        if(checkIfBusinessExistsWithNewNameAndEmail != null && checkIfBusinessExistsWithNewNameAndEmail.getBusinessId() != businessToUpdate.getBusinessId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ErrorMessages.BUSINESS_ALREADY_EXISTS, business.getBusinessName(), business.getBusinessEmail()));
        }

        businessToUpdate.setBusinessName(business.getBusinessName());
        businessToUpdate.setBusinessEmail(business.getBusinessEmail());
        businessToUpdate.setBusinessPhone(business.getBusinessPhone());
        businessToUpdate.setLatitude(businessLatitude);
        businessToUpdate.setLongitude(businessLongitude);
    }

    @Transactional
    public void updateBusinessSchedule(String businessName, String businessEmail, BusinessHoursObject businessHoursObject) {
        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business businessToUpdate = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(businessToUpdate == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_NOT_FOUND);
        }

        Long businessHoursId = businessHoursObject.getHoursId();

        if(businessHoursId == null){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        }

        BusinessHoursObject businessHoursToUpdate = businessHoursRepository.getReferenceById(businessHoursId);
        businessHoursToUpdate.setDayOfWeek(businessHoursObject.getDayOfWeek());
        businessHoursToUpdate.setOpeningHours(businessHoursObject.getOpeningHours());
        businessHoursToUpdate.setClosingHours(businessHoursObject.getClosingHours());
    }

    @Transactional
    public void updateBusinessHolidays(String businessName, String businessEmail, BusinessHolidaysObject businessHolidaysObject) {

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Business businessToUpdate = businessRepository.getBusinessByEmailAndName(businessName, businessEmail);

        if(businessToUpdate == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_NOT_FOUND);
        }

        Long businessHolidaysId = businessHolidaysObject.getHolidaysId();

        if(businessHolidaysId == null){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        }

        BusinessHolidaysObject businessHolidaysToUpdate = businessHolidaysRepository.getReferenceById(businessHolidaysId);

        businessHolidaysToUpdate.setCalendarHolidayStart(businessHolidaysObject.getCalendarHolidayStart());
        businessHolidaysToUpdate.setCalendarHolidayEnd(businessHolidaysObject.getCalendarHolidayEnd());

    }


}

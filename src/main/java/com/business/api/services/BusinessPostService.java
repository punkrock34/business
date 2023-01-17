package com.business.api.services;

import java.time.LocalDate;
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

@Service
public class BusinessPostService {

    private final BusinessRepository businessRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BusinessHolidaysRepository businessHolidaysRepository;

    @Autowired
    public BusinessPostService(BusinessRepository businessRepository, BusinessHoursRepository businessHoursRepository, BusinessHolidaysRepository businessHolidaysRepository) {
        this.businessRepository = businessRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.businessHolidaysRepository = businessHolidaysRepository;
    }

    //POST FUNCTIONS DOWN BELOW
    public void registerNewBusiness(Business business) {

        Double businessLatitude     = business.getLatitude();
        Double businessLongitude    = business.getLongitude();

        if(!business.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_OBJECT);
        }

        Business checkIfBusinessExists = businessRepository.getBusinessByEmailAndName(business.getBusinessName(), business.getBusinessEmail());

        if(checkIfBusinessExists != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ErrorMessages.BUSINESS_ALREADY_EXISTS, business.getBusinessName(), business.getBusinessEmail()));
        }

        if(businessLatitude != null && businessLongitude != null)
        {
            Business businessOptional = businessRepository.findByLatLng(businessLatitude,businessLongitude);
            if(businessOptional != null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BUSINESS_ALREADY_EXISTS_WITH_LAT_LNG);
            }
        }
        businessRepository.save(business);
    }

    public void addBusinessSchedule(String businessName, String businessEmail, BusinessHoursObject businessHours) {

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        Integer dayOfWeek = businessHours.getDayOfWeek();

        if(!businessHours.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_SCHEDULE);
        }

        Optional<BusinessHoursObject> businessScheduleExists = businessHoursRepository.findByBusinessEmailAndNameAndDayOfWeek(businessName, businessEmail, dayOfWeek);
        if(businessScheduleExists.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.SCHEDULE_ALREADY_EXISTS);
        }

        Integer businessSchedulesCount = businessHoursRepository.findByBusinessEmailAndName(businessName, businessEmail);

        if(businessSchedulesCount >= 7){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.TOO_MANY_SCHEDULES);
        }

        if (dayOfWeek < 0 || dayOfWeek > 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_DAY_OF_WEEK);
        }

        businessHours.setBusiness(businessRepository.getBusinessByEmailAndName(businessName, businessEmail));
        businessHoursRepository.save(businessHours);
    }

    public void addBusinessHoliday(String businessName, String businessEmail, BusinessHolidaysObject businessHolidaysObject) {

        if(businessName.trim() == "" || businessEmail.trim() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_BUSINESS_EMAIL_OR_NAME);
        }

        LocalDate calendarHolidayStart   = businessHolidaysObject.getCalendarHolidayStart();
        LocalDate calendarHolidayEnd     = businessHolidaysObject.getCalendarHolidayEnd();

        if(!businessHolidaysObject.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ErrorMessages.INVALID_CALENDAR_HOLIDAY, businessName, businessEmail));
        }

        if(calendarHolidayEnd.isBefore(calendarHolidayStart)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ErrorMessages.INVALID_CALENDAR_HOLIDAY, businessName, businessEmail));
        }

        Optional<BusinessHolidaysObject> businessHolidaysCheckIfExists = businessHolidaysRepository.checkIfExistsByBusinessEmailAndNameAndDate(businessName, businessEmail, calendarHolidayStart, calendarHolidayEnd);
        if(businessHolidaysCheckIfExists.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ErrorMessages.HOLIDAY_ALREADY_EXISTS, businessName, businessEmail));
        }

        businessHolidaysObject.setBusiness(businessRepository.getBusinessByEmailAndName(businessName, businessEmail));
        businessHolidaysRepository.save(businessHolidaysObject);

    }

}

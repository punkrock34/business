package com.business.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.business.api.dob.Business;
import com.business.api.dob.BusinessHolidaysObject;
import com.business.api.dob.BusinessHoursObject;
import com.business.api.services.BusinessDeleteService;
import com.business.api.services.BusinessGetService;
import com.business.api.services.BusinessPostService;
import com.business.api.services.BusinessPutService;

@RestController
@RequestMapping(path = "/api/v1/business")
public class RestfullBusinessController {

    private final BusinessGetService businessGetService;
    private final BusinessPostService businessPostService;
    private final BusinessPutService businessPutService;
    private final BusinessDeleteService businessDeleteService;

    @Autowired
    public RestfullBusinessController(BusinessGetService businessGetService, BusinessPostService businessPostService, BusinessPutService businessPutService, BusinessDeleteService businessDeleteService) {
        this.businessGetService     = businessGetService;
        this.businessPostService    = businessPostService;
        this.businessPutService     = businessPutService;
        this.businessDeleteService  = businessDeleteService;
    }

    @RequestMapping(value = {"/", ""})
    public RedirectView redirectToMain() {
        return new RedirectView("/api/v1/business/main");
    }


    @GetMapping(path = "/main")
    public Business getBusiness(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, ModelMap model) {
        return businessGetService.getBusiness(businessName, businessEmail);
    }

    @GetMapping(path = "/schedules")
    public List<BusinessHoursObject> getBusinessHours(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail){
        return businessGetService.getBusinessHours(businessName, businessEmail);
    }

    @GetMapping(path ="/holidays")
    public List<BusinessHolidaysObject> getBusinessHolidays(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail){
        return businessGetService.getBusinessHolidays(businessName, businessEmail);
    }

    @PostMapping(path = "/main")
    public void registerNewBusiness(@RequestBody Business business){
        businessPostService.registerNewBusiness(business);
    }

    @PostMapping(path = "/schedules")
    public void addBusinessSchedule(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody BusinessHoursObject businessHoursObject){
        businessPostService.addBusinessSchedule(businessName, businessEmail, businessHoursObject);
    }

    @PostMapping(path = "/holidays")
    public void addBusinessHoliday(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody BusinessHolidaysObject businessHolidaysObject){
        businessPostService.addBusinessHoliday(businessName, businessEmail, businessHolidaysObject);
    }

    @PutMapping(path = "/main")
    public void updateBusinessInformation(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody Business business){
        businessPutService.updateBusinessInformation(businessName, businessEmail, business);
    }

    @PutMapping(path = "/schedules")
    public void updateBusinessSchedule(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody BusinessHoursObject businessHoursObject){
        businessPutService.updateBusinessSchedule(businessName, businessEmail, businessHoursObject);
    }

    @PutMapping(path = "/holidays")
    public void updateBusinessHolidays(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody BusinessHolidaysObject businessHolidaysObject){
        businessPutService.updateBusinessHolidays(businessName, businessEmail, businessHolidaysObject);
    }

    @DeleteMapping(path = "/main")
    public void deleteBusiness(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody Business business){
        businessDeleteService.deleteBusiness(businessName, businessEmail, business);
    }

    @DeleteMapping(path = "/schedules")
    public void deleteBusinessSchedule(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody BusinessHoursObject businessHoursObject){
        businessDeleteService.deleteBusinessSchedule(businessName, businessEmail, businessHoursObject);
    }

    @DeleteMapping(path = "/holidays")
    public void deleteBusinessHolidays(@RequestParam(required = true) String businessName, @RequestParam(required = true) String businessEmail, @RequestBody BusinessHolidaysObject businessHolidaysObject){
        businessDeleteService.deleteBusinessHoliday(businessName, businessEmail, businessHolidaysObject);
    }
}

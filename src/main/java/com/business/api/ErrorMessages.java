package com.business.api;

public class ErrorMessages {

    public static final String INVALID_BUSINESS_EMAIL_OR_NAME       = "A business name and email are required to complete this operation. Please provide a valid business name and email and try again.";
    public static final String BUSINESS_NOT_FOUND                   = "I apologize, but we were unable to locate any businesses with the specified name and email. Please double-check the email and name and try again.";
    public static final String SCHEDULE_NOT_FOUND                   = "It appears that the schedule for the business with name %s and email %s does not exist. Please double-check the email and name and try again.";
    public static final String HOLIDAYS_NOT_FOUND                   = "I'm sorry, but it looks like the holidays for the business with name %s and email %s do not exist. Please check the email and name and try again.";
    public static final String INVALID_BUSINESS_OBJECT              = "We are sorry, but it seems that you have not provided a business with the required parameters. Please try again and provide a business with the following parameters: name, email.";
    public static final String BUSINESS_ALREADY_EXISTS              = "We are sorry, but it seems that a business already exists with the specified name: %s and email: %s.";
    public static final String TOO_MANY_SCHEDULES                   = "We are sorry, but it seems that the business already has its schedules set. You can update your schedules, but you cannot create more than 7 schedules per business.";
    public static final String INVALID_BUSINESS_SCHEDULE            = "Invalid business schedule. Please ensure that you have provided all required parameters: day of week, opening hours, closing hours.";
    public static final String SCHEDULE_ALREADY_EXISTS              = "A schedule already exists for the specified day of the week. Please choose a different day or update the existing schedule.";
    public static final String INVALID_DAY_OF_WEEK                  = "We are sorry, but it seems that the day of the week you provided is not a valid value. Please try again and provide an integer value between 0 and 6, representing Sunday to Saturday.";
    public static final String INVALID_CALENDAR_HOLIDAY             = "An invalid calendar holiday range was detected for the business with name %s and email %s. The calendar holiday end date must be after the calendar holiday start date. Please provide a valid calendar holiday range and try again.";
    public static final String HOLIDAY_ALREADY_EXISTS               = "A calendar holiday with the specified date range already exists for the business with name %s and email %s. Please provide a different date range and try again.";
    public static final String BUSINESS_ALREADY_EXISTS_WITH_LAT_LNG = "We are sorry but there is already a business at the specified latitude and longitude. Please try again";
}

import React, { useState } from 'react';
import axios from 'axios';
import { renderMainResults, renderSchedulesResults, renderHolidaysResults } from '../objects/Props.jsx'


export const PUT = () => {
    const [businessEmail, setBusinessEmail] = useState('');
    const [businessName, setBusinessName]   = useState('');
    const [error, setError] = useState(null); // Initialize an error state
    const [business, setBusiness] = useState([]);
    const [businessHours, setBusinessHours] = useState([])
    const [businessHolidays, setBusinessHolidays] = useState([]);
    const [editing, setEditing] = useState('');
    const [editingId, setEditingId] = useState(0);
    const [formData, setFormData] = useState({
        id: 0,
        type: '',
        dayOfWeek: '',
        openingHours: '',
        closingHours: '',
        calendarHolidayStart: '',
        calendarHolidayEnd: '',
        businessName: '',
        businessEmail: '',
        businessPhone: '',
        latitude: '',
        longitude: '',
    });


    const resetResults = () => {
        setBusiness([]);
        setBusinessHours([]);
        setBusinessHolidays([]);
        setError(null);
    }

    const handleSave = (formData) => {

        setEditing('');
        setEditingId(0);
        setFormData({
            id: 0,
            type: '',
            dayOfWeek: '',
            openingHours: '',
            closingHours: '',
            calendarHolidayStart: '',
            calendarHolidayEnd: '',
            businessName: '',
            businessEmail: '',
            businessPhone: '',
            latitude: '',
            longitude: '',
        })

        const url = window.location.href + `/api/v1/business/${formData.type}`
        const type = formData.type
        let data = {};

        if (type === "schedules") {

            const openingHours = formData.openingHours;
            const closingHours = formData.closingHours;

            let openingTime = new Date();
            let closingTime = new Date();

            let openingSplit = openingHours.split(':');
            let closingSplit = closingHours.split(':');

            let openingTimeFormat = new Intl.DateTimeFormat('en-US', {
                hour: 'numeric',
                minute: 'numeric',
                hour12: false
            });
            let closingTimeFormat = new Intl.DateTimeFormat('en-US', {
                hour: 'numeric',
                minute: 'numeric',
                hour12: false
            });

            openingTime.setHours(openingSplit[0]);
            openingTime.setMinutes(openingSplit[1]);
            closingTime.setHours(closingSplit[0]);
            closingTime.setMinutes(closingSplit[1]);

            data = {
                hoursId: formData.id,
                dayOfWeek: formData.dayOfWeek,
                openingHours: openingTimeFormat.format(openingTime)+":00",
                closingHours: closingTimeFormat.format(closingTime)+":00"
            };
        } else if(type === "holidays") {
            data = {
                holidaysId: formData.id,
                calendarHolidayStart: formData.calendarHolidayStart,
                calendarHolidayEnd: formData.calendarHolidayEnd,
            };
        } else if(type === "main") {
            data = {
                businessId: formData.id,
                businessName: formData.businessName,
                businessEmail: formData.businessEmail,
                businessPhone: formData.businessPhone,
                latitude:formData.latitude,
                longitude:formData.longitude
            }
            setBusinessName(data.businessName);
            setBusinessEmail(data.businessEmail);
        }

        axios.request({
            method: "put",
            url: url,
            params:{
                businessEmail: businessEmail,
                businessName: businessName
            },
            data: data,
        }).then((response) => {
            setError(null);
            // Refresh the data
            if(type === "main")
                fetchDataForBusiness(data.businessEmail, data.businessName, type);
            else
                fetchDataForBusiness(businessEmail, businessName, type);
        })
        .catch((error) => {
            setError(error);
        });

    }

    const handleSubmit = (event) => {

        event.preventDefault();

        const url = `http://localhost:8080/api/v1/business/main`;

        resetResults();

        axios.request({
                method: 'get',
                url: url,
                params:{
                    businessEmail: businessEmail,
                    businessName: businessName,
                },
            }).then((response) => {
                setBusiness(response.data);
                fetchDataForBusiness(businessEmail, businessName, "schedules");
                fetchDataForBusiness(businessEmail, businessName, "holidays");
            }).catch((error) => {
                setError(error);
            });
    }

    const fetchDataForBusiness = (businessEmail, businessName, type) => {
        const url = `http://localhost:8080/api/v1/business/${type}`;

        axios.request({
                method: 'get',
                url: url,
                params:{
                    businessEmail: businessEmail,
                    businessName: businessName,
                },
            }).then((response) => {
                if(type == "schedules"){
                    setBusinessHours(response.data)
                }else if(type == "holidays"){
                    setBusinessHolidays(response.data);
                }else if(type == "main"){
                    setBusiness(response.data);
                }
            }).catch((error) => {
                setError(error);
            });
    }

    return (
        <>
            <section className="form-data-container">
                <div className="container mt-5" id="form-component">
                    <form id="form" onSubmit={handleSubmit}>
                        <label htmlFor="business-email">Business Email:</label><br />
                        <input type="text" id="business-email" name="business-email" value={businessEmail} onChange={(e) => setBusinessEmail(e.target.value)} /><br />
                        <label htmlFor="business-name">Business Name:</label><br />
                        <input type="text" id="business-name" name="business-name" value={businessName} onChange={(e) => setBusinessName(e.target.value)} /><br />
                        <button type="submit">Submit</button>
                    </form>
                </div>
            </section>
            <section className="results-container">
                <div className="container mt-5" id="results-component">

                    {error && (error.response.status !== 404 || Object.keys(business).length === 0) && (
                        <div id="error">{error.response.data.message}</div>
                    )}

                    {
                        <>
                            {Object.keys(business).length !== 0 && (
                                <>
                                    <h3>Main Business Information</h3>
                                    {renderMainResults(business, 'edit', formData, setFormData, editingId, editing, setEditing, setEditingId, handleSave)}
                                </>
                            )}

                            {businessHours.length > 0 &&
                            (
                                <>
                                    <h3>Business Schedules</h3>
                                    {renderSchedulesResults(businessHours, 'edit', formData, setFormData, editingId, editing, setEditing, setEditingId, handleSave)}
                                </>
                            )}

                            {businessHolidays.length > 0 && (
                                <>
                                    <h3>Business Holidays</h3>
                                    {renderHolidaysResults(businessHolidays, 'edit', formData, setFormData, editingId, editing, setEditing, setEditingId, handleSave)}
                                </>
                            )}
                        </>
                    }

                </div>
            </section>
        </>
    );
}

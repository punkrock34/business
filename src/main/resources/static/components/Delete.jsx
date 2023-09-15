import React, { useState } from 'react';
import axios from 'axios';
import { renderMainResults, renderSchedulesResults, renderHolidaysResults } from '../objects/Props.jsx'


export const DELETE = () => {
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

    const handleDelete = (formData) => {

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

        const url = window.location.href + `/api/v1/business/${formData.type}`;
        const type = formData.type
        let data = {};

        if (type === "schedules") {
            data = {
                hoursId: formData.id,
                dayOfWeek: formData.dayOfWeek,
                openingHours: (formData.openingHours != "" ? formData.openingHours : ""),
                closingHours: (formData.closingHOurs != "" ? formData.closingHours : ""),
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
            method: "delete",
            url: url,
            params:{
                businessEmail: businessEmail,
                businessName: businessName
            },
            data: data,
        }).then((response) => {
            setError(null);
            // Refresh the data
            if(type !== "main")
                fetchDataForBusiness(businessEmail, businessName, type);
            else
                resetResults();
        })
        .catch((error) => {
            setError(error);
        });

    }

    const handleSubmit = (event) => {

        event.preventDefault();

        const url = window.location.href + `/api/v1/business/main`;

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
        const url = window.location.href + `/api/v1/business/${type}`;

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
                                    {renderMainResults(business, 'delete', formData, setFormData, editingId, editing, setEditing, setEditingId, handleDelete)}
                                </>
                            )}

                            {businessHours.length > 0 &&
                            (
                                <>
                                    <h3>Business Schedules</h3>
                                    {renderSchedulesResults(businessHours, 'delete', formData, setFormData, editingId, editing, setEditing, setEditingId, handleDelete)}
                                </>
                            )}

                            {businessHolidays.length > 0 && (
                                <>
                                    <h3>Business Holidays</h3>
                                    {renderHolidaysResults(businessHolidays, 'delete', formData, setFormData, editingId, editing, setEditing, setEditingId, handleDelete)}
                                </>
                            )}
                        </>
                    }

                </div>
            </section>
        </>
    );
}

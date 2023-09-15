import React, { useState } from 'react';
import axios from 'axios';
import { renderMainResults, renderSchedulesResults, renderHolidaysResults } from '../objects/Props.jsx'

export const POST = () => {
    const [businessEmail, setBusinessEmail] = useState('');
    const [businessName, setBusinessName]   = useState('');
    const [error, setError] = useState(null); // Initialize an error state
    const [business, setBusiness] = useState([]);
    const [businessHours, setBusinessHours] = useState([])
    const [businessHolidays, setBusinessHolidays] = useState([]);
    const [editing, setEditing] = useState(false);

    const resetResults = () => {
        setBusiness([]);
        setBusinessHours([]);
        setBusinessHolidays([]);
        setError(null);
    }

    const handleSubmit = (event) => {

        event.preventDefault();

        const url = window.location.protocol + "//" + window.location.host + `/api/v1/business/main`;

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
        const url = window.location.protocol + "//" + window.location.host + `/api/v1/business/${type}`;

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
                }else{
                    setBusinessHolidays(response.data);
                }
            }).catch((error) => {
                setError(error);
            });
    }

    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        type: '',
        day: '',
        open: '',
        close: '',
        start: '',
        end: '',
        businessName: '',
        businessEmail: '',
        businessPhone: '',
        latitude: '',
        longitude: '',
    });

    const handleAdd = (type) => {
        setError(null);
        setShowForm(true);
        setFormData({
            type,
            day: '',
            open: '',
            close: '',
            start: '',
            end: '',
            businessName: '',
            businessEmail: '',
            businessPhone: '',
            latitude: '',
            longitude: '',
        });
    };

    const handleSave = (type) => {
        const url = window.location.protocol + "//" + window.location.host + `/api/v1/business/${type}`;
        let data = {};
        if (type === "schedules") {

            const openingHours = formData.open;
            const closingHours = formData.close;

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
                dayOfWeek: formData.day,
                openingHours: openingTimeFormat.format(openingTime)+":00",
                closingHours: closingTimeFormat.format(closingTime)+":00"
            };
        } else if(type === "holidays") {
            data = {
                calendarHolidayStart: formData.start,
                calendarHolidayEnd: formData.end,
            };
        } else if(type === "main") {
            data = {
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
                method: "POST",
                url: url,
                params: {
                    businessEmail: businessEmail,
                    businessName: businessName,
                },
                data: data,
            })
            .then((res) => {
                setError(null);
                // Refresh the data
                fetchDataForBusiness(businessEmail, businessName, type);
            })
            .catch((error) => {
                setError(error);
            });

            setShowForm(false);
    };

    const handleCancel = () => {
        setShowForm(false);
        setFormData({});
    }

    const renderAddButton = (data) => {
        const type = data === businessHours ? "schedules" : "holidays";
        return (
        <>
                    {!showForm && (
                        <table className="table table-dark">
                            <tbody>
                                <tr>
                                    <td colSpan="3">
                                        <button onClick={() => handleAdd(type)}>Add</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    )}
                    { !error && showForm && formData.type === type && (
                        <form>
                            <table className="table table-dark">
                                <thead>
                                    {type === "schedules" && (
                                        <tr>
                                            <th>Day</th>
                                            <th>Opening Time</th>
                                            <th>Closing Time</th>
                                        </tr>
                                    )}

                                    {type === "holidays"  && (
                                        <tr>
                                            <th>Holiday Start Date</th>
                                            <th>Holiday End Date</th>
                                        </tr>
                                    )}
                                </thead>
                                <tbody>
                                    {type === "schedules" && (
                                        <tr>
                                            <td>
                                                <select id="day" name="day" value={formData.day} onChange={(e) => setFormData({ ...formData, day: e.target.value })}>
                                                    <option value = "">Select a week day to add</option>
                                                    <option value="0">Sunday</option>
                                                    <option value="1">Monday</option>
                                                    <option value="2">Tuesday</option>
                                                    <option value="3">Wednesday</option>
                                                    <option value="4">Thursday</option>
                                                    <option value="5">Friday</option>
                                                    <option value="6">Saturday</option>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="time" id="open" name="open" value={formData.open} placeholder = "Opening time"
                                                    onChange={(e) => setFormData({ ...formData, open: e.target.value })}
                                                />
                                            </td>
                                            <td>
                                                <input type="time" id="close" name="close" value={formData.close} placeholder = "Closing time"
                                                    onChange={(e) => setFormData({ ...formData, close: e.target.value })}
                                                />
                                            </td>
                                        </tr>
                                    )}
                                    {type === "holidays" && (
                                        <tr>
                                            <td>
                                                <input type="date" id="start" name="start" value={formData.start} placeholder = "Start date"
                                                    onChange={(e) => setFormData({ ...formData, start: e.target.value })}
                                                />
                                            </td>
                                            <td>
                                                <input type="date" id="end" name="end" value={formData.end} placeholder = "End date"
                                                    onChange={(e) => setFormData({ ...formData, end: e.target.value })}
                                                />
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </form>
                    )}
            { !error && showForm && formData.type === type && (
                <>
                    <div>
                            <button onClick={() => {handleSave(type)}}>Save</button>
                            <button onClick={handleCancel}>Cancel</button>
                    </div>
                    <br/><br/>
                </>
            )}
            {error && formData.type === type && (
                <div id="error">{error.response.data.message}</div>
            )}
        </>
        );
    };

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

                    {error && error.response.status !== 404 && Object.keys(business).length !== 0 && (
                        <div id="error">{error.response.data.message}</div>
                    )}

                    {error && error.response.status === 404 && Object.keys(business).length === 0 && (
                        <>
                            <form>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Business Name</th>
                                            <th>Business Email</th>
                                            <th>Business Phone</th>
                                            <th>Latitude</th>
                                            <th>Longitude</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <input type="text" id="businessName" name="businessName" onChange={(e) => setFormData({ ...formData, businessName: e.target.value })} />
                                            </td>
                                            <td>
                                                <input type="email" id="businessEmail" name="businessEmail" onChange={(e) => setFormData({ ...formData, businessEmail: e.target.value })} />
                                            </td>
                                            <td>
                                                <input type="tel" id="businessPhone" name="businessPhone" onChange={(e) => setFormData({ ...formData, businessPhone: e.target.value })} />
                                            </td>
                                            <td>
                                                <input type="number" id="latitude" name="latitude" onChange={(e) => setFormData({ ...formData, latitude: e.target.value })} />
                                            </td>
                                            <td>
                                                <input type="number" id="longitude" name="longitude" onChange={(e) => setFormData({ ...formData, longitude: e.target.value })} />
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                            <div>
                                <button onClick={() => {handleSave("main")}}>Save</button>
                                <button onClick={handleCancel}>Cancel</button>
                            </div>
                        </>
                    )}

                    {
                        <>
                            {Object.keys(business).length !== 0 && (
                                <>
                                    <h3>Main Business Information</h3>
                                    {renderMainResults(business)}
                                </>
                            )}

                            {businessHours.length > 0 ?
                            (
                                <>
                                    <h3>Business Schedules</h3>
                                    {renderSchedulesResults(businessHours)}
                                    {businessHours.length < 7 ? renderAddButton(businessHours) : null}
                                </>
                            ) : businessHours.length === 0 && Object.keys(business).length !== 0 ? (
                                <>
                                    <h3>Business Schedules</h3>
                                    {renderAddButton(businessHours)}
                                </>
                            ) : null}

                            {businessHolidays.length > 0 ? (
                                <>
                                    <h3>Business Holidays</h3>
                                    {renderHolidaysResults(businessHolidays)}
                                    {renderAddButton(businessHolidays)}
                                </>
                            ) : businessHolidays.length === 0 && Object.keys(business).length !== 0 ? (
                                <>
                                    <h3>Business Holidays</h3>
                                    {renderAddButton(businessHolidays)}
                                </>

                            ) : null}
                        </>
                    }

                </div>
            </section>
        </>
    );

};

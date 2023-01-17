import React from 'react';

// Map the results of type "main" to a table
export const renderMainResults = (results, type = "default", formData, setFormData, editingId = 0, editing = "business", setEditing, setEditingId, callBackFunction) => {
    if (Object.keys(results).length === 0) return '';

    return (
        <table className="table table-dark">
            <thead>
                <tr>
                    <th>Business Name</th>
                    <th>Business Email</th>
                    <th>Business Phone</th>
                    <th>Latitude</th>
                    <th>Longitude</th>
                    {type != "default" && (<th>Action</th>)}
                </tr>
            </thead>
            <tbody>
                <tr key={results.businessId}>
                    {editing != "business" || editingId != results.businessId ? (
                        <>
                            <td>{results.businessName}</td>
                            <td>{results.businessEmail}</td>
                            <td>{results.businessPhone}</td>
                            <td>{results.latitude}</td>
                            <td>{results.longitude}</td>
                            {type != "default" && (<td><button className="btn btn-primary" key = {results.businessId} onClick={() => {
                                setEditingId(results.businessId);
                                setEditing("business");
                                setFormData({
                                    ...formData,
                                    id:results.businessId,
                                    type:"main",
                                    businessName: results.businessName,
                                    businessEmail: results.businessEmail,
                                    businessPhone: results.businessPhone,
                                    latitude: results.latitude,
                                    longitude: results.longitude
                                })
                            }}>{type.toUpperCase()}</button></td>)}
                        </>
                    ):(
                        <>
                            {type === "edit" ?
                            (   <>
                                    <td><input type = "text" name = "businessName" value = {formData.businessName} onChange={(e) => setFormData({...formData, businessName:e.target.value})}></input></td>
                                    <td><input type = "text" name = "businessEmail" value = {formData.businessEmail} onChange={(e) => setFormData({...formData, businessEmail:e.target.value})}></input></td>
                                    <td><input type = "text" name = "businessPhone" value = {formData.businessPhone} onChange={(e) => setFormData({...formData, businessPhone:e.target.value})}></input></td>
                                    <td><input type = "number" name = "latitude" value = {formData.latitude} onChange={(e) => setFormData({...formData, latitude:e.target.value})}></input></td>
                                    <td><input type = "number" name = "longitude" value = {formData.longitude} onChange={(e) => setFormData({...formData, longitude:e.target.value})}></input></td>
                                </>
                            ):(
                                <>
                                    <td>{results.businessName}</td>
                                    <td>{results.businessEmail}</td>
                                    <td>{results.businessPhone}</td>
                                    <td>{results.latitude}</td>
                                    <td>{results.longitude}</td>
                                </>

                            )}
                            <td><button className="btn btn-primary" key = {results.businessId} onClick={(e) => {
                                callBackFunction(formData);
                            }}>{type === "edit" ? "SAVE" : "CONFIRM"}</button></td>
                        </>
                    )}
                </tr>
            </tbody>
        </table>
    );
}


// Map the results of type "schedules" to a table
export const renderSchedulesResults = (results, type = "default", formData, setFormData, editingId = 0, editing = false, setEditing, setEditingId, callBackFunction) => {
    if (results.length === 0) return '';

    const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    return (
        <table className="table table-dark">
            <thead>
                <tr>
                    <th>Day of Week</th>
                    <th>Opening Hours</th>
                    <th>Closing Hours</th>
                    {type != "default" && (<th>Action</th>)}
                </tr>
            </thead>
            <tbody>
                {results.map((result) => (
                    <tr key={result.hoursId}>
                        {editing != "schedules" || editingId != result.hoursId ? (
                            <>
                                <th>{days[result.dayOfWeek]}</th>
                                <th>{result.openingHours}</th>
                                <th>{result.closingHours}</th>
                                {type != "default" && (
                                    <td><button className="btn btn-primary" key = {result.hoursId} onClick={() => {
                                        setEditingId(result.hoursId);
                                        setEditing("schedules");
                                        setFormData({
                                            ...formData,
                                            id:result.hoursId,
                                            type:"schedules",
                                            dayOfWeek: result.dayOfWeek,
                                            openingHours: result.openingHours,
                                            closingHours: result.closingHours,
                                        })
                                    }}>{type.toUpperCase()}</button></td>
                                )}
                            </>
                        ):(
                            <>
                                {type === "edit" ? (
                                    <>
                                        <td><select name = "dayOfWeek" value = {formData.dayOfWeek} onChange={(e) => setFormData({...formData, dayOfWeek:e.target.value})}>
                                            <option value = "">Select a week day to add</option>
                                            <option value="0">Sunday</option>
                                            <option value="1">Monday</option>
                                            <option value="2">Tuesday</option>
                                            <option value="3">Wednesday</option>
                                            <option value="4">Thursday</option>
                                            <option value="5">Friday</option>
                                            <option value="6">Saturday</option>
                                        </select></td>
                                        <td><input type = "time" name = "openingHours" value = {formData.openingHours} onChange={(e) => setFormData({...formData, openingHours:e.target.value})}></input></td>
                                        <td><input type = "time" name = "closingHours" value = {formData.closingHours} onChange={(e) => setFormData({...formData, closingHours:e.target.value})}></input></td>
                                    </>
                                ) : (
                                    <>
                                        <th>{days[result.dayOfWeek]}</th>
                                        <th>{result.openingHours}</th>
                                        <th>{result.closingHours}</th>
                                    </>
                                )}
                                <td><button className="btn btn-primary" key = {result.hoursId} onClick={(e) => {
                                    callBackFunction(formData);
                                }}>{type === "edit" ? "SAVE" : "CONFIRM"}</button></td>
                            </>
                        )}
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

// Map the results of type "holidays" to a table
export const renderHolidaysResults = (results, type = "default", formData, setFormData, editingId = 0, editing = false, setEditing, setEditingId, callBackFunction) => {
    if (results.length === 0) return '';

    return (
        <table className="table table-dark">
            <thead>
                <tr>
                    <th>Calendar Holiday Start</th>
                    <th>Calendar Holiday End</th>
                    {type != "default" && (<th>Action</th>)}
                </tr>
            </thead>
            <tbody>
                {results.map((result) => (
                    <tr key={result.holidaysId}>
                        {editing != "holidays" || editingId != result.holidaysId ? (
                            <>
                                <th>
                                    {result.calendarHolidayStart}
                                </th>
                                <th>
                                    {result.calendarHolidayEnd}
                                </th>
                                {type != "default" && (
                                    <td><button className="btn btn-primary" key = {result.holidaysId} onClick={() => {
                                        setEditingId(result.holidaysId);
                                        setEditing("holidays");
                                        setFormData({
                                            ...formData,
                                            id:result.holidaysId,
                                            type:"holidays",
                                            calendarHolidayStart: result.calendarHolidayStart,
                                            calendarHolidayEnd: result.calendarHolidayEnd,
                                        })
                                    }}>{type.toUpperCase()}</button></td>
                                )}
                            </>
                        ):(
                            <>
                                {type === "edit" ? (
                                    <>
                                        <td><input type = "date" name = "calendarHolidayStart" value = {formData.calendarHolidayStart} onChange={(e) => setFormData({...formData, calendarHolidayStart:e.target.value})}></input></td>
                                        <td><input type = "date" name = "calendarHolidayEnd" value = {formData.calendarHolidayEnd} onChange={(e) => setFormData({...formData, calendarHolidayEnd:e.target.value})}></input></td>
                                    </>
                                ) : (
                                    <>
                                        <th>
                                            {result.calendarHolidayStart}
                                        </th>
                                        <th>
                                            {result.calendarHolidayEnd}
                                        </th>
                                    </>
                                )}
                                <td><button className="btn btn-primary" key = {result.holidaysId} onClick={(e) => {
                                    callBackFunction(formData);
                                }}>{type === "edit" ? "SAVE" : "CONFIRM"}</button></td>
                            </>
                        )}
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

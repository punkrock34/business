import React, { useState } from 'react';
import axios from 'axios';
import { renderMainResults, renderSchedulesResults, renderHolidaysResults} from '../objects/Props.jsx';

export const GET = () => {
    const [businessEmail, setbusinessEmail] = useState('');
    const [businessName, setbusinessName]   = useState('');
    const [type, setType] = useState('main');
    const [results, setResults] = useState([]);
    const [error, setError] = useState(null); // Initialize an error state

    const resetResults = () => {
        setResults([]);
        setError(null);
    };

    const handleTypeChange = (e) => {
        resetResults();
        setType(e.target.value);
    }


    const handleSubmit = (event) => {
        event.preventDefault();

        const url = window.location.protocol + "//" + window.location.host + `/api/v1/business/${type}`;

        resetResults();

        axios.request({
                method: 'get',
                url: url,
                params:{
                    businessEmail: businessEmail,
                    businessName: businessName,
                },
            }).then((response) => {
                setResults(response.data);
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
                        <input type="text" id="business-email" name="business-email" value={businessEmail} onChange={(e) => setbusinessEmail(e.target.value)} /><br />
                        <label htmlFor="business-name">Business Name:</label><br />
                        <input type="text" id="business-name" name="business-name" value={businessName} onChange={(e) => setbusinessName(e.target.value)} /><br />
                        <label htmlFor="type">Type:</label><br />
                        <select id="type" name="type" value={type} onChange={handleTypeChange}>
                            <option value="main">Main</option>
                            <option value="schedules">Schedules</option>
                            <option value="holidays">Holidays</option>
                        </select>
                        <br /><br />
                        <button type="submit">Submit</button>
                    </form>
                </div>
            </section>

            <section className="results-search">
                <div className="container mt-5" id="results">

                    {error && (
                        <div id="error">{error.response.data.message}</div>
                    )}

                    {!error && (Object.keys(results).length !== 0 || results.length !== 0) && (
                        <>
                            {type === 'main' && renderMainResults(results)}
                            {type === 'schedules' && renderSchedulesResults(results)}
                            {type === 'holidays' && renderHolidaysResults(results)}
                        </>
                    )}
                </div>
            </section>
        </>
    );
};

import React from 'react';
import { BrowserRouter as Router, Routes, Route, Outlet } from 'react-router-dom';
import { GET } from './components/Get.jsx';
import { POST } from './components/Post.jsx';
import { PUT } from './components/Put.jsx';
import { DELETE } from './components/Delete.jsx';

export const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/create" element={<POST />} />
                <Route path="/get" element={<GET />} />
                <Route path="/update" element={<PUT />} />
                <Route path="/delete" element={<DELETE />} />
            </Routes>
            <Outlet />
        </Router>
    );
};

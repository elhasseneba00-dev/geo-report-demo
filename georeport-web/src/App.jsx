import React from "react";
import { NavLink, Route, Routes } from "react-router-dom";
import ObservationsPage from "./pages/ObservationsPage.jsx";
import MapPage from "./pages/MapPage.jsx";

export default function App() {
    return (
        <div className="container">
            <header className="header">
                <h2>GeoReport</h2>
                <nav className="nav">
                    <NavLink to="/" end className={({ isActive }) => (isActive ? "active" : "")}>
                        Observations
                    </NavLink>
                    <NavLink to="/map" className={({ isActive }) => (isActive ? "active" : "")}>
                        Carte
                    </NavLink>
                </nav>
            </header>

            <Routes>
                <Route path="/" element={<ObservationsPage />} />
                <Route path="/map" element={<MapPage />} />
            </Routes>
        </div>
    );
}
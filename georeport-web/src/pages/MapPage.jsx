import React, { useEffect, useMemo, useState } from "react";
import { MapContainer, TileLayer, GeoJSON } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { getGeoJson } from "../api/observations";

const TYPES = ["", "INCIDENT", "INFRASTRUCTURE", "DANGER", "ENQUETE", "AUTRES"];

export default function MapPage() {
    const [geo, setGeo] = useState(null);
    const [type, setType] = useState("");
    const [err, setErr] = useState("");

    const center = useMemo(() => [18.1, -15.95], []);

    useEffect(() => {
        (async () => {
            setErr("");
            try {
                const data = await getGeoJson({ type: type || undefined });
                setGeo(data);
            } catch (e) {
                setErr(e?.response?.data?.error || e.message);
            }
        })();
    }, [type]);

    return (
        <div className="card">
            <div className="toolbar">
                <h3>Carte (Leaflet + GeoJSON)</h3>
                <select value={type} onChange={(e) => setType(e.target.value)}>
                    {TYPES.map((t) => (
                        <option key={t} value={t}>
                            {t === "" ? "Tous" : t}
                        </option>
                    ))}
                </select>
            </div>

            {err ? <pre className="error">{err}</pre> : null}

            <MapContainer center={center} zoom={12} style={{ height: 520, width: "100%" }}>
                <TileLayer
                    attribution='&copy; OpenStreetMap contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                {geo ? <GeoJSON data={geo} /> : null}
            </MapContainer>
        </div>
    );
}
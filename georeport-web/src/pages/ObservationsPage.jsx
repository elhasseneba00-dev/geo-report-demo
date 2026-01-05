import React, { useEffect, useState } from "react";
import { createObservation, importCsv, listObservations } from "../api/observations";

const TYPES = ["", "INCIDENT", "INFRASTRUCTURE", "DANGER", "ENQUETE", "AUTRES"];

export default function ObservationsPage() {
    const [items, setItems] = useState([]);
    const [typeFilter, setTypeFilter] = useState("");
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");

    const [form, setForm] = useState({
        titre: "",
        type: "INCIDENT",
        description: "",
        lat: 18.1,
        lon: -15.9,
        observationAt: new Date().toISOString(),
        source: "Manuelle",
    });

    async function refresh() {
        setLoading(true);
        setErr("");
        try {
            const data = await listObservations({ type: typeFilter || undefined });
            setItems(data);
        } catch (e) {
            setErr(e?.response?.data?.error || e.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        refresh();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [typeFilter]);

    async function onSubmit(e) {
        e.preventDefault();
        setErr("");
        try {
            await createObservation({
                ...form,
                lat: Number(form.lat),
                lon: Number(form.lon),
            });
            await refresh();
            setForm((f) => ({ ...f, titre: "", description: "" }));
        } catch (e2) {
            setErr(JSON.stringify(e2?.response?.data || e2.message));
        }
    }

    async function onImport(e) {
        const file = e.target.files?.[0];
        if (!file) return;
        setErr("");
        try {
            const res = await importCsv(file);
            await refresh();
            alert(`Import terminé. created=${res.created}, errors=${res.errors.length}`);
        } catch (e2) {
            setErr(JSON.stringify(e2?.response?.data || e2.message));
        } finally {
            e.target.value = "";
        }
    }

    return (
        <div className="grid">
            <section className="card">
                <h3>Ajouter une observation</h3>
                <form onSubmit={onSubmit} className="form">
                    <label>
                        Titre
                        <input value={form.titre} onChange={(e) => setForm({ ...form, titre: e.target.value })} required />
                    </label>

                    <label>
                        Type
                        <select value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value })}>
                            {TYPES.map((t) => (
                                <option key={t} value={t}>
                                    {t}
                                </option>
                            ))}
                        </select>
                    </label>

                    <label>
                        Description
                        <input value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
                    </label>

                    <div className="row">
                        <label>
                            Latitude
                            <input type="number" step="0.000001" value={form.lat} onChange={(e) => setForm({ ...form, lat: e.target.value })} />
                        </label>
                        <label>
                            Longitude
                            <input type="number" step="0.000001" value={form.lon} onChange={(e) => setForm({ ...form, lon: e.target.value })} />
                        </label>
                    </div>

                    <label>
                        Date d'Observation (ISO)
                        <input value={form.observationAt} onChange={(e) => setForm({ ...form, observationAt: e.target.value })} />
                    </label>

                    <label>
                        Source
                        <input value={form.source} onChange={(e) => setForm({ ...form, source: e.target.value })} required />
                    </label>

                    <button type="submit">Créer</button>
                </form>

                <hr />

                <h3>Import CSV (collecte)</h3>
                <input type="file" accept=".csv" onChange={onImport} />
                <p className="hint">CSV: titre,type,description,lat,lon,observationAt,source</p>

                {err ? <pre className="error">{err}</pre> : null}
            </section>

            <section className="card">
                <div className="toolbar">
                    <h3>Observations</h3>
                    <select value={typeFilter} onChange={(e) => setTypeFilter(e.target.value)}>
                        <option value="">Tous</option>
                        {TYPES.map((t) => (
                            <option key={t} value={t}>
                                {t}
                            </option>
                        ))}
                    </select>
                </div>

                {loading ? <p>Chargement...</p> : null}

                <table className="table">
                    <thead>
                    <tr>
                        <th>Titre</th>
                        <th>Type</th>
                        <th>Latitude</th>
                        <th>Longitude</th>
                        <th>Date d'Observation</th>
                        <th>Source</th>
                    </tr>
                    </thead>
                    <tbody>
                    {items.map((o) => (
                        <tr key={o.id}>
                            <td>{o.titre}</td>
                            <td>{o.type}</td>
                            <td>{o.lat}</td>
                            <td>{o.lon}</td>
                            <td>{o.observationAt}</td>
                            <td>{o.source}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </section>
        </div>
    );
}
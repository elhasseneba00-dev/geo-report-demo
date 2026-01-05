import { api } from "./client";

export async function listObservations({ type } = {}) {
    const params = {};
    if (type) params.type = type;
    const res = await api.get("/api/observations", { params });
    return res.data;
}

export async function createObservation(payload) {
    const res = await api.post("/api/observations", payload);
    return res.data;
}

export async function getGeoJson({ type } = {}) {
    const params = {};
    if (type) params.type = type;
    const res = await api.get("/api/observations/geo-json", { params });
    return res.data;
}

export async function importCsv(file) {
    const form = new FormData();
    form.append("file", file);
    const res = await api.post("/api/import/observations/csv", form, {
        headers: { "Content-Type": "multipart/form-data" },
    });
    return res.data;
}
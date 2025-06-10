package com.findamatch.model.geocoding;

import com.findamatch.model.Ubicacion;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleGeocoderAdapter implements Geocoder {
    private final String apiKey = "AIzaSyBvA95PglnPbHlLZeGn8R3aW-XcVBlnkBw";

    public GoogleGeocoderAdapter() {
    }

    @Override
    public Ubicacion getUbicacion(Ubicacion ubicacion) throws Exception {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(ubicacion.getDireccion(), StandardCharsets.UTF_8) +
                "&key=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Error al obtener coordenadas: " + response);
        }

        String json = response.body().string();
        JSONObject obj = new JSONObject(json);
        JSONArray results = obj.getJSONArray("results");

        if (results.isEmpty()) {
            throw new RuntimeException("No se encontraron coordenadas para: " + ubicacion.getDireccion());
        }

        JSONObject location = results.getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");

        ubicacion.setLatitud(lat);
        ubicacion.setLongitud(lng);
        ubicacion.setVarianza(0); // podés calcularlo después si querés

        return ubicacion;
    }
}
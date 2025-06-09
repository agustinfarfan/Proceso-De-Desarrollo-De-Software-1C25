package com.findamatch.model.geocoding;

import com.findamatch.model.Ubicacion;

public interface Geocoder {
    Ubicacion getUbicacion(Ubicacion ubicacion) throws Exception;
}
package com.targetapps.despierta;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by JOKO on 11/08/2015.
 */
public class UserInfoWindowAdapter2 implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater=null;
    private android.widget.Button btSi,btNo;


    UserInfoWindowAdapter2(LayoutInflater Inflater) {
        this.inflater = Inflater;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View infoWindow = inflater.inflate(R.layout.user_info_windows2, null);

        TextView Titulo = (TextView)infoWindow.findViewById(R.id.titulo);
        TextView Desc = (TextView)infoWindow.findViewById(R.id.descripcion);
        btSi = (android.widget.Button)infoWindow.findViewById(R.id.btAlerta);


        Titulo.setText(marker.getTitle());
        Desc.setText(marker.getSnippet());


        return infoWindow;
    }
}
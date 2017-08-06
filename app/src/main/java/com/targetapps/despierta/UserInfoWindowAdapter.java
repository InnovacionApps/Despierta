package com.targetapps.despierta;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by JOKO on 15/04/2015.
 */
public class UserInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater=null;
    private Button btSi,btNo;


    UserInfoWindowAdapter(LayoutInflater Inflater) {
        this.inflater = Inflater;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View infoWindow = inflater.inflate(R.layout.user_info_windows, null);

        TextView Titulo = (TextView)infoWindow.findViewById(R.id.titulo);
        TextView Desc = (TextView)infoWindow.findViewById(R.id.descripcion);
        btSi = (Button)infoWindow.findViewById(R.id.btAlerta);


        Titulo.setText(marker.getTitle());
        Desc.setText(marker.getSnippet());


        return infoWindow;
    }
}

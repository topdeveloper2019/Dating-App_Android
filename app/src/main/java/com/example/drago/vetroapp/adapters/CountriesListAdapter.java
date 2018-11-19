package com.example.drago.vetroapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.drago.vetroapp.R;
import java.util.Locale;

public class CountriesListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public CountriesListAdapter(Context context, String[] values) {
        super(context, R.layout.country_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.country_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.countrynameTextItem);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.countryflagImageItem);

        String[] g=values[position].split(",");
        textView.setText(GetCountryZipCode(g[1]).trim());


        String pngName = "c"+g[0].trim().toLowerCase();
        Toast.makeText(context, pngName, Toast.LENGTH_SHORT).show();
        imageView.setImageResource(context.getResources().getIdentifier("drawable/" + pngName, null, context.getPackageName()));
        return rowView;
    }

    private String GetCountryZipCode(String ssid){
        Locale loc = new Locale("", ssid);

        return loc.getDisplayCountry().trim();
    }
}

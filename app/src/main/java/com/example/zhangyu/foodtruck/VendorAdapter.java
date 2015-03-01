package com.example.zhangyu.foodtruck;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhangyu.foodtruck.entities.Event;
import com.example.zhangyu.foodtruck.entities.Vender;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class VendorAdapter extends ArrayAdapter<Vender> {
    private int resource;

    private static final String TAG = "VendorAdapter";

    public VendorAdapter(Context context, int textViewResourceId, List<Vender> objects) {
        super(context, textViewResourceId, objects);
        resource = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vender vender = getItem(position);
        LinearLayout layout = null;
        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        TextView nameView = (TextView) layout.findViewById(R.id.name);
        TextView occurrence = (TextView) layout.findViewById(R.id.occurrence);

        LinearLayout dateLayout = (LinearLayout) layout.findViewById(R.id.date_layout);

        nameView.setText(vender.getName());
        occurrence.setText(vender.getFrequency() + "");

        ImageView head_portrait = (ImageView) layout.findViewById(R.id.head_portrait);

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(6);

        // Display some pictures.
        switch (randomInt) {
            case 0:
                head_portrait.setImageResource(R.drawable.hanburge2);
                break;

            case 1:
                head_portrait.setImageResource(R.drawable.food7);
                break;

            case 2:
                head_portrait.setImageResource(R.drawable.food8);
                break;

            case 3:
                head_portrait.setImageResource(R.drawable.food9);
                break;

            case 4:
                head_portrait.setImageResource(R.drawable.food10);
                break;

            case 5:
                head_portrait.setImageResource(R.drawable.food11);
                break;

            default:
                head_portrait.setImageResource(R.drawable.food12);
                break;
        }

        dateLayout.setVisibility(View.GONE);
        return layout;
    }
}


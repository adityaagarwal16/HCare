package com.hcare.homeopathy.hcare.Disease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcare.homeopathy.hcare.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class CustomAdapter extends ArrayAdapter<String> {


    String[] spinnerTitles;
    int[] spinnerImages;

    Context mContext;

    public CustomAdapter(@NonNull Context context, String[] titles, int[] images) {
        super(context, R.layout.custom_spinner_row);
        this.spinnerTitles = titles;
        this.spinnerImages = images;

        this.mContext = context;
    }
    @Override
    public int getCount() {
        return spinnerTitles.length;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_row, parent, false);
            mViewHolder.mFlag = convertView.findViewById(R.id.diseaseimage);
            mViewHolder.mName = convertView.findViewById(R.id.diseasename);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);


        return convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;

    }

}

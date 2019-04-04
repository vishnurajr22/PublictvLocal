package com.tracking.m2comsys.adapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tracking.m2comsys.adapplication.R;

import java.util.ArrayList;

/**
 * Created by m2 on 12/9/17.
 */

public class AdapterGridView extends BaseAdapter {
    Context context;
    ArrayList<VideoDetailsModel> videolist;
    private static LayoutInflater inflater = null;
    public AdapterGridView(Context context,ArrayList<VideoDetailsModel> videolist) {
        this.context=context;
        this.videolist=videolist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return videolist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.gridiew_layout, null);

        TextView videoName = (TextView) view.findViewById(R.id.videoNameText);
        TextView noOfTimesPlayed = (TextView) view.findViewById(R.id.videoNoOfTimesPlayedText);
        TextView percentageText = (TextView) view.findViewById(R.id.videoPercentageText);
        TextView impressionText = (TextView) view.findViewById(R.id.impressionsText);
        TextView statusText = (TextView) view.findViewById(R.id.videoStatus);
        TextView errorCountText = (TextView) view.findViewById(R.id.errorCountDisp);

        VideoDetailsModel v = new VideoDetailsModel();

        for(int it=0;it<videolist.size();it++){
            try {
                v = videolist.get(i);
                errorCountText.setText(String.valueOf(v.getErrorCount()));
                videoName.setText(String.valueOf(v.getVideo_Name()));
                System.out.print(v.getVideo_Name());
                impressionText.setText(String.valueOf(v.getImpressions()));
                noOfTimesPlayed.setText(String.valueOf(v.getVideo_NoOfTimesPlayed()));
                percentageText.setText(String.valueOf(v.getPercentage()));
                statusText.setText(v.getStatus());

            }catch(Exception exp){
                LogWriter.writeLogException("GridFill",exp);
            }

        }


        return view;
    }
}

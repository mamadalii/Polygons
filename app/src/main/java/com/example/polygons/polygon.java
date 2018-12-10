package com.example.polygons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class polygon implements Parcelable {
//Fields
    //points of an Polygon;
   private ArrayList<LatLng> points= new ArrayList<>();
private int milliTime;
private String tag;

    public polygon(ArrayList<LatLng> points, String tag) {
        this.points = points;
        this.tag = tag;
        this.milliTime=0;
    }

    protected polygon(Parcel in) {
        points = in.createTypedArrayList(LatLng.CREATOR);
        milliTime = in.readInt();
        tag = in.readString();
    }

    public static final Creator<polygon> CREATOR = new Creator<polygon>() {
        @Override
        public polygon createFromParcel(Parcel in) {
            return new polygon(in);
        }

        @Override
        public polygon[] newArray(int size) {
            return new polygon[size];
        }
    };

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.points = points;
    }

    public int getMilliTime() {
        return milliTime;
    }

    public void setMilliTime(int milliTime) {
        this.milliTime = milliTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(points);
        dest.writeInt(milliTime);
        dest.writeString(tag);
    }
}

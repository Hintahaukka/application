package hifian.hintahaukka.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class ParcelableHashMap<K, V> extends HashMap<K, V> implements Parcelable {

    public ParcelableHashMap() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableHashMap> CREATOR = new Creator<ParcelableHashMap>() {
        @Override
        public ParcelableHashMap createFromParcel(Parcel in) {
            return null;
        }

        @Override
        public ParcelableHashMap[] newArray(int size) {
            return new ParcelableHashMap[size];
        }
    };
}

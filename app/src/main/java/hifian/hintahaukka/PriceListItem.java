package hifian.hintahaukka;

import android.os.Parcelable;
import android.os.Parcel;

public class PriceListItem implements Parcelable {

    // made Parcelable so can be passed array from fragment to another

    private String storeId;
    private String timestamp;
    private int cents;

    public PriceListItem(int cents, String storeId, String timestamp) {
        this.cents = cents;
        this.storeId = storeId;
        this.timestamp = timestamp;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public int getCents() {
        return cents;
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel out, int flags) {
        out.writeString(storeId);
        out.writeString(timestamp);
        out.writeInt(cents);
    }


    public static final Parcelable.Creator<PriceListItem> CREATOR
            = new Parcelable.Creator<PriceListItem>() {
        @Override
        public PriceListItem createFromParcel(Parcel in) {
            return new PriceListItem(in);
        }

        @Override
        public PriceListItem[] newArray(int size) {
            return new PriceListItem[size];
        }
    };


    private PriceListItem(Parcel in) {
        storeId = in.readString();
        timestamp = in.readString();
        cents = in.readInt();
    }

}

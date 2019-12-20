package hifian.hintahaukka.Domain;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * PriceListItem represents the price of a given product, spotted at a given time in a given store
 */
public class PriceListItem implements Parcelable {

    private String storeId;
    private String timestamp;
    private int cents;
    private String ean;

    public PriceListItem(int cents, String storeId, String timestamp, String ean) {
        this.cents = cents;
        this.storeId = storeId;
        this.timestamp = timestamp;
        this.ean = ean;
    }

    public String getEan() { return ean; }

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
        out.writeString(ean);
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
        ean = in.readString();
    }

}

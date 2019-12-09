package hifian.hintahaukka.Domain;

import android.os.Parcelable;
import android.os.Parcel;

public class PricesInStore implements Parcelable {

    // made Parcelable so can be passed array from fragment to another

    private String storeId;
    private int centsTotal;
    private PriceListItem[] prices;

    public PricesInStore(String storeId, int centsTotal, PriceListItem[] prices) {
        this.storeId = storeId;
        this.centsTotal = centsTotal;
        this.prices = prices;
    }

    public String getStoreId() {
        return storeId;
    }

    public int getCentsTotal() {
        return centsTotal;
    }

    public PriceListItem[] getPrices() {
        return prices;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(storeId);
        out.writeInt(centsTotal);
        out.writeTypedArray(prices, flags);
    }

    public static final Parcelable.Creator<PricesInStore> CREATOR
            = new Parcelable.Creator<PricesInStore>() {
        @Override
        public PricesInStore createFromParcel(Parcel in) {
            return new PricesInStore(in);
        }

        @Override
        public PricesInStore[] newArray(int size) {
            return new PricesInStore[size];
        }
    };

    private PricesInStore(Parcel in) {
        storeId = in.readString();
        centsTotal = in.readInt();
        prices = in.createTypedArray(PriceListItem.CREATOR);
    }

}

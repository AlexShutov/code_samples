package com.lodoss.examples.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 26/01/17.
 */

public class CountryData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paymentAllowed")
    @Expose
    private Boolean paymentAllowed;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     *
     * @param countryCode
     * The countryCode
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The paymentAllowed
     */
    public Boolean getPaymentAllowed() {
        return paymentAllowed;
    }

    /**
     *
     * @param paymentAllowed
     * The paymentAllowed
     */
    public void setPaymentAllowed(Boolean paymentAllowed) {
        this.paymentAllowed = paymentAllowed;
    }

    public CountryData(){}

    protected CountryData(Parcel in) {
        id = in.readString();
        countryCode = in.readString();
        name = in.readString();
        byte paymentAllowedVal = in.readByte();
        paymentAllowed = paymentAllowedVal == 0x02 ? null : paymentAllowedVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(countryCode);
        dest.writeString(name);
        if (paymentAllowed == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (paymentAllowed ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CountryData> CREATOR = new Parcelable.Creator<CountryData>() {
        @Override
        public CountryData createFromParcel(Parcel in) {
            return new CountryData(in);
        }

        @Override
        public CountryData[] newArray(int size) {
            return new CountryData[size];
        }
    };

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CountryData)) {
            return false;
        }
        CountryData rhs = ((CountryData) other);
        return new EqualsBuilder()
                .append(id, rhs.id)
                .isEquals();
    }
}

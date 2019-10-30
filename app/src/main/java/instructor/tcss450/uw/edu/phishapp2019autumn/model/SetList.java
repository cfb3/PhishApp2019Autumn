package instructor.tcss450.uw.edu.phishapp2019autumn.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class to encapsulate a Phish.net Set Lists. Building an Object requires a publish date and title.
 *
 * Optional fields include URL, teaser, and Author.
 *
 *
 * @author Charles Bryan
 * @version 14 September 2018
 */
public final class SetList implements Serializable, Parcelable {

    private final String mDate;
    private final String mLocation;
    private final String mUrl;
    private final String mSongList;
    private final String mNotes;
    private final double mRating;
    private final String mArtist;
    private final String mVenue;

    private SetList(Parcel in) {
        mDate = in.readString();
        mLocation = in.readString();
        mUrl = in.readString();
        mSongList = in.readString();
        mNotes = in.readString();
        mRating = in.readDouble();
        mArtist = in.readString();
        mVenue = in.readString();
    }

    public static final Creator<SetList> CREATOR = new Creator<SetList>() {
        @Override
        public SetList createFromParcel(Parcel in) {
            return new SetList(in);
        }

        @Override
        public SetList[] newArray(int size) {
            return new SetList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDate);
        dest.writeString(mLocation);
        dest.writeString(mUrl);
        dest.writeString(mSongList);
        dest.writeString(mNotes);
        dest.writeDouble(mRating);
        dest.writeString(mArtist);
        dest.writeString(mVenue);
    }


    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private final String mDate;
        private final String mLocation;
        private final String mVenue;
        private String mUrl = "";
        private String mSongList = "";
        private String mNotes = "";
        private double mRating = -1.0;
        private String mArtist = "";



        /**
         * Constructs a new Builder.
         *
         * @param date the date of the show
         * @param location the location of the show
         * @param venue the venue of the show
         */
        public Builder(String date, String location, String venue) {
            mDate = date;
            mLocation = location;
            mVenue = venue;
        }

        /**
         * Add an optional url for the full show post.
         * @param val an optional url for the full show post.
         * @return the Builder of this SetList
         */
        public Builder addUrl(final String val) {
            mUrl = val;
            return this;
        }

        /**
         * Add an optional song list for the show.
         * @param val an optional song list for the show.
         * @return the Builder of this SetList
         */
        public Builder addSongList(final String val) {
            mSongList = val;
            return this;
        }

        /**
         * Add an optional notes for the show.
         * @param val an optional notes for the show.
         * @return the Builder of this SetList
         */
        public Builder addNotes(final String val) {
            mNotes = val;
            return this;
        }

        /**
         * Add an optional rating for the show.
         * @param val an optional rating for the show.
         * @return the Builder of this SetList
         */
        public Builder addRating(final double val) {
            mRating = val;
            return this;
        }

        /**
         * Add an optional artist for the show.
         * @param val an optional artist for the show.
         * @return the Builder of this SetList
         */
        public Builder addArtist(final String val) {
            mArtist = val;
            return this;
        }


        public SetList build() {
            return new SetList(this);
        }

    }

    private SetList(final Builder builder) {
        mDate = builder.mDate;
        mLocation = builder.mLocation;
        mUrl = builder.mUrl;
        mSongList = builder.mSongList;
        mNotes = builder.mNotes;
        mRating = builder.mRating;
        mVenue = builder.mVenue;
        mArtist = builder.mArtist;
    }

    public String getDate() {
        return mDate;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getSongList() {
        return mSongList;
    }

    public String getNotes() {
        return mNotes;
    }

    public double getRating() { return mRating; }

    public String getVenue() { return mVenue; }

    public String getArtist() { return mArtist; }

    @Override
    public String toString() {
        return getDate() + " " + getLocation();
    }
}

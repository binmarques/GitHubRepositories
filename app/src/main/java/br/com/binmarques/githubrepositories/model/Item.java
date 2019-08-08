package br.com.binmarques.githubrepositories.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Marques on 24/07/2018
 */

@Entity(tableName = "repositories")
public class Item implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    private long id;

    private String name;

    private String description;

    private long forks;

    @Expose
    @SerializedName("stargazers_count")
    @ColumnInfo(name = "star_count")
    private long starCount;

    @Embedded
    private Owner owner;

    public Item() {}

    @Ignore
    public Item(long id, String name, String description, long forks, long starCount, Owner owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.forks = forks;
        this.starCount = starCount;
        this.owner = owner;
    }

    protected Item(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        forks = in.readLong();
        starCount = in.readLong();
        owner = in.readParcelable(Owner.class.getClassLoader());
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long forks) {
        this.forks = forks;
    }

    public long getStarCount() {
        return starCount;
    }

    public void setStarCount(long starCount) {
        this.starCount = starCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", forks=" + forks +
                ", starCount=" + starCount +
                ", owner=" + owner +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(forks);
        dest.writeLong(starCount);
        dest.writeParcelable(owner, flags);
    }
}

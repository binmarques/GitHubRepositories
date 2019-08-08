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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Daniel Marques on 25/07/2018
 */

@Entity(tableName = "pullrequests")
public class GitHubPullRequest implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "pull_request_id")
    private long id;

    private String title;

    private String body;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    @Expose
    @SerializedName("html_url")
    @ColumnInfo(name = "html_url")
    private String htmlUrl;

    @Embedded
    private User user;

    @Embedded
    private Item item;

    public GitHubPullRequest() {}

    @Ignore
    public GitHubPullRequest(long id, String title, String body, String createdAt,
                             String htmlUrl, User user, Item item) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.htmlUrl = htmlUrl;
        this.user = user;
        this.item = item;
    }

    protected GitHubPullRequest(Parcel in) {
        id = in.readLong();
        title = in.readString();
        body = in.readString();
        createdAt = in.readString();
        htmlUrl = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        item = in.readParcelable(Item.class.getClassLoader());
    }

    public static final Creator<GitHubPullRequest> CREATOR = new Creator<GitHubPullRequest>() {
        @Override
        public GitHubPullRequest createFromParcel(Parcel in) {
            return new GitHubPullRequest(in);
        }

        @Override
        public GitHubPullRequest[] newArray(int size) {
            return new GitHubPullRequest[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "GitHubPullRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", user=" + user +
                ", item=" + item +
                '}';
    }

    private static String formatDate(String dateTime) throws Exception {
        DateFormat readFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

        Date date = readFormat.parse(dateTime);
        DateFormat writeFormat =
                new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "br"));

        return writeFormat.format(date);
    }

    public String getDateFormatted() throws Exception {
        return formatDate(getCreatedAt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(createdAt);
        dest.writeString(htmlUrl);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(item, flags);
    }
}

package com.example.newsfeed.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.newsfeed.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@Entity(tableName = "news_feed_table")
public class News {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;
    @ColumnInfo(name = "section_id")
    @SerializedName("sectionId")
    @Expose
    private String sectionId;
    @ColumnInfo(name = "section_name")
    @SerializedName("sectionName")
    @Expose
    private String sectionName;
    @ColumnInfo(name = "web_publication_date")
    @SerializedName("webPublicationDate")
    @Expose
    private String webPublicationDate;
    @ColumnInfo(name = "web_title")
    @SerializedName("webTitle")
    @Expose
    private String webTitle;
    @ColumnInfo(name = "web_url")
    @SerializedName("webUrl")
    @Expose
    private String webUrl;
    @ColumnInfo(name = "api_url")
    @SerializedName("apiUrl")
    @Expose
    private String apiUrl;
    @Embedded
    @SerializedName("fields")
    @Expose
    private Fields fields;
    @ColumnInfo(name = "is_hosted")
    @SerializedName("isHosted")
    @Expose
    private boolean isHosted;
    @ColumnInfo(name = "pillar_id")
    @SerializedName("pillarId")
    @Expose
    private String pillarId;
    @ColumnInfo(name = "pillar_name")
    @SerializedName("pillarName")
    @Expose
    private String pillarName;
    @ColumnInfo(name = "is_pinned")
    private boolean isPinned = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getFormattedDate() {
        try {
            Date date = Utils.toCalendar(webPublicationDate).getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mmm-yyyy hh:mm", Locale.getDefault());
            return dateFormat.format(date);
        } catch (ParseException e) {
            return webPublicationDate;
        }
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isHosted() {
        return isHosted;
    }

    public void setHosted(boolean hosted) {
        isHosted = hosted;
    }

    public String getPillarId() {
        return pillarId;
    }

    public void setPillarId(String pillarId) {
        this.pillarId = pillarId;
    }

    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof News) {
            News compNews = (News) obj;
            return this.id.equals(compNews.id)
                    && this.sectionId.equals(compNews.sectionId)
                    && this.sectionName.equals(compNews.sectionName)
                    && this.webPublicationDate.equals(compNews.webPublicationDate)
                    && this.webTitle.equals(compNews.webTitle)
                    && this.webUrl.equals(compNews.webUrl)
                    && this.apiUrl.equals(compNews.apiUrl)
                    && this.isHosted == compNews.isHosted
                    && this.pillarId.equals(compNews.pillarId)
                    && this.pillarName.equals(compNews.pillarName);
        } else {
            return false;
        }
    }
}

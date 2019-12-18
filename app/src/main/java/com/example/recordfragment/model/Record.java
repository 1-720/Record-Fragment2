package com.example.recordfragment.model;

public class Record {
    private int id;
    private String recordTitle;
    private String recordedTime;
    private String recordComment;
    private String dateRecordAdded;

    public Record() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }

    public String getRecordComment() {
        return recordComment;
    }

    public void setRecordComment(String recordComment) {
        this.recordComment = recordComment;
    }

    public String getDateRecordAdded() {
        return dateRecordAdded;
    }

    public void setDateRecordAdded(String dateRecordAdded) {
        this.dateRecordAdded = dateRecordAdded;
    }
}

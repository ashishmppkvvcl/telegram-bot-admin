package com.mppkvvcl.telegrambotadmin.dto;

public class RecordsUpdated {
    private Integer successRecords;
    private Integer failureRecords;
    long seconds ;
    long minutes ;

    public RecordsUpdated() {
    }

    public RecordsUpdated(Integer successRecords, Integer failureRecords, long seconds, long minutes) {
        this.successRecords = successRecords;
        this.failureRecords = failureRecords;
        this.seconds = seconds;
        this.minutes = minutes;
    }

    public Integer getSuccessRecords() {
        return successRecords;
    }

    public void setSuccessRecords(Integer successRecords) {
        this.successRecords = successRecords;
    }

    public Integer getFailureRecords() {
        return failureRecords;
    }

    public void setFailureRecords(Integer failureRecords) {
        this.failureRecords = failureRecords;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "RecordsUpdated{" +
                "successRecords=" + successRecords +
                ", failureRecords=" + failureRecords +
                ", seconds=" + seconds +
                ", minutes=" + minutes +
                '}';
    }
}

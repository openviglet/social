package com.viglet.social.video.domain.history;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VigVideoCall {
    private List<String> otherRtcIds;
    private Date started;
    private long duration;
    private boolean inProgress;
    private List<String> otherNames;

    public VigVideoCall(List<String> members, boolean inProgress, Date begin, long duration) {
        this.otherRtcIds = members;
        this.started = begin;
        this.duration = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS);
        this.inProgress = inProgress;
    }

    public List<String> getOtherRtcIds() {
        return otherRtcIds;
    }

    public Date getStarted() {
        return started;
    }

    public Long getDuration() {
        return duration;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public List<String> getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(List<String> otherNames) {
        this.otherNames = otherNames;
    }
}

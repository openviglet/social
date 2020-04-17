package com.viglet.social.persistence.model.video;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import com.viglet.social.video.domain.history.VigVideoCall;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.joda.time.DateTime.now;

@Entity
@Table(name = "Members")
public class VigVideoMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private int id;

    @Column(name = "member_rtc_id")
    private String rtcId;

    @Column(name = "connected")
    private Date connected;

    @Column(name = "disconnected")
    private Date disconnected;

    @Column(name = "left_reason")
    private String leftReason;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private VigVideoConnection connection;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private VigVideoUser user;

    /**
     * for hibernate only
     */
    @Deprecated
    VigVideoMember() {
    }

    public VigVideoMember(String memberId, VigVideoUser user) {
        this.rtcId = memberId;
        this.user = user;
        this.connected = now().toDate();
    }

    public void disconnectWithReason(Optional<String> reason) {
        disconnected = now().toDate();
        reason.ifPresent(this::setLeftReason);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)[%s - %s]", id, rtcId, connected, disconnected);
    }

    public void leaves(VigVideoConversation conversation) {
        if (connection.isFor(conversation)) {
            connection.close();
        }
    }

    private void setLeftReason(String leftReason) {
        this.leftReason = leftReason;
    }

    public int startedBefore(VigVideoMember p) {
        return connected.compareTo(p.connected);
    }

    public VigVideoCall toCall() {
        if (connection != null) {
            List<String> members = connection.getConversationMembers().stream()
                    .filter(m -> !m.equals(this))
                    .map(m -> m.rtcId)
                    .collect(toList());
            return new VigVideoCall(members, !connection.isClosed(), connection.getBegin(), connection.getDuration());
        } else {
            return new VigVideoCall(new ArrayList<>(), false, connected, getDuration());
        }
    }

    private long getDuration() {
        if (disconnected == null) {
            return new Interval(new DateTime(connected), DateTime.now()).toDurationMillis();
        }
        return new Interval(new DateTime(connected), new DateTime(disconnected)).toDurationMillis();
    }

    public String getUsername() {
        return user.getUsername();
    }
}

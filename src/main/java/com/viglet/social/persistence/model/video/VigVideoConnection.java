package com.viglet.social.persistence.model.video;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static java.util.stream.Collectors.toList;
import static org.joda.time.DateTime.now;

@Entity
@Table(name = "Connections")
public class VigVideoConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "connection_id")
    private int id;

    @JoinColumn(name = "begin")
    private Date begin;

    @JoinColumn(name = "closed")
    private Date closed;

    @JoinColumn(name = "took")
    private Long took;

    @OneToOne
    @JoinColumn(name = "group_id")
    private VigVideoMember member;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private VigVideoConversation conversation;

    @Deprecated
    VigVideoConnection() {
    }

    VigVideoConnection(VigVideoConversation conversation, VigVideoMember member) {
        this.conversation = conversation;
        this.member = member;
        begin = now().toDate();
    }

    public boolean isClosed() {
        return closed != null;
    }

    public void close() {
        closed = now().toDate();
        took = new Interval(new DateTime(begin), new DateTime(closed)).toDurationMillis();
    }

    public boolean isFor(VigVideoConversation conversation) {
        return new EqualsBuilder()
                .append(this.conversation, conversation)
                .isEquals();
    }

    public List<VigVideoMember> getConversationMembers() {
        return conversation.getConnections().stream().map(c -> c.member).collect(toList());
    }

    public Date getBegin() {
        return begin;
    }

    public long getDuration() {
        if (took != null) {
            return took;
        }
        return new Interval(new DateTime(begin), DateTime.now()).toDurationMillis();
    }
}

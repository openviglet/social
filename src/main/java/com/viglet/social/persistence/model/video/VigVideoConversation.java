package com.viglet.social.persistence.model.video;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import static org.joda.time.DateTime.now;

@Entity
@Table(name = "Conversations")
public class VigVideoConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "conversation_id")
    private int id;

    @Column(name = "conversation_name")
    private String conversationName;

    @Column(name = "created")
    private Date created;

    @Column(name = "destroyed")
    private Date destroyed;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<VigVideoConnection> connections;

    @Deprecated
    VigVideoConversation() {
    }

    public VigVideoConversation(String conversationName) {
        this.conversationName = conversationName;
        created = now().toDate();
    }

    @Override
    public String toString() {
        return String.format("(%s)[%s - %s]", conversationName, created, destroyed);
    }

    public void destroy() {
        destroyed = now().toDate();
        connections.stream()
                .filter(VigVideoConnection::isClosed)
                .forEach(VigVideoConnection::close);
    }

    public void join(VigVideoMember member) {
        connections.add(new VigVideoConnection(this, member));
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof VigVideoConversation)) return false;
        final VigVideoConversation other = (VigVideoConversation) o;
        return other.id == id;
    }

    public int hashCode() {
        return id;
    }

    public Set<VigVideoConnection> getConnections() {
        return connections;
    }
}

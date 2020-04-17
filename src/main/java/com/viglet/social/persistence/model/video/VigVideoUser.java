package com.viglet.social.persistence.model.video;

import com.viglet.social.video.domain.history.VigVideoHistory;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Users")
public class VigVideoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "auth_provider_id")
    private String authProviderId;

    @Column(name = "confirmation_key")
    private String confirmationKey;

    @Column(name = "role")
    private String role = "USER";

    @Column(name = "email")
    private String email;

    @Column(name = "confirmed")
    private boolean confirmed = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<VigVideoMember> conversationMember;

    @Deprecated
    VigVideoUser() {
    }

    public VigVideoUser(String username, String password, String email, String confirmationKey) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirmationKey = confirmationKey;
    }

    public VigVideoUser(String username, String email, String authProviderId) {
        this.username = username;
        this.email = email;
        this.authProviderId = authProviderId;
        confirmed = true;
    }

    public String getUsername() {
        return username;
    }

    public void confirmEmail() {
        confirmed = true;
    }

    public VigVideoHistory prepareHistory() {
        return new VigVideoHistory(conversationMember);
    }
}

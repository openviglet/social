package com.viglet.social.video.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

import com.viglet.social.persistence.model.video.VigVideoUser;
import com.viglet.social.persistence.repository.video.VigVideoUserRepository;

@Service
public class VigVideoAuthUtils {

    @Autowired
    private VigVideoUserRepository userRepository;

    public VigVideoUser getAuthenticatedUser(Principal userPrincipal) {
        String name = userPrincipal.getName();
        Optional<VigVideoUser> byUsername = userRepository.findByUsername(name);
        if (!byUsername.isPresent()) {
            byUsername = userRepository.findByAuthProviderId(name);

        }
        return byUsername.orElseThrow(() -> new RuntimeException("User isn't authenticated!"));
    }
}

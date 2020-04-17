package com.viglet.social.video.service;

import com.viglet.social.persistence.model.video.VigVideoUser;
import com.viglet.social.persistence.repository.video.VigVideoUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class VigVideoRegisterUserService {

    @Autowired
    private VigVideoUserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public String generateConfirmationKey() {
    	return UUID.randomUUID().toString();
    }
    
    public void register(String username, String password, String email, String confirmationKey) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("user exists!");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("email is occupied!");
        }

        VigVideoUser user = new VigVideoUser(username, encoder.encode(password), email, confirmationKey);

        userRepository.save(user);
    }

    public void register(String name, String email, String providerId) {
        if (!userRepository.findByAuthProviderId(providerId).isPresent()) {
            userRepository.save(new VigVideoUser(name, email, providerId));
        }
    }
}

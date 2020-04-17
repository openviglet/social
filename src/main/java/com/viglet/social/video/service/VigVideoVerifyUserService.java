package com.viglet.social.video.service;

import com.viglet.social.persistence.model.video.VigVideoUser;
import com.viglet.social.persistence.repository.video.VigVideoUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class VigVideoVerifyUserService {

    @Autowired
    private VigVideoUserRepository userRepository;

    public boolean verify(String key) {
        Optional<VigVideoUser> confirmationKey = userRepository.findByConfirmationKey(key);
        confirmationKey.ifPresent(VigVideoUser::confirmEmail);
        return confirmationKey.isPresent();
    }
}

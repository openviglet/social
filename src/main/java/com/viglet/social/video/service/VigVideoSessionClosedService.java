package com.viglet.social.video.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.viglet.social.persistence.repository.video.VigVideoMemberRepository;

@Service
@Transactional
public class VigVideoSessionClosedService {

    @Autowired
    private VigVideoMemberRepository memberRepository;

    public void execute(String memberId, Optional<String> reasonOfClose) {
        memberRepository.getByRtcId(memberId).disconnectWithReason(reasonOfClose);
    }
}

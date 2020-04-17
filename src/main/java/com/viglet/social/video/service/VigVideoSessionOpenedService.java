package com.viglet.social.video.service;

import org.apache.log4j.Logger;
import com.viglet.social.video.auth.VigVideoAuthUtils;
import com.viglet.social.persistence.model.video.VigVideoMember;
import com.viglet.social.persistence.model.video.VigVideoUser;
import com.viglet.social.persistence.repository.video.VigVideoMemberRepository;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@PreAuthorize(value = "USER")
public class VigVideoSessionOpenedService {

    private static final Logger log = Logger.getLogger(VigVideoSessionOpenedService.class);
    @Autowired
    private VigVideoMemberRepository memberRepository;

    @Autowired
    private VigVideoAuthUtils authUtils;

    public void execute(VigRTCMemberDTO member) {
        VigVideoUser user = authUtils.getAuthenticatedUser(member.getSession().getUserPrincipal());

        createConversationMemberFor(member.getId(), user);
    }

    private void createConversationMemberFor(String memberId, VigVideoUser user) {
        log.info("Created member: " + memberRepository.save(new VigVideoMember(memberId, user)));
    }

}

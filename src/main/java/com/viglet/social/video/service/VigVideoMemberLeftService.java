package com.viglet.social.video.service;

import com.viglet.social.persistence.model.video.VigVideoConversation;
import com.viglet.social.persistence.model.video.VigVideoMember;
import com.viglet.social.persistence.repository.video.VigVideoConversationRepository;
import com.viglet.social.persistence.repository.video.VigVideoMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VigVideoMemberLeftService {

    @Autowired
    private VigVideoMemberRepository memberRepository;

    @Autowired
    private VigVideoConversationRepository conversationRepository;

    public void execute(String memberId, String conversationId) {
        VigVideoMember member = memberRepository.getByRtcId(memberId);
        VigVideoConversation conversation = conversationRepository.getByConversationName(conversationId);

        member.leaves(conversation);
    }
}

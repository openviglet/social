package com.viglet.social.video.service;

import com.viglet.social.persistence.model.video.VigVideoMember;
import com.viglet.social.persistence.model.video.VigVideoUser;
import com.viglet.social.persistence.repository.video.VigVideoMemberRepository;
import com.viglet.social.video.auth.VigVideoAuthUtils;
import com.viglet.social.video.domain.history.VigVideoCall;
import com.viglet.social.video.domain.history.VigVideoHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class VigVideoHistoryService {

    @Autowired
    private VigVideoAuthUtils authUtils;

    @Autowired
    private VigVideoMemberRepository memberRepository;

    public VigVideoHistory getHistoryFor(Principal principal) {
        VigVideoUser user = authUtils.getAuthenticatedUser(principal);

        VigVideoHistory history = user.prepareHistory();

        fillUserNamesBaseOnRtcIds(history);

        return history;
    }

    private void fillUserNamesBaseOnRtcIds(VigVideoHistory history) {
        for (VigVideoCall call : history.getCalls()) {
            List<String> others = call.getOtherRtcIds().stream()
                    .map(memberRepository::getByRtcId)
                    .map(VigVideoMember::getUsername)
                    .collect(toList());
            call.setOtherNames(others);
        }
    }

}

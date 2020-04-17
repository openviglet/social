package com.viglet.social.video.domain.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.viglet.social.persistence.model.video.VigVideoMember;

import static java.util.stream.Collectors.toList;

public class VigVideoHistory {

    private List<VigVideoCall> calls;

    public VigVideoHistory(Set<VigVideoMember> conversationMember) {
        List<VigVideoMember> sortedMembers = new ArrayList<>(conversationMember);
        sortedMembers.sort(VigVideoMember::startedBefore);
        calls = sortedMembers.stream().map(VigVideoMember::toCall).collect(toList());
    }

    public List<VigVideoCall> getCalls() {
        return calls;
    }
}

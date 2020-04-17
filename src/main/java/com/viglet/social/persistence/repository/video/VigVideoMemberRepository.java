package com.viglet.social.persistence.repository.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.viglet.social.persistence.model.video.VigVideoMember;

@Repository
@Transactional
public interface VigVideoMemberRepository extends JpaRepository<VigVideoMember, Integer> {

    @Query("select m from VigVideoMember m where m.rtcId = :rtcId and m.connected = (select max(mm.connected) from VigVideoMember mm where mm.rtcId = m.rtcId)")
    Optional<VigVideoMember> findByRtcId(@Param("rtcId") String rtcId);

    @Query("select m from VigVideoMember m where m.rtcId = :rtcId and m.connected = (select max(mm.connected) from VigVideoMember mm where mm.rtcId = m.rtcId)")
    VigVideoMember getByRtcId(@Param("rtcId") String rtcId);

}

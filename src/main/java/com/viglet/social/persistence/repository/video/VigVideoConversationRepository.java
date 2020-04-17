package com.viglet.social.persistence.repository.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viglet.social.persistence.model.video.VigVideoConversation;

@Repository
@Transactional
public interface VigVideoConversationRepository extends JpaRepository<VigVideoConversation, Integer> {

    @Query("select c from VigVideoConversation c where c.destroyed is null and c.conversationName = :conversationName")
    VigVideoConversation getByConversationName(@Param("conversationName") String conversationName);
}

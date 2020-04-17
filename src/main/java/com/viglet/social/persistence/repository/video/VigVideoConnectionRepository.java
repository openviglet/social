package com.viglet.social.persistence.repository.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viglet.social.persistence.model.video.VigVideoConnection;

@Repository
@Transactional
public interface VigVideoConnectionRepository extends JpaRepository<VigVideoConnection, Integer> {

}

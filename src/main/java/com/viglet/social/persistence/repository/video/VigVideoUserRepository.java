package com.viglet.social.persistence.repository.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.viglet.social.persistence.model.video.VigVideoUser;

@org.springframework.stereotype.Repository
@RepositoryRestResource(exported = false)
@Transactional
public interface VigVideoUserRepository extends JpaRepository<VigVideoUser, Integer> {

    Optional<VigVideoUser> findByUsername(@Param("username") String username);

    Optional<VigVideoUser> findByEmail(@Param("email") String email);

    Optional<VigVideoUser> findByConfirmationKey(@Param("confirmationKey") String key);

    Optional<VigVideoUser> findByAuthProviderId(@Param("authProviderId") String key);


}

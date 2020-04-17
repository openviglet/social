/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.social.spring.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.viglet.social.persistence.model.auth.VigGroup;
import com.viglet.social.persistence.model.auth.VigRole;
import com.viglet.social.persistence.model.auth.VigUser;
import com.viglet.social.persistence.repository.auth.VigGroupRepository;
import com.viglet.social.persistence.repository.auth.VigRoleRepository;
import com.viglet.social.persistence.repository.auth.VigUserRepository;

/**
 * @author Alexandre Oliveira
 */
@Service("customUserDetailsService")
public class VigCustomUserDetailsService implements UserDetailsService {
	@Autowired
	private VigUserRepository vigUserRepository;
	@Autowired
	private VigRoleRepository vigRoleRepository;
	@Autowired
	private VigGroupRepository vigGroupRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		VigUser vigUser = vigUserRepository.findByUsername(username);
		if (null == vigUser) {
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			List<VigUser> users = new ArrayList<>();
			users.add(vigUser);
			Set<VigGroup> vigGroups = vigGroupRepository.findByVigUsersIn(users);
			Set<VigRole> vigRoles = vigRoleRepository.findByVigGroupsIn(vigGroups);

			List<String> roles = new ArrayList<>();
			for (VigRole vigRole : vigRoles) {
				roles.add(vigRole.getName());
			}
			return new VigCustomUserDetails(vigUser, roles);
		}
	}

}

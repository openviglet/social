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
package com.viglet.social.persistence.model.auth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the VigGroup database table.
 * 
 * @author Alexandre Oliveira
 */
@Entity
@NamedQuery(name = "VigGroup.findAll", query = "SELECT g FROM VigGroup g")
public class VigGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.social.jpa.VigUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String name;

	private String description;

	@ManyToMany(mappedBy = "vigGroups")
	private Set<VigRole> vigRoles = new HashSet<>();

	@ManyToMany(mappedBy = "vigGroups")
	private Set<VigUser> vigUsers = new HashSet<>();

	public VigGroup() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<VigUser> getVigUsers() {
		return this.vigUsers;
	}

	public void setVigUsers(Set<VigUser> vigUsers) {
		this.vigUsers.clear();
		if (vigUsers != null) {
			this.vigUsers.addAll(vigUsers);
		}
	}
}
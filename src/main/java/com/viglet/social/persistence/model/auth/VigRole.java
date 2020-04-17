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
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the VigRole database table.
 * 
 * @author Alexandre Oliveira
 */
@Entity
@Table(name = "vigRole")
@NamedQuery(name = "VigRole.findAll", query = "SELECT r FROM VigRole r")
public class VigRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.social.jpa.VigUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;


	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(nullable = true, length = 255)
	private String description;

	@ManyToMany
	private Set<VigGroup> vigGroups = new HashSet<>();

	public VigRole() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<VigGroup> getVigGroups() {
		return this.vigGroups;
	}

	public void setVigGroups(Set<VigGroup> vigGroups) {
		this.vigGroups.clear();
		if (vigGroups != null) {
			this.vigGroups.addAll(vigGroups);
		}
	}

}
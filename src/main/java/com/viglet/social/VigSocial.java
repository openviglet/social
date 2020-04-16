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

package com.viglet.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class that can be used to bootstrap and launch a Viglet Social from a Java
 * main method.
 *
 * @author Alexandre Oliveira
 * @since 0.3.0
 **/

@SpringBootApplication
public class VigSocial {

    public static void main(String[] args) {
    	System.out.println("Viglet Social starting...");
		SpringApplication application = new SpringApplication(VigSocial.class);
		application.run(args);
		System.out.println("Viglet Social started");
    }
}

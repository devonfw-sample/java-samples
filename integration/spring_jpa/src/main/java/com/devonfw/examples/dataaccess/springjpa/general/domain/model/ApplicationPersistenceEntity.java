package com.devonfw.examples.dataaccess.springjpa.general.domain.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract Entity for all Entities with an id and a version field.
 *
 */

@Setter
@Getter
@MappedSuperclass
public abstract class ApplicationPersistenceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Version
	private Integer modificationCounter;
}
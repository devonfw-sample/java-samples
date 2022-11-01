package com.devonfw.quarkus.productmanagement.domain.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.quarkus.general.domain.model.ApplicationPersistenceEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Product")
// A JPA entity requires at least 2 things @Entity annotation and an ID
// by default, the DB table will have the same name as our class
public class ProductEntity extends ApplicationPersistenceEntity {

  private String title;

  private String description;

  private BigDecimal price;
}

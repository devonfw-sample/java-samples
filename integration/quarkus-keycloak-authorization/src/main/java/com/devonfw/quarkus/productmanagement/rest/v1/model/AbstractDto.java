package com.devonfw.quarkus.productmanagement.rest.v1.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractDto {
  private int modificationCounter;

  private Long id;
}

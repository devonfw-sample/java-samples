package com.devonfw.quarkus.productmanagement.rest.v1.model;

import java.math.BigDecimal;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.devonfw.quarkus.general.domain.model.ApplicationSearchCriteriaDto;

import lombok.Data;

@Data
public class ProductSearchCriteriaDto extends ApplicationSearchCriteriaDto {

  @Schema(description = "Product title")
  private String title;

  @Schema(description = "Product Min price")
  private BigDecimal priceMin;

  @Schema(description = "Product Max price")
  private BigDecimal priceMax;

  @Schema(description = "Product price")
  private BigDecimal price;
}

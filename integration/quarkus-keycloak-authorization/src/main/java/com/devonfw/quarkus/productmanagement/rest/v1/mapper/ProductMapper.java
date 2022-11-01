package com.devonfw.quarkus.productmanagement.rest.v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

@Mapper
public interface ProductMapper {

  ProductDto map(ProductEntity model);

  ProductEntity map(ProductDto dto);

  List<ProductDto> map(List<ProductEntity> Products);

  default Page<ProductDto> map(Page<ProductEntity> products) {

    List<ProductDto> productsDto = this.map(products.getContent());
    return new PageImpl<>(productsDto, products.getPageable(), products.getTotalElements());
  }
}

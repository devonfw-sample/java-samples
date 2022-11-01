package com.devonfw.quarkus.productmanagement.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

  @Query("select a from ProductEntity a where title = :title")
  ProductEntity findByTitle(@Param("title") String title);

  Page<ProductEntity> findAllByOrderByTitle();
}
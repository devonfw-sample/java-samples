package com.devonfw.quarkus.productmanagement.rest.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.rest.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

@Path("/product/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
public class ProductReadService {

  @Inject
  JsonWebToken jwt;

  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper productMapper;

  @GET
  @Path("claims")
  @SecurityRequirement(name = "jwt", scopes = {})
  public String getClaims() {

    Map<String, String> claims = new HashMap<>();
    this.jwt.getClaimNames().stream()
        .forEach(claimName -> claims.put(claimName, this.jwt.getClaim(claimName).toString()));
    return new JSONObject(claims).toJSONString();
  }

  @GET
  @SecurityRequirement(name = "jwt", scopes = {})
  public Page<ProductDto> getProducts() {

    Page<ProductEntity> products = this.productRepository.findAllByOrderByTitle();
    if (products != null) {
      return this.productMapper.map(products);
    }
    return null;
  }

  @GET
  @Path("{id}")
  @SecurityRequirement(name = "jwt", scopes = {})
  public ProductDto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    Optional<ProductEntity> product = this.productRepository.findById(Long.valueOf(id));
    if (product.isPresent()) {
      return this.productMapper.map(product.get());
    }
    return null;
  }

  @GET
  @Path("title/{title}")
  @SecurityRequirement(name = "jwt", scopes = {})
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    return this.productMapper.map(this.productRepository.findByTitle(title));
  }

}

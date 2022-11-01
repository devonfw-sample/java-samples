package com.devonfw.quarkus.productmanagement.rest.v1;

import static com.devonfw.quarkus.productmanagement.utils.StringUtils.isEmpty;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.status;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.rest.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.User;

import io.quarkus.security.identity.SecurityIdentity;

@Path("/product/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductWriteService {

  @Context
  UriInfo uriInfo;

  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper productMapper;

  @Inject
  SecurityIdentity keycloakSecurityContext;

  @GET
  @SecurityRequirement(name = "jwt", scopes = {})
  public User manage() {

    return new User(this.keycloakSecurityContext);
  }

  @POST
  @SecurityRequirement(name = "jwt", scopes = {})
  public Response createNewProduct(ProductDto product) {

    if (isEmpty(product.getTitle())) {
      throw new WebApplicationException("Title was not set on request.", 400);
    }

    ProductEntity productEntity = this.productRepository.save(this.productMapper.map(product));

    UriBuilder uriBuilder = this.uriInfo.getAbsolutePathBuilder().path(Long.toString(productEntity.getId()));
    return created(uriBuilder.build()).build();
  }

  @DELETE
  @Path("{id}")
  @SecurityRequirement(name = "jwt", scopes = {})
  public Response deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    this.productRepository.deleteById(Long.valueOf(id));
    return status(Status.NO_CONTENT.getStatusCode()).build();
  }

}

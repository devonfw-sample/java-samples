package com.devonfw.quarkus.productmanagement.rest.v1.model;

import io.quarkus.security.identity.SecurityIdentity;

public class User {

  private final String userName;

  public User(SecurityIdentity securityContext) {

    this.userName = securityContext.getPrincipal().getName();
  }

  public String getUserName() {

    return this.userName;
  }
}
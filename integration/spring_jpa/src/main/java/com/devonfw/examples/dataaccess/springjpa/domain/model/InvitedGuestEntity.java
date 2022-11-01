package com.devonfw.examples.dataaccess.springjpa.domain.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.examples.dataaccess.springjpa.general.domain.model.ApplicationPersistenceEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = true)
@Entity
@Table (name="Invited_Guest")
public class InvitedGuestEntity extends ApplicationPersistenceEntity {

  private String guestToken;

  private String email;

}

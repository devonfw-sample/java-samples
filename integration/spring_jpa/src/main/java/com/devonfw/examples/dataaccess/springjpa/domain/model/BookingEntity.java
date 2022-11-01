package com.devonfw.examples.dataaccess.springjpa.domain.model;

import static javax.persistence.CascadeType.ALL;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.devonfw.examples.dataaccess.springjpa.general.domain.model.ApplicationPersistenceEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = true)
@Entity
@Table (name="booking")
public class BookingEntity extends ApplicationPersistenceEntity {

  private String name;

  private String bookingToken;

  private String comment;

  private LocalDateTime bookingDate;

  private String email;

  private BookingStatus bookingStatus;

  @OneToMany(cascade = ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "id_booking")
  @BatchSize(size = 2)
  private Set<InvitedGuestEntity> invitedGuests;
}

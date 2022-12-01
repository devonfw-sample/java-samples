package com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitedGuestRepository extends JpaRepository<InvitedGuest, Long> {
}

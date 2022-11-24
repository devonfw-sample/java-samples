package com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;


@Entity
public class InvitedGuest {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Integer modificationCounter = 0;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModificationCounter() {
        return modificationCounter;
    }

    public void setModificationCounter(Integer modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


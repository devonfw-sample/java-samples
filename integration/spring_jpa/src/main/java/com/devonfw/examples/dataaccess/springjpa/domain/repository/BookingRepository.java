package com.devonfw.examples.dataaccess.springjpa.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devonfw.examples.dataaccess.springjpa.domain.model.BookingEntity;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, Long> {

}

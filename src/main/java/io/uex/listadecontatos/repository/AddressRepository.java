package io.uex.listadecontatos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.uex.listadecontatos.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}

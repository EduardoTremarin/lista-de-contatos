package io.uex.listadecontatos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.uex.listadecontatos.entity.Contact;
import io.uex.listadecontatos.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{

	Optional<List<Contact>> findByUser(User user);
	Optional<Contact> findBycpf(String cpf);
	
}

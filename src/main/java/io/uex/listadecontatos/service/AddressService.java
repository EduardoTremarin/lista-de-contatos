package io.uex.listadecontatos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.uex.listadecontatos.entity.Address;
import io.uex.listadecontatos.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
    AddressRepository addressRepository;

    public List<Address> listAll() {
        return addressRepository.findAll();
    }
    
    public Optional<Address> getSigner(Long id) {
    	return addressRepository.findById(id);
    }
	
}

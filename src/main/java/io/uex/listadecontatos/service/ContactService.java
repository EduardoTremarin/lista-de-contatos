package io.uex.listadecontatos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.uex.listadecontatos.dto.ContactDTO;
import io.uex.listadecontatos.dto.CreateContactDTO;
import io.uex.listadecontatos.entity.Address;
import io.uex.listadecontatos.entity.Contact;
import io.uex.listadecontatos.entity.User;
import io.uex.listadecontatos.exception.ContactAlreadyExistsException;
import io.uex.listadecontatos.exception.InvalidCPFException;
import io.uex.listadecontatos.exception.UserNotFoundException;
import io.uex.listadecontatos.repository.AddressRepository;
import io.uex.listadecontatos.repository.ContactRepository;
import io.uex.listadecontatos.repository.UserRepository;

@Service
public class ContactService {

	@Autowired
    UserRepository userRepository;
	
	@Autowired
    ContactRepository contactRepository;
	
	@Autowired
	AddressRepository addressRepository;

    public List<ContactDTO> listByUser(Long userId) throws UserNotFoundException {
    	Optional<User> user = userRepository.findById(userId);
    	
    	if(user.isPresent()) {
    		Optional<List<Contact>> contacts = contactRepository.findByUser(user.get());
    		
    		if(contacts.isPresent()) {
    			List<ContactDTO> contactsDTO = new ArrayList<>();
    			
    			contacts.get().forEach(c -> {
    				contactsDTO.add(new ContactDTO(c.getId(), c.getName(), c.getCpf(), c.getTelephone(), c.getUser(), c.getAddress()));
    			});
    			
    			return contactsDTO;
    		}
    	} else {
    		throw new UserNotFoundException();
    	}
    	
    	return null;
    }
    
    public ContactDTO createContact(CreateContactDTO createContactDTO) throws UserNotFoundException, InvalidCPFException, ContactAlreadyExistsException {
    	Optional<Contact> contactByCPF = contactRepository.findBycpf(createContactDTO.getCpf());
    	
    	if(contactByCPF.isPresent())
    		throw new ContactAlreadyExistsException();
    	
    	Optional<User> user = userRepository.findById(createContactDTO.getUserId());
    	
    	if(user.isPresent()) {
    		
    		if(!isValidCPF(createContactDTO.getCpf()))
    			throw new InvalidCPFException();
    		
    		Contact contact = Contact.builder()
    				.name(createContactDTO.getName())
    				.cpf(createContactDTO.getCpf())
    				.telephone(createContactDTO.getTelephone())
    				.user(user.get())
    				.build();
    		
    		contactRepository.save(contact);
    		
    		Address address = Address.builder()
    				.road(createContactDTO.getRoad())
    				.neighborhood(createContactDTO.getNeighborhood())
    				.city(createContactDTO.getCity())
    				.state(createContactDTO.getState())
    				.country(createContactDTO.getCountry())
    				.cep(createContactDTO.getCep())
    				.longitude(createContactDTO.getLongitude())
    				.latitude(createContactDTO.getLatitude())
    				.contact(contact)
    				.build();
    		
    		addressRepository.save(address);
    		
    		contact.setAddress(address);
    		contactRepository.save(contact);
    		
    		return new ContactDTO(contact.getId(), contact.getName(), contact.getCpf(),
    				contact.getTelephone(), contact.getUser(), contact.getAddress());
    	} else {
    		throw new UserNotFoundException();
    	}
    }
    
	private boolean isValidCPF(String cpf) {
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");

		try {
			Long.parseLong(cpf);
		} catch (NumberFormatException e) {
			return false;
		}

		int d1, d2;
		int digito1, digito2, resto;
		int digitoCPF;
		String nDigResult;

		d1 = d2 = 0;
		digito1 = digito2 = resto = 0;

		for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
			digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();

			// multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4
			// e assim por diante.
			d1 = d1 + (11 - nCount) * digitoCPF;

			// para o segundo digito repita o procedimento incluindo o primeiro
			// digito calculado no passo anterior.
			d2 = d2 + (12 - nCount) * digitoCPF;
		}
		;

		// Primeiro resto da divisão por 11.
		resto = (d1 % 11);

		// Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
		// menos o resultado anterior.
		if (resto < 2)
			digito1 = 0;
		else
			digito1 = 11 - resto;

		d2 += 2 * digito1;

		// Segundo resto da divisão por 11.
		resto = (d2 % 11);

		// Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
		// menos o resultado anterior.
		if (resto < 2)
			digito2 = 0;
		else
			digito2 = 11 - resto;

		// Digito verificador do CPF que está sendo validado.
		String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());

		// Concatenando o primeiro resto com o segundo.
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

		// comparar o digito verificador do cpf com o primeiro resto + o segundo
		// resto.
		return nDigVerific.equals(nDigResult);
	}
	
}

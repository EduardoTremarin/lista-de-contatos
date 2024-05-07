package io.uex.listadecontatos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import io.uex.listadecontatos.dto.ContactDTO;
import io.uex.listadecontatos.dto.CreateContactDTO;
import io.uex.listadecontatos.entity.User;
import io.uex.listadecontatos.exception.ContactAlreadyExistsException;
import io.uex.listadecontatos.exception.InvalidCPFException;
import io.uex.listadecontatos.exception.UserNotFoundException;
import io.uex.listadecontatos.repository.AddressRepository;
import io.uex.listadecontatos.repository.ContactRepository;
import io.uex.listadecontatos.repository.UserRepository;
import io.uex.listadecontatos.service.ContactService;

@SpringBootTest
public class ContactServiceTest {

	@Mock
	private ContactRepository contactRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private ContactService contactService;

	@Test
	void testCreateContact() throws UserNotFoundException, InvalidCPFException, ContactAlreadyExistsException {
		CreateContactDTO createContactDTO = new CreateContactDTO();
		createContactDTO.setUserId(1L);
		createContactDTO.setName("Maria");
		createContactDTO.setCpf("12885091037");
		createContactDTO.setTelephone("123456789");
		createContactDTO.setRoad("Silva Jardim");
		createContactDTO.setNeighborhood("Batel");
		createContactDTO.setCity("Curitiba");
		createContactDTO.setState("Paraná");
		createContactDTO.setCountry("Brasil");
		createContactDTO.setCep("12345-678");
		createContactDTO.setLongitude("0.0");
		createContactDTO.setLatitude("0.0");

		User user = new User();

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(contactRepository.findBycpf("12885091037")).thenReturn(Optional.empty());

		ContactDTO result = contactService.createContact(createContactDTO);

		assertNotNull(result);
		assertEquals("Maria", result.getName());
		assertEquals("12885091037", result.getCpf());
		assertEquals("123456789", result.getTelephone());
		assertEquals(user, result.getUser());
		assertNotNull(result.getAddress());
	}
	
}

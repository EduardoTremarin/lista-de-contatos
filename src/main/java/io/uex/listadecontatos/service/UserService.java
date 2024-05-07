package io.uex.listadecontatos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.uex.listadecontatos.dto.CreateUserDTO;
import io.uex.listadecontatos.dto.UserDTO;
import io.uex.listadecontatos.entity.User;
import io.uex.listadecontatos.exception.UserAlreadyExistsException;
import io.uex.listadecontatos.repository.UserRepository;

@Service
public class UserService {

	@Autowired
    UserRepository userRepository;

    public List<User> listAll() {
        return userRepository.findAll();
    }
    
    /**
     * Busca um usuário com base no id fornecido
     * @param id
     * @return
     */
    public UserDTO getUser(Long id) {
    	Optional<User> user = userRepository.findById(id);
    	
    	if(user.isPresent()) {
    		UserDTO userDTO = new UserDTO();
    		userDTO.setId(user.get().getId());
    		userDTO.setName(user.get().getName());
    		userDTO.setEmail(user.get().getEmail());
    		
    		return userDTO;
    	}
    	
    	return null;
    }
    
    /**
     * Cria um novo usuário na base de dados. Antes de tentar criar um novo usuário, verifica se já existe um usuário
     * cadastrado com mesmo email.
     * @param createUserDTO
     * @return
     * @throws UserAlreadyExistsException
     */
    public UserDTO createUser(CreateUserDTO createUserDTO) throws UserAlreadyExistsException {
    	Optional<User> user = userRepository.findByEmail(createUserDTO.getEmail());
    	
    	if(user.isPresent())
    		throw new UserAlreadyExistsException();
    	
    	User newUser = new User();
    	newUser.setName(createUserDTO.getName());
    	newUser.setEmail(createUserDTO.getEmail());
    	newUser.setPassword(createUserDTO.getPassword());
    	
    	newUser = userRepository.save(newUser);
    	
    	if(user.isPresent()) {
    		UserDTO userDTO = new UserDTO();
    		userDTO.setId(user.get().getId());
    		userDTO.setName(user.get().getName());
    		userDTO.setEmail(user.get().getEmail());
    		
    		return userDTO;
    	}
    	
    	return null;
    }
	
}

package io.uex.listadecontatos.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.uex.listadecontatos.entity.User;
import io.uex.listadecontatos.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(login);
		
		if (user.isPresent())
			return user.get();
		
		throw new UsernameNotFoundException("Dados inválidos!");
	}

}

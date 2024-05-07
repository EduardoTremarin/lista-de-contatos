package io.uex.listadecontatos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.uex.listadecontatos.dto.LoginDTO;
import io.uex.listadecontatos.dto.TokenDTO;
import io.uex.listadecontatos.entity.User;
import io.uex.listadecontatos.service.TokenService;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> executeLogin(@RequestBody LoginDTO formData) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(formData.getLogin(), formData.getPassword());
		var authentication = manager.authenticate(authenticationToken);
		var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
		
		if(authentication.isAuthenticated())
			return (ResponseEntity<?>) ResponseEntity.ok(new TokenDTO(tokenJWT));
		
		return (ResponseEntity<?>) ResponseEntity.status(402).build();
	}
	
}

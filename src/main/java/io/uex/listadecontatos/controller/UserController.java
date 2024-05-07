package io.uex.listadecontatos.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.uex.listadecontatos.dto.CreateUserDTO;
import io.uex.listadecontatos.dto.UserDTO;
import io.uex.listadecontatos.exception.StandardError;
import io.uex.listadecontatos.exception.UserAlreadyExistsException;
import io.uex.listadecontatos.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Operation(
    		summary = "Busca um usuário",
    		description = "Endpoint para recuperar um usuário, através do id do mesmo.",
    		responses = {
    				@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserDTO.class))),
    				@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    		})
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getDocument(@PathVariable @Parameter(description = "Id do usuário") Long id) {
		UserDTO user = userService.getUser(id);
    	
		if(user != null)
			return ResponseEntity.ok(user);
		
		return ResponseEntity.notFound().build();
    }
	
	@Operation(
    		summary = "Cria um novo usuário",
    		description = "Esse endpoint permite que você crie um usuário.",
    		responses = {
    				@ApiResponse(responseCode = "201", description = "Usuário criado", content = @Content(schema = @Schema(implementation = CreateUserDTO.class))),
    				@ApiResponse(responseCode = "400", description = "Ocorreu um erro ao tentar criar o usuário", content = @Content(schema = @Schema(implementation = StandardError.class)))
    		})
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserDTO, UriComponentsBuilder uriBuilder) {
		try {
			UserDTO userDTO = userService.createUser(createUserDTO);
			
			if(userDTO == null)
				return ResponseEntity.badRequest().build();
			
			var uri = uriBuilder.path("/{id}").buildAndExpand(userDTO.getId()).toUri();
			
			return ResponseEntity.created(uri).body(userDTO);
		} catch (UserAlreadyExistsException u) {
			StandardError standardError = new StandardError(new Date().getTime(), 400, "Erro ao criar usuário.", 
					"Já existe um usuário cadastrado com esse email.", "/");
			
			return ResponseEntity.badRequest().body(standardError);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
    }
	
}

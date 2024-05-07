package io.uex.listadecontatos.controller;

import java.util.Date;
import java.util.List;

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
import io.uex.listadecontatos.dto.ContactDTO;
import io.uex.listadecontatos.dto.CreateContactDTO;
import io.uex.listadecontatos.exception.ContactAlreadyExistsException;
import io.uex.listadecontatos.exception.InvalidCPFException;
import io.uex.listadecontatos.exception.StandardError;
import io.uex.listadecontatos.exception.UserNotFoundException;
import io.uex.listadecontatos.service.ContactService;

@RestController
@RequestMapping("/contact")
public class ContactController {
	
	@Autowired
	private ContactService contactService;

	@Operation(
    		summary = "Lista os contatos de um usuário",
    		description = "Endpoint para recuperar uma lista de contatos de um usuário, através do id do mesmo.",
    		responses = {
    				@ApiResponse(responseCode = "200", description = "Lista carregada", content = @Content(schema = @Schema(implementation = ContactDTO.class))),
    				@ApiResponse(responseCode = "404", description = "Nenhum contato encontrado"),
    				@ApiResponse(responseCode = "400", description = "Erro ao buscar contatos", content = @Content(schema = @Schema(implementation = StandardError.class)))})
	@GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDocument(@PathVariable @Parameter(description = "Id do usuário") Long userId) {
		try {
			List<ContactDTO> contacts = contactService.listByUser(userId);
	    	
			if(contacts != null)
				return ResponseEntity.ok().body(contacts);
			
			return ResponseEntity.notFound().build();
		} catch (UserNotFoundException e) {
			StandardError standardError = new StandardError(new Date().getTime(), 400, "Erro ao buscar contatos.", 
					e.getMessage(), "/");
			
			return ResponseEntity.badRequest().body(standardError);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
    }
	
	@Operation(
    		summary = "Cria um novo contato",
    		description = "Endpoint para criar um novo contato, com endereço e id de usuário.",
    		responses = {
    				@ApiResponse(responseCode = "201", description = "Contato criado", content = @Content(schema = @Schema(implementation = CreateContactDTO.class))),
    				@ApiResponse(responseCode = "400", description = "Ocorreu um erro ao tentar criar o contato", content = @Content(schema = @Schema(implementation = StandardError.class)))
    		})
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody CreateContactDTO createContactDTO, UriComponentsBuilder uriBuilder) {
		try {
			ContactDTO contactDTO = contactService.createContact(createContactDTO);
			
			if(contactDTO == null)
				return ResponseEntity.badRequest().build();
			
			var uri = uriBuilder.path("/{id}").buildAndExpand(contactDTO.getId()).toUri();
			
			return ResponseEntity.created(uri).body(contactDTO);
		} catch (ContactAlreadyExistsException c) {
			StandardError standardError = new StandardError(new Date().getTime(), 400, "Erro ao criar contato.", 
					c.getMessage(), "/");
			
			return ResponseEntity.badRequest().body(standardError);
		} catch (InvalidCPFException i) {
			StandardError standardError = new StandardError(new Date().getTime(), 400, "Erro ao criar contato.", 
					i.getMessage(), "/");
			
			return ResponseEntity.badRequest().body(standardError);
		}catch (UserNotFoundException u) {
			StandardError standardError = new StandardError(new Date().getTime(), 400, "Erro ao criar contato.", 
					u.getMessage(), "/");
			
			return ResponseEntity.badRequest().body(standardError);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
    }
	
}

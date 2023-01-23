package com.eduardogreff.api.controllers;

import com.eduardogreff.domain.entities.dto.PersonDTO;
import com.eduardogreff.domain.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/person")
@Tag(name = "People", description = "Endpoints to managing people")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds a list of people",description = "Finds a list of people.", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success case", responseCode = "200", content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                    }),
                    @ApiResponse(description = "Not Found case", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds person", description = "Finds a person by his id in URL path.",
            responses = {
                    @ApiResponse(description = "Success case", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Not Found case", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Bad Request case", responseCode = "400", content = @Content)
            }
    )
    public ResponseEntity<PersonDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "creates a person", description = "Creates a new person by passing data in JSON or XML.",
            responses = {
                    @ApiResponse(description = "Created case", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Bad Request case", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found case", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<Void> create(@RequestBody PersonDTO personDTO) {
        service.create(personDTO);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(personDTO.getId()).toUri()).build();
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Update a person",description = "Upgrades information of a person by passing data in JSON or XML.",
            responses = {
                    @ApiResponse(description = "No content case", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request case", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found case", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<Void> put(@RequestBody PersonDTO dto, @PathVariable Long id) {
        service.put(dto, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person", description = "Deletes a person by your id.",
            responses = {
                    @ApiResponse(description = "No content case", responseCode = "204",  content = @Content),
                    @ApiResponse(description = "Bad Request case", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found case", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

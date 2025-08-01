package com.example.tpo10.Controllers;

import com.example.tpo10.Models.LinkDTO;
import com.example.tpo10.Models.LinkResponse;
import com.example.tpo10.Models.UpdateLinkDTO;
import com.example.tpo10.Services.LinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkResponse> getLink(@PathVariable String id){
        return ResponseEntity.ok(linkService.getLink(id));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> createLink(@RequestBody @Valid LinkDTO linkDTO) {
        LinkResponse newDto = linkService.createLink(linkDTO);

        URI createdLinkLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();

        return ResponseEntity.created(createdLinkLocation).body(newDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLink(@PathVariable String id, @RequestBody UpdateLinkDTO updateLinkDTO){
        linkService.updateLink(id, updateLinkDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable String id, @RequestHeader(value = "pass", required = false) String password) {
        linkService.deleteLink(id, password);

        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handle(MethodArgumentNotValidException ex){
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
    }
}
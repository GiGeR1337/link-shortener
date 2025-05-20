package com.example.tpo10.Controllers;

import com.example.tpo10.Services.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class RedirectController {

    private final LinkService linkService;

    public RedirectController(LinkService service) {
        this.linkService = service;
    }

    @GetMapping("/red/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id) {
        String url = linkService.getRedirectUrl(id);
        return ResponseEntity.status(302).location(URI.create(url)).build();
    }
}
package com.example.tpo10.Services;

import com.example.tpo10.Models.Link;
import com.example.tpo10.Models.LinkDTO;
import com.example.tpo10.Models.LinkResponse;
import com.example.tpo10.Models.UpdateLinkDTO;
import com.example.tpo10.Repositories.LinkRepository;
import jakarta.validation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    private String generateId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public LinkResponse createLink(LinkDTO linkDTO){

        String id = generateId();
        while(linkRepository.existsById(id)){
            id = generateId();
        }
        Link link = new Link();
        link.setId(id);
        link.setName(linkDTO.getName());
        link.setTargetUrl(linkDTO.getTargetUrl());
        link.setPassword(linkDTO.getPassword());
        link.setVisits(0);
        linkRepository.save(link);

        return mapper(link);
    }

    public LinkResponse getLink(String id){
        return linkRepository.findById(id).map(this::mapper)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<LinkResponse> getAllLinks() {
        List<LinkResponse> responses = new ArrayList<>();
        linkRepository.findAll().forEach(link -> responses.add(mapper(link)));
        return responses;
    }

    public LinkResponse getLinkWithPasswordCheck(String id, String password) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (link.getPassword() != null && !link.getPassword().isBlank()) {
            if (!link.getPassword().equals(password)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
            }
        }

        return mapper(link);
    }

    public String getRedirectUrl(String id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        link.setVisits(link.getVisits() + 1);
        linkRepository.save(link);

        return link.getTargetUrl();
    }

    public void updateLink(String id, UpdateLinkDTO updateLinkDTO) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(link.getPassword() != null){
            if(updateLinkDTO.getPassword() == null || !updateLinkDTO.getPassword().equals(link.getPassword()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
        }

        if(updateLinkDTO.getName() != null) link.setName(updateLinkDTO.getName());

        if(updateLinkDTO.getTargetUrl() != null) link.setTargetUrl(updateLinkDTO.getTargetUrl());

        linkRepository.save(link);
    }

    public void deleteLink(String id, String password) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (link.getPassword() != null) {
            if (!link.getPassword().equals(password)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
            }
        }

        linkRepository.delete(link);
    }

    private LinkResponse mapper(Link link) {
        LinkResponse res = new LinkResponse();
        res.setId(link.getId());
        res.setName(link.getName());
        res.setTargetUrl(link.getTargetUrl());
        res.setRedirectUrl("http://localhost:8080/red/" + link.getId());
        res.setVisits(link.getVisits());
        return res;
    }
}

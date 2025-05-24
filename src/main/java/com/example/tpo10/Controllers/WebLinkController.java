package com.example.tpo10.Controllers;

import com.example.tpo10.Models.LinkDTO;
import com.example.tpo10.Models.LinkResponse;
import com.example.tpo10.Models.UpdateLinkDTO;
import com.example.tpo10.Services.LinkService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/links")
public class WebLinkController {

    private final LinkService linkService;

    public WebLinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/homepage")
    public String getHomepage() {
        return "homepage";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("linkDTO", new LinkDTO());
        return "create-link";
    }

    @PostMapping("/create")
    public String createLink(@Valid @ModelAttribute("linkDTO") LinkDTO linkDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "create-link";
        }

        LinkResponse newLink = linkService.createLink(linkDTO);
        return "redirect:/links/homepage";
    }

    @GetMapping("/info")
    public String showAccessForm(@RequestParam(required = false) String id, Model model) {
        model.addAttribute("id", id);
        return "access-link";
    }

    @PostMapping("/info")
    public String accessLink(@RequestParam String id,
                             @RequestParam(required = false) String password,
                             Model model) {
        try {
            LinkResponse link = linkService.getLinkWithPasswordCheck(id, password);
            model.addAttribute("link", link);
            model.addAttribute("updateDto", new UpdateLinkDTO());
            return "link-details";
        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getReason());
            model.addAttribute("id", id);
            return "access-link";
        }
    }

    @PostMapping("/edit/{id}")
    public String editLink(@PathVariable String id,
                           @Valid @ModelAttribute("updateDto") UpdateLinkDTO updateDto,
                           BindingResult result,
                           @RequestParam(required = false) String password,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Validation error.");
            model.addAttribute("link", linkService.getLink(id));
            return "link-details";
        }

        try {
            linkService.updateLink(id, updateDto);
            return "redirect:/links/info?id=" + id;
        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getReason());
            model.addAttribute("link", linkService.getLink(id));
            model.addAttribute("updateDto", updateDto);
            return "link-details";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteLink(@PathVariable String id,
                             @RequestParam(required = false) String password,
                             Model model) {
        try {
            linkService.deleteLink(id, password);
            return "redirect:/links/homepage";
        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getReason());
            model.addAttribute("link", linkService.getLink(id));
            model.addAttribute("updateDto", new UpdateLinkDTO());
            return "link-details";
        }
    }
}

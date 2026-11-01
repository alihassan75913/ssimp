package com.alihassan.ssimp.controller;

import com.alihassan.ssimp.model.Category;
import com.alihassan.ssimp.model.Ticket;
import com.alihassan.ssimp.model.User;
import com.alihassan.ssimp.repository.CategoryRepository;
import com.alihassan.ssimp.repository.TicketRepository;
import com.alihassan.ssimp.repository.UserRepository;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import com.alihassan.ssimp.repository.SuggestionRepository;

@Controller
public class UserTicketController {

  private final CategoryRepository categoryRepository;
private final TicketRepository ticketRepository;
private final UserRepository userRepository;
private final SuggestionRepository suggestionRepository;

public UserTicketController(
        CategoryRepository categoryRepository,
        TicketRepository ticketRepository,
        UserRepository userRepository,
        SuggestionRepository suggestionRepository) {

    this.categoryRepository = categoryRepository;
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
    this.suggestionRepository = suggestionRepository;
}
    @GetMapping("/user/tickets")
public String showMyTickets(Principal principal, Model model) {
    User account = userRepository.findByEmail(principal.getName())
            .filter(User::isActive)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.FORBIDDEN));

    model.addAttribute(
            "tickets",
            ticketRepository.findByUser_UserIdOrderByCreatedAtDesc(
                    account.getUserId()
            )
    );

    return "my-tickets";
}
@GetMapping("/user/tickets/{ticketId}")
public String showTicketDetails(
        @PathVariable("ticketId") Long ticketId,
        Principal principal,
        Model model) {

    User account = userRepository.findByEmail(principal.getName())
            .filter(User::isActive)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.FORBIDDEN));

    Ticket ticket = ticketRepository.findByTicketIdAndUser_UserId(
                    ticketId, account.getUserId()
            )
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    model.addAttribute("ticket", ticket);
    return "ticket-details";
}
    @GetMapping("/user/tickets/new")
    public String showTicketForm(Model model) {
        model.addAttribute("title", "");
        model.addAttribute("description", "");
        model.addAttribute("categoryId", null);
        model.addAttribute("priority", "NORMAL");
        return prepareForm(model);
    }

    @PostMapping("/user/tickets")
    public String submitTicket(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "description", defaultValue = "") String description,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "priority", defaultValue = "") String priority,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        title = title.trim();
        description = description.trim();

        
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("priority", priority);

        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId).orElse(null);
        }

        Ticket.Priority selectedPriority;
        try {
            selectedPriority = Ticket.Priority.valueOf(priority);
        } catch (IllegalArgumentException exception) {
            selectedPriority = null;
        }

        String error = null;

        if (title.isBlank() || title.length() > 150) {
            error = "Enter a title using no more than 150 characters.";
        } else if (description.isBlank() || description.length() > 5000) {
            error = "Enter a description using no more than 5,000 characters.";
        } else if (category == null) {
            error = "Choose a category from the list.";
        } else if (selectedPriority == null) {
            error = "Choose a valid priority.";
        }

        if (error != null) {
            model.addAttribute("error", error);
            return prepareForm(model);
        }

        // Use the account that is currently logged in.
        User account = userRepository.findByEmail(principal.getName())
                .filter(User::isActive)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.FORBIDDEN));

        Ticket ticket = new Ticket();
        ticket.setUser(account);
        ticket.setCategory(category);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(selectedPriority);
        ticket.setStatus(Ticket.Status.NEW);

        Ticket savedTicket = ticketRepository.saveAndFlush(ticket);

        redirectAttributes.addFlashAttribute(
                "success",
                "Your ticket was submitted. Ticket number: "
                        + savedTicket.getTicketId()
        );

        return "redirect:/user/tickets/new";
    }

    private String prepareForm(Model model) {
    model.addAttribute(
            "categories",
            categoryRepository.findAllByOrderByNameAsc()
    );

    model.addAttribute(
            "suggestions",
            suggestionRepository.findAllByOrderByStepNumberAsc()
    );

    return "ticket-form";
}
}
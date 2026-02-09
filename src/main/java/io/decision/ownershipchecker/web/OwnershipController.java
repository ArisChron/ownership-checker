package io.decision.ownershipchecker.web;


import io.decision.ownershipchecker.domain.DecisionInput;
import io.decision.ownershipchecker.service.OwnershipService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class OwnershipController {

    private final OwnershipService service;

    public OwnershipController(OwnershipService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("input", new DecisionInput());
        return "index";
    }

    @PostMapping("/check")
    public String check(@Valid @ModelAttribute("input") DecisionInput input,
                        BindingResult binding,
                        Model model) {
        if (binding.hasErrors()) {
            return "index";
        }
        var result = service.check(input);
        model.addAttribute("result", result);
        return "result";
    }
}


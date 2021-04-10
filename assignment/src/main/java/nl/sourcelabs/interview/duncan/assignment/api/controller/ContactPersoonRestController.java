package nl.sourcelabs.interview.duncan.assignment.api.controller;

import javax.validation.Valid;

import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonListModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonModel;
import nl.sourcelabs.interview.duncan.assignment.service.ContactPersoonService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContactPersoonRestController {

    private final ContactPersoonService contactPersoonService;

    @GetMapping(value = "/contact-persoon")
    public ContactPersoonListModel getAllContactPersonen() {
        return contactPersoonService.findAll();
    }

    @GetMapping(value = "/contact-persoon", params = "name")
    public ContactPersoonModel getContactPersoonByName(@RequestParam(name = "name") final String name) {
        return contactPersoonService.getByName(name);
    }

    @PostMapping("/contact-persoon")
    public void saveProductieInstallatie(@Valid @RequestBody final ContactPersoonModel contactPersoonModel) {
        contactPersoonService.save(contactPersoonModel);
    }

}

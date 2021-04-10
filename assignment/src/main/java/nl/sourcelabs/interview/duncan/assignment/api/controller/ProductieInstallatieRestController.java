package nl.sourcelabs.interview.duncan.assignment.api.controller;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieListModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieModel;
import nl.sourcelabs.interview.duncan.assignment.service.ProductieInstallatieService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductieInstallatieRestController {

    private final ProductieInstallatieService productieInstallatieService;

    @GetMapping(value = "/productie-installatie")
    public ProductieInstallatieListModel getAllProductieInstallaties(@RequestParam(name = "minOutputPower", required = false) final BigDecimal minOutputPower,
            @RequestParam(name = "maxOutputPower", required = false) final BigDecimal maxOutputPower) {
        return productieInstallatieService.findAll(Optional.ofNullable(minOutputPower), Optional.ofNullable(maxOutputPower));
    }

    @GetMapping(value = "/productie-installatie", params = "name")
    public ProductieInstallatieModel getProductieInstallatieByName(@RequestParam(name = "name") final String name) {
        return productieInstallatieService.getByName(name);
    }

    @PostMapping("/productie-installatie")
    public void saveProductieInstallatie(@Valid @RequestBody final ProductieInstallatieModel productieInstallatie) {
            productieInstallatieService.save(productieInstallatie);
    }

}

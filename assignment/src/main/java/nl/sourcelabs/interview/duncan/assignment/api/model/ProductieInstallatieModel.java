package nl.sourcelabs.interview.duncan.assignment.api.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductieInstallatieModel {

    @NotEmpty
    private final String name;

    @NotNull
    private final Integer contact;

    @DecimalMin("0.0001")
    @DecimalMax("999999")
    private final BigDecimal outputPower;


}

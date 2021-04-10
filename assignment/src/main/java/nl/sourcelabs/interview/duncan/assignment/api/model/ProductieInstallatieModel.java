package nl.sourcelabs.interview.duncan.assignment.api.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductieInstallatieModel {

    @NotEmpty
    private String name;

    @NotNull
    private Integer contact;

    @DecimalMin("0.0001")
    @DecimalMax("999999")
    @NotNull
    private BigDecimal outputPower;

}

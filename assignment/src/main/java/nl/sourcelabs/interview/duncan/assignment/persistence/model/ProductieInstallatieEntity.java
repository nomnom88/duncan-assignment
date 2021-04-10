package nl.sourcelabs.interview.duncan.assignment.persistence.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = @Index(columnList = "name", unique = true, name = "PI_NAME_UNIQUE_IDX"))
@Getter
@Setter
@EqualsAndHashCode
public class ProductieInstallatieEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Integer contact;

    @Column(precision = 10, scale = 4)
    private BigDecimal outputPower;

}

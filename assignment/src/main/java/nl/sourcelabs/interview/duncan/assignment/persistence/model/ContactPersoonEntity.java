package nl.sourcelabs.interview.duncan.assignment.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = @Index(columnList = "name", unique = true, name = "CP_NAME_UNIQUE_IDX"))
@Getter
@Setter
@EqualsAndHashCode
public class ContactPersoonEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String zipCode;

    private String city;

    private String number;

}

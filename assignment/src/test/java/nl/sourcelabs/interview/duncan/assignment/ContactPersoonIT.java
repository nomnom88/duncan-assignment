package nl.sourcelabs.interview.duncan.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonListModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ErrorResponse;
import nl.sourcelabs.interview.duncan.assignment.persistence.repo.ContactPersoonRepository;
import nl.sourcelabs.interview.duncan.assignment.service.ContactPersoonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactPersoonIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ContactPersoonRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ContactPersoonService service;

    @BeforeEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Given a persisted set of contact personen When we query for a list Then every contact persoon is returned")
    void get_list() {

        final ContactPersoonModel firstContactPersoon = new ContactPersoonModel();
        firstContactPersoon.setZipCode("1234AA");
        firstContactPersoon.setCity("city 1");
        firstContactPersoon.setName("Name 1");
        firstContactPersoon.setNumber("555");

        final ContactPersoonModel secondContactPersoon = new ContactPersoonModel();
        secondContactPersoon.setZipCode("4321AA");
        secondContactPersoon.setCity("city 2");
        secondContactPersoon.setName("Name 2");
        secondContactPersoon.setNumber("123");

        service.save(firstContactPersoon);
        service.save(secondContactPersoon);

        final String baseUrl = getBaseUrl();

        final ResponseEntity<ContactPersoonListModel> responseEntity = restTemplate.getForEntity(baseUrl, ContactPersoonListModel.class);

        final ContactPersoonListModel result = responseEntity.getBody();

        assertThat(result).isNotNull();
        assertThat(result.getContactPersonen()).isNotNull();
        assertThat(result.getContactPersonen()).containsExactlyInAnyOrder(firstContactPersoon, secondContactPersoon);

    }

    @Test
    @DisplayName("Given a persisted set of contact personen When we query by name Then the correct contact persoon is returned")
    void get_by_name() {

        final ContactPersoonModel firstContactPersoon = new ContactPersoonModel();
        firstContactPersoon.setZipCode("1234AA");
        firstContactPersoon.setCity("city 1");
        firstContactPersoon.setName("Name 1");
        firstContactPersoon.setNumber("555");

        final ContactPersoonModel secondContactPersoon = new ContactPersoonModel();
        secondContactPersoon.setZipCode("4321AA");
        secondContactPersoon.setCity("city 2");
        secondContactPersoon.setName("Name 2");
        secondContactPersoon.setNumber("123");

        service.save(firstContactPersoon);
        service.save(secondContactPersoon);

        final String baseUrl = getBaseUrl();
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("name", firstContactPersoon.getName());

        final ResponseEntity<ContactPersoonModel> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.build(false).toUri(), ContactPersoonModel.class);

        final ContactPersoonModel result = responseEntity.getBody();

        assertThat(result).isEqualTo(firstContactPersoon);
    }

    @Test
    @DisplayName("When we query by name that doesnt exist Then an error returned")
    void get_by_name_not_exists() {

        final String baseUrl = getBaseUrl();
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("name", "NON EXISTENT");

        final ResponseEntity<ErrorResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.build(false).toUri(), ErrorResponse.class);

        final ErrorResponse result = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Contact Persoon with that name does not exist");

    }

    @Test
    @DisplayName("When a contact persoon is persisted Then it can be found in the repository")
    void persist() {
        assertThat(repository.findAll().iterator().hasNext()).isFalse();

        final ContactPersoonModel firstContactPersoon = new ContactPersoonModel();
        firstContactPersoon.setZipCode("1234AA");
        firstContactPersoon.setCity("city 1");
        firstContactPersoon.setName("Name 1");
        firstContactPersoon.setNumber("555");

        restTemplate.postForEntity(getBaseUrl(), firstContactPersoon, Void.class);

        final ContactPersoonModel savedContactPersoon = service
                .getByName(firstContactPersoon.getName());

        assertThat(savedContactPersoon).isEqualTo(firstContactPersoon);
    }

    @Test
    @DisplayName("When a contact persoon is persisted with the same name of an existing contact persoon Then an error is returned")
    void persist_name_exists_error() {
        assertThat(repository.findAll().iterator().hasNext()).isFalse();

        final ContactPersoonModel firstContactPersoon = new ContactPersoonModel();
        firstContactPersoon.setZipCode("1234AA");
        firstContactPersoon.setCity("city 1");
        firstContactPersoon.setName("Name 1");
        firstContactPersoon.setNumber("555");

        restTemplate.postForEntity(getBaseUrl(), firstContactPersoon, Void.class);

        firstContactPersoon.setNumber("000");
        firstContactPersoon.setCity("New city");
        firstContactPersoon.setZipCode("9999ZZ");

        final ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(getBaseUrl(), firstContactPersoon, ErrorResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Name already exists");
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/contact-persoon";
    }

}

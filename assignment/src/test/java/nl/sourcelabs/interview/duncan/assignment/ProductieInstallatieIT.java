package nl.sourcelabs.interview.duncan.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import nl.sourcelabs.interview.duncan.assignment.api.model.ErrorResponse;
import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieListModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieModel;
import nl.sourcelabs.interview.duncan.assignment.persistence.repo.ProductieInstallatieRepository;
import nl.sourcelabs.interview.duncan.assignment.service.ProductieInstallatieService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductieInstallatieIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductieInstallatieService productieInstallatieService;

    @Autowired
    private ProductieInstallatieRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Given two ProductieInstallaties are created when we request a total list of ProductieInstallaties is requested then it is returned")
    void create_two_then_read_list() {

        final ProductieInstallatieModel firstProductieInstallatie = new ProductieInstallatieModel();
        firstProductieInstallatie.setName("Name-1");
        firstProductieInstallatie.setContact(123);
        firstProductieInstallatie.setOutputPower(new BigDecimal("100.0001"));

        final ProductieInstallatieModel secondProductieInstallatie = new ProductieInstallatieModel();
        secondProductieInstallatie.setName("Name-2");
        secondProductieInstallatie.setContact(321);
        secondProductieInstallatie.setOutputPower(new BigDecimal("567.0000"));

        final String baseUrl = getBaseUrl();

        restTemplate.postForEntity(baseUrl, firstProductieInstallatie, Void.class);
        restTemplate.postForEntity(baseUrl, secondProductieInstallatie, Void.class);

        final ResponseEntity<ProductieInstallatieListModel> responseEntity = restTemplate.getForEntity(baseUrl, ProductieInstallatieListModel.class);

        final ProductieInstallatieListModel result = responseEntity.getBody();

        assertThat(result).isNotNull();
        assertThat(result.getProductieInstallaties()).isNotNull();
        assertThat(result.getProductieInstallaties()).containsExactlyInAnyOrder(firstProductieInstallatie, secondProductieInstallatie);

    }

    @Test
    @DisplayName("When we try to create two ProductieInstallaties with the same name then an error is returned")
    void create_two_with_same_name_then_error() {

        final ProductieInstallatieModel firstProductieInstallatie = new ProductieInstallatieModel();
        final String sameName = "Samesies";
        firstProductieInstallatie.setName(sameName);
        firstProductieInstallatie.setContact(123);
        firstProductieInstallatie.setOutputPower(new BigDecimal("100.0001"));

        final ProductieInstallatieModel secondProductieInstallatie = new ProductieInstallatieModel();
        secondProductieInstallatie.setName(sameName);
        secondProductieInstallatie.setContact(321);
        secondProductieInstallatie.setOutputPower(new BigDecimal("567.0000"));

        final String baseUrl = getBaseUrl();

        restTemplate.postForEntity(baseUrl, firstProductieInstallatie, Void.class);
        final ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(baseUrl, secondProductieInstallatie, ErrorResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Name already exists");

    }

    @Test
    @DisplayName("When we request a total list of ProductieInstallaties filtered with maxOutputPower lower than 0 then an error is returned")
    void max_output_power_error() {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl())
                .queryParam("maxOutputPower", new BigDecimal("-1.0"));

        final ResponseEntity<ErrorResponse> responseEntity = getErrorResponseResponseEntity(uriBuilder);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Negative values are not supported for maximum outputPower");
    }

    @Test
    @DisplayName("When we request a total list of ProductieInstallaties filtered with maxOutputPower lower than minOutputPower then an error is returned")
    void max_smaller_than_mix_output_power_error() {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl())
                .queryParam("maxOutputPower", new BigDecimal("15.0002"))
                .queryParam("minOutputPower", new BigDecimal("50.1234"));

        final ResponseEntity<ErrorResponse> responseEntity = getErrorResponseResponseEntity(uriBuilder);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Max Output Power must be larger than min");
    }

    @Test
    @DisplayName("When we request a total list of ProductieInstallaties filtered with minOutputPower lower than 0 then an error is returned")
    void min_output_power_error() {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl())
                .queryParam("minOutputPower", new BigDecimal("-1.0"));

        final ResponseEntity<ErrorResponse> responseEntity = getErrorResponseResponseEntity(uriBuilder);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Negative values are not supported for minimum outputPower");
    }

    @Test
    @DisplayName("Given persisted ProductieInstallatie set when we request a total list of ProductieInstallaties filtered with min and max outputPower then the correct results are returned")
    void query_by_min_max() {

        final ProductieInstallatieModel firstProductieInstallatie = new ProductieInstallatieModel();
        firstProductieInstallatie.setName("Name-1");
        firstProductieInstallatie.setContact(123);
        firstProductieInstallatie.setOutputPower(new BigDecimal("100.0001"));

        final ProductieInstallatieModel secondProductieInstallatie = new ProductieInstallatieModel();
        secondProductieInstallatie.setName("Name-2");
        secondProductieInstallatie.setContact(321);
        secondProductieInstallatie.setOutputPower(new BigDecimal("567.0000"));

        final ProductieInstallatieModel thirdProductieInstallatieModel = new ProductieInstallatieModel();
        thirdProductieInstallatieModel.setName("Name-3");
        thirdProductieInstallatieModel.setContact(666);
        thirdProductieInstallatieModel.setOutputPower(new BigDecimal("99999.9900"));

        productieInstallatieService.save(firstProductieInstallatie);
        productieInstallatieService.save(secondProductieInstallatie);
        productieInstallatieService.save(thirdProductieInstallatieModel);

        ResponseEntity<ProductieInstallatieListModel> responseEntity = getListByMinAndMaxOutputPower("0", "999999");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getProductieInstallaties()).containsExactlyInAnyOrder(firstProductieInstallatie, secondProductieInstallatie, thirdProductieInstallatieModel);

        responseEntity = getListByMinAndMaxOutputPower("200", "999999");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getProductieInstallaties()).containsExactlyInAnyOrder(secondProductieInstallatie, thirdProductieInstallatieModel);

        responseEntity = getListByMinAndMaxOutputPower("200", "900");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getProductieInstallaties()).containsExactlyInAnyOrder(secondProductieInstallatie);

    }

    @Test
    @DisplayName("Given persisted ProductieInstallatie set when we request a specific ProductieInstallatie by name then the correct ProductieInstallatie is returned")
    void query_by_name() {
        final ProductieInstallatieModel firstProductieInstallatie = new ProductieInstallatieModel();
        firstProductieInstallatie.setName("Name-1");
        firstProductieInstallatie.setContact(123);
        firstProductieInstallatie.setOutputPower(new BigDecimal("100.0001"));

        final ProductieInstallatieModel secondProductieInstallatie = new ProductieInstallatieModel();
        secondProductieInstallatie.setName("Name-2");
        secondProductieInstallatie.setContact(321);
        secondProductieInstallatie.setOutputPower(new BigDecimal("567.0000"));

        productieInstallatieService.save(firstProductieInstallatie);
        productieInstallatieService.save(secondProductieInstallatie);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl())
                .queryParam("name", firstProductieInstallatie.getName());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        final HttpEntity<Object> entity = new HttpEntity<>(headers);

        final ResponseEntity<ProductieInstallatieModel> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, ProductieInstallatieModel.class);

        assertThat(responseEntity.getBody()).isNotNull();
        final ProductieInstallatieModel result = responseEntity.getBody();
        assertThat(result).isEqualTo(firstProductieInstallatie);

    }

    @Test
    @DisplayName("When we request a specific ProductieInstallatie by name which isn't found then no content is returned")
    void query_by_name_not_found() {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl())
                .queryParam("name", "DOES NOT EXIST");

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        final HttpEntity<Object> entity = new HttpEntity<>(headers);

        final ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isNotNull();
        assertThat(responseEntity.getBody()
                .getMessage()).isEqualTo("Productie Installatie with that name does not exist");
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/productie-installatie";
    }

    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(final UriComponentsBuilder uriBuilder) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        final HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, ErrorResponse.class);
    }

    private ResponseEntity<ProductieInstallatieListModel> getListByMinAndMaxOutputPower(final String min, final String max) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl())
                .queryParam("minOutputPower", new BigDecimal(min))
                .queryParam("maxOutputPower", new BigDecimal(max));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        final HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, ProductieInstallatieListModel.class);
    }
}

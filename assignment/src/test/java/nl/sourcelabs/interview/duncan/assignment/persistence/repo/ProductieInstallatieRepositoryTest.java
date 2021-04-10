package nl.sourcelabs.interview.duncan.assignment.persistence.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import nl.sourcelabs.interview.duncan.assignment.persistence.model.ProductieInstallatieEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductieInstallatieRepositoryTest {

    @Autowired
    private ProductieInstallatieRepository sut;


    @Test
    @DisplayName("Given a set of persisted ProductieInstallaties When queried by Min Max Then correct results returned")
    void min_max_query() {

        final ProductieInstallatieEntity firstEntity = new ProductieInstallatieEntity();
        firstEntity.setName("Name 1");
        firstEntity.setContact(1234);
        firstEntity.setOutputPower(new BigDecimal("10"));

        final ProductieInstallatieEntity secondEntity = new ProductieInstallatieEntity();
        secondEntity.setName("Name 2");
        secondEntity.setContact(4321);
        secondEntity.setOutputPower(new BigDecimal("25.0004"));

        sut.save(firstEntity);
        sut.save(secondEntity);

        assertResultsForMinMaxQuery(new BigDecimal("5"), new BigDecimal("15"), firstEntity);
        assertResultsForMinMaxQuery(new BigDecimal("5"), new BigDecimal("100"), firstEntity, secondEntity);
        assertResultsForMinMaxQuery(new BigDecimal("15"), new BigDecimal("100"), secondEntity);
        assertResultsForMinMaxQuery(new BigDecimal("25.0003"), new BigDecimal("25.0005"), secondEntity);
    }

    @Test
    @DisplayName("Given a set of persisted ProductieInstallaties When queried by Name Then correct results returned")
    void name_query() {

        final ProductieInstallatieEntity firstEntity = new ProductieInstallatieEntity();
        firstEntity.setName("Name 1");
        firstEntity.setContact(1234);
        firstEntity.setOutputPower(new BigDecimal("10"));

        final ProductieInstallatieEntity secondEntity = new ProductieInstallatieEntity();
        secondEntity.setName("Name 2");
        secondEntity.setContact(4321);
        secondEntity.setOutputPower(new BigDecimal("25.0004"));

        sut.save(firstEntity);
        sut.save(secondEntity);

        final ProductieInstallatieEntity queryResult = sut.findByName(firstEntity.getName())
                .orElseThrow();

        assertThat(queryResult).isEqualTo(firstEntity);

        assertThat(sut.findByName("no existent name")).isEmpty();
    }


    private void assertResultsForMinMaxQuery(final BigDecimal min, final BigDecimal max, final ProductieInstallatieEntity... expectedResults) {
        final Iterable<ProductieInstallatieEntity> byOutputPowerBetween = sut.findByOutputPowerBetween(min, max);
        assertThat(byOutputPowerBetween).containsExactlyInAnyOrder(expectedResults);
    }

}

package nl.sourcelabs.interview.duncan.assignment.persistence.repo;

import java.math.BigDecimal;
import java.util.Optional;

import nl.sourcelabs.interview.duncan.assignment.persistence.model.ProductieInstallatieEntity;

import org.springframework.data.repository.CrudRepository;

public interface ProductieInstallatieRepository extends CrudRepository<ProductieInstallatieEntity, Integer> {

    Iterable<ProductieInstallatieEntity> findByOutputPowerBetween(final BigDecimal min, final BigDecimal max);

    Optional<ProductieInstallatieEntity> findByName(final String name);
}

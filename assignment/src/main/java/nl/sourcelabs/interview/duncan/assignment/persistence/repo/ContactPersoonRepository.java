package nl.sourcelabs.interview.duncan.assignment.persistence.repo;

import java.util.Optional;

import nl.sourcelabs.interview.duncan.assignment.persistence.model.ContactPersoonEntity;

import org.springframework.data.repository.CrudRepository;

public interface ContactPersoonRepository extends CrudRepository<ContactPersoonEntity, Integer> {

    Optional<ContactPersoonEntity> findByName(final String name);
}

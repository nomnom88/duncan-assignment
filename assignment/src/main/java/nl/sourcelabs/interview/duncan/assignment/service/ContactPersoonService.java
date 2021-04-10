package nl.sourcelabs.interview.duncan.assignment.service;

import javax.persistence.EntityNotFoundException;

import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonListModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.mapper.ContactPersoonModelMapper;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.ContactPersoonEntity;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.mapper.ContactPersoonEntityMapper;
import nl.sourcelabs.interview.duncan.assignment.persistence.repo.ContactPersoonRepository;
import nl.sourcelabs.interview.duncan.assignment.util.UserInputValidationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactPersoonService {

    private final ContactPersoonRepository repository;
    private final ContactPersoonEntityMapper entityMapper;
    private final ContactPersoonModelMapper modelMapper;

    public ContactPersoonListModel findAll() {
        final ContactPersoonListModel contactPersoonListModel = new ContactPersoonListModel();
        contactPersoonListModel.setContactPersonen(modelMapper.mapAll(repository.findAll()));
        return contactPersoonListModel;
    }

    public ContactPersoonModel getByName(final String name) {
        return repository.findByName(name).map(modelMapper::map).orElseThrow(() -> new EntityNotFoundException("Contact Persoon with that name does not exist"));

    }

    public void save(final ContactPersoonModel contactPersoonModel) {
        final ContactPersoonEntity entity = entityMapper.map(contactPersoonModel);
        try {
            repository.save(entity);
        } catch (final DataIntegrityViolationException exception) {
            if(exception.getMostSpecificCause().getMessage().contains("NAME_UNIQUE_IDX")) {
                throw new UserInputValidationException("Name already exists");
            }
            throw exception;
        }
    }
}

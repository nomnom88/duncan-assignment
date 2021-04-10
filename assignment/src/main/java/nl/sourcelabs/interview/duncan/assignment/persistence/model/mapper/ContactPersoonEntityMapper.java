package nl.sourcelabs.interview.duncan.assignment.persistence.model.mapper;

import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonModel;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.ContactPersoonEntity;
import nl.sourcelabs.interview.duncan.assignment.util.Mapper;

import org.springframework.stereotype.Component;

@Component
public class ContactPersoonEntityMapper implements Mapper<ContactPersoonModel, ContactPersoonEntity> {

    @Override
    public ContactPersoonEntity map(final ContactPersoonModel input) {
        final ContactPersoonEntity contactPersoonEntity = new ContactPersoonEntity();
        contactPersoonEntity.setCity(input.getCity());
        contactPersoonEntity.setName(input.getName());
        contactPersoonEntity.setNumber(input.getNumber());
        contactPersoonEntity.setZipCode(input.getZipCode());
        return contactPersoonEntity;
    }
}

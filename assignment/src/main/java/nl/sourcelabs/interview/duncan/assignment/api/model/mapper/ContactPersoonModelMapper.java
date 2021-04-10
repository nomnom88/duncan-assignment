package nl.sourcelabs.interview.duncan.assignment.api.model.mapper;

import nl.sourcelabs.interview.duncan.assignment.api.model.ContactPersoonModel;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.ContactPersoonEntity;
import nl.sourcelabs.interview.duncan.assignment.util.Mapper;

import org.springframework.stereotype.Component;

@Component
public class ContactPersoonModelMapper implements Mapper<ContactPersoonEntity, ContactPersoonModel> {

    @Override
    public ContactPersoonModel map(final ContactPersoonEntity input) {
        final ContactPersoonModel model = new ContactPersoonModel();
        model.setCity(input.getCity());
        model.setName(input.getName());
        model.setNumber(input.getNumber());
        model.setZipCode(input.getZipCode());
        return model;
    }
}

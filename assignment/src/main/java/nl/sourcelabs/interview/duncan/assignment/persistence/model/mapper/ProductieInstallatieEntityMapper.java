package nl.sourcelabs.interview.duncan.assignment.persistence.model.mapper;

import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieModel;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.ProductieInstallatieEntity;
import nl.sourcelabs.interview.duncan.assignment.util.Mapper;

import org.springframework.stereotype.Component;

@Component
public class ProductieInstallatieEntityMapper implements Mapper<ProductieInstallatieModel, ProductieInstallatieEntity> {

    @Override
    public ProductieInstallatieEntity map(final ProductieInstallatieModel input) {
        final ProductieInstallatieEntity productieInstallatieEntity = new ProductieInstallatieEntity();
        productieInstallatieEntity.setContact(input.getContact());
        productieInstallatieEntity.setOutputPower(input.getOutputPower());
        productieInstallatieEntity.setName(input.getName());
        return productieInstallatieEntity;
    }
}

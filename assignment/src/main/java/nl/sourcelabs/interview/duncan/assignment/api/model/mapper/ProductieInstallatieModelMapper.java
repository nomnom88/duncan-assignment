package nl.sourcelabs.interview.duncan.assignment.api.model.mapper;

import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieModel;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.ProductieInstallatieEntity;
import nl.sourcelabs.interview.duncan.assignment.util.Mapper;

import org.springframework.stereotype.Component;

@Component
public class ProductieInstallatieModelMapper implements Mapper<ProductieInstallatieEntity, ProductieInstallatieModel> {

    @Override
    public ProductieInstallatieModel map(final ProductieInstallatieEntity input) {
        final ProductieInstallatieModel productieInstallatieModel = new ProductieInstallatieModel();
        productieInstallatieModel.setContact(input.getContact());
        productieInstallatieModel.setName(input.getName());
        productieInstallatieModel.setOutputPower(input.getOutputPower());
        return productieInstallatieModel;
    }
}

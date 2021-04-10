package nl.sourcelabs.interview.duncan.assignment.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieListModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.ProductieInstallatieModel;
import nl.sourcelabs.interview.duncan.assignment.api.model.mapper.ProductieInstallatieModelMapper;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.ProductieInstallatieEntity;
import nl.sourcelabs.interview.duncan.assignment.persistence.model.mapper.ProductieInstallatieEntityMapper;
import nl.sourcelabs.interview.duncan.assignment.persistence.repo.ProductieInstallatieRepository;
import nl.sourcelabs.interview.duncan.assignment.util.UserInputValidationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductieInstallatieService {

    private final ProductieInstallatieEntityMapper entityMapper;

    private final ProductieInstallatieModelMapper modelMapper;

    private final ProductieInstallatieRepository repository;

    public ProductieInstallatieListModel findAll(final Optional<BigDecimal> minOutputPower, final Optional<BigDecimal> maxOutputPower) {
        final Iterable<ProductieInstallatieEntity> productieInstallatieEntities = findRelevantProductieInstallaties(minOutputPower, maxOutputPower);
        return convertEntitiesListToListModel(productieInstallatieEntities);
    }

    public ProductieInstallatieModel getByName(final String name) {
        return repository.findByName(name)
                .map(modelMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Productie Installatie with that name does not exist"));
    }

    public void save(final ProductieInstallatieModel productieInstallatieModel) {
        final ProductieInstallatieEntity entity = entityMapper.map(productieInstallatieModel);
        try {
            repository.save(entity);
        } catch (final DataIntegrityViolationException exception) {
            if(exception.getMostSpecificCause().getMessage().contains("NAME_UNIQUE_IDX")) {
                throw new UserInputValidationException("Name already exists");
            }
            throw exception;
        }
    }

    private ProductieInstallatieListModel convertEntitiesListToListModel(final Iterable<ProductieInstallatieEntity> productieInstallatieEntities) {
        final List<ProductieInstallatieModel> productieInstallatieModelList = modelMapper.mapAll(productieInstallatieEntities);
        final ProductieInstallatieListModel productieInstallatieListModel = new ProductieInstallatieListModel();
        productieInstallatieListModel.setProductieInstallaties(productieInstallatieModelList);
        return productieInstallatieListModel;
    }

    private Iterable<ProductieInstallatieEntity> findRelevantProductieInstallaties(final Optional<BigDecimal> minOutputPower, final Optional<BigDecimal> maxOutputPower) {
        validateMinMaxOutputPower(minOutputPower, maxOutputPower);
        return repository.findByOutputPowerBetween(minOutputPower.orElse(BigDecimal.ZERO), maxOutputPower.orElse(new BigDecimal("999999")));
    }

    private void validateMinMaxOutputPower(final Optional<BigDecimal> minOutputPower, final Optional<BigDecimal> maxOutputPower) {

        minOutputPower.ifPresent(min -> {
            if (min.compareTo(BigDecimal.ZERO) < 0) {
                throw new UserInputValidationException("Negative values are not supported for minimum outputPower");
            }
        });

        maxOutputPower.ifPresent(max -> {
            if (max.compareTo(BigDecimal.ZERO) < 0) {
                throw new UserInputValidationException("Negative values are not supported for maximum outputPower");
            }
        });

        if (maxOutputPower.isPresent() && minOutputPower.isPresent()
            && maxOutputPower.get().compareTo(minOutputPower.get()) < 0) {
                throw new UserInputValidationException("Max Output Power must be larger than min");

        }
    }
}

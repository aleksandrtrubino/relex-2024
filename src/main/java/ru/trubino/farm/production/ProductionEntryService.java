package ru.trubino.farm.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.trubino.farm.product.Product;
import ru.trubino.farm.product.ProductRepository;
import ru.trubino.farm.product.exception.ProductNotFoundException;
import ru.trubino.farm.user.User;
import ru.trubino.farm.user.UserRepository;
import ru.trubino.farm.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductionEntryService {

    @Autowired
    ProductionEntryRepository productionEntryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public ProductionEntry createProductionEntry(ProductionEntryDto productionEntryDto){
        Long productId = productionEntryDto.productId();
        Long producerId = productionEntryDto.producerId();
        Long quantity = productionEntryDto.quantity();

        User producer = userRepository.findById(producerId)
                .orElseThrow(()-> new UserNotFoundException("User with id"+producerId+" not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with id"+productId+" not found"));

        ProductionEntry productionEntry = ProductionEntry.builder()
                .producer(producer)
                .product(product)
                .quantity(quantity)
                .build();
        return productionEntryRepository.save(productionEntry);
    }

    public void deleteProductionEntryById(Long id){
        productionEntryRepository.deleteById(id);
    }

    public List<ProductionEntry> findProductionEntriesBy(LocalDateTime from, LocalDateTime to, Long productId, Long producerId) {

        if (productId != null && !productRepository.existsById(productId))
            throw new ProductNotFoundException("Product with id " + productId + " not found");

        if (producerId != null && !userRepository.existsById(producerId))
            throw new UserNotFoundException("User with id " + producerId + " not found");

        if (from != null && to != null) {
            if (productId != null && producerId != null) {
                return productionEntryRepository.findByTimestampBetweenAndProductIdAndProducerId(from, to, productId, producerId);
            } else if (productId != null) {
                return productionEntryRepository.findByTimestampBetweenAndProductId(from, to, productId);
            } else if (producerId != null) {
                return productionEntryRepository.findByTimestampBetweenAndProducerId(from, to, producerId);
            } else {
                return productionEntryRepository.findByTimestampBetween(from, to);
            }
        } else if (from != null) {
            if (productId != null && producerId != null) {
                return productionEntryRepository.findByTimestampAfterAndProductIdAndProducerId(from, productId, producerId);
            } else if (productId != null) {
                return productionEntryRepository.findByTimestampAfterAndProductId(from, productId);
            } else if (producerId != null) {
                return productionEntryRepository.findByTimestampAfterAndProducerId(from, producerId);
            } else {
                return productionEntryRepository.findByTimestampAfter(from);
            }
        } else if (to != null) {
            if (productId != null && producerId != null) {
                return productionEntryRepository.findByTimestampBeforeAndProductIdAndProducerId(to, productId, producerId);
            } else if (productId != null) {
                return productionEntryRepository.findByTimestampBeforeAndProductId(to, productId);
            } else if (producerId != null) {
                return productionEntryRepository.findByTimestampBeforeAndProducerId(to, producerId);
            } else {
                return productionEntryRepository.findByTimestampBefore(to);
            }
        } else if (productId != null && producerId != null) {
            return productionEntryRepository.findByProductIdAndProducerId(productId, producerId);
        } else if (productId != null) {
            return productionEntryRepository.findByProductId(productId);
        } else if (producerId != null) {
            return productionEntryRepository.findByProducerId(producerId);
        } else {
            return productionEntryRepository.findAll();
        }
    }

    public List<ProductionEntry> getProductionEntriesStatisticsBy(LocalDateTime from, LocalDateTime to, Long productId, Long producerId){
        List<ProductionEntry> selectionEntries = findProductionEntriesBy(from, to, productId, producerId);
        Map<Pair<User, Product>, Long> statisticsMap = new HashMap<>();

        for(ProductionEntry entry : selectionEntries){
            Pair<User, Product> key = Pair.of(entry.getProducer(),entry.getProduct());

            if(statisticsMap.containsKey(key)){
                statisticsMap.put(key, statisticsMap.get(key) + entry.getQuantity());
            }
            else{
                statisticsMap.put(key, entry.getQuantity());
            }
        }

        List<ProductionEntry> statisticsEntries = new ArrayList<>();

        for (Map.Entry<Pair<User, Product>, Long> entry : statisticsMap.entrySet()) {
            Pair<User, Product> key = entry.getKey();
            ProductionEntry statisticsEntry = ProductionEntry.builder()
                    .producer(key.getFirst())
                    .product(key.getSecond())
                    .quantity(entry.getValue())
                    .timestamp(null)
                    .build();
            statisticsEntries.add(statisticsEntry);
        }

        return statisticsEntries;
    }
}

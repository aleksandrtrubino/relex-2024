package ru.trubino.farm.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trubino.farm.product.exception.ProductNotFoundException;
import ru.trubino.farm.unit.Unit;
import ru.trubino.farm.unit.UnitRepository;
import ru.trubino.farm.unit.exception.UnitNotFoundException;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UnitRepository unitRepository;

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product createProduct(ProductDto productDto){
        String name = productDto.name();
        Long unitId = productDto.unitId();
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(()-> new UnitNotFoundException("Unit with id"+unitId+" not found"));
        Product product = Product.builder()
                .name(name)
                .unit(unit)
                .build();
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto productDto){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with id"+id+" not found"));

        String name = productDto.name();
        Long unitId = productDto.unitId();
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(()-> new UnitNotFoundException("Unit with id"+unitId+" not found"));

        product.setName(name);
        product.setUnit(unit);

        return productRepository.save(product);
    }

    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }
}

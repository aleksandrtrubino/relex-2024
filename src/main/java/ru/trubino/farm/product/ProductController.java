package ru.trubino.farm.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Виды продуктов",
        description = "Позволяет владельцу создавать, редактировать и удалять виды продуктов"

)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(
            summary = "Возвращает список всех типов продуктов",
            description = "Возвращает список всех типов продуктов"
    )
    @GetMapping
    public ResponseEntity<?> findAllProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProducts());
    }

    @Operation(
            summary = "Позволяет создать новый тип продукта",
            description = "Позволяет создать новый тип продукта"
    )
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto){
        return ResponseEntity.status(HttpStatus.OK).body(productService.createProduct(productDto));
    }

    @Operation(
            summary = "Позволяет редактировать тип продукта",
            description = "Позволяет редактировать тип продукта"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable Long id, @RequestBody ProductDto productDto){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id,productDto));
    }

    @Operation(
            summary = "Удаляет тип продукта",
            description = "Удаляет тип продукта"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

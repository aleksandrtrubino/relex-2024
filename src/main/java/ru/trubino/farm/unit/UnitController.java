package ru.trubino.farm.unit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Меры измерения",
        description = "Позволяет владельцу создавать, удалять и редактировать меры измерения"
)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/products/units")
public class UnitController {

    @Autowired
    UnitService unitService;

    @Operation(
            summary = "Возвращает список со всеми доступными мерами измерения",
            description = "Возвращает список со всеми доступными мерами измерения"
    )
    @GetMapping
    public ResponseEntity<?> findAllUnits(){
        return ResponseEntity.status(HttpStatus.OK).body(unitService.findAllUnits());
    }

    @Operation(
            summary = "Позволяет создать новую меру измерения",
            description = "Позволяет создать новую меру измерения"
    )
    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody UnitDto unitDto){
        return ResponseEntity.status(HttpStatus.OK).body(unitService.createUnit(unitDto));
    }

    @Operation(
            summary = "Позволяет редактировать меру измерения",
            description = "Позволяет редактировать меру измерения"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @RequestBody UnitDto unitDto){
        return ResponseEntity.status(HttpStatus.OK).body(unitService.updateUnit(id,unitDto));
    }

    @Operation(
            summary = "Удаляет меру измерения",
            description = "Удаляет меру измерения"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id){
        unitService.deleteUnitById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

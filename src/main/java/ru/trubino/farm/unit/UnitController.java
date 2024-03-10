package ru.trubino.farm.unit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Unit Controller",
        description = ""
)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/products")
public class UnitController {

    @Autowired
    UnitService unitService;

    @Operation(
            summary = "",
            description = ""
    )
    @GetMapping("/units")
    public ResponseEntity<?> findAllUnits(){
        return ResponseEntity.status(HttpStatus.OK).body(unitService.findAllUnits());
    }

    @Operation(
            summary = "",
            description = ""
    )
    @PostMapping("/units")
    public ResponseEntity<?> createUnit(@RequestBody UnitDto unitDto){
        return ResponseEntity.status(HttpStatus.OK).body(unitService.createUnit(unitDto));
    }

    @Operation(
            summary = "",
            description = ""
    )
    @PutMapping("/units/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @RequestBody UnitDto unitDto){
        return ResponseEntity.status(HttpStatus.OK).body(unitService.updateUnit(id,unitDto));
    }

    @Operation(
            summary = "",
            description = ""
    )
    @DeleteMapping("/units/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id){
        unitService.deleteUnitById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

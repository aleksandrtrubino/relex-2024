package ru.trubino.farm.production;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(
        name = "Журнал производства",
        description = "Позволяет владельцу получать данные о производстве в виде статистики." +
                " Также, позволяет работникам создавать новые записи в журнале"
)
@RestController
@RequestMapping("/api/v1/production/entries")
public class ProductionEntryController {

    @Autowired
    ProductionEntryService productionEntryService;

    @Operation(
            summary = "Позволяет работнику создавать запись в производственном журнале от своего имени",
            description = "Позволяет работнику создавать запись в производственном журнале от своего имени"
    )
    @PostMapping
    @PreAuthorize("#productionEntryDto.producerId().toString() == principal.getUsername() and hasRole('EMPLOYEE')")
    public ResponseEntity<?> createProductionEntry(@RequestBody ProductionEntryDto productionEntryDto){
        return ResponseEntity.status(HttpStatus.OK).body(productionEntryService.createProductionEntry(productionEntryDto));
    }

    @Operation(
            summary = "Позволяет владельцу удалить запись из производственного журнала",
            description = "Позволяет владельцу удалить запись из производственного журнала"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> deleteProductionEntry(@PathVariable Long id){
        productionEntryService.deleteProductionEntryById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Возвращает список записей производственного журнала",
            description = "Возвращает список записей производственного журнала с возможностью сортировки по работнику, продукту или дате"
    )
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<?> findProductionEntriesBy(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long producerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productionEntryService.findProductionEntriesBy(from,to,productId,producerId));
    }

    @Operation(
            summary = "Возвращает статистику производства",
            description = "Возвращает статистику производства, которая представляет собой" +
                    "набор записей из производственного журнала сгруппированных по паре <producerId,ProductId> "
    )
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/statistics")
    public ResponseEntity<?> getProductionEntriesStatisticsBy(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long producerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productionEntryService.getProductionEntriesStatisticsBy(from,to,productId,producerId));
    }
}

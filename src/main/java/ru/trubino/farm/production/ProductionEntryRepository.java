package ru.trubino.farm.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductionEntryRepository extends JpaRepository<ProductionEntry, Long> {
    List<ProductionEntry> findByTimestampBetweenAndProductIdAndProducerId(LocalDateTime from, LocalDateTime to,Long productId,  Long producerId);
    List<ProductionEntry> findByTimestampBetweenAndProductId(LocalDateTime from, LocalDateTime to, Long productId);
    List<ProductionEntry> findByTimestampBetweenAndProducerId(LocalDateTime from, LocalDateTime to, Long producerId);
    List<ProductionEntry> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    List<ProductionEntry> findByTimestampAfterAndProductIdAndProducerId(LocalDateTime timestamp, Long productId, Long producerId);
    List<ProductionEntry> findByTimestampAfterAndProductId(LocalDateTime timestamp, Long productId);
    List<ProductionEntry> findByTimestampAfterAndProducerId(LocalDateTime timestamp, Long producerId);
    List<ProductionEntry> findByTimestampAfter(LocalDateTime timestamp);

    List<ProductionEntry> findByTimestampBeforeAndProductIdAndProducerId(LocalDateTime timestamp, Long productId, Long producerId);
    List<ProductionEntry> findByTimestampBeforeAndProductId(LocalDateTime timestamp, Long productId);
    List<ProductionEntry> findByTimestampBeforeAndProducerId(LocalDateTime timestamp, Long producerId);
    List<ProductionEntry> findByTimestampBefore(LocalDateTime timestamp);

    List<ProductionEntry> findByProductIdAndProducerId(Long productId, Long producerId);
    List<ProductionEntry> findByProductId(Long productId);
    List<ProductionEntry> findByProducerId(Long producerId);






}

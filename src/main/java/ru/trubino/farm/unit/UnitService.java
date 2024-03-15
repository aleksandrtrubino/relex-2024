package ru.trubino.farm.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trubino.farm.unit.exception.UnitAlreadyExistsException;
import ru.trubino.farm.unit.exception.UnitNotFoundException;

import java.util.List;

@Service
public class UnitService {

    @Autowired
    UnitRepository unitRepository;

    public List<Unit> findAllUnits(){
        return unitRepository.findAll();
    }

    public Unit createUnit(UnitDto unitDto){
        String name = unitDto.name();
        if(unitRepository.existsByName(name))
            throw new UnitAlreadyExistsException("Unit with name "+name+" already exists");
        Unit unit = Unit.builder().name(name).build();
        return unitRepository.save(unit);
    }

    public Unit updateUnit(Long id, UnitDto unitDto){
        Unit unit = unitRepository.findById(id)
                .orElseThrow(()-> new UnitNotFoundException("Unit with id"+id+" not found"));
        String name = unitDto.name();
        unit.setName(name);
        return unitRepository.save(unit);
    }

    public void deleteUnitById(long id){
        unitRepository.deleteById(id);
    }
}

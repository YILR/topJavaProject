package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.space.service.ShipSpecification.*;


@Service
public class ShipService {

    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> getByAllShip(){
        return shipRepository.findAll();
    }

    public Ship getByShipId(Long id){
        return shipRepository.findById(id).orElse(null);
    }

    public void save(Ship ship){
        shipRepository.save(ship);
    }

    public void delete(Long id){
        shipRepository.deleteById(id);
    }

    public boolean exists(Long id){
        return shipRepository.existsById(id);
    }

    public Page<Ship> getFindAllPageSort(Integer pageNumber, Integer pageSize, ShipOrder order){
        return shipRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, order.getFieldName()));
    }

    public List<Ship> getAllPageSort(String  name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                                     Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, Integer pageNumber, Integer pageSize, ShipOrder order){
        Specification<Ship> spec = Specification.where(likeName(name)).and(likePlanet(planet)).and(equalType(shipType))
                .and(betweenDate(after, before)).and(isUsed(isUsed)).and(rangeDouble("speed", minSpeed, maxSpeed)).and(rangeInt(minCrewSize, maxCrewSize))
                .and(rangeDouble("rating", minRating, maxRating));
        if(pageNumber == null)
            return shipRepository.findAll(spec);
        return shipRepository.findAll(spec, PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, order.getFieldName())).getContent();
    }

}

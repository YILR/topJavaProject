package com.space.controller;


import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.space.model.ShipCheck.*;

@RestController
@RequestMapping("/rest")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @GetMapping("/ships")
    public ResponseEntity<List<Ship>> getByAllShip(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                                                   @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                                                   @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                   @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                   @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating,
                                                   @RequestParam(value = "order", defaultValue = "ID") ShipOrder order, @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {


        List<Ship> ships = shipService.getAllPageSort(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, pageNumber, pageSize, order);

        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getByShipId(@PathVariable("id") Long id) {
        if (id == null || id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship = shipService.getByShipId(id);
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @GetMapping("/ships/count")
    public ResponseEntity<Integer> getShipCount(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                                                @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                                                @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating) {

        List<Ship> ships = shipService.getAllPageSort(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, null, null, null);

        return new ResponseEntity<>(ships.size(), HttpStatus.OK);
    }

    @PostMapping("/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {

        if (!checkURLParam(ship))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getUsed() == null) ship.setUsed(false);

        ship.setRating(sumRating(ship));

        shipService.save(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @PostMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id, @RequestBody Ship ship) {

        if (id == null || id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship currentShip = shipService.getByShipId(id);
        if (currentShip == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int count = hasUpdateShip(ship, currentShip);

        if (count == -1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (count > 0) {
            double rating = sumRating(currentShip);
            currentShip.setRating(rating);
            shipService.save(currentShip);
        }
        return new ResponseEntity<>(currentShip, HttpStatus.OK);

    }

    @DeleteMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {
        if (id == null || id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!shipService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        shipService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

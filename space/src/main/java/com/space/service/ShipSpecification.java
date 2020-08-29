package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Date;


public class ShipSpecification {

    public static Specification<Ship> likeName(String name){
        if(name == null)
            return null;
        return (root, query, criteriaBuilder) ->  criteriaBuilder.like(root.get("name"), "%"+name+"%");
    }

    public static Specification<Ship> likePlanet(String planet){
        if(planet == null)
            return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("planet"), "%"+planet+"%"));
    }

    public static Specification<Ship> equalType(ShipType type){
        if(type == null)
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shipType"), type);
    }

    public static Specification<Ship> betweenDate(Long min, Long max){
        Calendar calendar = Calendar.getInstance();
        calendar.set(3019,11,30);
        long after = max == null ? calendar.getTimeInMillis() : max;
        long before = min == null ? new Date().getTime() : min;
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(before)),
                criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"),new Date(after)));
    }

    public static Specification<Ship> isUsed(Boolean isUsed){
        if(isUsed == null)
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isUsed"), isUsed);
    }

    public static Specification<Ship> rangeDouble(String name, Double minimum, Double maximum){
        Double min = minimum == null ? 0.01 : minimum;
        Double max = maximum == null ? 999 : maximum;

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get(name), min),
                criteriaBuilder.lessThanOrEqualTo(root.get(name), max));

    }

    public static Specification<Ship> rangeInt(Integer minimum, Integer maximum){
        Integer min = minimum == null ? 0 : minimum;
        Integer max = maximum == null ? 999999 : maximum;

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), min),
                criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), max));
    }

}

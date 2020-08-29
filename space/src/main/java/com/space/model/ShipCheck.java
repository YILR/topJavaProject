package com.space.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShipCheck {

    public static boolean rangeNumber(double num, double min, double max) {
        return num >= min && num <= max;
    }

    public static double sumRating(Ship ship) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 3019);
        Calendar prodCalendar = Calendar.getInstance();
        prodCalendar.setTime(ship.getProdDate());
        double ret = ship.getUsed() ? 0.5 : 1;

        double result = 80 * ship.getSpeed() * ret / (calendar.get(Calendar.YEAR) - prodCalendar.get(Calendar.YEAR) + 1);
        result = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return result;
    }

    public static boolean checkURLParam(Ship ship){
        if (ship.getName() == null || ship.getName().isEmpty() || ship.getName().length() > 50 ||ship.getPlanet()==null || ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50 || ship.getShipType() == null ||
                ship.getProdDate() == null || ship.getProdDate().before(new GregorianCalendar(2800,0,1).getTime()) || ship.getProdDate().after(new GregorianCalendar(3019,0,1).getTime())
                || ship.getSpeed() == null || !rangeNumber(ship.getSpeed(), 0.01, 0.99) || ship.getCrewSize() == null || !rangeNumber(ship.getCrewSize(), 1, 9999)) {
            return false;
        }
        return true;
    }

    public static int hasUpdateShip(Ship ship, Ship currentShip){
        int count = 0;


        if (ship.getName() != null && ship.getName().isEmpty())
            return -1;
        if (ship.getName() != null && ship.getName().length() <=50) {
            currentShip.setName(ship.getName());
            count++;
        }
        if (ship.getPlanet() != null && !ship.getPlanet().isEmpty() && ship.getPlanet().length() <= 50) {
            currentShip.setPlanet(ship.getPlanet());
            count++;
        }
        if (ship.getShipType() != null) {
            currentShip.setShipType(ship.getShipType());
            count++;
        }
        if (ship.getProdDate() != null) {
            if (!rangeNumber(ship.getProdDate().getTime(),
                    new GregorianCalendar(2800, 0, 1).getTimeInMillis(),
                    new GregorianCalendar(3019, 0, 1).getTimeInMillis()))
                return -1;
            currentShip.setProdDate(ship.getProdDate());
            count++;
        }
        if (ship.getUsed() != null) {
            currentShip.setUsed(ship.getUsed());
            count++;
        }
        if (ship.getSpeed() != null) {
            if(!rangeNumber(ship.getSpeed(), 0.01, 0.99))
                return -1;
            currentShip.setSpeed(ship.getSpeed());
            count++;
        }
        if (ship.getCrewSize() != null) {
            if (!rangeNumber(ship.getCrewSize(), 1, 9999))
                return -1;
            currentShip.setCrewSize(ship.getCrewSize());
            count++;
        }

        return count;
    }
}

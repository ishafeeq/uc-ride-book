package com.shafeeq.ucrbook.ucridebook.app.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class HexToDriversRepository {
    private Map<String, List<String>> hexagonHashToListOfDrivers = new HashMap<>();
    private Map<String, String> driverIdToHexagonalHash = new HashMap<>();

    public void updateDriverToHexagon(String hexHash, String driverId){
        String currentHexagon = getHexagonalHashForDriverId(driverId);
        removeDriverFromHexHash(currentHexagon, driverId);
        addDriverToHexagon(hexHash, driverId);
    }

    public void addDriverToHexagon(String hexHash, String driverId){
        List<String> driversList = hexagonHashToListOfDrivers.get(hexHash);
        if(ObjectUtils.isEmpty(driversList)){
            driversList = new ArrayList<>();
            hexagonHashToListOfDrivers.put(hexHash, driversList);
        }
        driversList.add(driverId);
        driverIdToHexagonalHash.put(driverId, hexHash);
    }

    public String getHexagonalHashForDriverId(String driverId){
        return driverIdToHexagonalHash.get(driverId);
    }

    public List<String> getDriversListForHexHash(String hexHash){
        List<String> driversList = hexagonHashToListOfDrivers.get(hexHash);
        if(ObjectUtils.isEmpty(driversList)){
            driversList = new ArrayList<>();
            hexagonHashToListOfDrivers.put(hexHash, driversList);
        }
        return driversList;
    }

    public List<String> removeDriverFromHexHash(String hexHash, String driverId){
        if(StringUtils.isEmpty(hexHash) || StringUtils.isEmpty(driverId)){
            log.info("Nothing to remove");
            return null;
        }
        List<String> driversList = getDriversListForHexHash(hexHash);
        List<String> filteredDriver = driversList.stream().filter(driver -> {return driverId == driver;}).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(filteredDriver)){
            driversList.remove(driverId);
        }
        return driversList;
    }

}

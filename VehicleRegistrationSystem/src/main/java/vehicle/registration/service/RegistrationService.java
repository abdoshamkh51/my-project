package vehicle.registration.service;

import vehicle.registration.exception.DuplicatePlateException;
import vehicle.registration.exception.VehicleNotFoundException;
import vehicle.registration.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RegistrationService {

    private final List<Vehicle> vehicleList = new ArrayList<>();
    private final Map<String, Vehicle> plateIndex = new HashMap<>();
    private final Set<String> registeredPlates = new HashSet<>();

    public void registerVehicle(Vehicle vehicle) {
        String plate = vehicle.getPlateNumber()
                .trim()
                .toUpperCase();

        if (registeredPlates.contains(plate)) {
            throw new DuplicatePlateException(plate);
        }

        vehicleList.add(vehicle);
        plateIndex.put(plate, vehicle);
        registeredPlates.add(plate);

        System.out.println("Vehicle registered successfully!");
    }

    public Vehicle findByPlate(String plate) {
        String normalizedPlate = plate.trim().toUpperCase();

        Vehicle vehicle = plateIndex.get(normalizedPlate);

        if (vehicle == null) {
            throw new VehicleNotFoundException(normalizedPlate);
        }

        return vehicle;
    }

    public void deleteVehicle(String plate) {
        Vehicle vehicle = findByPlate(plate);

        String normalizedPlate = vehicle.getPlateNumber()
                .trim()
                .toUpperCase();

        vehicleList.remove(vehicle);
        plateIndex.remove(normalizedPlate);
        registeredPlates.remove(normalizedPlate);

        System.out.println("Vehicle deleted successfully!");
    }

    public void updateOwner(String plate, String newOwner) {
        Vehicle vehicle = findByPlate(plate);
        vehicle.setOwnerName(newOwner.trim());

        System.out.println("Owner updated successfully!");
    }

    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(vehicleList);
    }

    public List<Vehicle> filterByType(String type) {
        return vehicleList.stream()
                .filter(vehicle -> vehicle.getVehicleType()
                        .equalsIgnoreCase(type.trim()))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByOwner(String ownerName) {
        String searchName = ownerName.trim().toLowerCase();

        return vehicleList.stream()
                .filter(vehicle -> vehicle.getOwnerName()
                        .toLowerCase()
                        .contains(searchName))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getExpiredRegistrations(int currentYear) {
        return vehicleList.stream()
                .filter(vehicle -> currentYear
                        - vehicle.getRegistrationYear() > 5)
                .sorted((v1, v2) -> Integer.compare(
                        v1.getRegistrationYear(),
                        v2.getRegistrationYear()))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getSortedByYear(boolean ascending) {
        return vehicleList.stream()
                .sorted((v1, v2) -> ascending
                        ? Integer.compare(
                        v1.getRegistrationYear(),
                        v2.getRegistrationYear())
                        : Integer.compare(
                        v2.getRegistrationYear(),
                        v1.getRegistrationYear()))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getVehicleTypeCounts() {
        return vehicleList.stream()
                .collect(Collectors.groupingBy(
                        Vehicle::getVehicleType,
                        Collectors.counting()
                ));
    }

    public void printStatistics() {
        List<Vehicle> vehicles = getAllVehicles();

        System.out.println("\n========== Statistics Report ==========");
        System.out.println("Total Vehicles: " + vehicles.size());

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles registered.");
            System.out.println("=======================================");
            return;
        }

        System.out.println("\nVehicles by Type:");
        getVehicleTypeCounts().forEach((type, count) ->
                System.out.println(type + ": " + count));

        long activeCount = vehicles.stream()
                .filter(v -> "ACTIVE".equalsIgnoreCase(v.getStatus()))
                .count();

        long expiredCount = vehicles.stream()
                .filter(v -> "EXPIRED".equalsIgnoreCase(v.getStatus()))
                .count();

        System.out.println("\nVehicles by Status:");
        System.out.println("Active: " + activeCount);
        System.out.println("Expired: " + expiredCount);
        System.out.println("=======================================");
    }

}
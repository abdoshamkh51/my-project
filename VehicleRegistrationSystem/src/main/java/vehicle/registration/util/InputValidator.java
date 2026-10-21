package vehicle.registration.util;

import vehicle.registration.exception.InvalidInputException;

import java.time.Year;

public class InputValidator {

    private InputValidator() {
    }

    public static String validatePlateNumber(String plateNumber) {
        if (plateNumber == null
                || !plateNumber.trim().matches("[A-Za-z0-9-]{3,10}")) {
            throw new InvalidInputException(
                    "Plate number must be 3-10 alphanumeric characters");
        }

        return plateNumber.trim().toUpperCase();
    }

    public static String validateOwnerName(String ownerName) {
        if (ownerName == null
                || ownerName.trim().length() < 3
                || !ownerName.trim().matches("[\\p{L} ]+")) {
            throw new InvalidInputException(
                    "Owner name must contain only letters");
        }

        return ownerName.trim();
    }

    public static String validateVehicleType(String vehicleType) {
        if (vehicleType == null) {
            throw new InvalidInputException(
                    "Invalid type. Use: Car, Truck, Motorcycle");
        }

        String type = vehicleType.trim();

        if (type.equalsIgnoreCase("Car")) {
            return "Car";
        }

        if (type.equalsIgnoreCase("Truck")) {
            return "Truck";
        }

        if (type.equalsIgnoreCase("Motorcycle")) {
            return "Motorcycle";
        }

        throw new InvalidInputException(
                "Invalid type. Use: Car, Truck, Motorcycle");
    }

    public static int validateRegistrationYear(int year) {
        int currentYear = Year.now().getValue();

        if (year < 1990 || year > currentYear) {
            throw new InvalidInputException(
                    "Year must be between 1990 and " + currentYear);
        }

        return year;
    }

    public static String validateStatus(String status) {
        if (status == null) {
            throw new InvalidInputException(
                    "Status must be ACTIVE or EXPIRED");
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!normalizedStatus.equals("ACTIVE")
                && !normalizedStatus.equals("EXPIRED")) {
            throw new InvalidInputException(
                    "Status must be ACTIVE or EXPIRED");
        }

        return normalizedStatus;
    }
}
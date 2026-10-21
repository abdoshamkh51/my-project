package vehicle.registration.service.impl;

import vehicle.registration.exception.DuplicatePlateException;
import vehicle.registration.exception.InvalidInputException;
import vehicle.registration.exception.VehicleNotFoundException;
import vehicle.registration.model.Car;
import vehicle.registration.model.Motorcycle;
import vehicle.registration.model.Truck;
import vehicle.registration.model.Vehicle;
import vehicle.registration.service.ApplicationService;
import vehicle.registration.service.RegistrationService;
import vehicle.registration.util.InputValidator;

import java.time.Year;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VehicleApplicationServiceImpl implements ApplicationService {

    private final RegistrationService service = new RegistrationService();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void start() {
        seedData();

        while (true) {
            System.out.println("\n========================================");
            System.out.println(" VEHICLE REGISTRATION SYSTEM v1.0");
            System.out.println("========================================");
            System.out.println("1. Register New Vehicle");
            System.out.println("2. Search Vehicle by Plate");
            System.out.println("3. Update Owner Name");
            System.out.println("4. Delete Vehicle");
            System.out.println("5. List All Vehicles");
            System.out.println("6. Filter by Vehicle Type");
            System.out.println("7. Show Owner History");
            System.out.println("8. Show Expired Registrations");
            System.out.println("9. Statistics Report");
            System.out.println("0. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            if (choice == 0) {
                System.out.println("Goodbye!");
                sc.close();
                break;
            }

            try {
                switch (choice) {
                    case 1:
                        registerVehicle();
                        break;

                    case 2:
                        System.out.print("Enter plate number: ");
                        String plate = InputValidator.validatePlateNumber(
                                sc.nextLine());
                        System.out.println(service.findByPlate(plate));
                        break;

                    case 3:
                        System.out.print("Enter plate number: ");
                        String updatePlate = InputValidator.validatePlateNumber(
                                sc.nextLine());

                        System.out.print("Enter new owner name: ");
                        String newOwner = InputValidator.validateOwnerName(
                                sc.nextLine());

                        service.updateOwner(updatePlate, newOwner);
                        break;

                    case 4:
                        System.out.print("Enter plate number: ");
                        String deletePlate = InputValidator.validatePlateNumber(
                                sc.nextLine());
                        service.deleteVehicle(deletePlate);
                        break;

                    case 5:
                        displayVehicles(service.getAllVehicles());
                        break;

                    case 6:
                        System.out.print(
                                "Enter vehicle type (Car/Truck/Motorcycle): ");
                        String type = InputValidator.validateVehicleType(
                                sc.nextLine());
                        displayVehicles(service.filterByType(type));
                        break;

                    case 7:
                        System.out.print("Enter owner name: ");
                        String owner = InputValidator.validateOwnerName(
                                sc.nextLine());
                        displayVehicles(service.getVehiclesByOwner(owner));
                        break;

                    case 8:
                        displayVehicles(service.getExpiredRegistrations(
                                Year.now().getValue()));
                        break;

                    case 9:
                        service.printStatistics();
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please try again.");
                }

            } catch (DuplicatePlateException
                     | VehicleNotFoundException e) {
                System.out.println("Error: " + e.getMessage());

            } catch (InvalidInputException e) {
                System.out.println("Input Error: " + e.getMessage());

            } catch (InputMismatchException e) {
                System.out.println(
                        "Invalid input. Please enter a valid value.");
                sc.nextLine();
            }
        }
    }

    private void registerVehicle() {
        System.out.print("Enter plate number: ");
        String plate = InputValidator.validatePlateNumber(sc.nextLine());

        System.out.print("Enter owner name: ");
        String owner = InputValidator.validateOwnerName(sc.nextLine());

        System.out.print("Enter vehicle type (Car/Truck/Motorcycle): ");
        String type = InputValidator.validateVehicleType(sc.nextLine());

        System.out.print("Enter registration year: ");
        int year = sc.nextInt();
        sc.nextLine();
        year = InputValidator.validateRegistrationYear(year);

        System.out.print("Enter status (ACTIVE/EXPIRED): ");
        String status = InputValidator.validateStatus(sc.nextLine());

        Vehicle vehicle;

        switch (type) {
            case "Car":
                System.out.print("Enter number of doors: ");
                int doors = sc.nextInt();
                sc.nextLine();
                vehicle = new Car(plate, owner, year, status, doors);
                break;

            case "Truck":
                System.out.print("Enter cargo capacity in tons: ");
                double capacity = sc.nextDouble();
                sc.nextLine();
                vehicle = new Truck(plate, owner, year, status, capacity);
                break;

            case "Motorcycle":
                System.out.print("Enter engine type: ");
                String engine = sc.nextLine();
                vehicle = new Motorcycle(plate, owner, year, status, engine);
                break;

            default:
                return;
        }

        service.registerVehicle(vehicle);
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private void seedData() {
        service.registerVehicle(
                new Car("CAR-001", "Ahmed Ali", 2019, "EXPIRED", 4));
        service.registerVehicle(
                new Car("CAR-002", "Sara Kamel", 2023, "ACTIVE", 2));
        service.registerVehicle(
                new Truck("TRK-001", "Mohamed Said", 2021, "ACTIVE", 10.0));
        service.registerVehicle(
                new Truck("TRK-002", "Laila Nour", 2017, "EXPIRED", 5.5));
        service.registerVehicle(
                new Motorcycle("MOT-001", "Omar Fathi", 2022, "ACTIVE", "Sport"));
        service.registerVehicle(
                new Motorcycle("MOT-002", "Nadia Hamed", 2024, "ACTIVE", "Cruiser"));
    }
}
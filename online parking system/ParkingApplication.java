package parkingapplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ParkingSpot class start

class ParkingSpot {

    private int spotNumber;
    private boolean isAvailable;

    public ParkingSpot(int spotNumber) {
        this.spotNumber = spotNumber;
        this.isAvailable = true;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

// ParkingSpot class ends
// ParkingBooking class start

class ParkingBooking {
    private static int nextBookingId = 1;
    private int bookingId;
    private User user;
    private ParkingSpot parkingSpot;

    public ParkingBooking(User user, ParkingSpot parkingSpot) {
        this.bookingId = nextBookingId++;
        this.user = user;
        this.parkingSpot = parkingSpot;
    }

    public int getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParkingSpot getParkingSpot() {//
        return parkingSpot;
    }
}
// ParkingBooking class ends

// User class Start
class User {
    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
// User class ends

// ParkingSystem class start

class ParkingSystem {
    private List<ParkingSpot> parkingSpots;
    private List<ParkingBooking> bookings;
    private List<User> users;
    private Map<Integer, ParkingBooking> bookingMap;

    public ParkingSystem() {
        this.parkingSpots = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookingMap = new HashMap<>();
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addParkingSpot(ParkingSpot parkingSpot) {
        parkingSpots.add(parkingSpot);
    }

    public List<ParkingSpot> searchAvailableSpots() {
        List<ParkingSpot> availableSpots = new ArrayList<>();
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isAvailable()) {
                availableSpots.add(spot);
            }
        }
        return availableSpots;
    }

    public ParkingBooking bookParkingSpot(User user, ParkingSpot parkingSpot, boolean available) {
        if (!parkingSpot.isAvailable()) {
            return null;
        }

        ParkingBooking booking = new ParkingBooking(user, parkingSpot);
        bookings.add(booking);
        parkingSpot.setAvailable(available = false);
        bookingMap.put(booking.getBookingId(), booking);
        return booking;
    }

    public List<ParkingBooking> getUserBookings(User user) {
        List<ParkingBooking> userBookings = new ArrayList<>();
        for (ParkingBooking booking : bookings) {
            if (booking.getUser().equals(user)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public boolean isUserRegistered(String username) {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public ParkingBooking getBookingById(int bookingId) {
        return bookingMap.get(bookingId);
    }
}

// ParkingSystem class ends
// ParkingApplication class start

public class ParkingApplication {
    public static void main(String[] args) {
        ParkingSystem parkingSystem = new ParkingSystem();

        //
        parkingSystem.addParkingSpot(new ParkingSpot(1));
        parkingSystem.addParkingSpot(new ParkingSpot(2));
        parkingSystem.addParkingSpot(new ParkingSpot(3));
        parkingSystem.addParkingSpot(new ParkingSpot(4));

        Scanner scanner = new Scanner(System.in);
        int choice;
        String username, password;
        boolean loggedIn = false;
        User user = null;

        do {
            if (!loggedIn) {
                System.out.println("**************************************");
                System.out.println("Welcome to the Online Parking System");
                System.out.println("**************************************");
                System.out.println("*        1. Register User            *");
                System.out.println("*        2. Login                    *");
                System.out.println("*        3. Exit                     *");
                System.out.println("**************************************");
                System.out.println("Enter Your Choice : ");
                choice = scanner.nextInt();
                scanner.nextLine();// consume the new line character after reading the choice

                switch (choice) {
                    case 1:
                        // Register purpose
                        System.out.println("Enter Your Name : ");
                        username = scanner.nextLine();
                        if (parkingSystem.isUserRegistered(username)) {
                            System.out.println("Username is already taken.Choose another name");

                        } else {
                            System.out.println("Enter Your password : ");
                            password = scanner.nextLine();
                            user = new User(username, password);
                            parkingSystem.registerUser(user);
                            System.out.println("**User Registration Successfully**");
                            loggedIn = true;
                        }
                        break;
                    case 2:
                        // For Login
                        System.out.println("Enter Your Username : ");
                        username = scanner.nextLine();
                        System.out.println("Enter Your Password:  ");
                        password = scanner.nextLine();
                        user = parkingSystem.getUser(username);
                        if (user != null && user.getPassword().equals(password)) {
                            System.out.println("**Login Successfull**");
                        } else {
                            System.out.println("Invalid username and password.please try again");
                        }
                        break;
                    case 3:
                        System.out.println("Thankyou for using the online parking system");
                        break;
                    default:
                        System.out.println("Invliad choice please try again");
                }
            } else {
                System.out.println("****************************************");
                System.out.println("* 1. search for available parking spots*");
                System.out.println("* 2. Book parking spot                 *");
                System.out.println("* 3. view your bookings                *");
                System.out.println("* 4. Logout                            *");
                System.out.println("****************************************");
                System.out.println("Enter Your Choice : ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        // Search the parking spot
                        List<ParkingSpot> availableSpots = parkingSystem.searchAvailableSpots();
                        if (!availableSpots.isEmpty()) {
                            System.out.println("Parking spots Available");
                        } else {
                            System.out.println("No available parking spots");
                        }
                        break;
                    case 2:
                        // Booking spot
                        System.out.println("Enter The Spot Number To Book : ");
                        int spotNumber = scanner.nextInt();
                        scanner.nextLine();
                        ParkingSpot spotToBook = null;
                        for (ParkingSpot spot : parkingSystem.searchAvailableSpots()) {
                            if (spot.getSpotNumber() == spotNumber) {
                                spotToBook = spot;
                                break;
                            }
                        }
                        if (spotToBook != null) {
                            ParkingBooking booking = parkingSystem.bookParkingSpot(user, spotToBook, loggedIn);
                            if (booking != null) {
                                System.out.println("Booking successfull.......Booking ID: " + booking.getBookingId());
                            } else {
                                System.out.println("Spot is already booked.please choose another spot.");
                            }
                        } else {
                            System.out.println("Invalid spot number. please choose an available spot.");
                        }
                        break;
                    case 3:
                        // --> User view booking
                        List<ParkingBooking> userBookings = parkingSystem.getUserBookings(user);
                        if (!userBookings.isEmpty()) {
                            System.out.println("*User Bookings ");
                            for (ParkingBooking booking : userBookings) {
                                System.out.println("Booking ID : " + booking.getBookingId() + "   ,  Spot : "
                                        + booking.getParkingSpot());
                            }
                        } else {
                            System.out.println("No Booking found for " + user.getUserName() + ".");
                        }
                        break;
                    case 4:
                        // Logout
                        System.out.println("**Logout successfully**");
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid choice .please try again");
                }
            }
            System.out.println();

        } while (choice != 3);
        scanner.close();
    }
}

// ParkingApplication class ends

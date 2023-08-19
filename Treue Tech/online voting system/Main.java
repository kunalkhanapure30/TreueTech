import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class User {
    private String username;
    private String passwordHash;

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        String inputHash = hashPassword(password);
        return inputHash.equals(passwordHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hash = new StringBuilder();

            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class Ballot {
    private String title;
    private List<String> options;
    private Map<String, Integer> voteCounts;

    public Ballot(String title, List<String> options) {
        this.title = title;
        this.options = options;
        this.voteCounts = new HashMap<>();
        for (String option : options) {
            voteCounts.put(option, 0);
        }
    }

    public String getTitle() {
        return title;
    }

    public List<String> getOptions() {
        return options;
    }

    public void castVote(String option) {
        if (voteCounts.containsKey(option)) {
            voteCounts.put(option, voteCounts.get(option) + 1);
            System.out.println("Vote for '" + option + "' successfully cast.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    public void displayResults() {
        System.out.println("Results for '" + title + "':");
        for (Map.Entry<String, Integer> entry : voteCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " votes");
        }
    }
}

class VotingSystem {
    private Map<String, User> users;
    List<Ballot> ballots;

    public VotingSystem() {
        users = new HashMap<>();
        ballots = new ArrayList<>();
    }

    public void registerUser(String username, String password) {
        User newUser = new User(username, password);
        users.put(username, newUser);
    }

    public void createBallot(String title, List<String> options) {
        Ballot newBallot = new Ballot(title, options);
        ballots.add(newBallot);
    }

    public boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user != null) {
            return user.authenticate(password);
        }
        return false;
    }

    public void displayBallots() {
        System.out.println("Available ballots:");
        for (int i = 0; i < ballots.size(); i++) {
            System.out.println((i + 1) + ". " + ballots.get(i).getTitle());
        }
    }

    public void castVote(String username, int ballotIndex, String option) {
        if (ballotIndex >= 0 && ballotIndex < ballots.size()) {
            ballots.get(ballotIndex).castVote(option);
        } else {
            System.out.println("Invalid ballot index.");
        }
    }

    public void displayResults(int ballotIndex) {
        if (ballotIndex >= 0 && ballotIndex < ballots.size()) {
            ballots.get(ballotIndex).displayResults();
        } else {
            System.out.println("Invalid ballot index.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        VotingSystem votingSystem = new VotingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create Ballot");
            System.out.println("4. Display Ballots");
            System.out.println("5. Cast Vote");
            System.out.println("6. Display Results");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String regUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String regPassword = scanner.nextLine();
                    votingSystem.registerUser(regUsername, regPassword);
                    System.out.println("Registration successful.");
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    if (votingSystem.authenticateUser(username, password)) {
                        System.out.println("Login successful.");
                        loggedInMenu(username, votingSystem, scanner);
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;

                case 3:
                    System.out.print("Enter ballot title: ");
                    String ballotTitle = scanner.nextLine();
                    List<String> options = new ArrayList<>();
                    while (true) {
                        System.out.print("Enter option (or 'done' to finish): ");
                        String option = scanner.nextLine();
                        if (option.equals("done")) {
                            break;
                        }
                        options.add(option);
                    }
                    votingSystem.createBallot(ballotTitle, options);
                    System.out.println("Ballot creation successful.");
                    break;

                case 4:
                    votingSystem.displayBallots();
                    break;

                case 5:
                    System.out.print("Enter your username: ");
                    String voteUsername = scanner.nextLine();
                    System.out.print("Enter the index of the ballot: ");
                    int ballotIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (ballotIndex >= 0 && ballotIndex < votingSystem.ballots.size()) {
                        Ballot selectedBallot = votingSystem.ballots.get(ballotIndex);
                        System.out.println("Available options:");
                        for (int i = 0; i < selectedBallot.getOptions().size(); i++) {
                            System.out.println((i + 1) + ". " + selectedBallot.getOptions().get(i));
                        }
                        System.out.print("Select an option: ");
                        int optionIndex = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (optionIndex >= 1 && optionIndex <= selectedBallot.getOptions().size()) {
                            votingSystem.castVote(voteUsername, ballotIndex, selectedBallot.getOptions().get(optionIndex - 1));
                        } else {
                            System.out.println("Invalid option index.");
                        }
                    } else {
                        System.out.println("Invalid ballot index.");
                    }
                    break;

                case 6:
                    System.out.print("Enter the index of the ballot: ");
                    int resultsIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    votingSystem.displayResults(resultsIndex);
                    break;

                case 7:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
    }

    private static void loggedInMenu(String username, VotingSystem votingSystem, Scanner scanner) {
        System.out.println("Welcome, " + username + "!");
        while (true) {
            System.out.println("1. Display Ballots");
            System.out.println("2. Cast Vote");
            System.out.println("3. Display Results");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    votingSystem.displayBallots();
                    break;

                case 2:
                    System.out.print("Enter the index of the ballot: ");
                    int ballotIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (ballotIndex >= 0 && ballotIndex < votingSystem.ballots.size()) {
                        Ballot selectedBallot = votingSystem.ballots.get(ballotIndex);
                        System.out.println("Available options:");
                        for (int i = 0; i < selectedBallot.getOptions().size(); i++) {
                            System.out.println((i + 1) + ". " + selectedBallot.getOptions().get(i));
                        }
                        System.out.print("Select an option: ");
                        int optionIndex = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (optionIndex >= 1 && optionIndex <= selectedBallot.getOptions().size()) {
                            votingSystem.castVote(username, ballotIndex, selectedBallot.getOptions().get(optionIndex - 1));
                        } else {
                            System.out.println("Invalid option index.");
                        }
                    } else {
                        System.out.println("Invalid ballot index.");
                    }
                    break;

                case 3:
                    System.out.print("Enter the index of the ballot: ");
                    int resultsIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    votingSystem.displayResults(resultsIndex);
                    break;

                case 4:
                    System.out.println("Logging out.");
                    return;

                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration {
    private Map<String, User> users;
    private Map<String, List<String>> registrations;
    private EventManager eventManager;
    private FileManager fileManager;

    public Registration(EventManager eventManager) {
        this.users = new HashMap<>();
        this.registrations = new HashMap<>();
        this.eventManager = eventManager;
        this.fileManager = new FileManager();
        
        Map<String, User> loaded = fileManager.loadUsersFromFile();
        if (!loaded.isEmpty()) this.users = loaded;
    }

    public boolean registerUser(User user) {
        if (user == null || users.containsKey(user.getUsername())) return false;
        users.put(user.getUsername(), user);
        registrations.put(user.getUsername(), new ArrayList<>());
        return true;
    }

    public User loginUser(String username, String password) {
        User user = users.get(username);
        return (user != null && user.validateCredentials(username, password)) ? user : null;
    }

    public boolean registerForEvent(String username, String eventId) {
        if (!users.containsKey(username)) { System.out.println("User not found!"); return false; }
        
        Event event = eventManager.findEventById(eventId);
        if (event == null) { System.out.println("Event not found!"); return false; }
        if (event.getAvailableSeats() <= 0) { System.out.println("Event is fully booked!"); return false; }
        
        List<String> userRegs = registrations.get(username);
        if (userRegs == null) { userRegs = new ArrayList<>(); registrations.put(username, userRegs); }
        if (userRegs.contains(eventId)) { System.out.println("Already registered!"); return false; }
        
        if (eventManager.updateAvailableSeats(eventId, 1)) {
            userRegs.add(eventId);
            System.out.println("Registered successfully!");
            System.out.println("Event: " + event.getEventTitle());
            System.out.println("Company: " + event.getCompanyName());
            System.out.println("Date: " + event.getDate());
            System.out.println("Venue: " + event.getVenue());
            if (event.getRegistrationFee() > 0) System.out.println("Fee: $" + event.getRegistrationFee());
            return true;
        }
        return false;
    }

    public boolean cancelRegistration(String username, String eventId) {
        if (!users.containsKey(username)) { System.out.println("User not found!"); return false; }
        
        List<String> userRegs = registrations.get(username);
        if (userRegs == null || !userRegs.contains(eventId)) {
            System.out.println("Not registered for this event!");
            return false;
        }
        
        Event event = eventManager.findEventById(eventId);
        if (event != null) {
            event.setAvailableSeats(event.getAvailableSeats() + 1);
            userRegs.remove(eventId);
            System.out.println("Registration cancelled!");
            return true;
        }
        return false;
    }

    public List<String> getRegisteredEvents(String username) {
        return registrations.getOrDefault(username, new ArrayList<>());
    }

    public void displayRegisteredEvents(String username) {
        List<String> ids = registrations.get(username);
        if (ids == null || ids.isEmpty()) {
            System.out.println("No registered events.");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              YOUR REGISTERED EVENTS                                ║");
        System.out.println("╠════════╤═══════════════════════╤══════════════════════════════╤══════════════╤═══════════════╣");
        System.out.println("║ ID     │ Company               │ Title                        │ Type         │ Date          ║");
        System.out.println("╠════════╪═══════════════════════╪══════════════════════════════╪══════════════╪═══════════════╣");
        
        for (String id : ids) {
            Event e = eventManager.findEventById(id);
            if (e != null) {
                System.out.printf("║ %-6s │ %-21s │ %-28s │ %-12s │ %-13s ║\n",
                        e.getEventId(), e.getCompanyName(), truncate(e.getEventTitle(), 28),
                        e.getEventType(), e.getDate());
            }
        }
        System.out.println("╚════════╧═══════════════════════╧══════════════════════════════╧══════════════╧═══════════════╝");
    }

    private String truncate(String str, int max) {
        if (str == null) return "";
        return str.length() <= max ? str : str.substring(0, max - 3) + "...";
    }

    public Map<String, List<String>> getRegistrations() { return new HashMap<>(registrations); }
    public void setRegistrations(Map<String, List<String>> regs) { this.registrations = regs; }
    public void loadRegistrationsFromFile() {
        Map<String, List<String>> loaded = fileManager.loadRegistrationsFromFile();
        if (!loaded.isEmpty()) this.registrations = loaded;
    }
    public Map<String, User> getUsers() { return new HashMap<>(users); }
    public void setUsers(Map<String, User> users) { this.users = users; }
}

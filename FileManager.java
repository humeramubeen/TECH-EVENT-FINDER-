import java.io.*;
import java.util.*;

public class FileManager {
    private static final String EVENTS_FILE = "events.txt";
    private static final String REGISTRATIONS_FILE = "registrations.txt";
    private static final String USERS_FILE = "users.txt";

    public boolean saveEventsToFile(List<Event> events) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(EVENTS_FILE))) {
            for (Event e : events) { w.write(e.toFileString()); w.newLine(); }
            System.out.println("Events saved to " + EVENTS_FILE);
            return true;
        } catch (IOException e) { System.err.println("Error: " + e.getMessage()); return false; }
    }

    public List<Event> loadEventsFromFile() {
        List<Event> events = new ArrayList<>();
        File file = new File(EVENTS_FILE);
        if (!file.exists()) { System.out.println("Events file not found."); return events; }
        
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                Event e = Event.fromFileString(line);
                if (e != null) events.add(e);
            }
            System.out.println("Loaded " + events.size() + " events.");
        } catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
        return events;
    }

    public boolean saveRegistrationsToFile(Map<String, List<String>> registrations) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(REGISTRATIONS_FILE))) {
            for (Map.Entry<String, List<String>> entry : registrations.entrySet()) {
                w.write(entry.getKey() + ":" + String.join(",", entry.getValue()));
                w.newLine();
            }
            System.out.println("Registrations saved to " + REGISTRATIONS_FILE);
            return true;
        } catch (IOException e) { System.err.println("Error: " + e.getMessage()); return false; }
    }

    public Map<String, List<String>> loadRegistrationsFromFile() {
        Map<String, List<String>> regs = new HashMap<>();
        File file = new File(REGISTRATIONS_FILE);
        if (!file.exists()) { System.out.println("Registrations file not found."); return regs; }
        
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    List<String> ids = new ArrayList<>();
                    if (!parts[1].isEmpty()) ids.addAll(Arrays.asList(parts[1].split(",")));
                    regs.put(parts[0], ids);
                }
            }
            System.out.println("Loaded registrations.");
        } catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
        return regs;
    }

    public boolean saveUsersToFile(Map<String, User> users) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users.values()) {
                w.write(String.format("%s|%s|%s|%s", u.getUsername(), u.getPassword(), u.getFullName(), u.getEmail()));
                w.newLine();
            }
            System.out.println("Users saved to " + USERS_FILE);
            return true;
        } catch (IOException e) { System.err.println("Error: " + e.getMessage()); return false; }
    }

    public Map<String, User> loadUsersFromFile() {
        Map<String, User> users = new HashMap<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) { System.out.println("Users file not found."); return users; }
        
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    User u = new User(parts[0], parts[1], parts[2], parts[3]);
                    users.put(u.getUsername(), u);
                }
            }
            System.out.println("Loaded " + users.size() + " users.");
        } catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
        return users;
    }
}

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eventId, companyName, eventTitle, eventType, city, venue, date, time, duration, certificate;
    private int availableSeats;
    private double registrationFee, rating;

    public Event() {}

    public Event(String eventId, String companyName, String eventTitle, String eventType,
                 String city, String venue, String date, String time, String duration,
                 int availableSeats, String certificate, double registrationFee, double rating) {
        this.eventId = eventId;
        this.companyName = companyName;
        this.eventTitle = eventTitle;
        this.eventType = eventType;
        this.city = city;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.availableSeats = availableSeats;
        this.certificate = certificate;
        this.registrationFee = registrationFee;
        this.rating = rating;
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public String getCertificate() { return certificate; }
    public void setCertificate(String certificate) { this.certificate = certificate; }
    public double getRegistrationFee() { return registrationFee; }
    public void setRegistrationFee(double registrationFee) { this.registrationFee = registrationFee; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    @Override
    public String toString() {
        return String.format("ID: %-6s | %-20s | %-30s | %-12s | %-15s | %-15s",
                eventId, companyName, eventTitle, eventType, city, date);
    }

    public String toFileString() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%d|%s|%.2f|%.1f",
                eventId, companyName, eventTitle, eventType, city, venue,
                date, time, duration, availableSeats, certificate, registrationFee, rating);
    }

    public static Event fromFileString(String line) {
        String[] p = line.split("\\|");
        if (p.length == 13) {
            return new Event(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8],
                    Integer.parseInt(p[9]), p[10], Double.parseDouble(p[11]), Double.parseDouble(p[12]));
        }
        return null;
    }
}

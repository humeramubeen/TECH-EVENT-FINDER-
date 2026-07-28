import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TechEventFinderGUI extends Frame implements ActionListener {
    
    // Backend
    private EventManager eventManager;
    private Registration registration;
    private FileManager fileManager;
    
    // GUI Components
    private Panel headerPanel, searchPanel, statsPanel, resultPanel, footerPanel;
    private Label titleLabel, subtitleLabel, footerLabel;
    private TextField cityField, companyField, dateField;
    private Choice categoryChoice;
    private Button searchButton, resetButton, exitButton;
    private TextArea resultArea;
    
    // Color Constants
    private static final int WIDTH = 1150, HEIGHT = 750;
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color LIGHT_BLUE = new Color(235, 245, 251);
    private static final Color WHITE = Color.WHITE;
    private static final Color CARD_BG = new Color(248, 249, 250);
    private static final Color TEXT_DARK = new Color(51, 51, 51);
    private static final Color TEXT_LIGHT = new Color(153, 153, 153);
    private static final Color BUTTON_BLUE = new Color(41, 128, 185);
    private static final Color BUTTON_ORANGE = new Color(243, 156, 18);
    private static final Color BUTTON_RED = new Color(192, 57, 43);
    private static final String FONT = "Times New Roman";
    
    public TechEventFinderGUI() {
        eventManager = new EventManager();
        registration = new Registration(eventManager);
        fileManager = new FileManager();
        
        eventManager.loadEventsFromFile();
        registration.loadRegistrationsFromFile();
        
        setTitle("Tech Event Finder");
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        setBackground(LIGHT_BLUE);
        setResizable(false);
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - WIDTH) / 2, (screen.height - HEIGHT) / 2);
        
        initComponents();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { saveAndExit(); }
        });
        
        setVisible(true);
    }
    
    private void initComponents() {
        createHeader();
        createSearchPanel();
        createStatsPanel();
        createResultPanel();
        createFooter();
        displayWelcome();
    }
    
    private void createHeader() {
        headerPanel = new Panel();
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 25, WIDTH, 130);
        headerPanel.setBackground(PRIMARY_BLUE);
        
        titleLabel = createLabel("TECH EVENT FINDER", 0, 25, WIDTH, 40, FONT, Font.BOLD, 30, WHITE);
        subtitleLabel = createLabel("Find Workshops • Hackathons • Internships • Tech Events", 0, 55, WIDTH, 25, FONT, Font.PLAIN, 14, new Color(200, 220, 240));
        
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        add(headerPanel);
    }
    
    private void createSearchPanel() {
        searchPanel = new Panel();
        searchPanel.setLayout(null);
        searchPanel.setBounds(20, 125, WIDTH - 40, 150);
        searchPanel.setBackground(WHITE);
        
        addLabel(searchPanel, "Search Events", 25, 15, 200, 25, FONT, Font.BOLD, 18, PRIMARY_BLUE);
        
        // Fields
        cityField = createTextField(searchPanel, "Enter city...", 25, 72, 180, 32);
        companyField = createTextField(searchPanel, "Enter company...", 230, 72, 180, 32);
        dateField = createTextField(searchPanel, "MM/DD/YYYY", 640, 72, 160, 32);
        
        addLabel(searchPanel, "City", 25, 50, 80, 20, FONT, Font.PLAIN, 13, TEXT_DARK);
        addLabel(searchPanel, "Company", 230, 50, 80, 20, FONT, Font.PLAIN, 13, TEXT_DARK);
        addLabel(searchPanel, "Category", 435, 50, 80, 20, FONT, Font.PLAIN, 13, TEXT_DARK);
        addLabel(searchPanel, "Date (MM/DD/YYYY)", 640, 50, 130, 20, FONT, Font.PLAIN, 13, TEXT_DARK);
        
        // Category dropdown
        categoryChoice = new Choice();
        categoryChoice.setBounds(435, 72, 180, 32);
        categoryChoice.setFont(new Font(FONT, Font.PLAIN, 13));
        categoryChoice.setBackground(CARD_BG);
        String[] categories = {"All Categories", "Workshop", "Internship", "Hackathon", "Webinar", 
                               "Seminar", "Coding Contest", "Certification", "Placement Drive", "Tech Event"};
        for (String cat : categories) categoryChoice.add(cat);
        searchPanel.add(categoryChoice);
        
        // Buttons
        searchButton = createButton(searchPanel, "🔍 Search", 830, 70, 130, 35, BUTTON_BLUE);
        resetButton = createButton(searchPanel, "↺ Reset", 970, 70, 100, 35, BUTTON_ORANGE);
        
        add(searchPanel);
    }
    
    private void createStatsPanel() {
        statsPanel = new Panel();
        statsPanel.setLayout(null);
        statsPanel.setBounds(20, 290, WIDTH - 40, 85);
        statsPanel.setBackground(LIGHT_BLUE);
        
        List<Event> events = eventManager.getEvents();
        int total = events.size();
        long companies = events.stream().map(Event::getCompanyName).distinct().count();
        long cities = events.stream().map(Event::getCity).distinct().count();
        long categories = events.stream().map(Event::getEventType).distinct().count();
        
        int cardWidth = 240;
        int spacing = (WIDTH - 40 - (cardWidth * 4)) / 5;
        
        statsPanel.add(createStatCard(spacing, 0, cardWidth, 85, String.valueOf(total), "Total Events", new Color(41, 128, 185)));
        statsPanel.add(createStatCard(spacing * 2 + cardWidth, 0, cardWidth, 85, String.valueOf(companies), "Companies", new Color(46, 204, 113)));
        statsPanel.add(createStatCard(spacing * 3 + cardWidth * 2, 0, cardWidth, 85, String.valueOf(cities), "Cities", new Color(243, 156, 18)));
        statsPanel.add(createStatCard(spacing * 4 + cardWidth * 3, 0, cardWidth, 85, String.valueOf(categories), "Categories", new Color(155, 89, 182)));
        
        add(statsPanel);
    }
    
    private Panel createStatCard(int x, int y, int w, int h, String value, String title, Color color) {
        Panel card = new Panel();
        card.setLayout(null);
        card.setBounds(x, y, w, h);
        card.setBackground(WHITE);
        
        Label val = createLabel(value, 0, 15, w, 32, FONT, Font.BOLD, 28, color);
        Label ttl = createLabel(title, 0, 50, w, 20, FONT, Font.PLAIN, 13, TEXT_DARK);
        
        card.add(val);
        card.add(ttl);
        return card;
    }
    
    private void createResultPanel() {
        resultPanel = new Panel();
        resultPanel.setLayout(null);
        resultPanel.setBounds(20, 390, WIDTH - 40, 290);
        resultPanel.setBackground(WHITE);
        
        addLabel(resultPanel, "Event Results", 20, 10, 200, 25, FONT, Font.BOLD, 18, PRIMARY_BLUE);
        
        resultArea = new TextArea();
        resultArea.setBounds(15, 45, WIDTH - 70, 230);
        resultArea.setFont(new Font(FONT, Font.PLAIN, 13));
        resultArea.setBackground(CARD_BG);
        resultArea.setForeground(TEXT_DARK);
        resultArea.setEditable(false);
        resultPanel.add(resultArea);
        
        add(resultPanel);
    }
    
    private void createFooter() {
        footerPanel = new Panel();
        footerPanel.setLayout(null);
        footerPanel.setBounds(0, HEIGHT - 35, WIDTH, 35);
        footerPanel.setBackground(PRIMARY_BLUE);
        
        footerLabel = createLabel("© 2026 Tech Event Finder | Developed for Educational Purposes", 0, 5, WIDTH, 25, FONT, Font.PLAIN, 12, new Color(200, 220, 240));
        footerPanel.add(footerLabel);
        add(footerPanel);
    }
    
    private Label createLabel(String text, int x, int y, int w, int h, String font, int style, int size, Color color) {
        Label label = new Label(text, Label.CENTER);
        label.setBounds(x, y, w, h);
        label.setFont(new Font(font, style, size));
        label.setForeground(color);
        return label;
    }
    
    private void addLabel(Panel panel, String text, int x, int y, int w, int h, String font, int style, int size, Color color) {
        panel.add(createLabel(text, x, y, w, h, font, style, size, color));
    }
    
    private TextField createTextField(Panel panel, String placeholder, int x, int y, int w, int h) {
        TextField field = new TextField();
        field.setBounds(x, y, w, h);
        field.setFont(new Font(FONT, Font.PLAIN, 13));
        field.setBackground(CARD_BG);
        field.setForeground(TEXT_LIGHT);
        field.setText(placeholder);
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_LIGHT);
                }
            }
        });
        
        panel.add(field);
        return field;
    }
    
    private Button createButton(Panel panel, String text, int x, int y, int w, int h, Color color) {
        Button btn = new Button(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font(FONT, Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.addActionListener(this);
        panel.add(btn);
        return btn;
    }
    
    private void displayWelcome() {
        resultArea.setText("");
        resultArea.append("═══════════════════════════════════════════════════════════════════════════\n");
        resultArea.append("                     WELCOME TO TECH EVENT FINDER\n");
        resultArea.append("═══════════════════════════════════════════════════════════════════════════\n\n");
        resultArea.append("  📌 Find tech events based on:\n");
        resultArea.append("     • City\n     • Company\n     • Category\n     • Date\n\n");
        resultArea.append("  📊 " + eventManager.getEvents().size() + " events available\n\n");
        resultArea.append("  💡 Enter search criteria above and click 'Search'\n");
        resultArea.append("  🔄 Click 'Reset' to clear all fields\n");
        resultArea.append("\n═══════════════════════════════════════════════════════════════════════════\n");
        resultArea.append("  Ready to find your next tech event!\n");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) performSearch();
        else if (e.getSource() == resetButton) performReset();
        else if (e.getSource() == exitButton) performExit();
    }
    
    private void performSearch() {
        String city = cityField.getText().trim();
        String company = companyField.getText().trim();
        String category = categoryChoice.getSelectedItem();
        String date = dateField.getText().trim();
        
        boolean hasCity = !city.isEmpty() && !city.equals("Enter city...");
        boolean hasCompany = !company.isEmpty() && !company.equals("Enter company...");
        boolean hasDate = !date.isEmpty() && !date.equals("MM/DD/YYYY");
        boolean hasCategory = !category.equals("All Categories");
        
        if (!hasCity && !hasCompany && !hasDate && !hasCategory) {
            resultArea.setText("Please enter at least one search criteria.\nTip: Enter a city, company, date, or select a category.");
            return;
        }
        
        StringBuilder results = new StringBuilder();
        int count = 0;
        String criteria = buildCriteria(city, company, category, date);
        
        results.append("═══════════════════════════════════════════════════════════════════════════\n");
        results.append("  SEARCH RESULTS " + criteria + "\n");
        results.append("═══════════════════════════════════════════════════════════════════════════\n\n");
        
        for (Event event : eventManager.getEvents()) {
            boolean match = true;
            if (hasCity && !event.getCity().toLowerCase().contains(city.toLowerCase())) match = false;
            if (match && hasCompany && !event.getCompanyName().toLowerCase().contains(company.toLowerCase())) match = false;
            if (match && hasCategory && !event.getEventType().equalsIgnoreCase(category)) match = false;
            if (match && hasDate && !event.getDate().equals(date)) match = false;
            
            if (match) {
                results.append(formatEvent(event));
                results.append("\n");
                count++;
            }
        }
        
        if (count == 0) {
            results.append("  ❌ No Events Found matching your criteria.\n  💡 Try adjusting your search parameters.\n");
        } else {
            results.append("═══════════════════════════════════════════════════════════════════════════\n");
            results.append("  📊 Total Events Found: " + count + "\n");
        }
        
        resultArea.setText(results.toString());
    }
    
    private String buildCriteria(String city, String company, String category, String date) {
        StringBuilder sb = new StringBuilder("(");
        boolean first = false;
        if (!city.isEmpty() && !city.equals("Enter city...")) { sb.append("City: " + city); first = true; }
        if (!company.isEmpty() && !company.equals("Enter company...")) { if (first) sb.append(", "); sb.append("Company: " + company); first = true; }
        if (!category.equals("All Categories")) { if (first) sb.append(", "); sb.append("Category: " + category); first = true; }
        if (!date.isEmpty() && !date.equals("MM/DD/YYYY")) { if (first) sb.append(", "); sb.append("Date: " + date); }
        if (!first) sb.append("All Events");
        sb.append(")");
        return sb.toString();
    }
    
    private String formatEvent(Event e) {
        return String.format(
            "  ┌─────────────────────────────────────────────────────────────────┐\n" +
            "  │  %-60s │\n" +
            "  │  %-60s │\n" +
            "  │  ────────────────────────────────────────────────────────────── │\n" +
            "  │  City      : %-40s │\n" +
            "  │  Venue     : %-40s │\n" +
            "  │  Date      : %-40s │\n" +
            "  │  Time      : %-40s │\n" +
            "  │  Duration  : %-40s │\n" +
            "  │  Seats     : %-40d │\n" +
            "  │  Fee       : ₹%-39.2f │\n" +
            "  │  Certificate: %-39s │\n" +
            "  │  Rating    : %-40.1f │\n" +
            "  └─────────────────────────────────────────────────────────────────┘",
            e.getCompanyName(), e.getEventTitle(), e.getCity(), 
            truncate(e.getVenue(), 40), e.getDate(), e.getTime(), 
            e.getDuration(), e.getAvailableSeats(), e.getRegistrationFee(), 
            e.getCertificate(), e.getRating()
        );
    }
    
    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
    
    private void performReset() {
        cityField.setText("Enter city...");
        cityField.setForeground(TEXT_LIGHT);
        companyField.setText("Enter company...");
        companyField.setForeground(TEXT_LIGHT);
        categoryChoice.select("All Categories");
        dateField.setText("MM/DD/YYYY");
        dateField.setForeground(TEXT_LIGHT);
        displayWelcome();
    }
    
    private void performExit() {
        Dialog confirm = new Dialog(this, "Confirm Exit", true);
        confirm.setLayout(new FlowLayout());
        confirm.setSize(350, 130);
        confirm.setBackground(WHITE);
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        confirm.setLocation((screen.width - 350) / 2, (screen.height - 130) / 2);
        
        Label msg = new Label("Are you sure you want to exit?");
        msg.setFont(new Font(FONT, Font.PLAIN, 14));
        msg.setForeground(TEXT_DARK);
        confirm.add(msg);
        
        Panel btnPanel = new Panel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        Button yes = new Button("Yes");
        yes.setFont(new Font(FONT, Font.BOLD, 13));
        yes.setBackground(BUTTON_RED);
        yes.setForeground(WHITE);
        yes.addActionListener(e -> { saveAndExit(); confirm.dispose(); });
        
        Button no = new Button("No");
        no.setFont(new Font(FONT, Font.BOLD, 13));
        no.setBackground(BUTTON_BLUE);
        no.setForeground(WHITE);
        no.addActionListener(e -> confirm.dispose());
        
        btnPanel.add(no);
        btnPanel.add(yes);
        confirm.add(btnPanel);
        confirm.setVisible(true);
    }
    
    private void saveAndExit() {
        fileManager.saveEventsToFile(eventManager.getEvents());
        fileManager.saveRegistrationsToFile(registration.getRegistrations());
        fileManager.saveUsersToFile(registration.getUsers());
        dispose();
        System.exit(0);
    }
    
    public static void main(String[] args) {
        new TechEventFinderGUI();
    }
}

package com.hms.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PatientPortalView extends Stage {

    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String BG_LIGHT = "#F3F4F6";
    private static final String TEXT_MAIN = "#111827";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String CARD_STYLE = "-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String INPUT_STYLE = "-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12;";

    private final String patientEmail;
    private StackPane contentArea;
    private VBox activeBtn;

    public PatientPortalView(String patientEmail) {
        this.patientEmail = patientEmail;
        setTitle("Patient Workspace");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        // Initialize Views
        VBox profileView = buildProfileView();
        VBox bookingView = buildBookingView();
        VBox historyView = buildHistoryView();
        VBox rxView = buildPrescriptionView();
        VBox billingView = buildBillingView();

        contentArea.getChildren().addAll(billingView, rxView, historyView, bookingView, profileView);

        root.setLeft(buildSidebar(profileView, bookingView, historyView, rxView, billingView));
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1100, 720);
        setScene(scene);
        setMinWidth(1000);
        setMinHeight(650);
    }

    private VBox buildSidebar(VBox... views) {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(30, 15, 30, 15));

        // Brand
        Label brand = new Label("MediCare");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        brand.setTextFill(Color.web(PRIMARY_BLUE));
        VBox.setMargin(brand, new Insets(0, 0, 30, 10));

        // Navigation Buttons
        VBox btnProfile = createNavButton("My Details", views[0]);
        VBox btnBook = createNavButton("Book Appointment", views[1]);
        VBox btnHistory = createNavButton("History", views[2]);
        VBox btnRx = createNavButton("Prescriptions", views[3]);
        VBox btnBills = createNavButton("Invoices", views[4]);

        activeBtn = btnProfile; // Set default active
        setActiveStyle(activeBtn, true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Sign Out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> close());

        sidebar.getChildren().addAll(brand, btnProfile, btnBook, btnHistory, btnRx, btnBills, spacer, btnLogout);
        return sidebar;
    }

    private VBox createNavButton(String text, VBox targetView) {
        VBox btn = new VBox();
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.web(TEXT_MUTED));

        btn.getChildren().add(lbl);
        btn.setPadding(new Insets(12, 15, 12, 15));
        btn.setStyle("-fx-background-radius: 6; -fx-cursor: hand;");

        btn.setOnMouseClicked(e -> {
            setActiveStyle(activeBtn, false);
            activeBtn = btn;
            setActiveStyle(activeBtn, true);
            targetView.toFront();
        });

        return btn;
    }

    private void setActiveStyle(VBox btn, boolean isActive) {
        Label lbl = (Label) btn.getChildren().get(0);
        if (isActive) {
            btn.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(PRIMARY_BLUE));
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(TEXT_MUTED));
        }
    }

    private VBox buildProfileView() {
        VBox container = wrapInCard("Patient Profile", "Your personal and medical details.");

        GridPane grid = new GridPane();
        grid.setVgap(20); grid.setHgap(50);

        addProfileRow(grid, 0, "Full Name", "Ankur Mishra");
        addProfileRow(grid, 1, "Email Address", patientEmail);
        addProfileRow(grid, 2, "Phone Number", "+91 9876543210");
        addProfileRow(grid, 3, "Age & Gender", "21 yrs / Male");
        addProfileRow(grid, 4, "Blood Group", "O+");

        container.getChildren().add(grid);
        return container;
    }

    private void addProfileRow(GridPane grid, int row, String label, String value) {
        VBox box = new VBox(5);
        Label l = new Label(label.toUpperCase());
        l.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_MUTED + ";");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 15px; -fx-text-fill: " + TEXT_MAIN + "; -fx-font-weight: bold;");
        box.getChildren().addAll(l, v);
        grid.add(box, 0, row);
    }

    private VBox buildBookingView() {
        VBox container = wrapInCard("Schedule Consultation", "Book a new appointment with our specialists.");

        GridPane grid = new GridPane();
        grid.setVgap(15); grid.setHgap(20);

        ComboBox<String> cmbDept = new ComboBox<>();
        cmbDept.getItems().addAll("Cardiology", "Neurology", "General Medicine");
        cmbDept.setStyle(INPUT_STYLE); cmbDept.setPrefWidth(300);

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle(INPUT_STYLE); datePicker.setPrefWidth(300);

        TextArea txtSymptoms = new TextArea();
        txtSymptoms.setPrefRowCount(3);
        txtSymptoms.setStyle(INPUT_STYLE); txtSymptoms.setPrefWidth(300);

        grid.add(createLabeledInput("DEPARTMENT", cmbDept), 0, 0);
        grid.add(createLabeledInput("PREFERRED DATE", datePicker), 0, 1);
        grid.add(createLabeledInput("SYMPTOMS", txtSymptoms), 0, 2);

        Button btnBook = new Button("Confirm Booking");
        btnBook.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand;");
        VBox.setMargin(btnBook, new Insets(15, 0, 0, 0));

        container.getChildren().addAll(grid, btnBook);
        return container;
    }

    private VBox createLabeledInput(String labelText, Control input) {
        VBox box = new VBox(6);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_MUTED + ";");
        box.getChildren().addAll(label, input);
        return box;
    }

    private VBox buildHistoryView() {
        VBox container = wrapInCard("Appointment History", "Track your past and upcoming visits.");
        TableView<String[]> table = createTable(new String[]{"ID", "Doctor", "Dept", "Date", "Status"},
                new String[][]{{"APT-1001", "Dr. Sharma", "Cardiology", "2026-08-15", "Completed"}});
        container.getChildren().add(table);
        return container;
    }

    private VBox buildPrescriptionView() {
        VBox container = wrapInCard("Medical Prescriptions", "Review medications prescribed by your doctors.");
        TableView<String[]> table = createTable(new String[]{"Date", "Doctor", "Diagnosis", "Medicines", "Instructions"},
                new String[][]{{"2026-08-15", "Dr. Sharma", "Mild Hypertension", "Amlodipine 5mg", "1 tablet daily"}});
        container.getChildren().add(table);
        return container;
    }

    private VBox buildBillingView() {
        VBox container = wrapInCard("Invoices & Payments", "Manage your hospital bills.");
        TableView<String[]> table = createTable(new String[]{"Bill ID", "Date", "Service", "Amount (₹)", "Status"},
                new String[][]{{"INV-801", "2026-08-15", "Cardiology Consultation", "1500.00", "Paid"}});
        container.getChildren().add(table);
        return container;
    }

    private VBox wrapInCard(String title, String subtitle) {
        VBox card = new VBox(20);
        card.setStyle(CARD_STYLE);
        card.setPadding(new Insets(30));

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblTitle.setTextFill(Color.web(TEXT_MAIN));

        Label lblSub = new Label(subtitle);
        lblSub.setFont(Font.font("Segoe UI", 13));
        lblSub.setTextFill(Color.web(TEXT_MUTED));

        VBox header = new VBox(5, lblTitle, lblSub);
        card.getChildren().add(header);
        return card;
    }

    private TableView<String[]> createTable(String[] columns, String[][] data) {
        TableView<String[]> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-border-color: #E5E7EB; -fx-background-color: white;");

        for (int i = 0; i < columns.length; i++) {
            TableColumn<String[], String> col = new TableColumn<>(columns[i]);
            final int colIndex = i;
            col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[colIndex]));
            table.getColumns().add(col);
        }

        for (String[] row : data) table.getItems().add(row);
        VBox.setVgrow(table, Priority.ALWAYS);
        return table;
    }
}
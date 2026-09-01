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

public class AdminPortalView extends Stage {

    private static final String PRIMARY_INDIGO = "#4F46E5";
    private static final String BG_LIGHT = "#F3F4F6";
    private static final String TEXT_MAIN = "#111827";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String CARD_STYLE = "-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String INPUT_STYLE = "-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12;";

    private StackPane contentArea;
    private VBox activeBtn;

    public AdminPortalView() {
        setTitle("System Administration Hub");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        // Initialize Views
        VBox overviewView = buildOverviewView();
        VBox doctorsView = buildDoctorsView();
        VBox patientsView = buildPatientsView();

        // FIX: Only add the default view to the StackPane initially
        contentArea.getChildren().add(overviewView);

        root.setLeft(buildSidebar(overviewView, doctorsView, patientsView));
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1150, 750);
        setScene(scene);
        setMinWidth(1000);
        setMinHeight(650);
    }

    private VBox buildSidebar(VBox... views) {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(30, 15, 30, 15));

        Label brand = new Label("System Admin");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        brand.setTextFill(Color.web(PRIMARY_INDIGO));

        Label deptLabel = new Label("Hospital Control Center");
        deptLabel.setFont(Font.font("Segoe UI", 12));
        deptLabel.setTextFill(Color.web(TEXT_MUTED));

        VBox brandBox = new VBox(2, brand, deptLabel);
        VBox.setMargin(brandBox, new Insets(0, 0, 30, 10));

        VBox btnOverview = createNavButton("Dashboard Overview", views[0]);
        VBox btnDoctors = createNavButton("Manage Doctors", views[1]);
        VBox btnPatients = createNavButton("Patient Registry", views[2]);

        activeBtn = btnOverview;
        setActiveStyle(activeBtn, true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Sign Out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> close());

        sidebar.getChildren().addAll(brandBox, btnOverview, btnDoctors, btnPatients, spacer, btnLogout);
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

            // FIX: Swap the views entirely instead of just bringing to front
            contentArea.getChildren().setAll(targetView);
        });

        return btn;
    }

    private void setActiveStyle(VBox btn, boolean isActive) {
        Label lbl = (Label) btn.getChildren().get(0);
        if (isActive) {
            btn.setStyle("-fx-background-color: #EEF2FF; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(PRIMARY_INDIGO));
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(TEXT_MUTED));
        }
    }

    // FIX: Wrapped in a card for solid white background to match other tabs
    private VBox buildOverviewView() {
        VBox container = wrapInCard("Hospital Metrics", "Live overview of system activity.");

        HBox statBox = new HBox(20);
        statBox.getChildren().addAll(
                createStatCard("Total Patients", "1,245", "#EA580C"),
                createStatCard("Active Doctors", "48", PRIMARY_INDIGO),
                createStatCard("Today's Appts", "112", "#0D9488"),
                createStatCard("Pending Invoices", "15", "#EAB308")
        );

        container.getChildren().add(statBox);
        return container;
    }

    private VBox createStatCard(String title, String value, String colorHex) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;");
        card.setPadding(new Insets(25));
        card.setMinWidth(200);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label lblTitle = new Label(title.toUpperCase());
        lblTitle.setTextFill(Color.web(TEXT_MUTED));
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        Label lblValue = new Label(value);
        lblValue.setTextFill(Color.web(colorHex));
        lblValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private VBox buildDoctorsView() {
        VBox container = wrapInCard("Doctor Directory", "Manage hospital staff and their respective departments.");

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAddDoctor = new Button("+ Add New Doctor");
        btnAddDoctor.setStyle("-fx-background-color: " + PRIMARY_INDIGO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search by name or department...");
        txtSearch.setStyle(INPUT_STYLE);
        txtSearch.setPrefWidth(300);

        topBar.getChildren().addAll(btnAddDoctor, txtSearch);

        TableView<String[]> table = createTable(new String[]{"ID", "Name", "Department", "Email", "Phone", "Consultation Fee"},
                new String[][]{
                        {"DOC-001", "Dr. Sharma", "Cardiology", "sharma@medicare.com", "9876543211", "1500.00"},
                        {"DOC-002", "Dr. Patel", "General Medicine", "patel@medicare.com", "9876543212", "800.00"}
                });

        container.getChildren().addAll(topBar, table);
        return container;
    }

    private VBox buildPatientsView() {
        VBox container = wrapInCard("Patient Registry", "Comprehensive list of all registered patients.");

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search patients by ID, Name, or Email...");
        txtSearch.setStyle(INPUT_STYLE);
        txtSearch.setMaxWidth(400);

        TableView<String[]> table = createTable(new String[]{"Patient ID", "Name", "Email", "Age", "Gender", "Registration Date"},
                new String[][]{
                        {"PAT-001", "Ankur Mishra", "ankur@example.com", "21", "Male", "2026-08-10"},
                        {"PAT-002", "Priya Singh", "priya@example.com", "34", "Female", "2026-08-11"}
                });

        container.getChildren().addAll(txtSearch, table);
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
        VBox.setVgrow(card, Priority.ALWAYS);
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
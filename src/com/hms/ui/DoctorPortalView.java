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

public class DoctorPortalView extends Stage {

    private static final String PRIMARY_TEAL = "#0D9488"; // Clinical Teal
    private static final String BG_LIGHT = "#F3F4F6";
    private static final String TEXT_MAIN = "#111827";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String CARD_STYLE = "-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String INPUT_STYLE = "-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12;";

    private final String doctorEmail;
    private StackPane contentArea;
    private VBox activeBtn;

    public DoctorPortalView(String doctorEmail) {
        this.doctorEmail = doctorEmail;
        setTitle("Doctor Workspace");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        // Initialize Views
        VBox scheduleView = buildScheduleView();
        VBox prescriptionView = buildPrescriptionView();
        VBox recordsView = buildPatientRecordsView();

        // Add to StackPane (bottom to top)
        contentArea.getChildren().addAll(recordsView, prescriptionView, scheduleView);

        root.setLeft(buildSidebar(scheduleView, prescriptionView, recordsView));
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

        // Brand & User Info
        Label brand = new Label("MediCare Clinical");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        brand.setTextFill(Color.web(PRIMARY_TEAL));

        Label docLabel = new Label("Attending Physician");
        docLabel.setFont(Font.font("Segoe UI", 12));
        docLabel.setTextFill(Color.web(TEXT_MUTED));

        VBox brandBox = new VBox(2, brand, docLabel);
        VBox.setMargin(brandBox, new Insets(0, 0, 30, 10));

        // Navigation Buttons
        VBox btnSchedule = createNavButton("Today's Schedule", views[0]);
        VBox btnRx = createNavButton("Issue Prescription", views[1]);
        VBox btnRecords = createNavButton("Patient Records", views[2]);

        activeBtn = btnSchedule; // Set default active
        setActiveStyle(activeBtn, true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Sign Out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> close());

        sidebar.getChildren().addAll(brandBox, btnSchedule, btnRx, btnRecords, spacer, btnLogout);
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
            btn.setStyle("-fx-background-color: #F0FDFA; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(PRIMARY_TEAL));
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(TEXT_MUTED));
        }
    }

    private VBox buildScheduleView() {
        VBox container = wrapInCard("Daily Schedule", "Manage your appointments for today.");

        TableView<String[]> table = createTable(new String[]{"Appt ID", "Time", "Patient Name", "Symptoms", "Status"},
                new String[][]{
                        {"APT-1001", "09:30 AM", "Ankur Mishra", "Fever, chills", "Scheduled"},
                        {"APT-1002", "10:00 AM", "Rahul Verma", "Routine checkup", "Completed"}
                });

        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Button btnComplete = new Button("Mark Appointment as Completed");
        btnComplete.setStyle("-fx-background-color: " + PRIMARY_TEAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

        btnComplete.setOnAction(e -> {
            if (table.getSelectionModel().getSelectedItem() == null) {
                showAlert(Alert.AlertType.WARNING, "Please select an appointment from the list.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Appointment marked as completed.");
        });

        actionBox.getChildren().add(btnComplete);
        container.getChildren().addAll(table, actionBox);
        return container;
    }

    private VBox buildPrescriptionView() {
        VBox container = wrapInCard("Issue Prescription", "Draft clinical notes and prescribe medications.");

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(20);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        TextField txtApptId = new TextField();
        txtApptId.setPromptText("e.g., APT-1001");
        txtApptId.setStyle(INPUT_STYLE);

        TextField txtDiagnosis = new TextField();
        txtDiagnosis.setStyle(INPUT_STYLE);

        TextArea txtMedicines = new TextArea();
        txtMedicines.setPrefRowCount(4);
        txtMedicines.setStyle(INPUT_STYLE);

        TextArea txtInstructions = new TextArea();
        txtInstructions.setPrefRowCount(4);
        txtInstructions.setStyle(INPUT_STYLE);

        grid.add(createLabeledInput("APPOINTMENT ID", txtApptId), 0, 0);
        grid.add(createLabeledInput("PRIMARY DIAGNOSIS", txtDiagnosis), 1, 0);
        grid.add(createLabeledInput("MEDICATIONS (Format: Name - Dosage)", txtMedicines), 0, 1, 2, 1);
        grid.add(createLabeledInput("PATIENT INSTRUCTIONS", txtInstructions), 0, 2, 2, 1);

        HBox actionBox = new HBox();
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Button btnSave = new Button("Save & Issue Prescription");
        btnSave.setStyle("-fx-background-color: " + PRIMARY_TEAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand;");

        btnSave.setOnAction(e -> {
            if (txtApptId.getText().trim().isEmpty() || txtDiagnosis.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Appointment ID and Diagnosis are required.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Prescription saved and attached to patient records successfully!");
            txtApptId.clear(); txtDiagnosis.clear(); txtMedicines.clear(); txtInstructions.clear();
        });

        actionBox.getChildren().add(btnSave);
        VBox.setMargin(actionBox, new Insets(15, 0, 0, 0));

        container.getChildren().addAll(grid, actionBox);
        return container;
    }

    private VBox buildPatientRecordsView() {
        VBox container = wrapInCard("Patient Database", "Search and review histories for all treated patients.");

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search by Patient ID or Name...");
        txtSearch.setStyle(INPUT_STYLE);
        txtSearch.setMaxWidth(400);
        VBox.setMargin(txtSearch, new Insets(0, 0, 10, 0));

        TableView<String[]> table = createTable(new String[]{"Patient ID", "Name", "Age", "Gender", "Blood Group", "Last Visit"},
                new String[][]{
                        {"PAT-001", "Ankur Mishra", "21", "Male", "O+", "2026-08-15"},
                        {"PAT-045", "Priya Singh", "34", "Female", "B+", "2026-08-28"}
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
        return card;
    }

    private VBox createLabeledInput(String labelText, Control input) {
        VBox box = new VBox(6);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_MUTED + ";");
        box.getChildren().addAll(label, input);
        return box;
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

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
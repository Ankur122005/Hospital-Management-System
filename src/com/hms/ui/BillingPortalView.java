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

public class BillingPortalView extends Stage {

    private static final String PRIMARY_EMERALD = "#10B981"; // Emerald Green
    private static final String BG_LIGHT = "#F3F4F6";
    private static final String TEXT_MAIN = "#111827";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String CARD_STYLE = "-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String INPUT_STYLE = "-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12;";

    private StackPane contentArea;
    private VBox activeBtn;

    public BillingPortalView() {
        setTitle("Finance & Billing Desk");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        // Initialize Views
        VBox pendingView = buildPendingView();
        VBox historyView = buildHistoryView();
        VBox generateView = buildGenerateView();

        // Add to StackPane (bottom to top)
        contentArea.getChildren().addAll(generateView, historyView, pendingView);

        root.setLeft(buildSidebar(pendingView, historyView, generateView));
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

        // Brand & Info
        Label brand = new Label("Hospital Finance");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        brand.setTextFill(Color.web(PRIMARY_EMERALD));

        Label deptLabel = new Label("Billing Department");
        deptLabel.setFont(Font.font("Segoe UI", 12));
        deptLabel.setTextFill(Color.web(TEXT_MUTED));

        VBox brandBox = new VBox(2, brand, deptLabel);
        VBox.setMargin(brandBox, new Insets(0, 0, 30, 10));

        // Navigation Buttons
        VBox btnPending = createNavButton("Pending Invoices", views[0]);
        VBox btnHistory = createNavButton("Payment History", views[1]);
        VBox btnGenerate = createNavButton("Create Invoice", views[2]);

        activeBtn = btnPending; // Set default active
        setActiveStyle(activeBtn, true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Sign Out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> close());

        sidebar.getChildren().addAll(brandBox, btnPending, btnHistory, btnGenerate, spacer, btnLogout);
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
            btn.setStyle("-fx-background-color: #ECFDF5; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(PRIMARY_EMERALD));
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;");
            lbl.setTextFill(Color.web(TEXT_MUTED));
        }
    }

    private VBox buildPendingView() {
        VBox container = wrapInCard("Awaiting Payment", "Process pending patient invoices and accept payments.");

        TableView<String[]> table = createTable(new String[]{"Bill ID", "Date", "Patient Name", "Particulars", "Amount (₹)"},
                new String[][]{
                        {"INV-902", "2026-08-30", "Rahul Verma", "OPD Advance Booking", "500.00"},
                        {"INV-905", "2026-08-30", "Priya Singh", "X-Ray & Blood Test", "2300.00"}
                });

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        ComboBox<String> cmbPaymentMethod = new ComboBox<>();
        cmbPaymentMethod.getItems().addAll("Cash", "UPI", "Credit/Debit Card", "Insurance");
        cmbPaymentMethod.setPromptText("Select Method");
        cmbPaymentMethod.setStyle(INPUT_STYLE);

        Button btnProcess = new Button("Process Payment");
        btnProcess.setStyle("-fx-background-color: " + PRIMARY_EMERALD + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand;");

        btnProcess.setOnAction(e -> {
            if (table.getSelectionModel().getSelectedItem() == null) {
                showAlert(Alert.AlertType.WARNING, "Please select an invoice from the table first.");
                return;
            }
            if (cmbPaymentMethod.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a payment method.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Payment processed successfully! Invoice moved to History.");
            cmbPaymentMethod.getSelectionModel().clearSelection();
        });

        actionBox.getChildren().addAll(cmbPaymentMethod, btnProcess);
        container.getChildren().addAll(table, actionBox);
        return container;
    }

    private VBox buildHistoryView() {
        VBox container = wrapInCard("Completed Transactions", "Record of all settled hospital bills and pharmacy invoices.");

        TableView<String[]> table = createTable(new String[]{"Bill ID", "Date", "Patient Name", "Particulars", "Amount (₹)", "Method"},
                new String[][]{
                        {"INV-801", "2026-08-15", "Ankur Mishra", "Cardiology Consultation", "1500.00", "UPI"},
                        {"INV-812", "2026-08-20", "Anil Kumar", "Pharmacy Prescription", "450.00", "Cash"}
                });

        container.getChildren().add(table);
        return container;
    }

    private VBox buildGenerateView() {
        VBox container = wrapInCard("Create New Invoice", "Bill patients for consultations, treatments, or medicines.");

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(20);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        TextField txtPatientId = new TextField();
        txtPatientId.setStyle(INPUT_STYLE);

        TextField txtApptId = new TextField();
        txtApptId.setPromptText("Optional");
        txtApptId.setStyle(INPUT_STYLE);

        TextField txtParticulars = new TextField();
        txtParticulars.setStyle(INPUT_STYLE);

        TextField txtAmount = new TextField();
        txtAmount.setPromptText("0.00");
        txtAmount.setStyle(INPUT_STYLE);

        grid.add(createLabeledInput("PATIENT ID", txtPatientId), 0, 0);
        grid.add(createLabeledInput("APPOINTMENT ID (OPTIONAL)", txtApptId), 1, 0);
        grid.add(createLabeledInput("PARTICULARS / SERVICE RENDERED", txtParticulars), 0, 1, 2, 1);
        grid.add(createLabeledInput("TOTAL AMOUNT (₹)", txtAmount), 0, 2);

        HBox actionBox = new HBox();
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Button btnGenerate = new Button("Generate Invoice");
        btnGenerate.setStyle("-fx-background-color: " + PRIMARY_EMERALD + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand;");

        btnGenerate.setOnAction(e -> {
            if (txtPatientId.getText().trim().isEmpty() || txtParticulars.getText().trim().isEmpty() || txtAmount.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Patient ID, Particulars, and Amount are required.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Invoice generated successfully! Moved to Pending list.");
            txtPatientId.clear(); txtApptId.clear(); txtParticulars.clear(); txtAmount.clear();
        });

        actionBox.getChildren().add(btnGenerate);
        VBox.setMargin(actionBox, new Insets(15, 0, 0, 0));

        container.getChildren().addAll(grid, actionBox);
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
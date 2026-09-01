package com.hms.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainDashboard extends Application {

    private static final String BG_MAIN = "#F3F4F6";
    private static final String CARD_BG = "#FFFFFF";
    private static final String TEXT_PRIMARY = "#111827";
    private static final String TEXT_SECONDARY = "#6B7280";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hospital Management System — Enterprise Hub");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_MAIN + ";");

        root.setTop(buildHeader());
        root.setCenter(buildContent());
        root.setBottom(buildFooter());

        Scene scene = new Scene(root, 980, 640);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(880);
        primaryStage.setMinHeight(560);
        primaryStage.show();
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");
        header.setPadding(new Insets(20, 35, 20, 35));
        header.setAlignment(Pos.CENTER_LEFT);

        VBox brandBox = new VBox(4);
        Label title = new Label("MediCare Hospital Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(TEXT_PRIMARY));

        Label subTitle = new Label("Central Operations & Department Portals");
        subTitle.setFont(Font.font("Segoe UI", 13));
        subTitle.setTextFill(Color.web(TEXT_SECONDARY));
        brandBox.getChildren().addAll(title, subTitle);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusBadge = new Label("● System Online");
        statusBadge.setStyle("-fx-text-fill: #16A34A; -fx-background-color: #DCFCE7; -fx-padding: 6 14 6 14; -fx-background-radius: 20;");
        statusBadge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        header.getChildren().addAll(brandBox, spacer, statusBadge);
        return header;
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(35));

        Label sectionLabel = new Label("Select Department Portal");
        sectionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        sectionLabel.setTextFill(Color.web(TEXT_PRIMARY));

        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);

        grid.add(createModernCard("Admin Portal", "Manage user authentication, staff directories, and system configuration.", "Access Admin Controls →", "#4F46E5"), 0, 0);
        grid.add(createModernCard("Doctor Portal", "Review appointments, clinical diagnoses, and write prescriptions.", "Enter Doctor Workspace →", "#0D9488"), 1, 0);
        grid.add(createModernCard("Patient Portal", "Register new account, book appointments, and check medical history.", "Launch Patient Desk →", "#EA580C"), 0, 1);
        grid.add(createModernCard("Finance & Billing", "Process treatment invoices, pharmacy billing, and daily revenue reports.", "Open Finance Desk →", "#10B981"), 1, 1);

        // Ensure columns resize evenly
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        content.getChildren().addAll(sectionLabel, grid);
        return content;
    }

    private VBox createModernCard(String title, String description, String actionText, String accentHex) {
        VBox card = new VBox(12);
        String defaultStyle = "-fx-background-color: " + CARD_BG + "; -fx-border-color: #E5E7EB; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #F9FAFB; -fx-border-color: #D1D5DB; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";

        card.setStyle(defaultStyle);
        card.setPadding(new Insets(24));

        Label titleLabel = new Label("● " + title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web(TEXT_PRIMARY));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", 14));
        descLabel.setTextFill(Color.web(TEXT_SECONDARY));
        descLabel.setWrapText(true);
        VBox.setVgrow(descLabel, Priority.ALWAYS);

        Label actionLabel = new Label(actionText);
        actionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        actionLabel.setTextFill(Color.web(accentHex));

        card.getChildren().addAll(titleLabel, descLabel, actionLabel);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(defaultStyle));

        // Click action routing
        card.setOnMouseClicked(e -> {
            Stage currentStage = (Stage) card.getScene().getWindow();

            switch (title) {
                case "Admin Portal":
                    new AdminPortalView().show();
                    break;

                case "Doctor Portal":
                    DoctorAuthView docAuth = new DoctorAuthView(currentStage);
                    docAuth.showAndWait();

                    if (docAuth.isAuthenticated()) {
                        new DoctorPortalView(docAuth.getAuthenticatedEmail()).show();
                    }
                    break;

                case "Patient Portal":
                    PatientAuthView authView = new PatientAuthView(currentStage);
                    authView.showAndWait(); // Blocks until the user logs in or closes the window

                    if (authView.isAuthenticated()) {
                        // Launch the actual portal only if login was successful
                        new PatientPortalView(authView.getAuthenticatedEmail()).show();
                    }
                    break;

                case "Finance & Billing":
                    BillingAuthView billAuth = new BillingAuthView(currentStage);
                    billAuth.showAndWait();

                    if (billAuth.isAuthenticated()) {
                        // The user logged in successfully, so open the actual billing portal
                        new BillingPortalView().show();
                    }
                    break;
            }
        });

        return card;
    }

    private HBox buildFooter() {
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 1 0 0 0;");
        footer.setPadding(new Insets(12, 0, 12, 0));
        footer.setAlignment(Pos.CENTER);

        Label copy = new Label("Hospital Management System v2.0 • 3-Tier Architecture (JavaFX)");
        copy.setFont(Font.font("Segoe UI", 12));
        copy.setTextFill(Color.web(TEXT_SECONDARY));

        footer.getChildren().add(copy);
        return footer;
    }
}
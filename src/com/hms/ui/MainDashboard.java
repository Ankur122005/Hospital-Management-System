package com.hms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MainDashboard extends JFrame {

    // Modern color palette
    private static final Color BG_MAIN = new Color(243, 244, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);

    private static final Color COLOR_ADMIN = new Color(79, 70, 229);    // Indigo
    private static final Color COLOR_DOCTOR = new Color(13, 148, 136);  // Teal
    private static final Color COLOR_PATIENT = new Color(234, 88, 12);  // Amber/Orange
    private static final Color COLOR_FINANCE = new Color(16, 185, 129); // Emerald

    public MainDashboard() {
        setTitle("Hospital Management System");
        setSize(980, 640);
        setMinimumSize(new Dimension(880, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout(0, 0));

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildContentPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                new EmptyBorder(22, 35, 22, 35)
        ));

        // App Branding & Title
        JPanel brandPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        brandPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("MediCare Hospital Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subTitleLabel = new JLabel("Central Operations & Department Portals");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(TEXT_SECONDARY);

        brandPanel.add(titleLabel);
        brandPanel.add(subTitleLabel);
        header.add(brandPanel, BorderLayout.WEST);

        // System Status Badge
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        badgePanel.setOpaque(false);

        JLabel statusBadge = new JLabel("● Database Connected");
        statusBadge.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        statusBadge.setForeground(new Color(22, 163, 74));
        statusBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(187, 247, 208), 1, true),
                new EmptyBorder(6, 14, 6, 14)
        ));
        badgePanel.add(statusBadge);

        header.add(badgePanel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 35, 30, 35));

        // Section Title
        JLabel sectionLabel = new JLabel("Select Department Portal");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        sectionLabel.setForeground(TEXT_PRIMARY);
        sectionLabel.setBorder(new EmptyBorder(0, 0, 18, 0));
        contentPanel.add(sectionLabel, BorderLayout.NORTH);

        // 2x2 Grid for Department Cards
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 24, 24));
        gridPanel.setOpaque(false);

        gridPanel.add(createModernCard(
                "Admin Portal",
                "Manage user authentication, staff directories, system configuration, and audit logs.",
                "Access Admin Controls →",
                COLOR_ADMIN,
                this::openAdminPortal
        ));

        gridPanel.add(createModernCard(
                "Doctor Portal",
                "Review daily appointments, record clinical diagnoses, write prescriptions, and inspect patient history.",
                "Enter Doctor Workspace →",
                COLOR_DOCTOR,
                this::openDoctorPortal
        ));

        gridPanel.add(createModernCard(
                "Patient Portal",
                "Register new admissions, schedule OPD visits, assign wards, and manage medical records.",
                "Launch Patient Desk →",
                COLOR_PATIENT,
                this::openPatientPortal
        ));

        gridPanel.add(createModernCard(
                "Finance & Billing Portal",
                "Process treatment invoices, insurance claims, pharmacy billing, and daily revenue reports.",
                "Open Finance Desk →",
                COLOR_FINANCE,
                this::openFinancePortal
        ));

        contentPanel.add(gridPanel, BorderLayout.CENTER);
        return contentPanel;
    }

    private JPanel createModernCard(String title, String description, String actionText, Color accentColor, Runnable onClickAction) {
        ModernRoundedPanel card = new ModernRoundedPanel(16, CARD_BG);
        card.setLayout(new BorderLayout(15, 14));
        card.setBorder(new EmptyBorder(24, 24, 20, 24));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Header Section with accent bullet
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topRow.setOpaque(false);

        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dot.setForeground(accentColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);

        topRow.add(dot);
        topRow.add(titleLabel);
        card.add(topRow, BorderLayout.NORTH);

        // Body Description
        JLabel descLabel = new JLabel("<html><body style='width: 250px; line-height: 1.4; color: #4B5563;'>"
                + description + "</body></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(descLabel, BorderLayout.CENTER);

        // Action CTA
        JLabel actionLabel = new JLabel(actionText);
        actionLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        actionLabel.setForeground(accentColor);
        card.add(actionLabel, BorderLayout.SOUTH);

        // Hover animations
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(249, 250, 251));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BG);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });

        return card;
    }

    private JPanel buildFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(229, 231, 235)));

        JLabel copy = new JLabel("Hospital Management System v1.0 SNAPSHOT");
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copy.setForeground(TEXT_SECONDARY);
        footer.add(copy);

        return footer;
    }

    // Navigation triggers
    private void openAdminPortal() {
        JOptionPane.showMessageDialog(this, "Navigating to Admin Portal...", "System Navigation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openDoctorPortal() {
        JOptionPane.showMessageDialog(this, "Navigating to Doctor Portal...", "System Navigation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openPatientPortal() {
        JOptionPane.showMessageDialog(this, "Navigating to Patient Portal...", "System Navigation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openFinancePortal() {
        JOptionPane.showMessageDialog(this, "Navigating to Finance Portal...", "System Navigation", JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper custom panel for clean rounded corners and subtle borders
    private static class ModernRoundedPanel extends JPanel {
        private final int radius;
        private Color bgColor;

        ModernRoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        public void setBackground(Color bg) {
            this.bgColor = bg;
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background fill
            g2.setColor(bgColor != null ? bgColor : getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

            // Outer border
            g2.setColor(new Color(229, 231, 235));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
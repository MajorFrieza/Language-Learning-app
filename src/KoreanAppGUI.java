/**
 * Main GUI Dashboard for the Korean Learning App.
 * Displays user statistics (points, badges, streak) and provides access to learning modules.
 * Features a modern design with gradient header, progress tracking, and gamification elements.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Main interface for users to navigate the application and track learning progress.
 * Tester: [Hafizh Armansyah]
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KoreanAppGUI extends JFrame {

    private int points;
    private int badges;
    private int streak;
    private int lessonsCompleted;

    // Badge tracking: store unlocked badge IDs; keep numeric `badges` for UI compatibility
    private Set<String> unlockedBadges = new HashSet<>();

    // badge titles
    private static final Map<String, String> BADGE_TITLES = Map.of(
        "lesson_milestone", "Lesson Milestone",
        "streak_5", "5-Milestone Streak",
        "streak_10", "10-Milestone Streak",
        "quiz_expert", "Quiz Expert",
        "all_lessons", "Course Complete"
    );
    private static final Map<String, String> BADGE_DESCRIPTIONS = Map.of(
        "lesson_milestone", "Complete 10 lessons to earn this milestone.",
        "streak_5", "Achieve 5 consecutive lesson milestones (each milestone = 10 lessons).",
        "streak_10", "Achieve 10 consecutive lesson milestones (each milestone = 10 lessons).",
        "quiz_expert", "Score 80% or higher on a quiz.",
        "all_lessons", "Finish all lessons in the course."
    );

    // Reference to currently open badge gallery (if any) so we can refresh it live
    private BadgeGallery badgeGallery = null;

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel lblPoints = new JLabel();
    private final JLabel lblBadges = new JLabel();
    private final JLabel lblStreak = new JLabel();
    private final JLabel lblProgressText = new JLabel();
    private final JLabel lblPercentage = new JLabel();

    private final KoreanLearningModule learningModule;
    private static final String PROGRESS_FILE = "user-progress.properties";

    public KoreanAppGUI() {
        super("한국어 배우기 — Learn Korean");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(380, 700));
        setLocationRelativeTo(null);

        learningModule = new KoreanLearningModule();

        // Load saved progress if available
        points = 0;
        badges = 0;
        streak = 0;
        lessonsCompleted = 0;
        loadProgress();
        
        initUI();
        
        // Save progress when window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProgress();
            }
        });
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Top gradient header
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth();
                int h = getHeight();
                Color c1 = new Color(176, 58, 224);
                Color c2 = new Color(243, 77, 173);
                GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h + 40, 30, 30);
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(400, 240));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(16, 18, 18, 18));

        // Title area
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel title = new JLabel("한국어 배우기");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 28));
        titleRow.add(title, BorderLayout.WEST);

        // profile circle
        JLabel profile = new JLabel("\uD83D\uDC64");
        profile.setForeground(Color.WHITE);
        profile.setFont(new Font("SansSerif", Font.PLAIN, 20));
        profile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        profile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBadgeGallery();
            }
        });
        JPanel pRight = new JPanel();
        pRight.setOpaque(false);
        pRight.add(profile);
        titleRow.add(pRight, BorderLayout.EAST);

        header.add(titleRow, BorderLayout.NORTH);

        // Stats cards
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 12));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(12, 0, 0, 0));

        statsRow.add(createStatCard("\u2B50", "Points", lblPoints));
        statsRow.add(createStatCard("\uD83C\uDFC1", "Badges", lblBadges));
        statsRow.add(createStatCard("\uD83C\uDFC6", "Streak", lblStreak));

        header.add(statsRow, BorderLayout.CENTER);

        // Progress bar area
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.setBorder(new EmptyBorder(14, 0, 8, 0));
        lblProgressText.setForeground(new Color(255, 255, 255, 220));
        lblProgressText.setText("Learning Progress");
        progressPanel.add(lblProgressText, BorderLayout.WEST);
        lblPercentage.setForeground(new Color(255, 255, 255, 220));
        lblPercentage.setFont(new Font("SansSerif", Font.PLAIN, 12));
        progressPanel.add(lblPercentage, BorderLayout.EAST);

        progressBar.setMinimum(0);
        progressBar.setMaximum(10);
        progressBar.setValue(lessonsCompleted);
        progressBar.setPreferredSize(new Dimension(100, 18));
        progressBar.setForeground(new Color(255, 255, 255, 220));
        progressBar.setBackground(new Color(255, 255, 255, 90));
        progressBar.setBorder(null);
        progressBar.setStringPainted(false);
        progressBar.setToolTipText(null);
        progressBar.setOpaque(false);
        progressBar.setBorderPainted(false);

        progressPanel.add(progressBar, BorderLayout.SOUTH);
        header.add(progressPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // Main scrollable area
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(14, 14, 40, 14));
        content.setBackground(Color.WHITE);

        // Module cards
        content.add(createModuleCard(
            "Learning Modules",
            "<b>Earn +10 points</b> per lesson completed. Finish 10 lessons to gain a streak + badge.",
            new Color(14, 141, 250),
            e -> openLearning()
        ));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(createModuleCard(
            "Take a Quiz",
            "<b>Earn +1 point</b> per correct answer (20 questions, max 20 points).",
            new Color(22, 185, 106),
            e -> startGuiQuiz()
        ));
        content.add(Box.createRigidArea(new Dimension(0, 18)));

        // Motivational message
        JPanel msg = new JPanel();
        msg.setBackground(new Color(250, 240, 255));
        msg.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        msg.setLayout(new BorderLayout());
        JLabel m = new JLabel("✨ Keep up the great work! Every lesson brings you closer to fluency.");
        msg.add(m, BorderLayout.CENTER);
        content.add(msg);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Populate labels
        updateStatsLabels();

        pack();
    }

    /**
     * Load user progress from file (points, streak, lessonsCompleted).
     * File: user-progress.properties
     */
    private void loadProgress() {
        Properties props = new Properties();
        File f = new File(PROGRESS_FILE);
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(f)) {
                props.load(fis);
                points = Integer.parseInt(props.getProperty("points", "0"));
                badges = Integer.parseInt(props.getProperty("badges", "0"));
                streak = Integer.parseInt(props.getProperty("streak", "0"));
                lessonsCompleted = Integer.parseInt(props.getProperty("lessonsCompleted", "0"));
                String list = props.getProperty("badgesList", "");
                if (!list.isBlank()) {
                    unlockedBadges = new HashSet<>(Arrays.asList(list.split(",")));
                    badges = unlockedBadges.size();
                }
            } catch (Exception ex) {
                System.err.println("Error loading progress: " + ex.getMessage());
            }
        }
    }

    /**
     * Open (or focus) the badge gallery safely on the EDT.
     */
    private void showBadgeGallery() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (badgeGallery == null || !badgeGallery.isDisplayable()) {
                    badgeGallery = new BadgeGallery(KoreanAppGUI.this, unlockedBadges, BADGE_TITLES, BADGE_DESCRIPTIONS);
                    badgeGallery.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            badgeGallery = null;
                        }
                    });
                }
                badgeGallery.setVisible(true);
                badgeGallery.toFront();
            } catch (Exception ex) {
                badgeGallery = null;
                System.err.println("Could not open badge gallery: " + ex.getMessage());
            }
        });
    }

    /**
     * Save user progress to file (points, streak, lessonsCompleted).
     * File: user-progress.properties
     */
    private void saveProgress() {
        Properties props = new Properties();
        props.setProperty("points", String.valueOf(points));
        props.setProperty("badges", String.valueOf(badges));
        props.setProperty("streak", String.valueOf(streak));
        props.setProperty("lessonsCompleted", String.valueOf(lessonsCompleted));
        props.setProperty("badgesList", String.join(",", unlockedBadges));
        try (FileOutputStream fos = new FileOutputStream(PROGRESS_FILE)) {
            props.store(fos, "Korean Learning App - User Progress");
        } catch (Exception ex) {
            System.err.println("Error saving progress: " + ex.getMessage());
        }
    }

    private JPanel createStatCard(String icon, String title, JLabel valueLabel) {
        JPanel card = new RoundedPanel(new Color(255, 255, 255, 90));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("SansSerif", Font.PLAIN, 20));
        card.add(ic, BorderLayout.NORTH);

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        card.add(valueLabel, BorderLayout.CENTER);

        JLabel t = new JLabel(title);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        t.setForeground(new Color(255, 255, 255, 200));
        card.add(t, BorderLayout.SOUTH);

        // Make badges card clickable to open gallery
        if ("Badges".equalsIgnoreCase(title)) {
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showBadgeGallery();
                }
            });
        }

        return card;
    }

    private JPanel createModuleCard(String heading, String description, Color bg, ActionListener clickAction) {
        JPanel card = new RoundedPanel(bg, 14);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(320, 140));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel h = new JLabel(heading);
        h.setForeground(Color.WHITE);
        h.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(h, BorderLayout.NORTH);

        JLabel desc = new JLabel("<html><div style='color:rgba(255,255,255,0.9);'>" + description + "</div></html>");
        card.add(desc, BorderLayout.CENTER);

        JButton start = new JButton("Start");
        start.addActionListener(clickAction);
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnWrap.setOpaque(false);
        btnWrap.add(start);
        card.add(btnWrap, BorderLayout.SOUTH);

        return card;
    }

    private void openLearning() {
        KoreanLearningGUI.showLearningGUI(learningModule.getLessons(), () -> {
            // increment progress and award points when a lesson is completed
            lessonsCompleted = lessonsCompleted + 1;
            points += 10;

            // If the user reaches 10/10, celebrate, increment streak, and reset progress
            if (lessonsCompleted >= 10) {
                // Show a small celebration dialog
                try {
                    JOptionPane.showMessageDialog(this,
                        "Congrats! You completed 10 lessons — streak +1!",
                        "Milestone",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (java.awt.HeadlessException ex) {
                    // running in a headless environment; ignore UI dialog errors
                }
                lessonsCompleted = 0;
                streak += 1;
                // Award milestone badge
                awardBadge("lesson_milestone", BADGE_TITLES.getOrDefault("lesson_milestone", "Lesson Milestone"));
                // Streak-based badges
                if (streak == 5) {
                    awardBadge("streak_5", BADGE_TITLES.getOrDefault("streak_5", "5-Day Streak"));
                } else if (streak == 10) {
                    awardBadge("streak_10", BADGE_TITLES.getOrDefault("streak_10", "10-Day Streak"));
                }
            }

            // Auto-save progress after lesson completion
            saveProgress();
            updateStatsLabels();
        });
    }

     private void startGuiQuiz() {
        KoreanQuiz quiz = new KoreanQuiz();
        GameSystem game = new GameSystem();

        new Thread(() -> {
            quiz.startQuiz(game);

            // Only show results if the user finished (score was added to game system)
            if (game.getMaxScore() > 0 && game.getTotalScore() >= 0) {
                SwingUtilities.invokeLater(() -> {
                    String msg = "Quiz Done!\nScore: " + game.getTotalScore() + "/20\nGrade: " + (int) game.getPercent()
                            + "%";
                    JOptionPane.showMessageDialog(this, msg, "Results", JOptionPane.INFORMATION_MESSAGE);

                    this.points += game.getTotalScore(); // Update the main UI points
                    // Award quiz badge for high performance
                    double pct = game.getPercent();
                    if (pct >= 80.0) {
                        awardBadge("quiz_expert", BADGE_TITLES.getOrDefault("quiz_expert", "Quiz Expert"));
                    }
                    updateStatsLabels();
                });
            }
        }).start();
    }

    private void updateStatsLabels() {
        lblPoints.setText(String.valueOf(points));
        lblBadges.setText(String.valueOf(badges));
        lblStreak.setText(String.valueOf(streak));
        lblProgressText.setText("Learning Progress");
        progressBar.setValue(lessonsCompleted);
        progressBar.setMaximum(10);
        lblPercentage.setText(lessonsCompleted + "/10");
    }

    /**
     * Award a badge by id and show a brief popup. Persist immediately on new badge.
     */
    private void awardBadge(String badgeId, String badgeTitle) {
        if (unlockedBadges.add(badgeId)) {
            badges = unlockedBadges.size();
            try {
                JOptionPane.showMessageDialog(this, "Badge unlocked: " + badgeTitle, "Badge Earned!", JOptionPane.INFORMATION_MESSAGE);
            } catch (java.awt.HeadlessException ex) {
                // ignore in headless
            }
            updateStatsLabels();
            saveProgress();
            // If the gallery is open, refresh it to show the new badge immediately
            if (badgeGallery != null && badgeGallery.isDisplayable()) {
                badgeGallery.refresh();
            }
        }
    }

    // Small helper rounded panel
    static class RoundedPanel extends JPanel {
        private final Color bgc;
        private final int radius;

        RoundedPanel(Color bgc) { this(bgc, 10); }
        RoundedPanel(Color bgc, int radius) {
            super();
            this.bgc = bgc;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgc);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Simple badge gallery dialog showing unlocked and locked badges.
     */
    class BadgeGallery extends JDialog {
        private final Set<String> unlockedRef;
        private final Map<String, String> titlesRef;
        private final Map<String, String> descRef;
        private final JPanel grid;

        BadgeGallery(Frame owner, Set<String> unlocked, Map<String, String> titles, Map<String, String> desc) {
            super(owner, "Badge Gallery", true);
            this.unlockedRef = unlocked;
            this.titlesRef = titles;
            this.descRef = desc;

            grid = new JPanel(new GridLayout(0, 3, 10, 10));
            grid.setBorder(new EmptyBorder(12, 12, 12, 12));

            rebuildGrid();

            JScrollPane sp = new JScrollPane(grid);
            sp.setBorder(null);
            add(sp, BorderLayout.CENTER);

            JButton close = new JButton("Close");
            close.addActionListener(e -> dispose());
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            footer.add(close);
            add(footer, BorderLayout.SOUTH);

            setSize(540, 360);
            setLocationRelativeTo(owner);
        }

        private void rebuildGrid() {
            grid.removeAll();
            for (String id : titlesRef.keySet()) {
                JPanel card = new JPanel(new BorderLayout());
                boolean has = unlockedRef.contains(id);
                Color border = has ? new Color(34, 139, 34) : new Color(200, 200, 200);
                card.setBorder(BorderFactory.createLineBorder(border, 2));
                card.setBackground(Color.WHITE);
                JLabel t = new JLabel(titlesRef.get(id), SwingConstants.CENTER);
                t.setBorder(new EmptyBorder(8, 8, 8, 8));
                t.setFont(t.getFont().deriveFont(Font.BOLD, 14f));
                card.add(t, BorderLayout.CENTER);

                JLabel desc = new JLabel(" ", SwingConstants.CENTER);
                String d = descRef.getOrDefault(id, "");
                if (!d.isBlank()) {
                    desc.setText("\u25A0 " + d);
                    desc.setFont(desc.getFont().deriveFont(Font.PLAIN, 11f));
                }
                card.add(desc, BorderLayout.NORTH);

                JLabel state = new JLabel(has ? "\u2713" : "\u25CB", SwingConstants.CENTER);
                state.setBorder(new EmptyBorder(6, 6, 6, 6));
                card.add(state, BorderLayout.SOUTH);

                // show a dialog with details when clicked
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String title = titlesRef.get(id);
                        String body = descRef.getOrDefault(id, "No description available.");
                        String status = unlockedRef.contains(id) ? "Unlocked" : "Locked";
                        JOptionPane.showMessageDialog(BadgeGallery.this, body + "\n\nStatus: " + status, title, JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                grid.add(card);
            }
            grid.revalidate();
            grid.repaint();
        }

        void refresh() {
            rebuildGrid();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KoreanAppGUI gui = new KoreanAppGUI();
            gui.setVisible(true);
        });
    }
}

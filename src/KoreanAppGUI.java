// Modern dashboard GUI for Learn Korean App

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KoreanAppGUI extends JFrame {

    private int points;
    private int badges;
    private int streak;
    private int lessonsCompleted;

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel lblPoints = new JLabel();
    private final JLabel lblBadges = new JLabel();
    private final JLabel lblStreak = new JLabel();
    private final JLabel lblProgressText = new JLabel();
    private final JLabel lblPercentage = new JLabel();

    private final KoreanLearningModule learningModule;

    public KoreanAppGUI() {
        super("한국어 배우기 — Learn Korean");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(380, 700));
        setLocationRelativeTo(null);

        learningModule = new KoreanLearningModule();

        // Start fresh each session (no persistence)
        points = 0;
        badges = 0;
        streak = 0;
        lessonsCompleted = 0;
        initUI();
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
        content.add(createModuleCard("Learning Modules", "Explore 10+ lessons covering Hangul, vocabulary, and phrases", new Color(14, 141, 250), e -> openLearning()));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(createModuleCard("Take a Quiz", "Test your knowledge with 10 questions", new Color(22, 185, 106), e -> startGuiQuiz()));
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
            }

            // saveProgress();  // Disabled - app resets on each run
            updateStatsLabels();
        });
    }

    private void startGuiQuiz() {
        // Placeholder: quiz implementation removed from this branch.
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                """
                Quiz module is not included in this branch.
                Your teammate can implement KoreanQuiz.java and Quizzable.java and wire it up here.""",
                "Quiz Placeholder",
                JOptionPane.INFORMATION_MESSAGE);
        });
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KoreanAppGUI gui = new KoreanAppGUI();
            gui.setVisible(true);
        });
    }
}

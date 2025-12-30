// GUI-based learning module for displaying lessons with page navigation

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KoreanLearningGUI extends JFrame {

    private final KoreanLesson[] lessons;
    private int currentLessonIndex;
    private int currentPage;

    private final JLabel lessonTitle = new JLabel();
    private final JLabel pageContent = new JLabel();
    private final JLabel pageCounter = new JLabel();
    private final JButton prevBtn = new JButton("< Previous");
    private final JButton nextBtn = new JButton("Next >");
    private final JButton completeBtn = new JButton("Complete Lesson");

    private final Runnable onComplete;

    public KoreanLearningGUI(KoreanLesson[] lessons) {
        this(lessons, null);
    }

    public KoreanLearningGUI(KoreanLesson[] lessons, Runnable onComplete) {
        super("Learning Module");
        this.lessons = lessons;
        this.currentLessonIndex = 0;
        this.currentPage = 0;
        this.onComplete = onComplete;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        initUI();
        loadLesson(0);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(176, 58, 224));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(16, 16, 16, 16));

        lessonTitle.setForeground(Color.WHITE);
        lessonTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        header.add(lessonTitle, BorderLayout.CENTER);

        pageCounter.setForeground(new Color(255, 255, 255, 180));
        pageCounter.setFont(new Font("SansSerif", Font.PLAIN, 12));
        header.add(pageCounter, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Content area
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        pageContent.setVerticalAlignment(SwingConstants.TOP);
        pageContent.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(pageContent);
        scroll.setBorder(null);
        content.add(scroll, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // Footer with navigation
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel navBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        prevBtn.addActionListener(e -> previousPage());
        nextBtn.addActionListener(e -> nextPage());
        completeBtn.addActionListener(e -> completeLesson());

        navBtns.add(prevBtn);
        navBtns.add(nextBtn);
        navBtns.add(completeBtn);

        footer.add(navBtns, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadLesson(int lessonIndex) {
        if (lessonIndex >= 0 && lessonIndex < lessons.length) {
            currentLessonIndex = lessonIndex;
            currentPage = 0;
            updateDisplay();
        }
    }

    private void updateDisplay() {
        KoreanLesson lesson = lessons[currentLessonIndex];
        String[] pages = lesson.getPages();

        lessonTitle.setText(lesson.getTitle());
        pageContent.setText("<html><div style='line-height: 1.6;'>" + pages[currentPage].replace("\n", "<br>") + "</div></html>");
        pageCounter.setText((currentPage + 1) + " / " + pages.length);

        prevBtn.setEnabled(currentPage > 0);
        nextBtn.setEnabled(currentPage < pages.length - 1);
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateDisplay();
        }
    }

    private void nextPage() {
        KoreanLesson lesson = lessons[currentLessonIndex];
        if (currentPage < lesson.getPageCount() - 1) {
            currentPage++;
            updateDisplay();
        }
    }

    private void completeLesson() {
        if (onComplete != null) {
            try {
                onComplete.run();
            } catch (RuntimeException ex) {
                // ignore runtime exceptions thrown by callback
            }
        }
        JOptionPane.showMessageDialog(this,
            "Lesson \"" + lessons[currentLessonIndex].getTitle() + "\" completed!\n\nYour progress has been saved.",
            "Lesson Complete",
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Factory to launch the GUI with lesson selection
    public static void showLearningGUI(KoreanLesson[] lessons) {
        showLearningGUI(lessons, null);
    }

    public static void showLearningGUI(KoreanLesson[] lessons, Runnable onComplete) {
        if (lessons.length == 0) {
            JOptionPane.showMessageDialog(null, "No lessons available.", "Learning Module", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // If only one lesson, show it directly
        if (lessons.length == 1) {
            SwingUtilities.invokeLater(() -> {
                KoreanLearningGUI gui = new KoreanLearningGUI(lessons, onComplete);
                gui.setVisible(true);
            });
            return;
        }

        // Otherwise show a lesson selector
        String[] titles = new String[lessons.length];
        for (int i = 0; i < lessons.length; i++) {
            titles[i] = lessons[i].getTitle();
        }

        JComboBox<String> selector = new JComboBox<>(titles);
        int result = JOptionPane.showConfirmDialog(null, selector, "Select a Lesson", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int idx = selector.getSelectedIndex();
            SwingUtilities.invokeLater(() -> {
                KoreanLearningGUI gui = new KoreanLearningGUI(lessons, onComplete);
                gui.loadLesson(idx);
                gui.setVisible(true);
            });
        }
    }
}

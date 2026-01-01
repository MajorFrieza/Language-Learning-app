// Group: 3 Benny Java
// Theme: Language Learning app

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            KoreanAppGUI gui = new KoreanAppGUI();
            gui.setVisible(true);
        });
        
        try (Scanner scanner = new Scanner(System.in)) {
            final KoreanLearningModule learningModule = new KoreanLearningModule();
            final GameSystem game = new GameSystem();
        // Teammates will implement these:
         final KoreanQuiz quiz = new KoreanQuiz();
        // GameSystem game = new GameSystem();

        int choice;
        do {
            System.out.println("===== Learn Korean App =====");
            System.out.println("1. Learning Module");
            System.out.println("2. Quiz Module");
            System.out.println("3. Gamification / Score");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> learningModule.showLessonMenu();
                case 2 ->   case 2 -> quiz.startQuiz(game);
                case 3 -> game.showResult();
                case 4 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 4);
        }
    }
}

// Group: 3 Benny Java
// Theme: Language Learning app

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        // Launch the Graphical Dashboard
        javax.swing.SwingUtilities.invokeLater(() -> {
            KoreanAppGUI gui = new KoreanAppGUI();
            gui.setVisible(true);
        });

        try (Scanner scanner = new Scanner(System.in)) {
            final KoreanLearningModule learningModule = new KoreanLearningModule();
            final GameSystem game = new GameSystem();
            final KoreanQuiz quiz = new KoreanQuiz();

            // FIX: Initialize choice to 0 to prevent the compiler error
            int choice = 0;

            do {
                System.out.println("\n===== Learn Korean App =====");
                System.out.println("1. Learning Module (Console)");
                System.out.println("2. Quiz Module (Terminal or GUI)");
                System.out.println("3. Gamification / Score (Console)");
                System.out.println("4. Exit");
                System.out.print("Choose: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid number.");
                    scanner.next(); // Clear invalid input
                    continue;
                }

                choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer after nextInt()

                switch (choice) {
                    case 1 -> learningModule.showLessonMenu();
                    case 2 -> {
                        // Sub-menu for Terminal vs GUI Quizzes
                        System.out.println("\n--- Select Quiz Mode ---");
                        System.out.println("1. Terminal (Questions in this window)");
                        System.out.println("2. GUI (Questions in popup windows)");
                        System.out.println("3. Back");
                        System.out.print("Choice: ");

                        if (scanner.hasNextInt()) {
                            int quizChoice = scanner.nextInt();
                            scanner.nextLine(); // Clear buffer

                            if (quizChoice == 1) {
                                // Prints questions directly to the terminal
                                quiz.startQuizConsole(game, scanner);
                            } else if (quizChoice == 2) {
                                // Original teammate-implemented GUI quiz
                                quiz.startQuiz(game);
                            }
                        }
                    }
                    case 3 -> game.showResult();
                    case 4 -> System.out.println("Goodbye!");
                    default -> System.out.println("Invalid choice.");
                }

            } while (choice != 4);
        }
    }
}

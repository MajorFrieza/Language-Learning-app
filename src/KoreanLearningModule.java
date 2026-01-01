/**
 * Korean Learning Module - Manages the learning content for Korean language.
 * Extends the abstract Module class and implements the Learnable interface.
 * Provides lessons organized by difficulty level and topic.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Display and manage Korean language lessons for users.
 * Tester: [Team Member Name]
 */

import java.util.Scanner;

public class KoreanLearningModule extends Module {

    private final KoreanLesson[] lessons;
    private final Scanner scanner;

    public KoreanLearningModule() {
        super("Korean Learning Module", "Learn Korean through structured lessons covering Hangul, vocabulary, phrases, and grammar.");
        this.lessons = KoreanLesson.createAllLessons();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Get all lessons available in this module.
     * @return Array of KoreanLesson objects
     */
    public KoreanLesson[] getLessons() {
        return lessons;
    }

    /**
     * Get the total number of lessons in this module.
     * @return Number of lessons
     */
    public int getLessonCount() {
        return lessons.length;
    }

    /**
     * Implementation of Learnable interface - displays the lesson selection menu.
     * Allows users to choose which lesson to study.
     */
    @Override
    public void showLessonMenu() {
        int choice;
        do {
            System.out.println("===== Korean Learning Module =====");
            for (int i = 0; i < lessons.length; i++) {
                System.out.println((i + 1) + ". " + lessons[i].getTitle());
            }
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose a lesson: ");

            choice = scanner.nextInt();

            if (choice >= 1 && choice <= lessons.length) {
                showLesson(choice);
            } else if (choice != 0) {
                System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    /**
     * Implementation of Learnable interface - displays a specific lesson.
     * @param lessonNumber The 1-based lesson number to display
     */
    @Override
    public void showLesson(int lessonNumber) {
        if (lessonNumber >= 1 && lessonNumber <= lessons.length) {
            lessons[lessonNumber - 1].display();
            System.out.println("Press any number then Enter to go back to lessons menu.");
            scanner.nextInt();
        } else {
            System.out.println("Invalid lesson number.");
        }
    }
}

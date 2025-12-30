// Creator: [Your Name]
// Purpose: Manages the Korean learning module and displays lessons to the user.

import java.util.Scanner;

public class KoreanLearningModule implements Learnable {

    private final KoreanLesson[] lessons;
    private final Scanner scanner;

    public KoreanLearningModule() {
        this.lessons = KoreanLesson.createAllLessons();
        this.scanner = new Scanner(System.in);
    }

    public KoreanLesson[] getLessons() {
        return lessons;
    }

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

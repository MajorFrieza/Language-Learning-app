/**
 * Learnable Interface
 * Contract for all learning modules in the application.
 * Defines methods that all learning modules must implement.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Establish interface for learning module implementations.
 */

public interface Learnable {
    /**
     * Display a menu for selecting lessons.
     */
    void showLessonMenu();

    /**
     * Display a specific lesson to the user.
     * @param lessonNumber The 1-based lesson number to display
     */
    void showLesson(int lessonNumber);
}

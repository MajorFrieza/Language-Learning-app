/**
 * Gamifiable Interface
 * Contract for gamification systems in the application.
 * Defines methods that all gamification implementations must provide.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Establish interface for gamification feature implementations.
 */

public interface Gamifiable {
    /**
     * Add points to the score for gamification.
     * @param points The number of points to add
     */
    void addScore(int points);

    /**
     * Display the results and feedback based on score.
     */
    void showResult();
}

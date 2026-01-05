
/**
 * Game System - Handles gamification scoring and feedback.
 * Implements the Gamifiable interface to manage points, scores, and motivational messages.
 * Provides score tracking, percentage calculation, performance feedback, and file persistence for quiz scores.
 *
 * Creator: Group 3 Benny Java, Hafizh Armansyah
 * Purpose: Manage scoring and provide motivational feedback based on user performance.
 * Tester: Faqrulrazi, Macallister
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameSystem implements Gamifiable {

    private int totalScore = 0;
    private int maxScore = 0;

    // Where to persist quiz results
    private static final String SCORE_FILE = "quiz-scores.txt";

    /**
     * Add points to the total score.
     * Implements Gamifiable interface.
     * 
     * @param points The points to add
     */
    @Override
    public void addScore(int points) {
        totalScore += points;
    }

    /**
     * Reset the score and set the maximum possible score.
     * Called when starting a new quiz.
     * 
     * @param maxScore The maximum points available
     */
    public void resetScore(int maxScore) {
        this.totalScore = 0;
        this.maxScore = maxScore;
    }

    /**
     * Persist the latest quiz score to a text file for record-keeping.
     * File format: timestamp,total,max,percent
     */
    public void persistLatestScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILE, true))) {
            String ts = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String line = String.format("%s,%d,%d,%.2f%%%n", ts, totalScore, maxScore, getPercent());
            bw.write(line);
        } catch (IOException ex) {
            System.err.println("Failed to persist quiz score: " + ex.getMessage());
        }
    }

    /**
     * Get the total score achieved.
     * 
     * @return The current total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Get the maximum possible score.
     * 
     * @return The maximum score available
     */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * Calculate the percentage score.
     * 
     * @return The score as a percentage (0-100)
     */
    public double getPercent() {
        if (maxScore == 0)
            return 0;
        return (totalScore * 100.0) / maxScore;
    }

    /**
     * Display the quiz results with motivational feedback.
     * Provides message based on performance level:
     * 80%+: Outstanding! | 60-79%: That's good! | 40-59%: Good try!
     * 20-39%: You can do better! | 0-19%: Don't give up!
     * Implements Gamifiable interface.
     */
    @Override
    public void showResult() {
        System.out.println("==== Result ====");
        System.out.println("Total score: " + totalScore + " / " + maxScore);

        double percent = getPercent();
        String message;

        if (percent >= 80) {
            message = "Outstanding!";
        } else if (percent >= 60) {
            message = "That's good!";
        } else if (percent >= 40) {
            message = "Good try!";
        } else if (percent >= 20) {
            message = "You can do better!";
        } else {
            message = "Don't give up!";
        }

        System.out.println(message);

        // Simple text badge
        if (percent >= 80) {
            System.out.println("Badge earned: GOLD STAR");
        } else if (percent >= 60) {
            System.out.println("Badge earned: SILVER STAR");
        } else if (percent >= 40) {
            System.out.println("Badge earned: BRONZE STAR");
        } else {
            System.out.println("Badge earned: PRACTICE STARTER");
        }
        System.out.println();
    }
}

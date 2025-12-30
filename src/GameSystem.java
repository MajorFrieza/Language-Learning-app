// Creator: [Your Name]
// Purpose: Handles gamification: total score, percentage and message.

public class GameSystem implements Gamifiable {

    private int totalScore = 0;
    private int maxScore = 0;

    @Override
    public void addScore(int points) {
        totalScore += points;
    }

    // Call this when starting a new quiz
    public void resetScore(int maxScore) {
        this.totalScore = 0;
        this.maxScore = maxScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public double getPercent() {
        if (maxScore == 0) return 0;
        return (totalScore * 100.0) / maxScore;
    }

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

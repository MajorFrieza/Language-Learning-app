/**
 * Represents a quiz question with multiple parts.
 * This is an abstract base class for different question types.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Define the structure and behavior for all question types in the quiz module.
 */

public abstract class Question {

    protected String questionText;
    protected int pointsValue;

    /**
     * Constructor to initialize a question with text and points.
     * @param text The question text
     * @param points The points awarded for correct answer
     */
    public Question(String text, int points) {
        this.questionText = text;
        this.pointsValue = points;
    }

    /**
     * Get the question text.
     * @return The question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Get the points value for this question.
     * @return Points awarded for correct answer
     */
    public int getPointsValue() {
        return pointsValue;
    }

    /**
     * Check if the user's answer is correct.
     * @param userAnswer The user's response
     * @return true if correct, false otherwise
     */
    public abstract boolean isCorrect(String userAnswer);

    /**
     * Get the correct answer (for feedback).
     * @return The correct answer as a string
     */
    public abstract String getCorrectAnswer();

    /**
     * Display the question to the user.
     * Subclasses override this to show question-specific formats.
     */
    public abstract void display();

    @Override
    public String toString() {
        return "[" + pointsValue + " pts] " + questionText;
    }
}

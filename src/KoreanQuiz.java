import javax.swing.JOptionPane;

public class KoreanQuiz implements Quizzable {

    private static final int EXIT_CODE = -999;

    @Override
    public void startQuiz(GameSystem game) {
        int localScore = 0;
        int totalQuestions = 20;
        game.resetScore(totalQuestions);

        // --- SECTION 1: MULTIPLE CHOICE (1-10) ---
        // Format: {Question, Option A, Option B, Option C, Correct Letter}
        String[][] mcqs = {
                { "What is 'ㅏ'?", "A) a", "B) o", "C) u", "A" },
                { "'Hello' in Korean is...", "A) Annyeong", "B) Bap", "C) Gada", "A" },
                { "Which is number 1?", "A) Dul", "B) Hana", "C) Set", "B" },
                { "Which is 'Red'?", "A) Ppalgan", "B) Paran", "C) Noran", "A" },
                { "What is 'Rice'?", "A) Bap", "B) Kimchi", "C) Mul", "A" },
                { "'Abeoji' means...", "A) Mom", "B) Dad", "C) Friend", "B" },
                { "What is 'Monday'?", "A) Wolyoil", "B) Hwayoil", "C) Suyoil", "A" },
                { "'Gada' means...", "A) To eat", "B) To go", "C) To sleep", "B" },
                { "'Yeoppeuda' means...", "A) Big", "B) Small", "C) Pretty", "C" },
                { "'Eodi' means...", "A) Who", "B) What", "C) Where", "C" }
        };

        for (int i = 0; i < mcqs.length; i++) {
            int res = askMCQ(i + 1, mcqs[i][0], mcqs[i][1], mcqs[i][2], mcqs[i][3], mcqs[i][4]);
            if (res == EXIT_CODE)
                return; // Exit immediately if Cancel is pressed
            localScore += res;
        }

        // --- SECTION 2: TRUE / FALSE (11-15) ---
        Object[][] tfs = {
                { 11, "Hangul was made by King Sejong.", true },
                { 12, "'Annyeong' is for friends (Casual).", true },
                { 13, "'Dul' means number 5.", false },
                { 14, "'Eomma' means Mom.", true },
                { 15, "'Hayan-saek' is Black.", false }
        };

        for (Object[] tf : tfs) {
            int res = askTF((int) tf[0], (String) tf[1], (boolean) tf[2]);
            if (res == EXIT_CODE)
                return;
            localScore += res;
        }

        // --- SECTION 3: FILL IN THE BLANK (16-20) ---
        String[][] blanks = {
                { "16", "Type the number 2 (Native): dul, hana, or set?", "dul" },
                { "17", "Type 'Blue' in Korean: paran or noran?", "paran" },
                { "18", "Type 'To eat' in Korean: meokda or jada?", "meokda" },
                { "19", "Type 'Today' in Korean: oneul or naeil?", "oneul" },
                { "20", "Type 'What' in Korean: mwo or wae?", "mwo" }
        };

        for (String[] b : blanks) {
            int res = askBlank(Integer.parseInt(b[0]), b[1], b[2]);
            if (res == EXIT_CODE)
                return;
            localScore += res;
        }

        game.addScore(localScore);
        game.persistLatestScore();
    }

    /**
     * Overloaded quiz runner that accepts custom question sets (overloading).
     * @param game scoring system
     * @param mcqs multiple-choice questions: {question, A, B, C, correctLetter}
     * @param tfs true/false questions: {number(Integer), text(String), correct(Boolean)}
     * @param blanks fill-in questions: {number, prompt, correctAnswer}
     */
    public void startQuiz(GameSystem game, String[][] mcqs, Object[][] tfs, String[][] blanks) {
        int localScore = 0;
        int totalQuestions = mcqs.length + tfs.length + blanks.length;
        game.resetScore(totalQuestions);

        for (int i = 0; i < mcqs.length; i++) {
            int res = askMCQ(i + 1, mcqs[i][0], mcqs[i][1], mcqs[i][2], mcqs[i][3], mcqs[i][4]);
            if (res == EXIT_CODE)
                return;
            localScore += res;
        }

        for (Object[] tf : tfs) {
            int res = askTF((int) tf[0], (String) tf[1], (boolean) tf[2]);
            if (res == EXIT_CODE)
                return;
            localScore += res;
        }

        for (String[] b : blanks) {
            int res = askBlank(Integer.parseInt(b[0]), b[1], b[2]);
            if (res == EXIT_CODE)
                return;
            localScore += res;
        }

        game.addScore(localScore);
        game.persistLatestScore();
    }

    private int askMCQ(int num, String q, String a, String b, String c, String correct) {
        String msg = "Question " + num + "/20\n" + q + "\n\n" + a + "\n" + b + "\n" + c + "\n\nType A, B, or C:";
        while (true) {
            String ans = JOptionPane.showInputDialog(null, msg, "Quiz", JOptionPane.QUESTION_MESSAGE);
            if (ans == null)
                return EXIT_CODE;
            String trimmed = ans.trim();
            if (!(trimmed.equalsIgnoreCase("A") || trimmed.equalsIgnoreCase("B") || trimmed.equalsIgnoreCase("C"))) {
                JOptionPane.showMessageDialog(null, "Please enter only A, B, or C.", "Quiz", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            return (trimmed.equalsIgnoreCase(correct)) ? 1 : 0;
        }
    }

    private int askTF(int num, String q, boolean correct) {
        int choice = JOptionPane.showConfirmDialog(null, "Question " + num + "/20\n" + q + "\n\nIs this True?",
                "True/False", JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
            return EXIT_CODE;
        return (choice == JOptionPane.YES_OPTION == correct) ? 1 : 0;
    }

    private int askBlank(int num, String q, String correct) {
        String ans = JOptionPane.showInputDialog(null, "Question " + num + "/20\n" + q, "Fill in the Blank",
                JOptionPane.QUESTION_MESSAGE);
        if (ans == null)
            return EXIT_CODE;
        return (ans.trim().equalsIgnoreCase(correct)) ? 1 : 0;
    }

    /**
     * Console-based quiz runner. Prints questions to stdout and reads answers from stdin.
     * This allows the quiz to be used from the terminal (console) menu.
     */
    public void startQuizConsole(GameSystem game, java.util.Scanner scanner) {
        int localScore = 0;
        int totalQuestions = 20;
        game.resetScore(totalQuestions);

        // --- SECTION 1: MULTIPLE CHOICE (1-10) ---
        String[][] mcqs = {
                { "What is 'ㅏ'?", "A) a", "B) o", "C) u", "A" },
                { "'Hello' in Korean is...", "A) Annyeong", "B) Bap", "C) Gada", "A" },
                { "Which is number 1?", "A) Dul", "B) Hana", "C) Set", "B" },
                { "Which is 'Red'?", "A) Ppalgan", "B) Paran", "C) Noran", "A" },
                { "What is 'Rice'?", "A) Bap", "B) Kimchi", "C) Mul", "A" },
                { "'Abeoji' means...", "A) Mom", "B) Dad", "C) Friend", "B" },
                { "What is 'Monday'?", "A) Wolyoil", "B) Hwayoil", "C) Suyoil", "A" },
                { "'Gada' means...", "A) To eat", "B) To go", "C) To sleep", "B" },
                { "'Yeoppeuda' means...", "A) Big", "B) Small", "C) Pretty", "C" },
                { "'Eodi' means...", "A) Who", "B) What", "C) Where", "C" }
        };

        System.out.println("Starting console quiz (20 questions). Type 'exit' to cancel.\n");
        for (int i = 0; i < mcqs.length; i++) {
            System.out.println("Question " + (i + 1) + "/20: " + mcqs[i][0]);
            System.out.println(mcqs[i][1] + "  " + mcqs[i][2] + "  " + mcqs[i][3]);
            System.out.print("Answer (A/B/C): ");
            String ans = scanner.nextLine();
            if (ans == null) return;
            if (ans.trim().equalsIgnoreCase("exit")) return;
            if (ans.trim().equalsIgnoreCase(mcqs[i][4])) localScore++;
        }

        // --- SECTION 2: TRUE / FALSE (11-15) ---
        Object[][] tfs = {
                { 11, "Hangul was made by King Sejong.", true },
                { 12, "'Annyeong' is for friends (Casual).", true },
                { 13, "'Dul' means number 5.", false },
                { 14, "'Eomma' means Mom.", true },
                { 15, "'Hayan-saek' is Black.", false }
        };

        for (Object[] tf : tfs) {
            System.out.println("Question " + tf[0] + "/20: " + tf[1]);
            System.out.print("True or False (T/F): ");
            String a = scanner.nextLine();
            if (a == null) return;
            if (a.trim().equalsIgnoreCase("exit")) return;
            boolean answer = a.trim().equalsIgnoreCase("T") || a.trim().equalsIgnoreCase("True");
            if (answer == (boolean) tf[2]) localScore++;
        }

        // --- SECTION 3: FILL IN THE BLANK (16-20) ---
        String[][] blanks = {
                { "16", "Type the number 2 (Native): dul, hana, or set?", "dul" },
                { "17", "Type 'Blue' in Korean: paran or noran?", "paran" },
                { "18", "Type 'To eat' in Korean: meokda or jada?", "meokda" },
                { "19", "Type 'Today' in Korean: oneul or naeil?", "oneul" },
                { "20", "Type 'What' in Korean: mwo or wae?", "mwo" }
        };

        for (String[] b : blanks) {
            System.out.println("Question " + b[0] + "/20: " + b[1]);
            System.out.print("Answer: ");
            String an = scanner.nextLine();
            if (an == null) return;
            if (an.trim().equalsIgnoreCase("exit")) return;
            if (an.trim().equalsIgnoreCase(b[2])) localScore++;
        }

        game.addScore(localScore);
        System.out.println("Quiz complete. Score: " + localScore + "/20\n");
        game.persistLatestScore();
    }
}

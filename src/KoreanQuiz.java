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
    }

    private int askMCQ(int num, String q, String a, String b, String c, String correct) {
        String msg = "Question " + num + "/20\n" + q + "\n\n" + a + "\n" + b + "\n" + c + "\n\nType A, B, or C:";
        String ans = JOptionPane.showInputDialog(null, msg, "Quiz", JOptionPane.QUESTION_MESSAGE);
        if (ans == null)
            return EXIT_CODE;
        return (ans.trim().equalsIgnoreCase(correct)) ? 1 : 0;
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
}
/**
 * KoreanLesson - Represents a single Korean language lesson.
 * Encapsulates lesson content with title, educational material, and paging support.
 * Supports splitting lessons into multiple pages for better readability and navigation.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Store and manage lesson content with page navigation.
 * Tester: [Team Member Name]
 */

public class KoreanLesson {

    /**
     * The title of the lesson.
     */
    private final String title;

    /**
     * The complete content of the lesson, formatted with double newlines between pages.
     */
    private final String content;

    /**
     * Optional path to an image for this lesson.
     */
    private final String imagePath;

    /**
     * Constructor to create a new lesson with title and content.
     * @param title The lesson title
     * @param content The lesson content (use double newlines to separate pages)
     */
    public KoreanLesson(String title, String content, String imagePath) {
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
    }

    /**
     * Get the lesson title.
     * @return The title of this lesson
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the complete lesson content.
     * @return The full content text
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the optional image path for this lesson.
     * @return Path to image file or null if none set
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Get lesson pages. Page 1 is the full text; if an image exists, page 2 is the image placeholder.
     * @return Array of page identifiers/content
     */
    public String[] getPages() {
        java.util.List<String> pages = new java.util.ArrayList<>();
        pages.add(content == null ? "" : content.trim());
        if (imagePath != null && !imagePath.isBlank()) {
            pages.add("__IMAGE_PAGE__");
        }
        return pages.toArray(String[]::new);
    }

    /**
     * Get the total number of pages in this lesson.
     * @return The page count
     */
    public int getPageCount() {
        return getPages().length;
    }

    /**
     * Returns true if the given page index points to the image page.
     * @param pageIndex 0-based page index
     * @return true if this is the image page
     */
    public boolean isImagePage(int pageIndex) {
        String[] pages = getPages();
        return pageIndex >= 0 && pageIndex < pages.length && "__IMAGE_PAGE__".equals(pages[pageIndex]);
    }

    /**
     * Simple console display for now (used by the console learning module).
     */
    public void display() {
        System.out.println("== " + title + " ==");
        System.out.println(content);
    }

    /**
     * Create a comprehensive set of Korean learning lessons with multiple pages each.
     * add images path for each lesson.
     */
    public static KoreanLesson[] createAllLessons() {
        return new KoreanLesson[] {
            new KoreanLesson("1. Introduction to Hangul", """
                What is Hangul?
                Hangul (한글) is the Korean alphabet, created in 1443 by King Sejong the Great.
                It is one of the most scientific and logical writing systems in the world.
                Unlike Chinese characters, Hangul is phonetic and can be learned quickly.

                Basic Vowels
                ㅏ (a) - as in "father"
                ㅓ (eo) - as in "up"
                ㅗ (o) - as in "no"
                ㅜ (u) - as in "moon"
                ㅣ (i) - as in "feet"

                Basic Consonants
                ㄱ (g) - as in "go"
                ㄴ (n) - as in "no"
                ㄷ (d) - as in "dog"
                ㅁ (m) - as in "man"
                ㅂ (b) - as in "box"
                """, "src/assets/lesson1.png"),

            new KoreanLesson("2. Basic Greetings", """
                Saying Hello

                안녕하세요 (annyeonghaseyo)
                Formal hello - use with strangers, elders, or in business settings

                안녕 (annyeong)
                Casual hello - use with friends

                Saying Goodbye

                안녕히 가세요 (annyeonghi gaseyo)
                "Go well" - say this when someone is leaving

                안녕히 계세요 (annyeonghi gyeseyo)
                "Stay well" - say this when you are leaving

                Polite Expressions

                감사합니다 (gamsahamnida)
                Thank you (formal)

                죄송합니다 (joesonghamnida)
                I'm sorry (formal)
                """, "src/assets/lesson2.png"),

            new KoreanLesson("3. Numbers 1-10", """
                Native Korean Numbers 1-5

                하나 (hana) - 1
                둘 (dul) - 2
                셋 (set) - 3
                넷 (net) - 4
                다섯 (daseot) - 5

                Numbers 6-10

                여섯 (yeoseot) - 6
                일곱 (ilgop) - 7
                여덟 (yeodeol) - 8
                아홉 (ahop) - 9
                열 (yeol) - 10

                Practice Counting

                Try counting from 1 to 10 using native Korean numbers.
                These are used for ages, quantities, and general counting.
                """, "src/assets/lesson3.png"),

            new KoreanLesson("4. Colors", """
                Basic Colors

                빨간색 (ppalgan-saek) - Red
                파란색 (paran-saek) - Blue
                노란색 (noran-saek) - Yellow
                초록색 (chorok-saek) - Green
                검은색 (geomun-saek) - Black

                More Colors

                하얀색 (hayan-saek) - White
                주황색 (juhwang-saek) - Orange
                보라색 (bora-saek) - Purple
                분홍색 (bunhong-saek) - Pink
                갈색 (gal-saek) - Brown
                """, "src/assets/lesson4.png"),

            new KoreanLesson("5. Food & Dining", """
                Common Foods

                밥 (bap) - Rice
                김치 (gimchi) - Kimchi (spicy pickled vegetables)
                불고기 (bulgogi) - Marinated grilled beef
                비빔밥 (bibimbap) - Mixed rice with vegetables
                라면 (ramyeon) - Instant noodles

                At a Restaurant

                주문할게요 (jumunhal-ge-yo)
                I'll order (polite)

                맛있어요 (mas-iss-eo-yo)
                It's delicious (polite)

                계산서 주세요 (gyesan-seo ju-se-yo)
                The bill, please
                """, "src/assets/lesson5.png"),

            new KoreanLesson("6. Family Members", """
                Immediate Family

                가족 (gajok) - Family
                아버지 (abeoji) - Father (formal)
                어머니 (eomeoni) - Mother (formal)
                아빠 (appa) - Dad (informal)
                엄마 (eomma) - Mom (informal)

                Siblings

                오빠 (oppa) - Older brother (for females)
                형 (hyeong) - Older brother (for males)
                언니 (eonni) - Older sister (for females)
                누나 (nuna) - Older sister (for males)
                동생 (dongsaeng) - Younger sibling
                """, "src/assets/lesson6.png"),

            new KoreanLesson("7. Days of the Week", """
                The First Four Days

                월요일 (wol-yo-il) - Monday
                화요일 (hwa-yo-il) - Tuesday
                수요일 (su-yo-il) - Wednesday
                목요일 (mok-yo-il) - Thursday

                Rest of the Week

                금요일 (geum-yo-il) - Friday
                토요일 (to-yo-il) - Saturday
                일요일 (il-yo-il) - Sunday
                오늘 (oneul) - Today
                내일 (naeil) - Tomorrow
                어제 (eoje) - Yesterday
                """, "src/assets/lesson7.png"),

            new KoreanLesson("8. Common Verbs", """
                Daily Actions

                가다 (gada) - To go
                오다 (oda) - To come
                먹다 (meokda) - To eat
                마시다 (masida) - To drink
                자다 (jada) - To sleep

                More Verbs

                보다 (boda) - To see/watch
                듣다 (deutda) - To listen
                말하다 (malhada) - To speak
                읽다 (ilkda) - To read
                쓰다 (sseuda) - To write
                """, "src/assets/lesson8.png"),

            new KoreanLesson("9. Common Adjectives", """
                Describing Things

                크다 (keuda) - To be big
                작다 (jakda) - To be small
                좋다 (jota) - To be good
                나쁘다 (nappeuda) - To be bad
                예쁘다 (yeppeuda) - To be pretty

                More Descriptions

                맛있다 (mas-itda) - To be delicious
                재미있다 (jaemi-itda) - To be interesting/fun
                어렵다 (eoryeopda) - To be difficult
                쉽다 (swipda) - To be easy
                빠르다 (ppareuda) - To be fast
                """, "src/assets/lesson9.png"),

            new KoreanLesson("10. Asking Questions", """
                Question Words

                누구 (nugu) - Who
                뭐/무엇 (mwo/mueot) - What
                어디 (eodi) - Where
                언제 (eonje) - When
                왜 (wae) - Why

                Common Questions

                이름이 뭐예요? (ireumi mwo-ye-yo?)
                What is your name?

                어디에 살아요? (eodi-e sara-yo?)
                Where do you live?

                몇 살이에요? (myeot sal-i-e-yo?)
                How old are you?
                """, "src/assets/lesson10.png")
        };
    }
}


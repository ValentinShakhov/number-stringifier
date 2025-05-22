import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts non-negative integers to their English word representation.
 * For example, the input 123 will be converted to "one hundred twenty three".
 * Supports numbers up to the millions.
 */
public class Converter {

    static final Map<Integer, String> segments = new HashMap<>();
    static final Map<String, String> ones = new HashMap<>();
    static final Map<String, String> onesTens = new HashMap<>();
    static final Map<String, String> tens = new HashMap<>();

    static final int SEGMENT_SIZE = 3;

    static {
        segments.put(0, "");
        segments.put(1, "thousand");
        segments.put(2, "million");

        ones.put("0", "");
        ones.put("1", "one");
        ones.put("2", "two");
        ones.put("3", "three");
        ones.put("4", "four");
        ones.put("5", "five");
        ones.put("6", "six");
        ones.put("7", "seven");
        ones.put("8", "eight");
        ones.put("9", "nine");

        onesTens.put("10", "ten");
        onesTens.put("11", "eleven");
        onesTens.put("12", "twelve");
        onesTens.put("13", "thirteen");
        onesTens.put("14", "fourteen");
        onesTens.put("15", "fifteen");
        onesTens.put("16", "sixteen");
        onesTens.put("17", "seventeen");
        onesTens.put("18", "eighteen");
        onesTens.put("19", "nineteen");

        tens.put("2", "twenty");
        tens.put("3", "thirty");
        tens.put("4", "forty");
        tens.put("5", "fifty");
        tens.put("6", "sixty");
        tens.put("7", "seventy");
        tens.put("8", "eighty");
        tens.put("9", "ninety");
    }

    /**
     * Demonstrates the usage of the {@link #convert(int)} method with example inputs.
     * Prints the English word representation of several sample numbers to the console.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        final String result24 = convert(24);
        System.out.println(result24);

        final String result6 = convert(6);
        System.out.println(result6);

        final String result10 = convert(10);
        System.out.println(result10);

        final String result20 = convert(20);
        System.out.println(result20);

        final String result124 = convert(124);
        System.out.println(result124);

        final String result104 = convert(104);
        System.out.println(result104);

        final String result123013546 = convert(123_013_546);
        System.out.println(result123013546);
    }

    /**
     * Converts a non-negative integer to its English word representation.
     * For example, 123 is converted to "one hundred twenty three".
     * Handles 0 specifically by returning "zero".
     * The method processes the number in segments of three digits (hundreds, tens, ones),
     * converting each segment and then appending the appropriate scale word (e.g., "thousand", "million").
     *
     * @param input The non-negative integer to convert. Must be less than 2,147,483,647 (Integer.MAX_VALUE).
     *              Currently supports up to millions. Larger numbers might produce incorrect scale words.
     * @return The English word representation of the input integer.
     */
    public static String convert(int input) {
        if (input == 0) {
            return "zero";
        }

        String originalInputString = String.valueOf(input);
        int inputLength = originalInputString.length();
        int leadingZerosCount = (SEGMENT_SIZE - (inputLength % SEGMENT_SIZE)) % SEGMENT_SIZE;
        String paddedInputString = "0".repeat(leadingZerosCount) + originalInputString;

        final List<String> resultParts = new ArrayList<>();
        int numSegments = paddedInputString.length() / SEGMENT_SIZE;

        for (int segmentStartIndex = 0; segmentStartIndex < paddedInputString.length(); segmentStartIndex += SEGMENT_SIZE) {
            String hundredDigit = String.valueOf(paddedInputString.charAt(segmentStartIndex));
            String tenDigit = String.valueOf(paddedInputString.charAt(segmentStartIndex + 1));
            String oneDigit = String.valueOf(paddedInputString.charAt(segmentStartIndex + 2));

            String segmentWords = convertSegmentToString(hundredDigit, tenDigit, oneDigit);

            if (!segmentWords.isEmpty()) {
                resultParts.add(segmentWords);
                int segmentIndex = numSegments - (segmentStartIndex / SEGMENT_SIZE) - 1;
                if (segmentIndex > 0) {
                    resultParts.add(segments.get(segmentIndex));
                }
            }
        }
        return String.join(" ", resultParts).replaceAll("\\s+", " ").trim();
    }

    /*
     * Helper method to convert a three-digit segment (represented as character strings for hundred, ten, and one digits)
     * into its English word representation.
     * For example, if hundredDigitChar="1", tenDigitChar="2", oneDigitChar="3", it returns "one hundred twenty three".
     * If a segment is "000", it returns an empty string.
     * Handles tens and teens (10-19) correctly.
     */
    private static String convertSegmentToString(String hundredDigitChar, String tenDigitChar, String oneDigitChar) {
        List<String> parts = new ArrayList<>();

        if (!hundredDigitChar.equals("0")) {
            parts.add(ones.get(hundredDigitChar));
            parts.add("hundred");
        }

        if (tenDigitChar.equals("1")) {
            parts.add(onesTens.get(tenDigitChar + oneDigitChar));
        } else if (tenDigitChar.equals("0")) {
            if (!oneDigitChar.equals("0") || parts.isEmpty()) { // Add "zero" for "000" if parts is empty
                 String oneWord = ones.get(oneDigitChar);
                 if (!oneWord.isEmpty() || parts.isEmpty()) { // Avoid adding empty string unless it's "000"
                    parts.add(oneWord);
                 }
            }
        } else { // Tens digit is 2-9
            parts.add(tens.get(tenDigitChar));
            String oneWord = ones.get(oneDigitChar);
            if (!oneWord.isEmpty()) {
                parts.add(oneWord);
            }
        }
        
        parts.removeIf(String::isEmpty); // Clean up any empty strings added if ones.get("0") was involved.
                                         // This is important if "0" in ones map is ""

        return String.join(" ", parts);
    }
}

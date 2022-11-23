import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

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

    public static void main(String[] args) {
        {
            final String result = convert(24);
            System.out.println(result);
        }
        {
            final String result = convert(6);
            System.out.println(result);
        }
        {
            final String result = convert(10);
            System.out.println(result);
        }
        {
            final String result = convert(20);
            System.out.println(result);
        }
        {
            final String result = convert(124);
            System.out.println(result);
        }
        {
            final String result = convert(104);
            System.out.println(result);
        }
        {
            final String result = convert(123_013_546);
            System.out.println(result);
        }
    }

    public static String convert(int input) {
        final String inputAsString = String.valueOf(input);
        final List<String> subResults = new ArrayList<>();

        int segment = 0;
        for (int i = inputAsString.length() - 1; i >= 0; i -= SEGMENT_SIZE) {
            final StringBuilder subRes = new StringBuilder();
            final String hundred = inputAsString.length() > 2 ? String.valueOf(inputAsString.charAt(i - 2)) : "0";
            final String ten = inputAsString.length() > 1 ? String.valueOf(inputAsString.charAt(i - 1)) : "0";
            final String one = String.valueOf(inputAsString.charAt(i));

            if (!hundred.equals("0")) {
                subRes.append(ones.get(hundred)).append(" ").append("hundred");
                if (!ten.equals("0") || !one.equals("0")) {
                    subRes.append(" ");
                }
            }

            if (ten.equals("1")) {
                subRes.append(onesTens.get(ten + one));
            } else if (ten.equals("0")) {
                subRes.append(ones.get(one));
            } else {
                subRes.append(tens.get(ten)).append(" ").append(ones.get(one));
            }

            subRes.append(" ").append(segments.get(segment));

            segment++;

            subResults.add(subRes.toString());
        }

        Collections.reverse(subResults);

        return String.join(" ", subResults);
    }
}

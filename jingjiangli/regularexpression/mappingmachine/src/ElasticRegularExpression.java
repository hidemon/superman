/**
 * will be refracterd in the future
 */
package jingjiangli.regularexpression.mappingmachine.src;

import static java.lang.String.valueOf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijingjiang on 10/27/16 1:27 AM
 */

/**
 * This is an elastic regular expression library to simplify my work
 * to work with processed input raw string
 */
public class ElasticRegularExpression {

    private final Pattern pattern;

    /**
     * Use factory mode to create regular expression
     */
    public static class Builder {

        private StringBuilder prefixes = new StringBuilder();
        private StringBuilder source = new StringBuilder();
        private StringBuilder suffixes = new StringBuilder();
        private int modifiers = Pattern.MULTILINE;

        private static final Map<Character, Integer> SYMBOL_MAP = new HashMap<Character, Integer>() {{
            put('d', Pattern.UNIX_LINES);
            put('i', Pattern.CASE_INSENSITIVE);
            put('x', Pattern.COMMENTS);
            put('m', Pattern.MULTILINE);
            put('s', Pattern.DOTALL);
            put('u', Pattern.UNICODE_CASE);
            put('U', Pattern.UNICODE_CHARACTER_CLASS);
        }};

        /**
         * Package private.
         */
        Builder() {
        }

        /**
         * Escapes any non-word char with two backslashes
         * target for url
         *
         * @param pValue - the string for char escaping
         * @return sanitized string value
         */
        private String sanitize(final String pValue) {
            return pValue.replaceAll("[\\W]", "\\\\$0");
        }

        /**
         * Counts occurrences of some substring in whole string
         * to differentiate some complicated command
         *
         * @param where - where to find
         * @param what  - what needs to count matches
         * @return 0 if nothing found, count of occurrences instead
         */
        private int countOccurrencesOf(String where, String what) {
            return (where.length() - where.replace(what, "").length()) / what.length();
        }

        public ElasticRegularExpression build() {
            Pattern pattern = Pattern.compile(new StringBuilder(prefixes)
                    .append(source).append(suffixes).toString(), modifiers);
            return new ElasticRegularExpression(pattern);
        }

        /**
         * Basic method to Append literal expression
         * Everything added to the expression should go trough this method
         * Example:
         * regex().add("\n.*").build()
         *
         * @param pValue - literal expression, not sanitized
         * @return this builder
         */
        public Builder add(final String pValue) {
            this.source.append(pValue);
            return this;
        }

        /**
         * Append a regex from builder and wrap it with unnamed group (?: ... )
         *
         * @param regex - ElasticRegularExpression.Builder, that not changed
         * @return this builder
         */
        public Builder add(final Builder regex) {
            return this.group().add(regex.build().toString()).endGr();
        }

        /**
         * Enable or disable the expression to start at the beginning of the line
         *
         * @param pEnable - enables or disables the line starting
         * @return this builder
         */
        public Builder startOfLine(final boolean pEnable) {
            this.prefixes.append(pEnable ? "^" : "");
            if (!pEnable) {
                this.prefixes = new StringBuilder(this.prefixes.toString().replace("^", ""));
            }
            return this;
        }

        /**
         * Mark the expression to start at the beginning of the line
         * Same as startOfLine with default parameter
         *
         * @return this builder
         */
        public Builder startOfLine() {
            return startOfLine(true);
        }

        /**
         * Enable or disable the expression to end at the last character of the line
         *
         * @param pEnable - enables or disables the line ending
         * @return this builder
         */
        public Builder endOfLine(final boolean pEnable) {
            this.suffixes.append(pEnable ? "$" : "");
            if (!pEnable) {
                this.suffixes = new StringBuilder(this.suffixes.toString().replace("$", ""));
            }
            return this;
        }

        /**
         * Mark the expression to end at the last character of the line
         * Same as endOfLine with true arg
         *
         * @return this builder
         */
        public Builder endOfLine() {
            return endOfLine(true);
        }

        /**
         * Add a string to the expression
         *
         * @param pValue - the string to be looked for (sanitized)
         * @return this builder
         */
        public Builder then(final String pValue) {
            return this.add("(?:" + sanitize(pValue) + ")");
        }

        /**
         * Add a string to the expression
         * Syntax sugar for then find operation - use it in case:
         * regex().find("string") // when it goes first
         *
         * @param value - the string to be looked for (sanitized)
         * @return this builder
         */
        public Builder find(final String value) {
            return this.then(value);
        }

        /**
         * Add a string to the expression that might appear once (or not)
         * Example:
         * The following matches all strings that contain http:// or https://
         * ElasticRegularExpression regex = regex()
         * .find("http")
         * .maybe("s")
         * .then("://")
         * .anythingBut(" ").build();
         * regex.test("http://")    //true
         * regex.test("https://")   //true
         *
         * @param pValue - the string to be looked for
         * @return this builder
         */
        public Builder maybe(final String pValue) {
            return this.then(pValue).add("?");
        }

        /**
         * Add a regex to the expression that might appear once (or not)
         * Example:
         * The following matches all names that have a prefix or not.
         * ElasticRegularExpression.Builder commandPrefix = regex().oneOf("git fetch", "git fetus");
         * ElasticRegularExpression name = regex()
         *    .maybe(commandPrefix)
         *    .space()
         *    .zeroOrMore()
         *    .word()
         *    .oneOrMore()
         *    .build();
         * regex.test("git fetch anything you want")    //true
         * regex.test("git fetus anything you want")    //true
         *
         * @param regex - the string to be looked for
         * @return this builder
         */
        public Builder maybe(final Builder regex) {
            return this.group().add(regex).endGr().add("?");
        }

        /**
         * Add expression that matches anything (includes empty string)
         *
         * @return this builder
         */
        public Builder anything() {
            return this.add("(?:.*)");
        }

        /**
         * Add expression that matches anything, but not passed argument
         *
         * @param pValue - the string not to match
         * @return this builder
         */
        public Builder anythingBut(final String pValue) {
            return this.add("(?:[^" + sanitize(pValue) + "]*)");
        }

        /**
         * Add expression that matches something that might appear once (or more)
         *
         * @return this builder
         */
        public Builder something() {
            return this.add("(?:.+)");
        }

        public Builder somethingButNot(final String pValue) {
            return this.add("(?:[^" + sanitize(pValue) + "]+)");
        }

        /**
         * Add universal line break expression
         *
         * @return this builder
         */
        public Builder lineBreak() {
            return this.add("(?:\\n|(?:\\r\\n)|(?:\\r\\r))");
        }

        /**
         * Shortcut for lineBreak();
         *
         * @return this builder
         */
        public Builder br() {
            return this.lineBreak();
        }

        /**
         * Add expression to match a tab character ('\u0009')
         *
         * @return this builder
         */
        public Builder tab() {
            return this.add("(?:\\t)");
        }

        /**
         * Add word, same as [a-zA-Z_0-9]+
         *
         * @return this builder
         */
        public Builder word() {
            return this.add("(?:\\w+)");
        }


        /**
         * Add word character, same as [a-zA-Z_0-9]
         *
         * @return this builder
         */
        public Builder wordChar() {
            return this.add("(?:\\w)");
        }


        /**
         * Add non-word character: [^\w]
         *
         * @return this builder
         */
        public Builder nonWordChar() {
            return this.add("(?:\\W)");
        }

        /**
         * Add non-digit: [^0-9]
         *
         * @return this builder
         */
        public Builder nonDigit() {
            return this.add("(?:\\D)");
        }

        /**
         * Add same as [0-9]
         *
         * @return this builder
         */
        public Builder digit() {
            return this.add("(?:\\d)");
        }

        /**
         * Add whitespace character, same as [ \t\n\x0B\f\r]
         *
         * @return this builder
         */
        public Builder space() {
            return this.add("(?:\\s)");
        }

        /**
         * Add non-whitespace character: [^\s]
         *
         * @return this builder
         */
        public Builder nonSpace() {
            return this.add("(?:\\S)");
        }


        /**
         * The following method is used to translate
         * some complicated regular expression pattern
         *
         * @param pValue - CharSequence every char from can be matched
         * @return this builder
         */
        public Builder anyOf(final String pValue) {
            this.add("[" + sanitize(pValue) + "]");
            return this;
        }

        /**
         * Shortcut to anyof
         *
         * @param value - CharSequence every char from can be matched
         * @return this builder
         */
        public Builder any(final String value) {
            return this.anyOf(value);
        }

        /**
         * Add expression to match a range (or multiply ranges)
         * Usage: .range(from, to [, from, to ... ])
         * Example: The following matches a hexadecimal number:
         * regex().range( "0", "9", "a", "f") // produce [0-9a-f]
         *
         * @param pArgs - pairs for range
         * @return this builder
         */
        public Builder range(final String... pArgs) {
            StringBuilder value = new StringBuilder("[");
            for (int firstInPairPosition = 1; firstInPairPosition < pArgs.length; firstInPairPosition += 2) {
                String from = sanitize(pArgs[firstInPairPosition - 1]);
                String to = sanitize(pArgs[firstInPairPosition]);

                value.append(from).append("-").append(to);
            }
            value.append("]");

            return this.add(value.toString());
        }

        /**
         * Modifier enable on regular expression matching machine
         *
         * @param pModifier regular expression modifier
         * @return this builder
         */
        public Builder addModifier(final char pModifier) {
            if (SYMBOL_MAP.containsKey(pModifier)) {
                modifiers |= SYMBOL_MAP.get(pModifier);
            }

            return this;
        }

        /**
         * remove modifier from the regular expression
         *
         * @param pModifier regular expression modifier
         * @return this builder
         */
        public Builder removeModifier(final char pModifier) {
            if (SYMBOL_MAP.containsKey(pModifier)) {
                modifiers &= ~SYMBOL_MAP.get(pModifier);
            }

            return this;
        }

        /**
         * Using the previous helper function to mapping the case
         * Example:
         * // matches "a"
         * // matches "A"
         * regex().find("a").withAnyCase(true)
         *
         * @return this builder
         */
        public Builder withAnyCase(final boolean pEnable) {
            if (pEnable) {
                this.addModifier('i');
            } else {
                this.removeModifier('i');
            }
            return this;
        }

        /**
         * Turn ON matching with ignoring case
         * Example:
         * // matches "a"
         * // matches "A"
         * regex().find("a").withAnyCase()
         *
         * @return this builder
         */
        public Builder withAnyCase() {
            return withAnyCase(true);
        }

        public Builder searchOneLine(final boolean pEnable) {
            if (pEnable) {
                this.removeModifier('m');
            } else {
                this.addModifier('m');
            }
            return this;
        }

        /**
         * Convenient method to show that string usage count is exact count, range count or simply one or more
         * Usage:
         * regex().multiply("abc")                                  // Produce (?:abc)+
         * regex().multiply("abc", null)                            // Produce (?:abc)+
         * regex().multiply("abc", (int)from)                       // Produce (?:abc){from}
         * regex().multiply("abc", (int)from, (int)to)              // Produce (?:abc){from, to}
         * regex().multiply("abc", (int)from, (int)to, (int)...)    // Produce (?:abc)+
         *
         * @param pValue - the string to be looked for
         * @param count  - (optional) if passed one or two numbers, it used to show count or range count
         * @return this builder
         */
        public Builder multiple(final String pValue, final int... count) {
            if (count == null) {
                return this.then(pValue).oneOrMore();
            }
            switch (count.length) {
                case 1:
                    return this.then(pValue).count(count[0]);
                case 2:
                    return this.then(pValue).count(count[0], count[1]);
                default:
                    return this.then(pValue).oneOrMore();
            }
        }

        /**
         * Adds "+" char to regexp
         * Same effect as atLeast(int) with "1" argument
         * Also, used by multiple(String, int...) when second argument is null, or have length more than 2
         *
         * @return this builder
         */
        public Builder oneOrMore() {
            return this.add("+");
        }

        /**
         * Adds "*" char to regexp, means zero or more times repeated
         * Same effect as atLeast(int) with "0" argument
         *
         * @return this builder
         */
        public Builder zeroOrMore() {
            return this.add("*");
        }

        /**
         * Add count of previous group
         * for example:
         * .find("w").count(3) // produce - (?:w){3}
         *
         * @param count - number of occurrences of previous group in expression
         * @return this Builder
         */
        public Builder count(final int count) {
            this.source.append("{").append(count).append("}");
            return this;
        }

        /**
         * Produce range count
         * for example:
         * .find("w").count(1, 3) // produce (?:w){1,3}
         *
         * @param from - minimal number of occurrences
         * @param to   - max number of occurrences
         * @return this Builder
         */
        public Builder count(final int from, final int to) {
            this.source.append("{").append(from).append(",").append(to).append("}");
            return this;
        }

        /**
         * Produce range count with only minimal number of occurrences
         * for example:
         * .find("w").atLeast(1) // produce (?:w){1,}
         *
         * @param from - minimal number of occurrences
         * @return this Builder
         * @see #count(int)
         * @see #oneOrMore()
         * @see #zeroOrMore()
         */
        public Builder atLeast(final int from) {
            return this.add("{").add(valueOf(from)).add(",}");
        }

        /**
         * Add a alternative expression to be matched
         *
         * @param pValue - the string to be looked for
         * @return this builder
         */
        public Builder or(final String pValue) {
            this.prefixes.append("(?:");

            int opened = countOccurrencesOf(this.prefixes.toString(), "(");
            int closed = countOccurrencesOf(this.suffixes.toString(), ")");

            if (opened >= closed) {
                this.suffixes = new StringBuilder(")" + this.suffixes.toString());
            }

            this.add(")|(?:");
            if (pValue != null) {
                this.then(pValue);
            }
            return this;
        }

        /**
         * Adds an alternative expression to be matched
         * based on an array of values
         *
         * @param pValues - the strings to be looked for
         * @return this builder
         */
        public Builder oneOf(final String... pValues) {
            if(pValues != null && pValues.length > 0) {
            this.add("(?:");
            for(int i = 0; i < pValues.length; i++) {
                String value = pValues[i];
                this.add("(?:");
                this.add(value);
                this.add(")");
                if(i < pValues.length - 1) {
                    this.add("|");
                }
            }
            this.add(")");
            }
            return this;
        }

        /**
         * Adds capture - open brace to current position and closed to suffixes
         *
         * @return this builder
         */
        public Builder capture() {
            this.suffixes.append(")");
            return this.add("(");
        }

        /**
         * create shortcut
         *
         * @return this builder
         */
        public Builder capt() {
            return this.capture();
        }

        /**
         *
         * Design group behavior for regular expression
         * May be used to set count of duplicated captures, without creating a new saved capture
         * Example:
         * // Without group() - count(2) applies only to second capture
         * regex().group()
         * .capt().range("0", "1").endCapt().tab()
         * .capt().digit().count(5).endCapt()
         * .endGr().count(2);
         *
         * @return this builder
         */
        public Builder group() {
            this.suffixes.append(")");
            return this.add("(?:");
        }


        /**
         * Close brace for previous capture and remove last closed brace from suffixes
         * Can be used to continue build regex after capture or to add
         * multiply captures
         *
         * @return this builder
         */
        public Builder endCapture() {
            if (this.suffixes.indexOf(")") != -1) {
                this.suffixes.setLength(suffixes.length() - 1);
                return this.add(")");
            } else {
                throw new IllegalStateException("Can't end capture (group) when it not started");
            }
        }


        /**
         * Shortcut for endCapture()
         *
         * @return this builder
         */
        public Builder endCapt() {
            return this.endCapture();
        }


        /**
         * Closes current unnamed and unmatching group
         * Example:
         * regex().group().maybe("word").count(2).endGr()
         *
         * @return this builder
         */
        public Builder endGr() {
            return this.endCapture();
        }
    }


    /**
     * @param pattern - java.util.regex.Pattern that constructed by builder
     */
    private ElasticRegularExpression(final Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Test that full string matches regular expression
     *
     * @param pToTest - string to check match
     * @return true if matches exact string, false otherwise
     */
    public boolean testExact(final String pToTest) {
        boolean ret = false;
        if (pToTest != null) {
            ret = pattern.matcher(pToTest).matches();
        }
        return ret;
    }

    /**
     * Test that full string contains regex
     *
     * @param pToTest - string to check match
     * @return true if string contains regex, false otherwise
     */
    public boolean test(final String pToTest) {
        boolean ret = false;
        if (pToTest != null) {
            ret = pattern.matcher(pToTest).find();
        }
        return ret;
    }

    /**
     * Extract full string that matches regex
     *
     * @param toTest - string to extract from
     * @return group 0, extracted from text
     */
    public String getText(final String toTest) {
        return getText(toTest, 0);
    }

    /**
     * Extract exact group from string
     *
     * @param toTest - string to extract from
     * @param group  - group to extract
     * @return extracted group
     */
    public String getText(final String toTest, final int group) {
        Matcher m = pattern.matcher(toTest);
        StringBuilder result = new StringBuilder();
        while (m.find()) {
            result.append(m.group(group));
        }
        return result.toString();
    }

    /**
     * Extract exact group from string and add it to list
     * Simplify keyword extraction process
     *
     * Example:
     * String text = "clone/ git clone a repo lijingjiang";
     * ElasticRegularExpression regex = regex().capt().oneOf("clone", "git clone").endCapt().maybe("lijingjiang.git").build();
     * list = regex.getTextGroups(text, 0) //result: "clone", "lijingjiang.git"
     * list = regex.getTextGroups(text, 1) //result: "git clone", "lijingjiang"
     *
     * @param toTest - string to extract from
     * @param group  - group to extract
     * @return list of extracted groups
     */
    public List<String> getTextGroups(final String toTest, final int group) {
        List<String> groups = new ArrayList<>();
        Matcher m = pattern.matcher(toTest);
        while (m.find()) {
            groups.add(m.group(group));
        }
        return groups;
    }

    /**
     * generate raw regular expression string
     */
    @Override
    public String toString() {
        return pattern.pattern();
    }

    /**
     * Creates new instance of ElasticRegularExpression builder from cloned builder
     *
     * @param pBuilder - instance to clone
     * @return new ElasticRegularExpression.Builder copied from passed
     */
    public static Builder regex(final Builder pBuilder) {
        Builder builder = new Builder();

        //Using created StringBuilder
        builder.prefixes.append(pBuilder.prefixes);
        builder.source.append(pBuilder.source);
        builder.suffixes.append(pBuilder.suffixes);
        builder.modifiers = pBuilder.modifiers;

        // using jdk8 stream design mode
        return builder;
    }

    /**
     * Creates new instance of ElasticRegularExpression builder
     *
     * @return new ElasticRegularExpression.Builder
     */
    public static Builder regex() {
        return new Builder();
    }
}

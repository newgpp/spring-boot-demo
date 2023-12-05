package com.felix;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    public static int runTest(String regex, String text) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int matches = 0;
        while (matcher.find()) {
            matches++;
        }
        return matches;
    }

    /**
     * . 匹配任意字符
     */
    @Test
    public void meta_characters_match_any(){
        int matches = runTest(".", "foo");
        Assert.assertEquals(3, matches);
    }

    @Test
    public void meta_characters_match_any_01(){
        int matches = runTest("foo.", "foofoo");
        Assert.assertEquals(1, matches);
    }

    @Test
    public void or_match(){
        int matches = runTest("[abc]", "b");
        Assert.assertEquals(1, matches);
    }

    @Test
    public void or_match_three_times(){
        int matches = runTest("[abc]", "cab");
        Assert.assertEquals(3, matches);
    }

    @Test
    public void or_match_three_times_01(){
        int matches = runTest("[bcr]at", "bat cat rat");
        Assert.assertEquals(3, matches);
    }

    @Test
    public void nor_match_01(){
        int matches = runTest("[^abc]", "g");
        Assert.assertEquals(1, matches);
    }
    @Test
    public void nor_match_02(){
        int matches = runTest("[^bcr]at", "sat mat eat");
        Assert.assertEquals(3, matches);
    }

    @Test
    public void range_match_01(){
        int matches = runTest("[A-Z]", "Two Uppercase");
        Assert.assertEquals(2, matches);
    }

    @Test
    public void range_match_02(){
        int matches = runTest("[a-z]", "Two Uppercase");
        Assert.assertEquals(10, matches);
    }

    @Test
    public void range_match_03(){
        int matches = runTest("[A-Za-z]", "Two Uppercase");
        Assert.assertEquals(12, matches);
    }

    @Test
    public void number_range_match_01(){
        int matches = runTest("[1-5]", "Two Uppercase alphabets 34 overall");
        Assert.assertEquals(2, matches);
    }

    @Test
    public void number_range_match_02(){
        int matches = runTest("3[0-5]", "Two Uppercase alphabets 34 overall");
        Assert.assertEquals(1, matches);
    }

    @Test
    public void number_range_match_03(){
        int matches = runTest("[1-3[7-9]]", "123456789");
        Assert.assertEquals(6, matches);
    }

    @Test
    public void number_range_match_04(){
        int matches = runTest("[1-6&&[3-9]]", "123456789");
        Assert.assertEquals(4, matches);
    }

    @Test
    public void number_range_match_05(){
        int matches = runTest("[1-6&&[^2468]]", "123456789");
        Assert.assertEquals(3, matches);
    }

    /**
     * \d equal to [0-9]
     */
    @Test
    public void pre_defined_number_01(){
        int matches = runTest("\\d", "123");
        Assert.assertEquals(3, matches);
    }

    /**
     * \D equal to [^0-9]
     */
    @Test
    public void pre_defined_number_02(){
        int matches = runTest("\\D", "123a");
        Assert.assertEquals(1, matches);
    }

    @Test
    public void pre_defined_white_space(){
        int matches = runTest("\\s", "a c");
        Assert.assertEquals(1, matches);
    }

    @Test
    public void pre_defined_none_white_space(){
        int matches = runTest("\\S", "a c");
        Assert.assertEquals(2, matches);
    }

    /**
     * \w equal to [a-zA-Z_0-9]
     */
    @Test
    public void pre_defined_word_character(){
        int matches = runTest("\\w", "hello!");
        Assert.assertEquals(5, matches);
    }

    @Test
    public void pre_defined_none_word_character(){
        int matches = runTest("\\W", "hello!");
        Assert.assertEquals(1, matches);
    }


}

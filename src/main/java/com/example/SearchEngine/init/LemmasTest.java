package com.example.SearchEngine.init;


import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LemmasTest {
    public static void main(String[] args) {

        try( FileWriter writer = new FileWriter("example.txt")){
            Connection.Response connectorResp = Jsoup.connect("https://dombulgakova.ru")
                .ignoreHttpErrors(true)
                .userAgent("engineSearch/0.1")
                .referrer("https://www.google.com")
                .maxBodySize(0)
                .timeout(10000)
                .execute();
            String body = connectorResp.body();
            List<String> cyrillicWords = extractCyrillicWords(body);
            for (String word : cyrillicWords) {
                LuceneMorphology luceneMorph =
                        new RussianLuceneMorphology();

                writer.write(word + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static List<String> extractCyrillicWords(String text) {
        List<String> cyrillicWords = new ArrayList<>();

        Pattern pattern = Pattern.compile("[А-Яа-яЁё]+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            cyrillicWords.add(matcher.group());
        }

        return cyrillicWords;
    }
}

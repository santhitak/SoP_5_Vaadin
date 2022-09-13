package com.example.lab05serivevaadin;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher {

    @Autowired
    private RabbitTemplate rabbit;

    protected Word words;

    public WordPublisher() {
        words = new Word();
        words.badWords.add("fuck");
        words.badWords.add("olo");
        words.goodWords.add("happy");
        words.goodWords.add("enjoy");
        words.goodWords.add("like");
    }

    @RequestMapping("addBad/{s}")
    public ArrayList<String> addBadWord(@PathVariable("s") String s){
        words.badWords.add(s);
        return words.badWords;
    }

    @RequestMapping("delBad/{s}")
    public ArrayList<String> deleteBadWord(@PathVariable("s") String s){
        words.badWords.remove(s);
        return words.badWords;
    }

    @RequestMapping("addGood/{s}")
    public ArrayList<String> addGoodWord(@PathVariable("s") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }

    @RequestMapping("delGood/{s}")
    public ArrayList<String> delGoodWord(@PathVariable("s") String s){
        words.goodWords.remove(s);
        return words.goodWords;
    }

    @RequestMapping("/proof/{s}")
    public String proofSentence(@PathVariable("s") String s) {
        boolean bad = false;
        boolean good = false;

        for (String word : s.split(" ")) {
            good = words.goodWords.contains(word) || good;
            bad = words.badWords.contains(word) || bad;
        }

        if (bad && good) {
            rabbit.convertAndSend("Fanout", "", s);
            return "Found bad & good word";
        } else if (good) {
            rabbit.convertAndSend("Direct", "good", s);
            return "Found good word";
        } else if (bad) {
            rabbit.convertAndSend("Direct", "bad", s);
            return "Found bad word";
        }
        return null;
    }

}

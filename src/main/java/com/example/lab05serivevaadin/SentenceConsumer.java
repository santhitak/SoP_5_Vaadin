package com.example.lab05serivevaadin;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {
    protected Sentence sentences = new Sentence();

    @RabbitListener(queues = "GetQueue")
    public Sentence getSentences(){
        System.out.println(sentences);
        return sentences;
    }

    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s) {
        sentences.badSentences.add(s);
        System.out.println(sentences.badSentences);
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s) {
        sentences.goodSentences.add(s);
        System.out.println(sentences.goodSentences);
    }
}

package com.example.lab05serivevaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Route("")
public class WordsView extends HorizontalLayout {
    TextField wordField = new TextField("Add Word");
    ComboBox<String> goodBox = new ComboBox<>("Good Words");
    ComboBox<String> badBox = new ComboBox<>("Bad Words");

    TextField senField = new TextField("Add Sentence");
    TextArea goodSen = new TextArea("Good Sentences");
    TextArea badSen = new TextArea("Bad Sentences");

    public WordsView(){
        FormLayout wordSide = new FormLayout();
        FormLayout senSide = new FormLayout();

        Button addGood = new Button("Add Good Word");
        Button addBad = new Button("Add Bad Word");

        wordSide.add(wordField, addGood, addBad, goodBox, badBox);
        wordSide.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        Button addSen = new Button("Add Sentence");
        Button showSen = new Button("Show Sentences");

        senSide.add(senField, addSen, goodSen, badSen, showSen);
        senSide.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        add(wordSide, senSide);
        setSizeFull();

        addGood.addClickListener(e -> addWord(goodBox, "addGood"));

        addBad.addClickListener(e -> addWord(badBox, "addBad"));

        addSen.addClickListener(e -> {
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proof")
                    .body(Mono.just(senField.getValue()), String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            senField.setValue("");
        });

        showSen.addClickListener(e -> showSentences(goodSen, badSen));
    }

    public void addWord(ComboBox<String> display, String type){
        List out = WebClient.create()
                .post()
                .uri("http://localhost:8080/" + type + "/")
                .body(Mono.just(wordField.getValue()), String.class)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        display.setItems(out);
        wordField.setValue("");
    }

    public void showSentences(TextArea good, TextArea bad){
        Sentence out = WebClient.create()
                .get()
                .uri("http://localhost:8080/getSentence")
                .retrieve()
                .bodyToMono(Sentence.class)
                .block();

        good.setValue(out.goodSentences+"");
        bad.setValue(out.badSentences+"");
        senField.setValue("");
    }
}

package com.example.lab05serivevaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

@Route("")
public class WordsView extends HorizontalLayout {
    Word words = new Word();
    TextField wordField = new TextField("Add Word");
    ComboBox<String> goodBox = new ComboBox<>("Good Words");
    ComboBox<String> badBox = new ComboBox<>("Bad Words");
    TextArea goodSen = new TextArea("Good Sentences");
    TextArea badSen = new TextArea("Bad Sentences");

    public WordsView(){
        FormLayout wordSide = new FormLayout();
        FormLayout senSide = new FormLayout();

        Button addGood = new Button("Add Good Word");
        Button addBad = new Button("Add Bad Word");

        goodBox.setItems(words.goodWords);
        badBox.setItems(words.badWords);

        wordSide.add(wordField, addGood, addBad, goodBox, badBox);
        wordSide.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        TextField senField = new TextField("Add Word");
        Button addSen = new Button("Add Good Word");
        Button showSen = new Button("Show Sentences");

        senSide.add(senField, addSen, goodSen, badSen, showSen);
        senSide.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        add(wordSide, senSide);
        setSizeFull();

        addGood.addClickListener(e->{});
        addBad.addClickListener(e->{});
    }

    public void addWord(String s){
        String out = WebClient.create()
                .post()
                .uri("http://localhost:8080/"+ s + "/" + wordField.getValue() )
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}

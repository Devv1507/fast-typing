package com.example.mainClasses;

/**
 * Represents a word in the game.
 */
public class Word {
    private String text;

    public Word(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
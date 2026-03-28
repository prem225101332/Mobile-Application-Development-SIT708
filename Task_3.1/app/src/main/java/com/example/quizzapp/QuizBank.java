package com.example.quizzapp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizBank {

    public static List<Question> getQuestions() {
        List<Question> questions = Arrays.asList(
                new Question(
                        "Which fruit is yellow and loved by monkeys?",
                        Arrays.asList("Mango", "Banana", "Lemon", "Pineapple"),
                        1,
                        "🍌"
                ),
                new Question(
                        "Which vegetable is orange and loved by rabbits?",
                        Arrays.asList("Potato", "Tomato", "Carrot", "Beetroot"),
                        2,
                        "🥕"
                ),
                new Question(
                        "Which fruit is known as the king of fruits?",
                        Arrays.asList("Apple", "Mango", "Durian", "Jackfruit"),
                        1,
                        "🥭"
                ),
                new Question(
                        "Which green vegetable looks like a tiny tree?",
                        Arrays.asList("Celery", "Zucchini", "Broccoli", "Spinach"),
                        2,
                        "🥦"
                ),
                new Question(
                        "Which fruit has seeds on the outside?",
                        Arrays.asList("Watermelon", "Orange", "Strawberry", "Peach"),
                        2,   // Strawberry
                        "🍓"
                ),
                new Question(
                        "Which vegetable makes your eyes water when cut?",
                        Arrays.asList("Garlic", "Onion", "Ginger", "Chilli"),
                        1,
                        "🧅"
                ),
                new Question(
                        "Which fruit is technically a vegetable?",
                        Arrays.asList("Carrot", "Potato", "Tomato", "Onion"),
                        2,
                        "🍅"
                ),
                new Question(
                        "Which purple vegetable is used to make babaganoush?",
                        Arrays.asList("Purple Cabbage", "Beetroot", "Eggplant", "Red Onion"),
                        2,
                        "🍆"
                ),
                new Question(
                        "Which fruit grows on a cactus?",
                        Arrays.asList("Kiwi", "Lychee", "Dragon Fruit", "Guava"),
                        2,
                        "🌵"
                ),
                new Question(
                        "What colour is a ripe banana?",
                        Arrays.asList("Red", "Green", "Purple", "Yellow"),
                        3,
                        "🍌"
                )
        );
        Collections.shuffle(questions);
        return questions;
    }
}
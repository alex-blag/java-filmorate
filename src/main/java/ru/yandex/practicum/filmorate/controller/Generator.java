package ru.yandex.practicum.filmorate.controller;

class Generator {

    private static int id = 1;

    private Generator() {
    }

    static int getId() {
        return id++;
    }

}

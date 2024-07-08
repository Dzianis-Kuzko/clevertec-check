package ru.clevertec.check;

import ru.clevertec.check.controller.console.ConsoleController;

public class CheckRunner {
    public static void main(String[] args) {
        ConsoleController checkController = new ConsoleController();
        checkController.processCheck(args);
    }
}

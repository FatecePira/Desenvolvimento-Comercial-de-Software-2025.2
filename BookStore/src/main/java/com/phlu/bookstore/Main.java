package com.phlu.bookstore;

import com.phlu.bookstore.controller.SistemaController;
import com.phlu.bookstore.view.ConsoleView;

/**
 * Classe principal do sistema
 */
public class Main {

    public static void main(String[] args) {
        // Cria a view (interface de console)
        ConsoleView view = new ConsoleView();

        // Cria o controller, passando a view
        SistemaController controller = new SistemaController(view);

        // Inicia o sistema
        controller.iniciar();
    }
}

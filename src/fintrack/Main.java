package fintrack;

import fintrack.controller.FinanceiroController;
import fintrack.view.ConsoleView;

public class Main {

    public static void main(String[] args) {
        FinanceiroController controller = new FinanceiroController();
        ConsoleView view = new ConsoleView(controller);
        view.iniciar();
    }
}

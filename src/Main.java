import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String printMessage = "print test ####";
        PrintCommand printCommand = new PrintCommand(printMessage);

        Dog baduk = new Dog();
        ArrayList<DogAction> dogActions = new ArrayList<>();
        dogActions.add(DogAction.SIT);
        dogActions.add(DogAction.STAY);
        dogActions.add(DogAction.SIT);

        DogCommand dogCommand = new DogCommand(baduk, dogActions);

        Invoker invoker = new Invoker();
        invoker.addCommand(printCommand);
        invoker.addCommand(dogCommand);

        invoker.runCommands();
    }
}

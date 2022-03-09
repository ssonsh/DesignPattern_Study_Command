public class PrintCommand implements Command{

    private final String printMessage;

    public PrintCommand(String printMessage){
        this.printMessage = printMessage;
    }

    @Override
    public void execute() {
        System.out.println("[PrintCommand] " + printMessage);
    }
}

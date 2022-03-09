import java.util.List;

public class DogCommand implements Command{
    private final Dog dog;
    private final List<DogAction> dogActions;

    public DogCommand(Dog dog, List<DogAction> dogActions) {
        this.dog = dog;
        this.dogActions = dogActions;
    }

    @Override
    public void execute() {
        for (DogAction dogAction : dogActions) {
            if(dogAction == DogAction.SIT){
                dog.sit();
            }else if(dogAction == DogAction.STAY){
                dog.stay();
            }
        }
    }
}

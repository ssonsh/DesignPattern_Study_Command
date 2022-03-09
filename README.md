# DesignPattern_Study_Command

# Notion Link
https://five-cosmos-fb9.notion.site/Command-94dd9b87fcda4ede81f56f81dfc2e034

# 명령 패턴 (Command)

### 의도

요청 자체를 캡슐화하는 것.

이를 통해 요청이 서로 다른 사용자를 매개변수로 만들고, 요청을 대기시키거나 로깅하며

되돌릴 수 있는 연산을 지원한다.

<aside>
🎈 다른이름 : 작동(Action), 트랜잭션(Transaction)

</aside>

### Command Pattern이란

***여러 명령들을 추상화 해서 클래스로 정의해서 오브젝트로 만들어 사용하는 패턴이다.***

<aside>
🎈 명령을 추상화 해서 객체로 다룬다!!! 끝.

</aside>

- 명령들이란 ? 간단한 명령, 다른 오브젝트의 Action을 발생시키는 명령이 될 수 있다.
- 명령을 **추상화**시키는 이유?
    - 명령들을 오브젝트처럼 관리할 수 있고
    - 오브젝트들을 모아두었다가 정해진 시간에 실행시키는 동작들을 할 수 있다.

**간단히 본다면?**

- **System.out.println(”..”)** 을 **PrintCommand.execute()** 처럼 !

![image](https://user-images.githubusercontent.com/18654358/157388015-75f35176-6cea-4733-92f7-531dd4174269.png)

[Command.java](http://Command.java) (interface)

```java
public interface Command {
    void execute();
}
```

[PrintCommand.java](http://PrintCommand.java) (implements Command)

```java
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
```

테스트

```java
public class Main {
    public static void main(String[] args) {
        String printMessage = "print test ####";

        // 직접 프린트
        System.out.println(printMessage);

        // PrintCommand 객체 이용하여 프린트
        PrintCommand printCommand = new PrintCommand(printMessage);
        printCommand.execute();

    }
}
```

```java
print test ####
[PrintCommand] print test ####
```

**Command를 하나 더 만들어볼까?**

![image](https://user-images.githubusercontent.com/18654358/157388056-5951bf4c-1d61-45c0-a27f-6a288627744c.png)

**Dog.java**

```java
public class Dog {
    public void sit(){
        System.out.println("The dog sat down");
    }

    public void stay(){
        System.out.println("The dog is staying");
    }
}
```

**DogAction.java**

```java
public enum DogAction {
    SIT,
    STAY
}
```

**DogCommand.java**

- Receiver인 Dog를 주입받고, 수행할 명령 리스트를 인자로 전달받는다.
- 전달받은 명령 리스트를 loop를 돌면서 해당하는 Action에 대한 행위를 Dog가 수행하도록 한다.

```java
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
```

테스트

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Dog baduk = new Dog();
        ArrayList<DogAction> dogActions = new ArrayList<>();
        dogActions.add(DogAction.SIT);
        dogActions.add(DogAction.STAY);
        dogActions.add(DogAction.SIT);

        DogCommand dogCommand = new DogCommand(baduk, dogActions);
        dogCommand.execute();;
    }
}
```

```java
The dog sat down
The dog is staying
The dog sat down
```

**Invoker 활용**

- 명령들을 Collection 형태로 가지고 있고
- 특정 명령(runCommands)을 통해 가지고 있는 모든 Command를 수행시킬 수 있다.

```java
import java.util.ArrayList;
import java.util.List;

public class Invoker {
    private List<Command> commands = new ArrayList<>();

    public void addCommand(Command command){
        this.commands.add(command);
    }

    public void runCommands(){
        for (Command command : commands) {
            command.execute();
        }
    }
}
```

위에서 만든 Invoker를 이용해 PrintCommand와 DogCommand를 함께 수행시켜볼까?

```java
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
```

```java
[PrintCommand] print test ####
The dog sat down
The dog is staying
The dog sat down
```

<aside>
🎈 이렇게 각 ConcreteCommand는 Command 인터페이스를 구현한 구현체임으로 
Invoker는 다형성을 통해 Command를 같은 녀석으로 간주하여 처리해낼 수 있다.

</aside>

---

### 확인해보고 싶은 부분

Spring-data-commons → **EntityCallbackInvoker.java**

- 

---

### 활용성

- 수행할 동작을 객체로 매개변수화하고자 할 때. 절차지향 프로그램에서는 이를 콜백(callback) 함수, 즉 어딘가 등록되었다가 나중에 호출되는 함수를 사용해서 이러한 매개변수화를 표현할 수 있다.
    - 명령 패턴은 콜백을 객체지향 방식으로 나타낸 것이다.
- 서로 다른 시간에 요청을 명시하고, 저장하며, 실행하고 싶을 때 Command 객체는 원래의 요청과 다른 생명주기(liftetime)가 있다. 요청을 받아 처리하는 객체가 주소 지정 방식과는 독립적으로 표현될 수 있다면, Command 객체를 다른 프로세스에게 넘겨주고 거기서 해당 처리를 진행할 수 있다.
- 실행 취소 기능을 지원하고 싶을 때. Command의 Execute() 연산은 상태를 저장할 수 있는데, 이를 이용해 지금까지 얻은 겨로가를 바꿀 수 있다.
    - 이를 위해 Unexecute() 연산을 Command 클래스의 인터페이스에 추가한다.
    - 실행된 명렁어를 모두 기록해 두었다가 이 리스트를 역으로 탐색해서 다시 Unexecute()를 수행하게 한다.
    - Execute()와 Unexecute() 연산의 반복 사용을 통해 수행과 취소를 무한 반복할 수 있다.
- 시스템이 고장났을 때 재적용이 가능하도록 변경 과정에 대한 로깅을 지원하고 싶을 때.
    - Command 인터페이스를 확장해서 load()와 store() 연산를 정의하면 상태의 변화를 지속적(persistent) 저장소에 저장해 둘 수 있다.
    - 시스템 장애가 발생했을 때 해당 저장소에서 저장된 명령을 다시 읽어 Execute() 연산을 통해 재실행 할 수 있다.
    
    <aside>
    🎈 이것은 JPA의 영속성 컨텍스트의 역할인것 같다!?
    
    </aside>
    
- 기본적인 연산의 조합으로 만든 상위 수준 연산을 써서 시스템을 구조화하고 싶을 때
    - 정보 시스템의 일반적인 특성은 **트랜잭션(transaction)**을 처리해야 한다는 것이다.
    - Command 패턴은 이런 트랜잭션의 모델링을 가능하게 한다.
    - Command 클래스는 일관된 인터페이스를 정의하는데, 이로써 모든 트랜잭션이 동일한 방식으로 호출될 수 있다.
    - 새로운 트랜잭션을 만들면 상속으로 Command 클래스를 확장하면 되므로 시스템 확장도 용이하다.

### 구조

![image](https://user-images.githubusercontent.com/18654358/157388097-dfb4bc01-9319-47e5-bcab-ad38eae89625.png)

### 참여자

**Command**

- 연산 수행에 필요한 인터페이스를 선언

**ConcreteCommand**

- Receiver 객체와 액션 간의 연결성을 정의
- 또한 처리 객체에 정의된 연산을 호출하도록 Execute를 구현한다.

**Client**

- ConcreteCommand 객체를 생성하고 처리 객체로 정의

**Invoker**

- 명령어에 처리를 수행할 것을 요청

**Receiver**

- 요청에 관련한 연산 수행 방법을 알고 있다.
- 어떤 클래스도 요청 수신자로서 동작할 수 있다.

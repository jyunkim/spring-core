package jihyun.core.singleton;

public class SingletonService {

    // 하나의 객체를 공유하기 위해 static 선언
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    // 외부에서 객체를 생성하지 못하게 하도록 private 사용
    private SingletonService() {}
}

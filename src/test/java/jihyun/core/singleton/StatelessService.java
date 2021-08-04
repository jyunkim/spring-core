package jihyun.core.singleton;

public class StatelessService {

    // 공유 필드 제거
    public int order(String name, int price) {
        System.out.println("name = " + name + "price = " + price);
        return price;
    }
}

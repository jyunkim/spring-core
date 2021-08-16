package jihyun.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("disconnect: " + url);
    }

    // 의존관계 주입이 끝나면 실행됨
    @PostConstruct
    public void init() {
        // 초기화
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 소멸될 때 실행됨
    @PreDestroy
    public void close() {
        // 종료
        disconnect();
    }
}

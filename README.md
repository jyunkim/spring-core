# Spring Core
## 1. 객체지향 설계와 스프링
### 스프링이란?
Spring이란 Spring Framework, Spring Boot, Spring Data 등 여러 프로젝트들의 모음

Spring Framework는 객체 지향의 특징을 잘 활용할 수 있게 해주며, 
개발자들이 핵심 비즈니스 로직 구현에만 집중할 수 있게 해주는 프레임워크
- 핵심 기술: 스프링 DI 컨테이너, AOP
- 웹 기술: 스프링 MVC
- 데이터 접근 기술: JDBC, ORM 지원
- 테스트: 스프링 기반 테스트 지원

초기 Spring은 설정이 어렵다는 문제점 보유 -> Spring Boot 탄생   
Spring Boot는 독립적으로 실행할 수 있는 Spring 애플리케이션을 쉽게 만들 수 있게 해줌   
- 의존성 관리(spring-boot-starter) - 라이브러리간의 의존성과 버전 자동 설정
- 자동 설정(@EnableAutoConfiguration) - 스프링 빈 자동 등록, DB 설정
- 내장 WAS(Tomcat) - 별도의 WAS 설치 및 설정 필요 x

스프링은 자바, 즉 객체 지향 언어 기반의 프레임워크로, 좋은 객체 지향 애플리케이션을 개발할 수 있게 해주는 프레임워크

**객체 지향 프로그래밍**   
프로그램을 객체들의 모임으로 파악   
유연하고 변경에 용이 <= 다형성

**다형성**   
역할과 구현을 분리   
클라이언트는 대상의 역할만 알면 됨(구현 대상의 내부 구조는 몰라도 됨)   
클라이언트는 구현 대상의 내부 구조가 변경되거나, 구현 대상 자체가 변경되어도 영향을 받지 않음   
역할 = 인터페이스, 구현 = 구현 클래스   
인터페이스를 구현한 객체를 실행 시점에 유연하게 변경할 수 있음   
-> 클라이언트를 변경하지 않고 서버의 구현 기능을 변경할 수 있다

인터페이스를 남발하면 코드 파악이 어려움   
-> 기능을 확장할 가능성이 없다면 구체 클래스 사용

**SOLID**   
좋은 객체 지향 설계의 5가지 원칙   
1. SRP: 단일 책임 원칙(Single Responsibility principle)   
   하나의 클래스는 하나의 책임만 가져야 함   
   -> 변경이 있을 때 파급 효과가 적어야 한다
   
2. OCP: 개방 폐쇄의 원칙(Open-Closed Principle)   
   소프트웨어 요소는 확장에는 열려 있으나, 변경에는 닫혀 있어야 함   
   다형성 활용 -> 구현 클래스를 추가   
   But, 클라이언트가 구현 클래스를 직접 선택해야 함
   -> 구현 객체를 변경하려면 클라이언트 코드를 변경해야 함
   -> OCP 위반
   
3. LSP: 리스코프 치환 원칙(Liskov Substitution Principle)   
   구현 클래스는 인터페이스 규약을 모두 지켜야 함
   
4. ISP: 인터페이스 분리 원칙(Interface Segregation Principle)   
   하나의 범용 인터페이스보다 특정 클라이언트를 위한 인터페이스를 여러 개로 분리하는 것이 좋음
   
5. DIP: 의존관계 역전 원칙(Dependency Inversion Principle)   
   구체화가 아닌 추상화에 의존해야 함   
   -> 클라이언트가 구현 클래스가 아닌 인터페이스에 의존하게 해야 한다   
   다형성 활용   
   But, 클라이언트가 구현 클래스를 직접 선택해야 함
   -> 클라이언트가 인터페이스뿐만 아니라 구현 클래스에도 의존
   -> DIP 위반
   
=> 다형성만으로는 모든 원칙을 지킬 수 없다   
-> 스프링의 DI 컨테이너 이용    
-> 클라이언트의 코드 변경 없이 기능 확장 가능

## 비즈니스 요구사항과 설계
도메인 협력 관계도
![캡처](https://user-images.githubusercontent.com/68456385/126960745-d4e05bab-69ff-4262-8990-1ac3711e6625.PNG)

클래스 다이어그램
![캡처](https://user-images.githubusercontent.com/68456385/126960860-8d08360d-cd7c-45f0-8144-1a0015471878.PNG)

객체 다이어그램
![캡처](https://user-images.githubusercontent.com/68456385/126960942-92508a79-9951-4577-8908-107391c4160e.PNG)

역할과 구현의 분리, 단일 책임 원칙을 잘 지킨 설계
![캡처](https://user-images.githubusercontent.com/68456385/127164269-a4e2c28b-a0be-48bf-a996-a48c314dae72.PNG)

```java
public class MemberServiceImpl implements MemberService {

   private final MemberRepository memberRepository = new MemoryMemberRepository();
   
}
```
구현 클래스를 변경하려면 클라이언트(service) 코드를 변경해야 함   
클라이언트(service)가 인터페이스뿐만 아니라 구현 클래스에도 의존   
=> OCP, DIP 위반   
-> 인터페이스에만 의존하도록 해볼까?
```java
public class MemberServiceImpl implements MemberService {

   private MemberRepository memberRepository;
   
}
```
=> 구현체가 없기 때문에 실행 불가   
-> 다른 누군가가 클라이언트에 구현 객체를 대신 생성하고 주입해주어야 함

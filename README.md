# Spring Core
출처: https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/

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

## 2. 비즈니스 요구사항과 설계
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

## 3. 객체 지향 원리 적용
### 관심사 분리
애플리케이션의 전체 동작 방식을 구성하기 위해 구현 객체를 생성하고, 연결해주는 설정 클래스를 별도로 만듬

**AppConfig**   
애플리케이션 실제 동작에 필요한 구현 객체를 생성   
생성한 객체 인스턴스의 참조를 생성자를 통해 주입(의존성 주입)
```java
public class AppConfig {

   public MemberService memberService() {
      return new MemberServiceImpl(new MemoryMemberRepository());
   }
}
```
```java
public class MemberServiceImpl implements MemberService {

//   private final MemberRepository memberRepository = new MemoryMemberRepository();
   private final MemberRepository memberRepository;

   public MemberServiceImpl(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
   }
}
```

![캡처](https://user-images.githubusercontent.com/68456385/127280431-1f7aa10a-2c10-47bf-9555-eab93598cc38.PNG)
![캡처](https://user-images.githubusercontent.com/68456385/127288612-7b96cd9b-f7ec-43c8-99f6-353b76d7c541.PNG)

MemberServiceImpl(클라이언트)가 인터페이스에만 의존   
=> DIP 만족

생성자를 통해 어떤 구현 객체가 들어올지는 외부에서 결정   
객체를 생성하고 연결하는 역할과, 기능을 실행하는 역할이 명확히 분리   
-> 클라이언트가 의존관계에 대한 고민은 외부에 맡기고 실행에만 집중 가능
=> SRP를 더 잘 지킬 수 있음

구현 클래스를 변경해도 사용 영역에 있는 클라이언트 코드를 변경할 필요 없이,
구성 영역에 있는 AppConfig만 수정하면 됨   
=> OCP 만족

AppConfig 리팩토링
```java
public class AppConfig {

   public MemberService memberService() {
      return new MemberServiceImpl(memberRepository());
   }

   private MemberRepository memberRepository() {
      return new MemoryMemberRepository();
   }

   public OrderService orderService() {
      return new OrderServiceImpl(memberRepository(), discountPolicy());
   }

   private DiscountPolicy discountPolicy() {
      return new FixedDiscountPolicy();
   }
}
```
중복 제거, 개별 클래스의 역할과 구현 쉽게 파악 가능

### IOC(Inversion of Control)
제어의 역전   
프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것

기존 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성, 연결, 실행   
-> 구현 객체가 프로그램의 제어 흐름을 스스로 조종   
-> 개발자 입장에서 자연스러운 흐름

AppConfig가 등장한 이후에 구현 객체는 자신의 로직을 실행하는 역할만 담당   
클라이언트 구현 객체는 어떤 서버 구현 객체가 생성될지 모름    
-> 프로그램의 제어 흐름에 대한 권한은 AppConfig가 가져감

**프레임워크 vs 라이브러리**   
내가 작성한 코드를 제어하고, 대신 실행하면 프레임워크(ex. Junit)
내가 작성한 코드가 직접 제어의 흐름을 담당한다면 라이브러리

### DI(Dependency Injection)
의존관계 주입   

- 런타임에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결 되는 것   
- 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고, 클라이언트가 호출하는 대상의 타입 인스턴스를 변경할 수 있음   
- 의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고, 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있음

**정적인 클래스 의존관계**   
클래스가 사용하는 import 코드만 보고 의존관계 판단 가능   
정적인 의존관계는 애플리케이션을 실행하지 않아도 분석할 수 있음   

클래스 다이어그램
![캡처](https://user-images.githubusercontent.com/68456385/127444812-0c2d158c-962d-47fa-be41-8d281911e686.PNG)

**동적인 객체(인스턴스) 의존관계**   
애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존 관계

객체 다이어그램
![캡처](https://user-images.githubusercontent.com/68456385/127444970-bd6a3bb5-8b31-48f1-91e4-953ac698543c.PNG)

### DI 컨테이너
AppConfig와 같이 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것

### 스프링 적용
```java
@Configuration
public class AppConfig {
    
    // 반환된 객체가 스프링 컨테이너에 스프링 빈으로 등록됨
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
```
```java
public class OrderApp {

   public static void main(String[] args) {
      // 스프링 컨테이너, AppConfig를 설정 정보로 사용
      ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
      // param1 = 스프링 빈 이름(default = @Bean 메서드 이름), param2 = 스프링 빈의 클래스 타입
      MemberService memberService = ac.getBean("memberService", MemberService.class);
      OrderService orderService = ac.getBean("orderService", OrderService.class);
   }
}
```
- 기존에는 AppConfig 객체를 직접 생성하고 DI를 했지만, 이제부터는 스프링 컨테이너를 통해 DI 수행   
- ApplicationContext = 스프링 컨테이너   
- 스프링 컨테이너는 @Configuration이 붙은 클래스를 구성 정보로 사용   
- 구성 정보에서 @Bean이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록   
- 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 함   
- 스프링 빈은 기본값으로 @Bean이 붙은 메서드의 명을 스프링 빈의 이름으로 사용   
- 스프링 빈은 getBean 메서드를 사용해서 찾을 수 있음

## 4. 스프링 컨테이너와 스프링 빈
### 스프링 컨테이너 생성
ApplicationContext = 스프링 컨테이너(인터페이스)      
스프링 컨테이너 구현체는 어노테이션 기반의 자바 설정 클래스, XML 등으로 만들 수 있음
![image](https://user-images.githubusercontent.com/68456385/127629145-1e8673ae-2c22-4656-b237-4d1b9bb88602.png)

스프링 컨테이너 생성 과정
1. 스프링 컨테이너 생성
![image](https://user-images.githubusercontent.com/68456385/127610656-55127281-ae1c-43ee-b8ce-05e8bd7d94ae.png)


2. 스프링 빈 등록
![image](https://user-images.githubusercontent.com/68456385/127610876-4e4bef2f-6124-4784-8de5-c4a5699f60e7.png)
   빈 이름은 항상 다른 이름을 부여해야 함   
   같은 이름을 부여하면 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생   
   빈 이름 직접 부여 가능 - @Bean(name="memberService")
   

3. 스프링 빈 의존관계 설정
   ![image](https://user-images.githubusercontent.com/68456385/127611759-16de1fa1-9641-49d9-8ec0-897d9711c27e.png)
   스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)   
   원래 스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있음   
   그런데 이렇게 자바 코드로 스프링 빈을 등록하면, 생성자를 호출하면서 의존관계 주입도 한번에 처리됨
   
### 스프링 빈 조회
스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법
- ac.getBean(빈이름, 타입)
- ac.getBean(타입)   
  타입으로 조회시 같은 타입의 스프링 빈이 둘 이상이면 오류 발생

부모 타입으로 조회하면, 자식 타입도 함께 조회된다   
-> Object 타입으로 조회하면, 모든 스프링 빈을 조회

### BeanFactory와 ApplicationContext
![image](https://user-images.githubusercontent.com/68456385/127628131-ba2746c4-7d0e-423b-bbd6-7d5eed8dc03d.png)

**BeanFactory**   
스프링 컨테이너의 최상위 인터페이스   
스프링 빈을 관리하고 조회하는 역할을 담당   
지금까지 우리가 사용했던 대부분의 기능은 BeanFactory가 제공

**ApplicatonContext 부가 기능**
![image](https://user-images.githubusercontent.com/68456385/127628340-842f4b9e-f332-45b9-9b4a-97015e154929.png)
- 메시지소스를 활용한 국제화 기능   
-> 들어온 국가의 언어로 출력 
- 환경변수   
-> 로컬, 개발, 운영 환경 등으로 구분해서 처리
- 애플리케이션 이벤트   
-> 이벤트를 발행하고 구독하는 모델을 편리하게 지원
- 편리한 리소스 조회   
-> 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회
  
### 스프링 빈 설정 메타 정보
스프링은 어떻게 이런 다양한 설정 형식을 지원하는 것일까?   
-> BeanDefinition 추상화 이용   
-> 스프링 컨테이너는 BeanDefinition에만 의존
![image](https://user-images.githubusercontent.com/68456385/127630119-84df755c-bd81-4cb1-b1a9-b5bf64f51192.png)

BeanDefinition = 빈 설정 메타 정보   
빈 하나당 하나의 메타 정보 생성   
스프링 컨테이너는 이 메타 정보를 기반으로 스프링 빈 생성

AnnotationConfigApplicationContext가 AnnotatedBeanDefinitionReader를 사용해서
AppConfig.class를 읽고 BeanDefinition 생성

## 5. 싱글톤 컨테이너
웹 애플리케이션은 보통 여러 고객이 동시에 요청을 함   
스프링을 사용하지 않은 DI 컨테이너는 요청이 올 때마다 새로운 객체를 생성
-> 메모리 낭비

### 싱글톤 패턴
객체 인스턴스가 JVM 내 하나만 존재하도록 하는 패턴   
싱글톤 패턴을 적용하면 고객의 요청이 올 때 마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 사용할 수 있음   
But, 코드가 많이 들어가고, 의존관계상 클라이언트가 구체 클래스에 의존하기 때문에 DIP 위반   
내부 속성을 변경하거나 초기화 하기 어렵고, private 생성자로 자식 클래스를 만들기 어려움 -> 유연성이 떨어짐

### 싱글톤 컨테이너
스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스(스프링 빈)를 싱글톤으로 관리함
스프링 컨테이너는 싱글톤 컨테이너 역할을 하며, 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 함

![image](https://user-images.githubusercontent.com/68456385/128148853-a75fe714-64a8-40cb-b8e0-1bc56a901384.png)

### 싱글톤 방식의 주의점
싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에, 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안됨

-> 무상태(stateless)로 설계해야 함
- 특정 클라이언트에 의존적인 필드가 있으면 안된다.
- 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
- 가급적 읽기만 가능해야 한다.
- 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.

스프링 빈의 필드에 공유 값을 설정하면 매우 큰 장애 발생 가능

```java
public class StatefulService {

    // 상태를 유지하는 필드
    private int price;

    public void order(String name, int price) {
        System.out.println("name = " + name + "price = " + price);
        this.price = price; // 문제 발생
    }

    public int getPrice() {
        return price;
    }
}
```

```java
public class StatelessService {

    // 공유 필드 제거
    public int order(String name, int price) {
        System.out.println("name = " + name + "price = " + price);
        return price;
    }
}
```

### @Configuration
```java
@Configuration
public class AppConfig {
    
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
```
memberService, orderService 빈 생성 시 MemoryMemberRepository 객체가 각각 생성되기 때문에 싱글톤이 깨지는 것처럼 보임   
하지만 확인해보면 memberRepository 인스턴스는 모두 같은 인스턴스가 공유되어 사용됨   
어떻게 된 것일까?

@Bean 호출 로그 확인   
로직상으론 memberRepository() 메서드가 여러번 호출되어야 함   
-> But, 로그를 확인해보면 한번만 호출됨

AppConfig 스프링 빈을 조회해서 클래스 정보를 출력해보면 다음과 같이 나온다
(AnnotationConfigApplicationContext에 파라미터로 넘긴 값은 스프링 빈으로 등록되기 때문에 AppConfig도 스프링 빈이 됨)
> class hello.core.AppConfig$$EnhancerBySpringCGLIB$$bd479d70

만약 순수한 클래스라면 다음과 같이 출력되어야 함
> class hello.core.AppConfig

-> 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 
그 클래스를 스프링 빈으로 등록한 것   
그리고 그 클래스가 싱글톤이 되도록 보장해줌
![image](https://user-images.githubusercontent.com/68456385/128162799-19a990cf-5b7c-417b-9c46-61079429d24e.png)

AppConfig@CGLIB 예상 코드
![image](https://user-images.githubusercontent.com/68456385/128163345-e1b76507-1c94-48dc-b162-eaf5930d1148.png)
=> @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고,
스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어짐

@Configuration을 적용하지 않으면 어떻게 될까?   
-> AppConfig가 CGLIB 기술 없이 순수한 AppConfig로 스프링 빈에 등록된 것을 확인할 수 있음
> bean = class hello.core.AppConfig

@Bean 호출 로그를 찍어보면, 한 메서드가 여러번 실행되는 것을 확인할 수 있음   
-> 객체가 여러 개 생성됨   
-> 싱글톤 패턴이 깨짐

=> @Configuration을 사용하지 않고 @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.

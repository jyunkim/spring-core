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
- 스프링 컨테이너는 인자로 넣어준 클래스를 구성 정보로 사용   
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
(AnnotationConfigApplicationContext에 파라미터로 넘긴 값도 스프링 빈으로 등록되기 때문에 AppConfig도 스프링 빈이 됨)
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

## 6. 컴포넌트 스캔
지금까지의 @Bean을 통한 스프링 빈 등록 방식은 개수가 많아질수록 복잡하고 번거로워짐   
스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔, 
의존관계를 자동으로 주입하는 @Autowired 기능도 제공

### 컴포넌트 스캔
@Component 어노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록   
@Configuration도 내부적으로 @Component 어노테이션이 붙어있기 때문에 자동으로 등록됨   
\* 어노테이션에 다른 어노테이션이 붙는 것은 자바 언어 문법이 아니라 스프링에서 지원하는 기능

스프링 빈의 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용   
만약 스프링 빈의 이름을 직접 지정하고 싶으면 @Component("memberService2") 이런식으로 부여 가능
```java
@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
```
excludeFilters를 이용해서 기존의 AppConfig와 같은 설정 정보는 컴포넌트 스캔 대상에서 제외(충돌 방지)

이제 각 클래스가 컴포넌트 스캔의 대상이 되도록 @Component 애노테이션을 붙여줌

### 의존관계 주입
그렇다면 의존관계 주입은?   
-> @Autowired를 이용한 자동 의존관계 주입 사용
```java
@Component
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long id) {
        return memberRepository.findById(id);
    }
}
```
생성자에 @Autowired를 지정하면, 스프링 컨테이너가 자동으로 인자로 필요한 스프링 빈을 찾아서 주입   
getBean(MemberRepository.class)과 같이 타입을 이용한 조회 방식 사용

### 탐색 위치와 기본 스캔 대상
컴포넌트 스캔 탐색 시작 위치를 지정할 수 있음
```java
@ComponentScan(
        basePackages = "hello.core" // hello.core 내부만 탐색
//        basePackages = {"hello.core", "hello.service"}  // 여러 위치 지정 가능
//        basePackageClasses = AutoAppConfig.class // 지정한 클래스의 패키지를 탐색 시작 위치로 지정
)
```
기본값 - @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 됨

@Configuration, @Controller와 같은 어노테이션은 내부적으로 @Component가 붙어있기 때문에 컴포넌트의 기본 스캔 대상

부가 기능   
- @Controller: 스프링 MVC 컨트롤러로 인식
- @Repository: 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해줌
- @Configuration: 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리
- @Service: 특별한 처리를 하지 않지만, 개발자들이 핵심 비즈니스 로직 위치를 인식하는데 도움이 됨

### 필터
- includeFilters: 컴포넌트 스캔 대상을 추가로 지정
- excludeFilters: 컴포넌트 스캔에서 제외할 대상을 지정

```java
@ComponentScan(
    includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
)
```

Filter Type 옵션   
- ANNOTATION: 기본값, 애노테이션을 인식해서 동작
- ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작
- ASPECTJ: AspectJ 패턴 사용
- REGEX: 정규 표현식
- CUSTOM: TypeFilter 이라는 인터페이스를 구현해서 처리

### 중복 등록과 충돌
1. 자동 등록 vs 자동 등록   
   이름이 같은 경우 발생   
   -> ConflictingBeanDefinitionException 예외 발생
   
2. 자동 등록 vs 수동 등록   
   이 경우 수동 빈 등록이 우선권을 가짐   
   최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 함
   (CoreApplication 실행 시 확인 가능)

## 7. 의존관계 자동 주입
### 의존관계 주입 방법
1. 생성자 주입   
   생성자 호출시점에 딱 1번만 호출되는 것이 보장   
   불변, 필수(final) 의존관계에 사용     
   생성자가 1개만 있으면 @Autowired를 생략해도 자동 주입 됨(스프링 빈에만 해당)


2. 수정자 주입(setter 주입)   
   setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법   
   선택, 변경 가능성이 있는 의존관계에 사용
   

3. 필드 주입   
   필드에 바로 주입하는 방법   
   외부에서 변경이 불가능해서 테스트 하기 힘듬   
   순수 자바 코드를 사용하는 단위 테스트에서는 사용할 수 없음   
   테스트 코드에서만 사용하자
   

4. 일반 메서드 주입   
   일반 메서드를 통해서 주입   
   한번에 여러 필드를 주입 받을 수 있음
   
   
### 옵션 처리
@Autowired의 기본 동작은 주입할 대상이 없으면 오류 발생

자동 주입 대상을 옵션으로 처리하는 방법   
- @Autowired(required = false) - 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
- @Nullable - 자동 주입할 대상이 없으면 null이 입력됨
- Optional<> - 자동 주입할 대상이 없으면 Optional.empty가 입력됨

### 생성자 주입
**불변**   
대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없음   
대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안됨   
생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없기 때문에 불변하게 설계할 수 있음   
수정자 주입을 사용하면, 메서드를 public으로 열어두어야 하므로 누군가 실수로 변경할 수 있음

**누락**   
프레임워크 없이 순수한 자바 코드를 단위 테스트 하는 경우
```java
@Test
void createOrder() {
    OrderServiceImpl orderService = new OrderServiceImpl();
    orderService.createOrder(1L, "itemA", 10000);
}
```
수정자 주입   
NullPointerException 발생   
-> 버그를 잡기 번거로움

생성자 주입   
컴파일 오류   
-> 바로 어떤 값을 필수로 주입해야 하는지 알 수 있음

** 컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다!

**final**   
필드 선언 시 바로 초기화를 해주거나, 생성자로 초기화 해줘야 함   
-> 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아줌   
생성자 주입을 제외한 나머지 주입 방식은 final 키워드 사용 불가

기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식에 옵션을 부여하여 사용하자

### Lombok
```java
@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();

        helloLombok.setAge(1);
        helloLombok.setName("name");

        System.out.println("helloLombok = " + helloLombok);
    }
}
```
롬복이 자바의 애노테이션 프로세서라는 기능을 이용해서 컴파일 시점에 생성자와 메서드 코드를 자동으로 생성해줌

@RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만들어줌
```java
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

   private final MemberRepository memberRepository;
   private final DiscountPolicy discountPolicy;
   
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }
}
```
생성자가 1개만 있으면, @Autowired를 생략할 수 있으므로 사용 가능

### 조회할 타입의 빈이 2개 이상인 경우
@Autowired는 빈의 타입으로 조회한다   
-> 부모 타입으로 조회했을 때 선택된 빈이 2개 이상일 때 문제가 발생(NoUniqueBeanDefinitionException)

타입을 하위 타입으로 지정하면?   
-> DIP위반, 유연성이 떨어짐

**해결 방법**
1. 수동 등록


2. @Autowired 필드 명 매칭   
   타입 매칭 결과 2개 이상의 빈이 있으면 필드 명, 파라미터 명으로 빈 이름을 추가 매칭한다.
```java
@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy rateDiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = rateDiscountPolicy;
    }
}
```

3. @Qualifier   
   추가 구분자를 붙여주는 방법
```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}
```
```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```
@Qualifier("mainDiscountPolicy") 처럼 문자를 적으면 컴파일시 타입 체크가 되지 않음   
-> 어노테이션을 직접 만들어 해결   
@Qualifier에 붙어있는 어노테이션 + @Qualifier
```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```
```java
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {}
```
```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

4. @Primary   
   우선순위를 정하는 방법   
   여러 빈이 매칭되면 @Primary가 붙은 빈이 우선권을 가짐
```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}
```

@Primary만으로 해결할 수 있으면 @Primary를 사용하고, 
해결할 수 없다면 @Qualifier 사용

### 여러 빈이 모두 필요할 경우
해당 타입의 스프링 빈이 다 필요한 경우도 있다   
-> map, list 이용

```java
public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        
        int discountPrice = discountService.discount(member, 10000, "fixedDiscountPolicy");

        assertThat(discountPrice).isEqualTo(1000);
    }

    static class DiscountService {

        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        // @Autowired 생략
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        // 인자로 넘어온 정책에 따라 할인 방식 결정
        public int discount(Member member, int price, String discountCode) {
            // 스프링 빈 이름으로 조회
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
    }
}
```
Map<String, DiscountPolicy>    
map의 키에 스프링 빈의 이름을 넣어주고, 값으로 DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아준다.

List<DiscountPolicy\>   
DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아준다.

### 자동, 수동 등록 선택
최근 스프링 부트도 컴포넌트 스캔을 기본으로 사용하는 등 점점 자동을 선호하는 추세

**수동 빈 등록은 언제 사용하면 좋을까**   
애플리케이션은 크게 업무 로직과 기술 지원 로직으로 나눌 수 있음   
- 업무 로직 빈: 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 
  리포지토리등이 모두 업무 로직이다. 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
- 기술 지원 빈: 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 
  로그 처리처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.
  
업무 로직은 수가 많고, 유사한 패턴이 있으므로 자동 등록을 사용하는 것이 좋음   
기술 지원 로직은 애플리케이션 전반에 걸쳐 광범위하게 영향을 미치고, 
문제가 발생했을 때 어디가 문제인지 파악하기 어렵기 때문에 수동 등록을 사용해서 
명확히 드러내는 것이 좋음

\* 스프링과 스프링 부트가 자동으로 등록하는 수 많은 빈들은 예외   
Ex. DataSource(기술 지원 로직)

비즈니스 로직 중에서 다형성을 적극 활용할 때   
조회한 빈이 모두 필요할 때(List, Map 사용)   
-> 어떤 빈들이 주입될 지 한눈에 파악할 수 있는 것이 중요   
=> 수동 등록을 사용하거나, 특정 패키지에 모아서 자동 등록
```java
@Configuration
public class DiscountPolicyConfig {
    
   @Bean
   public DiscountPolicy rateDiscountPolicy() {
       return new RateDiscountPolicy();
   }
   @Bean
   public DiscountPolicy fixDiscountPolicy() {
       return new FixDiscountPolicy();
   }
}
```

## 8. 빈 생명주기 콜백
### 객체 생성과 초기화 분리
생성자는 필수 정보를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가짐   
반면 초기화는 이렇게 생성된 값들을 활용해서 외부 커넥션을 연결하는등 무거운 동작을 수행   
-> 생성자 안에서 무거운 초기화 작업을 함께 하는 것 보다는, 
객체를 생성하는 부분과 초기화 하는 부분을 명확하게 나누는 것이 유지보수 관점에서 좋음

### 객체의 초기화와 종료
애플리케이션 시작 시점에 필요한 연결을 미리 해두고,
애플리케이션 종료 시점에 연결을 모두 종료하는 작업이 필요한 경우가 있음   
Ex. 데이터베이스 커넥션 풀, 네트워크 소켓

스프링 빈은 객체를 생성하고, 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료됨   
-> 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 호출해야 함   
-> 개발자가 의존관계 주입이 모두 완료된 시점을 알아야 함   
스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공   
또한 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 주므로 안전하게 종료 작업을 진행할 수 있음

**스프링 빈의 이벤트 라이프사이클**   
스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료

### InitializingBean, DisposableBean
인터페이스 이용
```java
public class NetworkClient implements InitializingBean, DisposableBean {

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
    @Override
    public void afterPropertiesSet() throws Exception {
        // 초기화
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 소멸될 때 실행됨
    @Override
    public void destroy() throws Exception {
        // 종료
        disconnect();
    }
}
```
**초기화, 소멸 인터페이스 단점**   
- 이 인터페이스는 스프링 전용 인터페이스이므로,
해당 코드가 스프링 전용 인터페이스에 의존하게됨
- 메서드 이름을 변경할 수 없음 
- 외부 라이브러리에는 적용할 수 없음
> 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법이고, 
지금은 다음의 더 나은 방법들이 있어서 거의 사용하지 않는다.

### @Bean 속성 이용
```java
public class NetworkClient {
    
    ...
    
    // 의존관계 주입이 끝나면 실행됨
    public void init() {
        // 초기화
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 소멸될 때 실행됨
    public void close() {
        // 종료
        disconnect();
    }
}
```
```java
@Configuration
class LifeCycleConfig {

     @Bean(initMethod = "init", destroyMethod = "close")
     public NetworkClient networkClient() {
         NetworkClient networkClient = new NetworkClient();
         networkClient.setUrl("http://www.spring.com");
         return networkClient;
     }
 }
```
- 메서드 이름을 자유롭게 줄 수 있음
- 스프링 빈이 스프링 코드에 의존하지 않음
- 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 
외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있음

\* 종료 메서드 추론   
@Bean의 destroyMethod 속성에는 아주 특별한 기능이 있다.   
라이브러리는 대부분 close, shutdown이라는 이름의 종료 메서드를 사용하는데,
종료 메서드의 추론 기능은 close, shutdown라는 이름의 메서드를 자동으로 호출해준다.   
따라서 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 동작한다.

### @PostConstruct, @PreDestory
```java
public class NetworkClient {

    ...
    
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
```
- 최신 스프링에서 가장 권장하는 방법
- 스프링에 종속적인 기술이 아니라 JSR-250라는 자바 표준 기술이기 때문에
  스프링이 아닌 다른 컨테이너에서도 동작한다.
- 외부 라이브러리에는 적용하지 못하기 때문에 외부 라이브러리를 초기화, 종료해야 하면
@Bean의 기능을 사용하자.
  
외부 라이브러리를 사용할 땐 @Bean의 속성을 사용하고, 
그 외에는 어노테이션을 사용하자

## 9. 빈 스코프
### 빈 스코프
빈이 존재할 수 있는 범위

- 싱글톤: 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프
- 프로토타입: 빈의 생성과 의존관계 주입까지만 관리되는 매우 짧은 범위의 스코프
- 웹 관련 스코프
   - request: 웹 요청이 들어오고 나갈 때까지 유지되는 스코프
   - session: 웹 세션이 생성되고 종료될 때까지 유지되는 스코프
   - application: 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프
   
### 프로토타입 스코프
싱글톤과 달리 프로토타입 스코프를 스프링 컨테이너에 조회하면 스프링 컨테이너는 항상 새로운 인스턴스를 생성해서 반환   
스프링 컨테이너는 프로토타입 빈 생성, 의존관계 주입, 초기화까지만 처리   
이후 프로토타입 빈을 관리할 책임은 프로토타입 빈을 받은 클라이언트에 있음

```java
public class PrototypeTest {

    @Test
    void prototypeBeanFind() {
       // 인자로 넘겨준 클래스는 @Component가 없어도 컴포넌트 스캔의 대상이 됨
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("Get prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("Get prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void close() {
            System.out.println("PrototypeBean.close");
        }
    }
}
```

실행 결과
```
find prototypeBean1
PrototypeBean.init
find prototypeBean2
PrototypeBean.init
prototypeBean1 = hello.core.scope.PrototypeTest$PrototypeBean@13d4992d
prototypeBean2 = hello.core.scope.PrototypeTest$PrototypeBean@302f7971
org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing
```

싱글톤 빈은 스프링 컨테이너 생성 시점에 초기화 메서드가 실행 되지만, 
프로토타입 스코프의 빈은 스프링 컨테이너에서 빈을 조회할 때 생성되고, 초기화 메서드도 실행됨   
또, 프로토타입 빈을 조회할 때마다 완전히 다른 스프링 빈이 생성됨   
싱글톤 빈은 스프링 컨테이너가 관리하기 때문에 스프링 컨테이너가 종료될 때 빈의 종료 메서드가 실행되지만, 
프로토타입 빈은 스프링 컨테이너가 생성과 의존관계 주입 그리고 초기화 까지만 관여하고, 
더는 관리하지 않기 때문에 스프링 컨테이너가 종료될 때 종료 메서드가 실행되지 않음   
-> 클라이언트가 직접 종료 메서드를 호출해야 함

그러면 프로토타입 빈을 언제 사용할까?   
-> 매번 사용할 때마다 의존관계 주입이 완료된 새로운 객체가 필요한 경우

### 싱글톤 빈 내 프로토타입 빈
싱글톤 빈에서 프로토타입 빈을 주입 받아 사용하는 경우   
요청이 올 때마다 프로토타입 빈을 새로 생성해서 사용하길 원함      
싱글톤 빈 내 프로토타입 빈은 의존관계 주입 시점에 생성됨   
But, 의존관계 주입은 싱글톤 빈이 처음 생성될 때 한번만 일어나므로 프로토타입 빈 또한 한번만 생성된다.

싱글톤 빈과 프로토타입 빈을 함께 사용할 때, 어떻게 하면 사용할 때 마다 항상 새로운 프로토타입 빈을 생성할 수 있을까?

\* Dependency Lookup(DL), 의존관계 조회(탐색): 의존관계를 외부에서 주입(DI) 받는게 아니라 이렇게 직접 필요한 의존관계를 찾는 것
```java
static class ClientBean {

   @Autowired
   private ApplicationContext ac;
   
   public int logic() {
      PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
      prototypeBean.addCount();
      int count = prototypeBean.getCount();
      return count;
   }
}
```
그런데 이렇게 스프링의 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워짐   
-> Provider 이용

**ObjectProvider**   
스프링에서 지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 제공하는 것
```java
static class ClientBean {

   @Autowired
   private ObjectProvider<PrototypeBean> prototypeBeanProvider;
   
   public int logic() {
      PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
      prototypeBean.addCount();
      return prototypeBean.getCount();
   }
}
```
getObject()를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환(DL)   
-> 항상 새로운 프로토타입 빈이 생성됨   
Provider는 컨테이너 생성 시 주입됨   
기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기 훨씬 쉬워짐

**JSR-330 Provider**   
자바 표준 provider

javax.inject:javax.inject:1 라이브러리를 gradle에 추가해야함
```java
static class ClientBean {

   @Autowired
   private Provider<PrototypeBean> prototypeBeanProvider;
   
   public int logic() {
      PrototypeBean prototypeBean = prototypeBeanProvider.get();
      prototypeBean.addCount();
      return prototypeBean.getCount();
   }
}
```
자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용할 수 있음

ObjectProvider는 DL을 위한 편의 기능을 많이 제공해주고 스프링 외에 별도의 의존관계 추가가 필요 없기 때문에, 
스프링이 아닌 다른 컨테이너를 사용하지 않는다면 ObjectProvider를 사용

### 웹 스코프
웹 스코프는 웹 환경에서만 동작(spring-boot-starter-web 라이브러리 추가)   
-> hello.core.CoreApplication의 main 메서드를 실행하면 웹 애플리케이션이 실행됨
(@SpringBootApplication에 @ComponentScan이 포함되어 있음)   
-> 스프링 부트가 내장 톰켓 서버를 활용해서 웹 서버와 스프링을 함께 실행시킴
> 스프링 부트는 웹 라이브러리가 없으면 AnnotationConfigApplicationContext를 기반으로 애플리케이션을 구동한다.    
웹 라이브러리가 추가되면 웹과 관련된 추가 설정과 환경들이 필요하므로 AnnotationConfigServletWebServerApplicationContext를 
기반으로 애플리케이션을 구동한다.

프로토타입과 다르게 스프링이 해당 스코프의 종료시점까지 관리   
-> 종료 메서드가 호출됨

**request 스코프**   
HTTP 요청 하나가 들어오고 응답으로 나갈 때까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고 관리됨

![image](https://user-images.githubusercontent.com/68456385/129887250-5b125d4a-3f0c-4bf3-9d3a-f64e5e6f7471.png)

```java
@Component
@Scope("request")
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        // 임의의 랜덤 id 부여
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean created: " + this);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[" + uuid + "] request scope bean destroyed: " + this);
    }
}
```
request scope를 사용하지 않고 파라미터로 모든 정보를 서비스 계층에 넘긴다면, 파라미터가 많아서 지저분해짐   
또한 requestURL 같은 웹과 관련된 정보가 웹과 관련없는 서비스 계층까지 넘어가게 됨(웹과 관련된 부분은 컨트롤러까지만 사용해야 함)      
서비스 계층은 웹 기술에 종속되지 않고, 가급적 순수하게 유지하는 것이 유지보수 관점에서 좋음   
```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    // 자바 표준 서블릿 규약에 맞는 요청
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("Controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
```
-> 실행해보면 LogDemoController에서 의존관계 주입 시 MyLogger 빈을 찾을 수 없다는 에러 발생   
-> request 스코프 빈은 HTTP 요청이 들어온 이후에 생성될 수 있기 때문에 스프링 컨테이너가 생성되는 시점에는 생성되지 않는다!   
-> Provider 이용

### ObjectProvider
```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping("log-demo")
    @ResponseBody
    // 자바 표준 서블릿 규약에 맞는 요청
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("Controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
```
getObject()를 호출하시는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리됨   
요청마다 별개의 스프링 빈 반환, 같은 HTTP 요청이면 같은 스프링 빈이 반환   
=> getObject()를 호출하는 시점까지 request scope 빈의 생성을 지연시켜 처리

### 프록시
```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
}
```
적용 대상이 클래스 - TARGET_CLASS   
적용 대상이 인터페이스 - INTERFACES

LogDemoController, LogDemoService는 Provider 사용 전과 동일

스프링 컨테이너가 CGLIB라는 바이트코드를 조작하는 라이브러리를 사용해서 MyLogger를 상속받은 가짜 프록시 객체를 생성한 후 주입

![image](https://user-images.githubusercontent.com/68456385/129887948-b2da3d9b-06f9-4968-8606-6f11baddf72c.png)
가짜 프록시 객체에는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있고, 싱글톤으로 동작    
가짜 프록시 객체의 메서드 호출 -> 진짜 request 스코프의 메서드 호출   
가짜 프록시 객체는 원본 클래스를 상속 받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 
원본인지 아닌지도 모르게, 동일하게 사용할 수 있음(다형성)

클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사용할 수 있음   
진짜 객체 조회를 꼭 필요한 시점까지 지연 처리      
어노테이션만 추가하면 클라이언트 코드를 수정하지 않아도 된다!

마치 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 결국 주의해서 사용해야함   
특별한 scope는 무분별하게 사용하면 유지보수하기 어려워지므로 꼭 필요한 곳에만 최소화해서 사용

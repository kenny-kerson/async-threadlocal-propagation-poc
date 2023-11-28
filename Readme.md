# @Async와 pub/sub의 sub에서 메인쓰레드의 Threadlocal을 전파받기
## Problem
1. 어플리케이션에서 하나의 요청 혹은 쓰레드에서 공통적으로 사용하는 value를 관리하기 위해 Threadlocal을 사용한다
2. 하지만, 메인쓰레드 이외의 다른 쓰레드에서 동작하는 로직들( @Async, pub/sub의 sub )은 메인쓰레드의 Threadlocal을 사용할 수 없다
3. 여기서는, 메인쓰레드의 Threadlocal을 메인로직에서 생성된 비동기 쓰레드들에 전파하는 문제를 해결한다

---

## Goal
1. API 요청이 들어오면, Controller부터 그 이후 호출되는 모든 메서드들에서 guid, userId에 접근할 수 있어야 한다
2. 메인쓰레드와 분리된 @Async 메서드들에서도, 동일한 guid / userId에 접근할 수 있어야 한다
3. Message Broker로 pub한 쓰레드의 guid / userId를, sub한 쓰레드에서도 동일하게 접근할 수 있어야 한다
4. 메인쓰레드 및 비동기 쓰레드들에서 관리된 guid / userId는, 쓰레드가 종료시 적절히 정리되어야 한다

---

## Requirements
기능 요건
- N/A

비기능 요건
1. (REQ-1) 1개의 요청쓰레드에서 guid, userId를 관리하기 위한 ThreadLocal를 구현한다  
2. (REQ-2) @Async 메서드에서 사용할 별도의 쓰레드풀을 구성한다
3. (REQ-3) pub/sub을 처리하기 위한 Message Broker(kafka)를 구성한다
4. (REQ-4) API가 요청되면 Controller에서 guid, userId를 담는 ThreadLocal를 생성한다
5. (REQ-5) @Async 메서드에서 메인워커쓰레드의 ThreadLocal를 전파받아, 별도의 ThreadLocal를 생성한다
6. (REQ-6) kafka로 pub할때 메인워커쓰레드의 ThreadLocal를 함께 전달한다
7. (REQ-7) kafka에서 sub할때 전달받은 메인워커쓰레드의 ThreadLocal을 전파받아, 별도의 ThreadLocal를 생성한다


Out of Scope
1. Service Layer의 비지니스 로직은 구현하지 않는다

---

## Context
- TBD

---

## Solution
### 1️⃣ @Async에서 ThreadLocal 전파하기 
#### 1. InheritableThreadLocal 사용하기
InheritableThreadLocal이란?
- 비동기 쓰레드 생성시, 원본 쓰레드의 쓰레드로컬을 전파해주는 쓰레드로컬
- 별도의 설정을 하지 않아도 전파할 수 있어서 간편
- 전파과정이 없어서 명시적이지 않은 단점이 있음
- 사용한 이후에 자원에 대한 정리가 필수 
- Spring MVC RequestContextHolder에서도 사용하는 방식

InheritableThreadLocal 로컬 생성하기
![11.png](img%2F11.png)

InheritableThreadLocal 비동기 쓰레드 전파 확인하기
1) 비동기 쓰레드에서 ThreadLocal 로그 출력
![12.png](img%2F12.png)
2) 별도의 작업을 하지 않아도 원본 쓰레드와 동일한 쓰레드로컬값이 출력됨
![13.png](img%2F13.png)


#### 2. ThreadLocal & TaskDecorator 사용하기
1) TaskDecorator 생성하기
![21.png](img%2F21.png)
2) ThreadPoolTaskExecutor에 Decorator 추가하기
![21.png](img%2F22.png)
3) 원본 쓰레드와 동일한 쓰레드로컬값이 출력됨
![13.png](img%2F13.png)

#### 3. 테스트해보기
1) Application 기동
2) api.http 파일의 GET http://localhost:8080/async/threadlocal/kenny/202311211025000 실행

---

### 2️⃣ kafka pub/sub에서 ThreadLocal 전파하기
#### 방법
1. payload에 threadlocal에 해당하는 item들도 포함하여 pub / sub하기
2. 반복되는 데이터는 kafka header를 통해 pub하고, consumer에서는 Before Aspect에서 threadlocal로 생성하기 

#### 선택
- 2번 선택 
- 관심사를 분리 / payload는 compact하게 유지 / 중복된 코드를 제거를 위해 선택함 

#### 구현
1) Producer에서 kafka로 send할때, ProducerRecord 객체의 RecordHeader를 이용해 context를 담는다. 이때, payload는 value에 별도로 담긴다( payload는 매번 바뀌지만, header는 항상 동일한 구성의 context를 가져와서 셋팅함 )
![3-1.png](img%2F3-1.png)
2) Consumer에서는 payload로 전달받은 객체를 매개변수로 받는다. 이 때, context는 이미 셋팅되어 있다
![3-3.png](img%2F3-3.png)
3) Consumer Aspect의 Before 포인트컷에서 kafka header를 읽어서 이 쓰레드의 context를 만든다
![3-4.png](img%2F3-4.png)
4) 사용한 threadlocal 자원은 After 시점에 정리한다
![3-5.png](img%2F3-5.png)

#### 테스트
1) Application 기동
2) api.http 파일의 GET http://localhost:8080/async/threadlocal/kenny/202311211025000 실행
   

![3-6.png](img%2F3-6.png)
Consumer에서 threadlocal을 생성하고, 자원을 정리한 로그를 확인할 수 있다

---

## Material References
- TBD

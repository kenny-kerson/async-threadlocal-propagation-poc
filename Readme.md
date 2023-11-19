# @Async와 pub/sub의 sub에서 메인쓰레드의 Threadlocal을 전파받기
## Problem
1. 어플리케이션에서 하나의 요청 혹은 쓰레드에서 공통적으로 사용하는 value를 관리하기 위해 Threadlocal을 사용한다
2. 하지만, 메인쓰레드 이외의 다른 쓰레드에서 동작하는 로직들( @Async, pub/sub의 sub )은 메인쓰레드의 Threadlocal을 사용할 수 없다
3. 여기서는, 메인쓰레드의 Threadlocal을 메인로직에서 생성된 비동기 쓰레드들에 전파하는 로직구현을 목표로 한다

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
- TBD

---

## Material References
- TBD

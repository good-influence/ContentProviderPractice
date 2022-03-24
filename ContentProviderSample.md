# ContentProvider with Room
안드로이드 ContentProvider 컴포넌트 와 Room 데이터베이스를 사용한 연습 레포지토리입니다.

## 시나리오
- getListenerValue
    1. A 앱에서 B 앱의 특정 메소드를 provider의 call 메소드를 통해 실행함.
    2. 특정 메소드에서는 네트워크 작업이 이루어지고 네트워크의 작업 결과는 listener에 반환됨.
    3. listener에 결과가 반환될 때 까지 기다렸다가 bundle 에 담아 A앱에 반환

- doSomething  
    1. A앱에서 B 앱의 특정 메소드를 provider의 call 메소드를 통해 실행함.
    2. 특정 메소드에서는 네트워크 작업이 이루어지고 네트워크 작업 결과를 db에 저장함.
    3. A앱에서 ContentObserver를 등록하여 uri 값을 관찰하여 데이터 업데이트



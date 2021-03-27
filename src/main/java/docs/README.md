## 기능 목록

- [x] Position
    - [x] 좌표 x, y 값을 가지고 있는다.
    - [x] 들어온 좌표가 0보다 작거나 7보다 크면 예외가 발생한다.


- [x] ROW (행) `->` ENUM
    - [x] a -> h 를 0 -> 7 으로 치환

- [x] COL (열) `->` ENUM
    - [x] 1 -> 8 를 7 -> 0 으로 치환

- [x] Command
    - [x] 명령어 목록을 가지고 있는 enum
    - [x] 지원하는 명령어인지 확인하는 기능
    - [x] 명령어에 따라 알맞은 기능을 수행한다.

- [x] BoardFactory
    - [x] Board 를 생성해준다.
        - [x] `Map<Team, List<Piece>>`를 생성한다.
            - [x] 타입별로 초기 ROW 좌표를 정해주며 Piece 를 생성한다.

- [x] Board
    - [x] 각 팀별 `piece` 리스트를 가진다. `Map<Team, List<Piece>>`  (상태)
    - [x] `말(piece)`을 움직이는것을 요청한다.
        - [x] `이동하려는 말의 좌표`와 `목적지 좌표`와 `목적지 좌표로 움직이는 유저의 팀`을 전해받는다. `(startPoint, endPoint, team)`
            - [x] 유저가 자기의 말을 움직이려고 하는게 맞는지 확인한다.
                - [x] 이동하려는 `Piece` 가 Team 키를 통해 `Map<Team, List<Piece>>` 로부터 얻어온 리스트 `List<Piece>`에 있는지 확인한다.
        - [x] `Piece` 에게 `endPoint`로의 이동을 요청한다. `(Map, EndPoint) `
        - [x] 좌표를 받은뒤, 해당 좌표가 맵 크기를 벗어나는지 확인한다.

- Piece
    - [x] 자신의 `team`을 가지고 있는다. (상태)
    - [x] 자기의 `position`을 가진다 (상태)
    - [x] 자신의 `이니셜`을 가진다 (상태)
    - [x] 팀에 맞춰 자신의 시작 위치에서 생성된다.
        - [x] 팀(B or W) 별로 Row(행)이 결정된다.
        - [x] piece 별로 잘못된 위치에 생성하려고 하면 예외 발생
        - [x] Pawn
        - [x] Rook
        - [x] knight
        - [x] bishop
        - [x] queen
        - [x] king
    - [x] 목적지로 이동할수 있는지 확인하고 움직인다.
        - [x] 목적지로 이동할 수 있는지 확인한다.
            - [x] 이동할수 있는 `List<Position>`를 확인한다. (장애물까지 모두 고려한 리스트) 요청할때 넘겨주는 정보는 `(Map)`
                - [x] piece 별로 갈 수 있는 위치 리스트를 반환한다. `(Map)`
                    - [x] pawn
                        - [x] 대각선 **공격**
                        - [x] 아래팀(white)인 경우 black 팀 리스트 에 현재 위치에서 `(-1, -1)`만큼 이동한 곳에 `Piece`가 있다면 포함 o
                        - [x] 아래팀(white)인 경우 black 팀 리스트 에 현재 위치에서`(-1, 1)` 만큼 이동한 곳에 `Piece`가 있다면 포함 o
                        - [x] 초기위치인 경우
                            - [x] 아래팀(white)인 경우 black 팀 리스트 에 현재 위치에서`(-2, 0)`만큼 이동한 곳에 `Piece`가 있다면 포함 x
                            - [x] 아래팀(white)인 경우 black 팀 리스트 에 현재 위치에서`(-1, 0)`만큼 이동한 곳에`Piece`가 있다면 포함 x
                        - [x] 초기위치가 아닌경우
                            - [x] 아래팀(white)인 경우 black 팀 리스트 에 현재 위치에서`(-1, 0)`만큼 이동한 곳에 `Piece`가 있다면 포함 x
                        - [x] 윗팀(black) 인경우 부등호만 바꿔주면 동치

                    - [x] rook
                        - [x] `상 하 좌 우`로 한 칸씩 전진 해보면서 리스트에 갈수있는 position 추가
                            - ex) `상`으로 전진 `(-1 , 0)` 을 계속 더해나감
                                - 맵을 벗어나려고 하는 경우 (리스트에 불포함) 루프 종료
                                - 전진 하려는 위치가 같은 편인 경우(리스트에 불포함) 루프 종료
                                - 전진 하려는 위치가 다른 편인 경우(리스트 포함) 루프 종료

                - [x] 목적지(`endPoint`)가, 갈 수 있는 위치 `List<Position>` 안에 존재하는지 확인한다.
                    - [x] 포함된다면 `piece` 에 position을 변경한다..
                    - [x] Map의 key를 통해 확인한 적팀 리스트에 목적지에 위치한 `piece`가 있다면 적팀 리스트에서 해당 `piece`를 삭제한다.

        - [x] knight
        - [x] bishop
        - [x] queen
        - [x] king

- [x] Pieces
    - [x] 팀별로 점수를 계산한다.
    - [x] 왕이 살아있는지 확인한다.
    - [x] 특정위치의 말이 있는지 확인한다.
    - [x] 특정위치의 말을 가져온다.
    - [x] 특정위치의 말을 삭제한다.

- [x] ChessGame
    - [x] 왕이 잡히면 게임이 종료된다.

---

- [x] View
    - [x] InputView
    - [x] OutputView

- [x] ChessGameController
    - [x] 턴을 번걸아가며 말을 조작하는 기능 구현

---

## Refactoring TODO

- [x] 커맨드 입력 메시지 출력 기능추가 (move)
- [x] 게임 플레이 순서를 Controller가 아닌 ChessGame 도메인에서 처리하도록 수정
- [x] 상속을 원치 않는 클래스에 대해서 final 키워드 추가
- [x] 불필요한 필드값 제거
- [x] getBoard시 매번 초기화 과정을 거치지 않고, 초기화 작업은 1회만 진행. 이후 초기화된 값을 복사해서 넘겨주게 수정
- [x] Col 와 Row를 역할에 맞춰 유틸 클래로 변경스
- [x] 축약한 네이밍 변경
- [x] 명령어에 따른 분기 처리를 Enum에서 처리하지 않고, 컨트롤러에서 처리하도록 수정
- [x] 게임이 시작되면 새로운 ChessGame객체를 만들도록 수정
- [x] 말들의 생성자 접근제어자 범위를 줄이고, 위치를 조정하는 팩토리 메서드 추가
- [x] 중복되는 메소드들을 수정
    - [x] Piece들은 자신의 위치값, 움직일 방향을 지니게함
- [x] 클래스 변수와 인스턴스 변수 사이에 공백을 만들어 가독성 향상
- [x] 클래스내 메소드들을 중요도에 맞춰 위치 수정
- [x] 불필요한 테스트 코드 삭제
- [x] 테스트에 공통되게 사용되는 객체들을 모아두는 클래스 구현
- [x] 각 움직임에 관한 기능을 여러번 이동(룩, 비숍 퀸) 이냐, 한번 이동이냐(나이트, 킹)에 따라 인터페이스로 분리
- [x] Piece에게 팀을 주면 해당 팀에 해당하는 초기 위치의 Pieces를 얻어오게 수정
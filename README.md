# Backend
# 소개
반려동물의 숏폼 영상을 공유하고 공감할 수 있는 서비스 Paws입니다.
# 기능
1. 로그인 & 소셜 로그인 & 로그아웃
2. 숏폼 동영상 등록, 수정/삭제
4. 동영상 좋아요 기능
5. 관심영상
6. 댓글 및 댓글 좋아요
7. 동영상, 유저를 관리하기 위한 관리자
## git convention

| 태그이름 | 설명                                                  |
| -------- | ----------------------------------------------------- |
| feat     | 새로운 기능 추가                                      |
| fix      | 버그 수정                                             |
| design   | css 등 사용자 UI 수정                                 |
| style    | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우 |
| refactor | 코드 리팩토링                                         |
| comment  | 필요한 주석 추가 및 변경                              |
| docs     | 문서 수정                                             |
| chore    | 패키지 매니저 설정                                    |
| rename   | 파일 혹은 폴더명 수정하거나 옮기는 작업               |
| remove   | 파일을 삭제하는 작업만 하는 경우                      |

```
[convention 명] : [#이슈번호] 커밋 내역

ex ) feat : #1 add chat feature
```

## CI & CD
![image](https://user-images.githubusercontent.com/25299428/165933244-897084cb-aec9-4ce8-b621-ac4012ebad63.png)

## ERD
![image](https://user-images.githubusercontent.com/25299428/169738922-620ac28a-5f16-4b90-9a08-3e1de554dc80.png)

## 트러블 슈팅
- [동영상 재생 기술 의사선택 히스토리](https://github.com/Numble3/Paws-Backend/wiki/%EB%8F%99%EC%98%81%EC%83%81-%EC%9E%AC%EC%83%9D-%EA%B8%B0%EC%88%A0-%EC%9D%98%EC%82%AC%EC%84%A0%ED%83%9D-%ED%9E%88%EC%8A%A4%ED%86%A0%EB%A6%AC)
- [ffmpeg를 통한 영상 convert 과정 serverless 적용 시도](https://velog.io/@appti/ffmpeg%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%98%81%EC%83%81-convert-%EA%B3%BC%EC%A0%95-serverless-%EC%A0%81%EC%9A%A9-%EC%8B%9C%EB%8F%84)
- [코드 구조에 대한 고민](https://andrewyun.tistory.com/139)

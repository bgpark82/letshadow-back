# version 1.0.0

## 원칙

- 필요한 기능의 작은 시작,  많은 리펙토링
- [서비스를 시작하며](https://bgpark.tistory.com/140)

## 요약

- Youtube 동영상의 구간 반복 기능을 제공하여 외국어 회화 학습 기능을 제공

## 배경

- 유튜브에서는 전체 반복은 하지만 원하는 구간의 반복을 할 수 없다
    - 구글 extension으로 [Language Learning with Youtube BETA](https://chrome.google.com/webstore/detail/language-learning-with-yo/jkhhdcaafjabenpmpcpgdjiffdpmmcjb?hl=en)가 있지만 **모바일에서는 사용불가**
    - 기존의 랭스터라는 모바일 서비스가 있었지만 **서비스 종료** (2020년)
    - 실제 사용자는 모바일로 사용하는 횟수가 많을 것이라 가정 (**모바일, 웹 사용량 측정 및 비교 필요**)

## 목표

**프론트엔드**

- 구글 로그인
- 비디오의 구간 반복기능 제공
- 영어 자막의 단어 번역

**백엔드**

- 구글 로그인
- 사용자 Youtube 계정의 좋아요한 비디오 리스트 API
- 사용자 계정 상세 API
- Jenkins CI/CD
- SSL 설정
- Post man
- Jenkins 배포 자동화
- 모니터링 툴

## 목표가 아닌 것

**프론트엔드**

- React, Next, Typescript, Styled Component, Storybook, Cypress, Redux Toolkit, React Testing Library
- 동영상 속도 조절
- 동영상 반복 중 간격조절
- 커뮤니티 게시판
- 검색 단어 통계
- 찾은 단어장
- 접속 시간, 영상 재생 시간 통계
- 최근 본 비디오 표시
- 동영상 플레이 타임에 따라 전체 스크립트에서 해당 시간의 스크립트 문장 하이라이트
- 소리 녹음 : [데모](https://webaudiodemos.appspot.com/AudioRecorder/index.html)
- 디자인 프로토 타입 툴 : [Figma](https://www.notion.so/Figma-718940ab9c294b99b931fab8f463f318)

**백엔드**

- Elastic Search, QueryDSL, Swagger, Jira
- Jenkins의 Blue Ocean으로 blue/green 배포
- scouter 설치
- 데이터베이스 마스터/슬레이브
- S3에 DB 백업
- S3에 로그 백업
- 구글 토큰 DB저장하지 않고 decode해서 처리해보기
- 성능 테스트 : [nGrinder](https://hyuntaeknote.tistory.com/11?category=916435)

## 스펙

프론트엔드

- Javascript ECMA6
- Jest
- VSCode
- Netlify

백엔드

- SpringBoot 2.2.4,
- Spring Data JPA,
- MariaDB
- Junit5
- Jenkins
- Amazon Linux 2
- Intellij

![images/Untitled.png](images/Untitled.png)

## 계획 (Detail)

## 프론트엔드

### 1. **youtube quota**

- API 요청 별 부과하는 quota 상이
- **quota의 제한으로 인해 quota가 1인 API만 사용**
    - list 중 1의 quota를 소비하는 API 선별
    - 기본적으로 insert, update, delete에는 1이상의 quota 적용
- 읽기 작업에 대해 **하루 1,000,000**를 기본 제공
- quota 1에 해당하는 API 목록

    ```jsx
    // quota 1
    activities 
    channels   
    channelSections 
    commentThreads 
    guideCategories
    i18nLanguages
    i18nRegions
    members
    membershipsLevels
    playlistItems
    playlists
    subscriptions
    videoAbuseReportReasons
    videoCategories
    videos
    ```

- 참조
    - [https://developers.google.com/youtube/v3/determine_quota_cost](https://developers.google.com/youtube/v3/determine_quota_cost)
    - [https://joypinkgom.tistory.com/29](https://joypinkgom.tistory.com/29)
    - [https://developers.google.com/youtube/v3/guides/authentication](https://developers.google.com/youtube/v3/guides/authentication)

### **2. Authorization**

**2-1. scope**

- Google의 OAuth 2.0 사용
- youtube data api를 사용하기 위해서는 **sensitive access scope** 사용
    - 해당 내역은 Google oauth consent 화면 verification 시 추가 검증 작업 발생
    - access scope 목록

  ![images/Untitled%201.png](images/Untitled%201.png)

    - 위 목록 중 `/oauth/youtube` 사용
    - 해당 scope은 사용자가 좋아요한 비디오 목록을 호출하는 API를 호출가능
        - quota 1 소비
        - `/video`

**2-2. Google OAuth 요청**

- google 인증 요청
- 백엔드 서버로 redirect하여 서버에서 google 토큰을 받아서 사용

**요청**

```markdown
https://accounts.google.com/o/oauth2/v2/auth?
redirect_uri=[서버 redirect url]&
response_type=code&
scope=openid profile email https://www.googleapis.com/auth/youtube&
client_id=[클라이언트 아이디]
access_type=offline,
```

- `redirect_uri` : 인증 절차 이후 클라이언트로 바로 돌아오는 uri
- `response_type=code` : response uri에 구글에서 발급한 인증용 code 발급
- `scope` : 구글 로그인의 scope인 openid profile email과 youtube data api의 scope인 [https://www.googleapis.com/auth/youtube](https://www.googleapis.com/auth/youtube) 제공

**응답**

```markdown
[클라이언트 redirect uri]? 
code=[code]&
scope=openid profile email https://www.googleapis.com/auth/youtube
```

[Youtube OAuth 2.0 ](https://www.notion.so/Youtube-OAuth-2-0-ed71c25cd0b9494ea9daf772cb47e1b4)

[OAuth 2.0 + Spring Boot](https://www.notion.so/OAuth-2-0-Spring-Boot-44990e75b5d74702a62ac4c20c0e5f09)

[refresh token](https://www.notion.so/refresh-token-c47bfdbd694046c6b1a7149336c40d75)

### 3. 라이브러리 생성

**3-1.** **Youtube-Loop** (미정)

- [youtube iframe](https://developers.google.com/youtube/iframe_api_reference)의 커스텀 라이브러리
- 추후 npm 라이브러리로 publish

**3-2. Youtube-Transcript** (미정)

- timetext api 사용
- **요청**

    ```markdown
    https://video.google.com/timedtext?
    lang=en&
    v=[비디오 id]
    ```

    - `lang=en` : 번역 대상 언어
    - `v` : youtube data api에서 제공하는 비디오 id
- **응답**
    - xml 형식의 응답

    ```markdown
    <transcript>
    	<text start="0" dur="2.218">(bass guitar riff)</text>
    	<text start="2.218" dur="2.156">South Korea is experiencing a golden age</text>
    	<text start="4.374" dur="2.325">of rapid progress and economic growth.</text>
    	<text start="6.699" dur="3.136">In just 25 years, the previous war-torn country</text>
    	<text start="9.835" dur="3.477">has risen to become the world&#39;s twelfth largest economy.</text>
    	<text start="13.312" dur="1.812">The birthplace of Samsung</text>
    </transcript>
    ```

    - `start` : 시작 시간 (초)
    - `end` : 종료 시간 (초)
- **xml parser**

    ```jsx
    const response = await fetch(`https://video.google.com/timedtext?lang=en&v=${videoId}`);
    const text = await response.text();
    const parser = new DOMParser();
    const subtitle = await parser.parseFromString(text, "text/xml");
    ```

- 참고
    - [https://stackoverflow.com/questions/32142656/get-youtube-captions/58435817#58435817](https://stackoverflow.com/questions/32142656/get-youtube-captions/58435817#58435817)
    - [https://community.canvaslms.com/t5/Developers-Group/Interactive-YouTube-transcript/td-p/159732](https://community.canvaslms.com/t5/Developers-Group/Interactive-YouTube-transcript/td-p/159732)

### 4. 구글 번역 API

- **요청**

    ```jsx
    https://translation.googleapis.com/language/translate/v2?
    q=apple&
    target=ko&
    source=en&
    key=[API 키]

    header {
    	method: "POST",
    }
    ```

    - `q` : 번역 대상 단어
    - `target` : 번역어
    - `source` : 번역 대상 단어의 언어
- **응답**

    ```markdown
    {
        "data": {
            "translations": [
                {
                    "translatedText": "사과"
                }
            ]
        }
    }
    ```

- **참고**
    - [https://developers.google.com/apis-explorer/#p/translate/v3/](https://developers.google.com/apis-explorer/#p/translate/v3/)

### 5. **모바일 동영상 실행 시, 자동 전체화면 방지**

- 모바일에서 동영상을 시작하면 자동으로 전체화면으로 전환
- 루프 컨트롤을 할 수 없으므로 iframe의 속성을 inline으로 변경

```jsx
<iframe src="....?**playsinline=1" />**
```

- `playsinline`
    - IOS의 HTML5 플레이어에서 동영상을 인라인으로 재생할지 전체화면으로 재생할지 제어
    - `0` : 전체화면 재생 (default)
    - `1` : allowslnlineMediaPlayback 속성과 UIWebViews가 인라인으로 재생

![images/Untitled%202.png](images/Untitled%202.png)

- **참고**
    - [https://uulog.tistory.com/411](https://uulog.tistory.com/411)
    - [https://developers.google.com/youtube/player_parameters](https://developers.google.com/youtube/player_parameters)

### 6. UI

- **grid box**
    - minmax(최소, 최대)
        - 1fr : 한 개의 <div>
    - auto-fit
        - grid 사이즈에 크기를 맞춤

    ```css
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(18rem, 1fr));
    ```

- **vh, vw**
    - 화면의 높이, 너비에 따라 **크기** 변경
- **-webkit-line-clamp**
    - [https://developer.mozilla.org/en-US/docs/Web/CSS/-webkit-line-clamp](https://developer.mozilla.org/en-US/docs/Web/CSS/-webkit-line-clamp)
    - 라인 개수를 넘어가는 글은 ...으로 처리

    ```css
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
    ```

  ![images/Untitled%203.png](images/Untitled%203.png)

### 7. 참고 디자인

- strapi : [https://strapi.io/](https://strapi.io/)
- strapi blog : [https://strapi.io/blog](https://strapi.io/blog)
- favicon : [https://favicon.io/favicon-generator/](https://favicon.io/favicon-generator/)
- figma : [https://www.figma.com/](https://www.figma.com/)

## 백엔드

### 1. 젠킨스 CI/CD 구축

- docker [jenkins/jenkin](https://hub.docker.com/r/jenkins/jenkins) 이미지 사용
    - ~~jenkins~~, ~~jenkinsci/jenkins~~는 deprecated (2018년)
- 이미지 실행

    ```cpp
    docker run -d -p 9090:8080 -v jenkins_home:/var/jenkins_home --name jenkins jenkins/jenkins:lts
    ```

- jenkins 접속

    ```cpp
    localhost:9090
    ```

- **publish over ssh** 플러그인 사용
    - 로컬에서 빌드한 후, jar 파일을 운영서버로 전송

### 2. 구글 api deserialize

- @JsonDeserialize
- [JsonDeserializer<T>](https://fasterxml.github.io/jackson-databind/javadoc/2.8/com/fasterxml/jackson/databind/JsonDeserializer.html)
    - Dto와 Json객체의 관심사를 분리하기 좋다
    - Json의 로직이 Dto와 강하게 연결된 부분을 분리

```java
public class VideoDeserializer extends JsonDeserializer<VideoDto.Res> {

	@Override
	public VideoDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
	
	    ObjectCodec oc = jsonParser.getCodec();
	    JsonNode node = oc.readTree(jsonParser);
	
			return new VideoDto(node.get("items").asText())
	}
}
```

```java
@JsonDeserialize(using = VideoDeserializer.class)
public static class VideoDto {

    private String item;
}
```

### 3. 도메인 호스팅 사이트

- [namecheap](https://www.namecheap.com/)
    - letshadow.net
    - letshadow.me
    - letshadow.org
- ~~GoDaddy~~
    - letshadow.com
    - 고객 서비스 전무
    - 기존의 사용하던 채팅 상담도 제약 (한국서비스는 종료, 미국서비스는 버튼 disable)
    - 한국 미국 전화상담 모두 불가능
    - 만료기간에 맞춰서 namecheap으로 이전 계획

### 4. SSL/TLS certificate 등록

- 프론트엔드 : netlify에서 자동 등록
- 백엔드 : certbot

### 5. Google OAuth

[Google OAuth 동의화면 등록](https://www.notion.so/Google-OAuth-a21a7d075f3e4e27b33a084f79c3a5e3)

- OAuth consent screen
    - youtube data api는 [sensitive scope](https://support.google.com/cloud/answer/9110914?hl=en) 사용
    - 구글에서 요구하는 준비사항을 모두 만족한 이후 verification 작업을 통해 sensitive scope에 대한 인증을 받는다
- Domain verification 방법
    - DNS TXT 방법으로 [www.letshadow.net](http://www.letshadow.net) 확인
    - 도메인 공급업체(Namecheap)에 구글이 제공하는 DNS TXT 코드 입력
    - 설정 방법 : [https://www.namecheap.com/support/knowledgebase/article.aspx/9252/2208/how-do-i-add-my-domain-to-google-sites/](https://www.namecheap.com/support/knowledgebase/article.aspx/9252/2208/how-do-i-add-my-domain-to-google-sites/)
    - 공급업체는 Namecheap, DNS는 netlify에 있으므로 netlify에서 DNS 세팅
    - 인증 후 search console로 가는 링크 클릭

  ![images/Untitled%204.png](images/Untitled%204.png)

### 5. **youtube data api**

[https://developers.google.com/youtube/v3/docs/search/list](https://developers.google.com/youtube/v3/docs/search/list)

요청

```yaml
https://www.googleapis.com/youtube/v3/search? \
key=AIzaSyDUGzvwaAESxzaSO2Lr2iXseACgzjdV3mM& \
q=영국남자& \
maxResults=25& \
videoCaption=closedCaption& \
type=video& \
nextPageToken=CBkQAA& \
part=snippet \
```

응답

```yaml
{
    "kind": "youtube#searchListResponse",
    "etag": "P5lWNKxAoL0mynXqBdROJ1Gf5Mk",
    "nextPageToken": "CBkQAA",
    "regionCode": "KR",
    "pageInfo": {
        "totalResults": 1000000,
        "resultsPerPage": 25
    },
    "items": [
        {
            "kind": "youtube#searchResult",
            "etag": "lpa5gzszQEjib2ioDRlBB8JLHqo",
            "id": {
                "kind": "youtube#video",
                "videoId": "eMygau1Yy_0"
            },
            "snippet": {
                "publishedAt": "2016-11-30T11:29:33Z",
                "channelId": "UCg-p3lQIqmhh7gHpyaOmOiQ",
                "title": "🐖삼겹살을 처음 먹어본 영국인들의 반응!?!🐖",
                "description": "이 전편: https://youtu.be/BiTLL8uOVQ8 안녕하세요! 이번 편은 여러분들이 기다리고 기다리시던 삼겹살 먹방 입니다!! 영국에선 돼지고기를 많이 먹긴 하지만 좀 밋밋 ...",
                "thumbnails": {
                    "default": {
                        "url": "https://i.ytimg.com/vi/eMygau1Yy_0/default.jpg",
                        "width": 120,
                        "height": 90
                    },
                    "medium": {
                        "url": "https://i.ytimg.com/vi/eMygau1Yy_0/mqdefault.jpg",
                        "width": 320,
                        "height": 180
                    },
                    "high": {
                        "url": "https://i.ytimg.com/vi/eMygau1Yy_0/hqdefault.jpg",
                        "width": 480,
                        "height": 360
                    }
                },
                "channelTitle": "영국남자 Korean Englishman",
                "liveBroadcastContent": "none",
                "publishTime": "2016-11-30T11:29:33Z"
            }
        }
    ]
}
```

### 6. 사용자 계정의 좋아요한 비디오 리스트 API

요청

```cpp
[header]
method: GET
path: /videos
authorization: Bearer
```

응답

```cpp
items: [
	{id: "fB7nyxXaczY", publishedAt: "2019-10-16T14:00:07Z", channelId: "UCV0qA-eDDICsRR9rPcnG7tw",…},…
	{id: "fB7nyxXaczY", publishedAt: "2019-10-16T14:00:07Z", channelId: "UCV0qA-eDDICsRR9rPcnG7tw",…}
		caption: "false"
		channelId: "UCV0qA-eDDICsRR9rPcnG7tw"
		channelTitle: "Joma Tech"
		description: "My brother (https://twitter.com/madavidj) talks about how to successfully leverage Machine Learning in Startups. Learn computer science, math, science, and algorithms at http://brilliant.org/joma (first 200 get 20% off premium).↵↵Apply to AppFolio here:↵https://www.appfolioinc.com/jobs-openings?p=job%2FodduafwO&nl=1↵↵► Learn how to code with Python 3 for Data Science and Software Engineering. High-quality video courses:↵https://python.jomaclass.com/↵↵► Chat with me on Discord:↵https://discord.gg/EQwMWFsmQs↵↵► Support my channel to get bonus content like Private Discord Chat and Extra Videos/Vlogs:↵https://www.youtube.com/c/JomaOppa/join↵↵► Resume Template and Cover letter I used for applying to software internships and full-time jobs:↵https://resume.joma.io↵↵↵► Traveling? ↵Save $55 for Airbnb: https://www.airbnb.com/c/jma366?currency=USD↵Save $6 for Uber: https://www.uber.com/invite/jonathanm35052ue↵Save $5 for Lyft: https://www.lyft.com/ici/MA45788↵► Social Media↵https://www.instagram.com/jomaoppa/↵https://twitter.com/jomaoppa↵https://www.facebook.com/jomaoppa↵↵► My Gear↵Laptop - https://amzn.to/2GN6IqD↵Ultrawide Monitor - https://amzn.to/2YBFp7W↵Main Camera - http://amzn.to/2Fs1JeX↵Main Lens  - http://amzn.to/2IkeYwm↵Wide lens - http://amzn.to/2DgzIRD↵Mic I use - http://amzn.to/2p8gZmj↵Gorilla Pod - http://amzn.to/2oZZeX8"
		duration: "PT29M20S"
		id: "fB7nyxXaczY"
		publishedAt: "2019-10-16T14:00:07Z"
		thumbnails: {standard: {url: "https://i.ytimg.com/vi/fB7nyxXaczY/sddefault.jpg", width: 640, height: 480},…}
		title: "Interview With My Brother Who Sold His Startup For $60 Million | Machine Learning Engineer"
	}
]
nextPageToken: "CB4QAA"
resultsPerPage: 30
totalResults: 1528
```

### 7. 사용자 계정 상세 API

요청

```cpp
[header]
method: GET
path: /api/v1/userInfo
authorization: Bearer 
```

응답

```cpp
{
	email: "popo8682@gmail.com"
	name: "Byeonggil Park"
	picture: "https://lh3.googleusercontent.com/a-/AOh14Gi-EsFMl85CqCbx8LzBgPBOSMxkuZgASurJjyJqxII=s96-c"
}
```

### 8. SSL 설정

- certbot 사용

```cpp
// epel 설치
sudo amazon-linux-extras install epel -y 
sudo yum-config-manager --enable epel
// certbot 설치
sudo yum install -y certbot
// python-certbot-nginx 설치
sudo yum install -y python-certbot-nginx 
// nginx certbot 실행
sudo yum certbot --nginx
```

nginx ssl 설정

```cpp
server_name www.letshadow.me letshadow.me

// certbot이 자동으로 생성
ssl_certificate 
ssl_certificate_key 
ssl_session_cache 
ssl_session_timeout
ssl_ciphers 

location / {
	proxy_pass http://localhost:8080;
}
```

### 9. visualvm 모니터링

- 초기단계라 모니터링에 크게 비중을 두지 않겠지만 예기치 못한 성능이슈나 에러에 대비하기 위해 실행
- visualvm 사용
    - JDK8에서 JVM의 JMX를 기본으로 제공
    - [JMX를 사용하여 원격에서 모니터링 가능](https://bgpark.tistory.com/139)
    - 추가적인 큰 리소스 사용이 없어서 개발 초기에 용이
- ~~Springboot Admin~~
    - 추가로 스프링 어드민 어플리케이션이 필요
- ~~scouter~~
    - 오픈소스
    - scouter agent를 설치할 추가 리소스 필요

## 개선방향

### 프론트엔드

1. **디자인의 일관성을 위해 디자인 프로토타입 툴의 필요성을 느낀다**
    - Figma 고려
        - 프로토타입핑 이후, css 속성을 가져오기 편하다
        - 모바일, 데스크탑용 프레임을 따로 제공한다
    - Figma로 프로토타입한 이후 atom design 고려
        - template, atom, .. 분리
        - 컴포넌트들을 분리 가능하다
        - 컴포넌트들의 재활용률 증가한다
        - 디렉토리 관리가 용이하다
2. **React, Redux로 이전**
    - 시간이 지날 수록 기존의 순수 자바스크립트로 개발하는 비용증가
    - React로 여러 컴포넌트들을 재활용하여 사용할 수 있다
    - rendering할 부분만 rendering하여 모든 컴포넌트들이 한꺼번에 render되는 문제 해결
    - 프레임워크 중에서 라이브러리가 많이 제공되어 개발 시간 단축에 용이
    - Redux로 state 관리 용이
        - redux saga로 비동기 state 관리
        - redux toolkit으로 redux 코드의 복잡도 제거
    - Next.js로 SSR 고려
        - 순수 React의 SSR 세팅의 비용이 매우 크다
        - 유지 보수에도 힘들다
        - 하지만 Next만의 무거움이 단점이다
3. **Typescript 도입**
    - 컴파일 단계에서 발생할 수 있는 문법오류 방지
4. **React Testing Library 도입**
    - jest 방식의 테스팅 프레임워크
    - 리액트와 매우 호환이 잘되어 있다
    - UI 테스트를 하는데도 큰 문제가 없다
        - 하지만, **UI 테스트는 최종적인 텍스트와 배치가 중요하다고 생각**한다.
        - UI 테스트는 여전히 코드에 종속되어 있어 테스트 유지 비용이 많이 듦
        - UI 테스트는 중요한 로직에 대해서만 테스트하고 추후 **Cypress로 이전**
5. **모듈 분리**
    - React로 전환시 현재 사용하고 있는 **Youtube-Loop**(가제), **Youtube-transcript**(가제)는 npm으로 publish하여 사용
    - 책임분리, 관심사 분리
    - Typescript, webpack을 사용
        - type safe하게 작성
        - class로 전환, ES6 문법사용 (explore)

### 백엔드

모니터링 도구

- [https://torreswoo.github.io/posts/springboot-jolokia/](https://torreswoo.github.io/posts/springboot-jolokia/)

## 작성자

- 박병길
- email : bgpark82@gmail.com
- github : www.github.com/bgpark82

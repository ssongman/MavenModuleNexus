# Nexus를 활용한 Maven Module



Nexus를 활용한 Maven Module 활용법을 살펴본다.

또한 Module 이 버젼업시에 해당 모듈을 Main 프로젝트에서 자동으로 참고하는 방법에 대해서 살펴본다.






# 1. Nexus 셋팅



## 1) Nexus 설치

설치과정 생략

nexus 링크 : http://nexus.ssongman.duckdns.org/#admin/security/users





## 2) maven repo 추가

Maven repo 추가 과정 생략



## 3) Role 생성

- 메뉴 : Administration > Security > Roles
- ID :  ssongman-admin-role
- 필터란에 생성한 레파지토리명으로 검색해서 **nx-repository-view-…** 로 시작하는 권한들 추가
- 해당 Role로 레파지토리에 업로드도 할 수 있게 하기 위해 **nx-component-upload** 권한 추가





## 4) User 생성

- 메뉴 : Administration > Security > Users
- ID : ssongman-admin
- pass : new****!
- Roles : ssongman-admin-role





# 2. Module Project

간단한 module 을 개발후 Nexus 에 배포해보자.



## 1) Module src

https://github.com/ssongman/MavenModuleNexus/tree/main/SampleSrc/airport-core





## 2) settings.xml 설정



```xml
# 저장위치
# C:\Users\ssong\.m2\settings.xml

---
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
 http://maven.apache.org/xsd/settings-1.0.0.xsd">

 <servers>
   <server>
     <id>ssongman-repo</id>
     <username>ssongman-admin</username>
     <password>new****!</password>
   </server>
 </servers>
</settings>
---
```





## 3) pom.xml

nexus repository에 업로드 하기 위해 pom.xml

```xml
<distributionManagement>
    <repository>
      <id>ssongman-repo</id>
      <name>ssongman nexus repository</name>
      <url>http://nexus.ssongman.duckdns.org/repository/ssongman-repo/</url>
    </repository>
</distributionManagement>

```

- id : settings.xml에서 설정했던 id와 동일해야함.
- name : 사람이 식별 할 수 있게 설정.
- url : 생성한 repository의 url



## 4) Deploy

해당 모듈을 compile 후 nexus 에 배포해보자.

```sh
$ mvn clean compile deploy

...
[INFO] --- deploy:3.1.1:deploy (default-deploy) @ airport-core ---
Uploading to ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/0.0.6/airport-core-0.0.6.pom
Uploaded to ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/0.0.6/airport-core-0.0.6.pom (1.2 kB at 1.8 kB/s)
Uploading to ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/0.0.6/airport-core-0.0.6.jar
Uploaded to ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/0.0.6/airport-core-0.0.6.jar (3.6 kB at 50 kB/s)
Downloading from ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/maven-metadata.xml
Uploading to ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/maven-metadata.xml
Uploaded to ssongman-repo: http://nexus.ssongman.duckdns.org/repository/ssongman-repo/com/ssongman/airport/airport-core/maven-metadata.xml (312 B at 5.2 kB/s)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  14.085 s
[INFO] Finished at: 2023-10-08T13:44:33+09:00
[INFO] ------------------------------------------------------------------------
```



nexus 확인

```
http://nexus.ssongman.duckdns.org/#browse/browse:ssongman-repo:com%2Fssongman%2Fairport%2Fairport-core%2F0.0.6

```





## 5) Deploy(version 명시)

mvn 명령 수행시 version 명시하기 위해서는 properties 기능을 이용한다.

 

* pom.xml

```xml
...
<groupId>net.test</groupId>
<artifactId>test-parent</artifactId>
<version>${revision}</version>
...
<properties>
  <revision>0.1.0</revision>
</properties>
...


```



* deploy

```sh
$ mvn -Drevision=0.1.0  clean compile deploy
$ mvn -Drevision=0.1.1  clean compile deploy
$ mvn -Drevision=0.1.2  clean compile deploy
$ mvn -Drevision=0.1.3 -DskipTests clean compile deploy
$ mvn -Drevision=0.1.4 -DskipTests clean compile deploy
$ mvn -Drevision=0.1.5 -DskipTests clean compile deploy


mvn -Drevision=0.1.5.0  -DskipTests clean compile deploy
mvn -Drevision=0.1.5.9  -DskipTests clean compile deploy
mvn -Drevision=0.1.5.10 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.11 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.12 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.13 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.14 -DskipTests clean compile deploy

mvn -Drevision=0.1.6.0 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.1 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.3 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.4 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.5 -DskipTests clean compile deploy

mvn -Drevision=0.1.5.15 -DskipTests clean compile deploy

# 순서를 거꾸로 해보자.
mvn -Drevision=0.1.6.7 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.6 -DskipTests clean compile deploy

# 역시 0.1.6.7 이 max 값으로 잘 인식한다.

# 이제는 특정값으로
mvn -Drevision=0.1.6.7-Beta1 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.8 -DskipTests clean compile deploy
mvn -Drevision=0.1.6.9 -DskipTests clean compile deploy



```



## 6) Deploy(oracle문서비교)

* 아래 버젼이 올바르게 인식하는지 확인해 보자. (오라클문서와 비교)
  * https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN401

```sh
1.0.1.0
1.0.9.3
1.0.10.1
1.0.10.2


# 오라클 문서에는 아래처럼 정렬된다고 한다.
1.0.1.0
1.0.10.1
1.0.10.2
1.0.9.3
# Version 1.0.9.3 should come before 1.0.10.1 and 1.0.10.2, 
# but the unexpected fourth field (.3) forced Maven to evaluate the version as a string.


# deploy
mvn -Drevision=1.0.1.0  -DskipTests clean compile deploy
mvn -Drevision=1.0.9.3  -DskipTests clean compile deploy
mvn -Drevision=1.0.10.1 -DskipTests clean compile deploy
mvn -Drevision=1.0.10.2 -DskipTests clean compile deploy
mvn -Drevision=1.0.9.4 -DskipTests clean compile deploy


```









# 3. Main Project

업로드된 모듈을 사용하는 AP를 개발 및 설정해 보자.



## 1) AP src

https://github.com/ssongman/MavenModuleNexus/tree/main/SampleSrc/airport-api



## 2) pom.xml

pom.xml에 아래와 같은 설정을 추가한다.

* pom.xml

```xml
<!-- 모듈화되서 업로드된 도메인 프로젝트 -->

		<dependency>
		  <groupId>com.ssongman.airport</groupId>
		  <artifactId>airport-core</artifactId>
		  <version>0.0.7</version>
		</dependency>

...


	<!-- 라이브러리가 업로드된 Nexus Repository 정보 -->
	<repositories>
	  <repository>
	      <id>ssongman-repo</id>
	      <name>ssongman nexus repository</name>
	      <url>http://nexus.ssongman.duckdns.org/repository/ssongman-repo/</url>
	  </repository>
	</repositories>

```

- dependency에 추가
- 라이브러리가 업로드된 nexus repository의 정보를 입력





## 3) 실행 후 테스트

#### 실행

```sh
STS 실행

```



#### 테스트

```sh
$ curl localhost:8081/api/health

$ curl localhost:8081/api/planes
```





## 4) maven version range

모듈이 버젼업 된다면 해당 모듈을 참고하는 Main AP 에서도 버젼을 변경해야 하는 번거러움이 존재한다.

이런 번거러움을 해결하기 위해 version range 방법을 이요하여 항상 모듈의 최신버젼을 참고하도록 설정해보자.

참고링크 : https://maven.apache.org/enforcer/enforcer-rules/versionRanges.html



### (1) [참고] version range

RequireMavenVersion 및 RequireJavaVersion 규칙은 사용 편의성을 위해 한 가지 사소한 변경 사항이 포함된 표준 Maven 버전 범위 구문을 사용합니다(*로 표시).

| Range         | Meaning                                                 |
| ------------- | ------------------------------------------------------- |
| 1.0           | x >= 1.0 *                                              |
| (,1.0]        | x <= 1.0                                                |
| (,1.0)        | x < 1.0                                                 |
| [1.0]         | x == 1.0                                                |
| [1.0,)        | x >= 1.0                                                |
| (1.0,)        | x > 1.0                                                 |
| (1.0,2.0)     | 1.0 < x < 2.0                                           |
| [1.0,2.0]     | 1.0 <= x <= 2.0                                         |
| (,1.0],[1.2,) | x <= 1.0 or x >= 1.2. Multiple sets are comma-separated |
| (,1.1),(1.1,) | x != 1.1                                                |





### (2) range 지정

```xml
<!-- 모듈화된 업로드된 도메인 프로젝트 -->
		<dependency>
		  <groupId>com.ssongman.airport</groupId>
		  <artifactId>airport-core</artifactId>
		  <version>[0.0.5,)</version>
		</dependency>
...
```

- dependency에 추가
- 라이브러리가 업로드된 nexus repository의 정보를 입력
- version
  - [0.0.5,)     :      x >= 0.0.5





### (3) 최신버젼 작동원리

최신버젼이 셋팅되는 원리를 살펴보자.

테스트 결과 .m2 캐쉬의 내용이 테스트 방법에 따라 다르다는 것을 알 수 있었다.

테스트 하는 방법에 따른 .m2 캐쉬 의 반응을 살펴보면...

* pom.xml 에 특정 버젼을 명시하는 방법으로 수행시
  * 해당 버젼의 모듈만 가져온다.
* pom.xml에 version range 로 셋팅후 수행시
  * 범위에 따른 모든 버젼의 모듈을 가져온다.
  * metadat.xml 파일을 가져온다.



그러므로 range 내에서 아래 metadata 파일을 가져온 이후 최신버젼을 셋팅하도록 구성되는 듯 하다.



maven-metadata.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<metadata>
  <groupId>com.ssongman.airport</groupId>
  <artifactId>airport-core</artifactId>
  <versioning>
    <latest>0.1.3</latest>
    <release>0.1.6.8</release>
    <versions>
      <version>0.0.6</version>
      <version>0.0.7</version>
      <version>0.0.9</version>
      <version>0.0.10</version>
      <version>0.0.11</version>
      <version>0.1.0</version>
      <version>0.1.2</version>
      <version>0.1.3</version>
      <version>0.1.4</version>
      <version>0.1.5</version>
      <version>0.1.5.0</version>
      <version>0.1.5.9</version>
      <version>0.1.5.10</version>
      <version>0.1.5.11</version>
      <version>0.1.5.13</version>
      <version>0.1.5.14</version>
      <version>0.1.6.0</version>
      <version>0.1.6.1</version>
      <version>0.1.6.3</version>
      <version>0.1.6.4</version>
      <version>0.1.6.5</version>
      <version>0.1.5.15</version>
      <version>0.1.6.7</version>
      <version>0.1.6.6</version>
      <version>0.1.6.7-Beta1</version>
      <version>0.1.6.8</version>
    </versions>
    <lastUpdated>20231008144407</lastUpdated>
  </versioning>
</metadata>
```





### (4) 최신버젼 적용

신규버젼 모듈이 nexus에 upload 되었는데 Local PC Main AP소스에서는 아무런 이벤트 없이 해당 모듈의 최신  버젼을 자동으로 셋팅하지는 않는다. 

* nexus에 업로드 된 airport-core 버젼업
  * airport-core-0.1.6.0
  * airport-core-0.1.6.1   <-- 신규로 upload 됨
* Main AP Maven Dependencies 확인
  * airport-core-0.1.6.0.jar  파일로 유지 됨.



아래와 같이 특정 이벤트가 있어야지만 자동 반영이 된다.

* STS Update Maven Project (Alt + F5)
* pom.xml 파일 수정
* STS 재기동시 새롭게 반영됨



하지만, Jenkins를 활용한 CI에서는 당연히 이벤트가 발생할 것이므로 최신버젼을 잘 가져올 것이라고 판단된다. 

(가져오지 못하는 상황은 아지 확인 전임)

그럼에도 불구하고 CICD 자동화 스크립트시 좀더 확실하게 최신버젼을 가져올 수 있도록 강제로 update 하는 부분을 찾아본다.



eclipse 를 재기동 하지 않고 최신버젼을 가져올 수 있는 CLI 명령을 찾아보자.

```sh
mvn clean -U   <-- 안된다.

mvn install -U   <-- 안된다.

mvn clean install -U   <-- 안된다.

mvn -X clean install -U   <-- 안된다.

mvn dependency:resolve   <-- 안된다.

mvn dependency:resolve -U  <-- 안된다.

mvn dependency:resolve -U -Dmaven.wagon.http.ssl.insecure=true  <-- 안된다.


# refresh workspace 

mvn dependency:purge-local-repository  <-- 안된다.
mvn dependency:resolve -U
mvn clean install -U

mvn dependency:purge-local-repository clean install  <-- 안된다.
mvn clean install -e -U -Dmaven.test.skip=true

```



#### 오류 발생

nexus 에는 새로운 버젼이 올라가 있지만 최신버젼을 가져오지 못하는 상황에서는 아래와 같이 에러 발생한다.

"Could not transfer artifact ..."  



```sh
$ mvn clean install -U

[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< com.ssongman.airport:airport-api >------------------
[INFO] Building airport-api 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
Downloading from central: https://repo.maven.apache.org/maven2/com/ssongman/airport/airport-core/maven-metadata.xml
[WARNING] Could not transfer metadata com.ssongman.airport:airport-core/maven-metadata.xml from/to maven-default-http-blocker (http://0.0.0.0/): Blocked mirror for repositories: [ssongman-repo (http://nexus.ssongman.duckdns.org/repository/ssongman-repo/, default, releases+snapshots)]
Downloading from central: https://repo.maven.apache.org/maven2/com/ssongman/airport/airport-core/0.1.3/airport-core-0.1.3.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.517 s
[INFO] Finished at: 2023-10-08T15:33:40+09:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal on project airport-api: Could not resolve dependencies for project com.ssongman.airport:airport-api:jar:0.0.1-SNAPSHOT: Failed to collect dependencies at com.ssongman.airport:airport-core:jar:0.1.3: Failed to read artifact descriptor for com.ssongman.airport:airport-core:jar:0.1.3: Could not transfer artifact com.ssongman.airport:airport-core:pom:0.1.3 from/to maven-default-http-blocker (http://0.0.0.0/): Blocked mirror for repositories: [ssongman-repo (http://nexus.ssongman.duckdns.org/repository/ssongman-repo/, default, releases+snapshots)] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/DependencyResolutionException

```



No versions available for com.ssongman.airport:airport-core:jar:[0.1.0,) within specified range -> [Help 1] 

```
[ERROR] Failed to execute goal on project airport-api: Could not resolve dependencies for project com.ssongman.airport:airport-api:jar:0.0.2.4: Failed to collect dependencies at com.ssongman.airport:airport-core:jar:[0.1.0,): No versions available for com.ssongman.airport:airport-core:jar:[0.1.0,) within specified range -> [Help 1]      
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:        
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/DependencyResolutionException

```

어떻게든 최신버젼을 매핑하는 방법을 찾아야 한다.





## 5) version maven plugin

Version 관리하는 plugin 을 활용하는 방법을 검토해 보자.

Version Maven Plugin 은 POM의 artifacts 의 Version 을 관리하는 경우 사용되며 MojoHaus 에서 제공된다.

MojoHaus 프로젝트는 Apache Maven용 플러그인 모음이다.

관련링크 : https://www.mojohaus.org/versions/versions-maven-plugin/index.html



### version plugin 지정

먼저 pom.xml plugin 을 지정한다.

```xml

	<build>
		<plugins>
            
			<!-- To use the plugin goals in your POM or parent POM -->
	        <plugin>
	          <groupId>org.codehaus.mojo</groupId>
	          <artifactId>versions-maven-plugin</artifactId>
	          <version>2.15.0</version>
	        </plugin>

		</plugins>
	</build>
```

* 굳이 선언하지 않아도 아래 명령어들이 잘 작동되었다.   왜지?



몇몇 유용한 plugin 들을 살펴보자.



### version:resolve-ranges

range 로 지정된 pom.xml 을 조건에 맞는 버젼(최신버젼)을 가져와 셋팅하고 pom.xml 파일이 수정된다.

기존 파일은 "pom.xml.versionsBackup" 로 보관다. 

이후 commit 을 수행하게 되면 versionsBackup 파일은 삭제 된다.

````sh
# 수행전
...
		<dependency>
		  <groupId>com.ssongman.airport</groupId>
		  <artifactId>airport-core</artifactId>
		  <version>[0.0.5,)</version>
		</dependency>
...



$ mvn versions:resolve-ranges
# 잘 반영됨
# Total time:  21.700 s



# 수행후 - version이 최신버젼으로 변경됨.
...
		<dependency>
		  <groupId>com.ssongman.airport</groupId>
		  <artifactId>airport-core</artifactId>
		  <version>0.1.6.5</version>
		</dependency>
...



# 변경 범위를 축소시킬 수 있다. -  groupID 이용
$ mvn versions:resolve-ranges -Dincludes=com.ssongman.airport:airport-core
# Total time:  7.980 s

$ mvn versions:resolve-ranges -Dincludes=com.ssongman.airport:*




# commit - versionsBackup 삭제된다.
$ mvn versions:commit

# rollback - versionsBackup 파일로 원복후 파일은 삭제된다.
$ mvn versions:revert

````



* Jenkins CI 에서의 사용방안
  * resolve-ranges 기능을 이용해 최신버젼을 set하여 build 한다.
  * 이후 git  에 commit push 는 하지 않는다.(Build 시에만 사용한다.)
  * 단점
    * resolve-ranges 수행하는 시간만큼 더 소요된다.
    * 약 10초 소요(pom.xml 의 양에 따라 달라짐)



### version:set (project version)

project version을 변경 할 수 있다.

당연히 ${project.version} 를 사용하는 부분도 같이 변경된다.

실제로 pom.xml 이 변경된다.

```sh
# 수행전
...
	<groupId>com.ssongman.airport</groupId>
	<artifactId>airport-api</artifactId>
    <version>0.0.2.0</version>
	<name>airport-api</name>
...



$ mvn versions:set -DnewVersion=0.0.2.6


# 수행후
...
	<groupId>com.ssongman.airport</groupId>
	<artifactId>airport-api</artifactId>
    <version>0.0.2.6</version>
	<name>airport-api</name>
...



# commit - versionsBackup 삭제된다.
$ mvn versions:commit

# rollback - versionsBackup 파일로 원복후 파일은 삭제된다.
$ mvn versions:revert

```



clean / install 과 같이 사용 하지 말자.

```sh
# version 이 변경된 후 clean / install 해야 할텐데 그렇지 않다.
# 변경전 값으로 clean install 된다.
$ mvn versions:set -DnewVersion=0.0.2.6 -DskipTests clean install -U



# 굳이 사용하려면 아래와 같이 나눠서 사용하자.
$ mvn versions:set -DnewVersion=0.0.2.6
$ mvn -DskipTests clean install -U

```

* 그냥 revision 방식으로 한방 command 가 빠를 듯... ★★★

  mvn -X -Drevision=0.0.2.6 -DskipTests clean install -U





### version:display-plugin-updates

빌드에서 사용중인 plugin 버젼중 새로운 버젼을 보여준다.

```sh
$ mvn versions:display-plugin-updates

# 굳이...
```





### version:display-dependency-updates

사용중인 라이브러리들 update 대상 버젼 목록 추출 한다.

```sh
# 
$ mvn versions:display-dependency-updates
...
[INFO]   org.jetbrains.kotlin:kotlin-test-common ....... 1.8.22 -> 1.9.20-Beta2
[INFO]   org.jetbrains.kotlin:kotlin-test-js ........... 1.8.22 -> 1.9.20-Beta2
[INFO]   org.jetbrains.kotlin:kotlin-test-junit ........ 1.8.22 -> 1.9.20-Beta2
[INFO]   org.jetbrains.kotlin:kotlin-test-junit5 ....... 1.8.22 -> 1.9.20-Beta2
[INFO]   org.jetbrains.kotlin:kotlin-test-testng ....... 1.8.22 -> 1.9.20-Beta2
...


# 특정 접미사 무시
$ mvn versions:display-dependency-updates "-Dmaven.version.ignore=.*-M.*,.*-Beta2"


$ mvn org.codehaus.mojo:versions-maven-plugin:display-dependency-updates "-Dmaven.version.ignore=.*-M.*,.*-alpha.*"


```

* 가끔씩 사용하면 유용할듯...



### version:display-property-updates

```sh
$ mvn versions:display-property-updates
[INFO] Scanning for projects...
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.16.1/versions-maven-plugin-2.16.1.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.16.1/versions-maven-plugin-2.16.1.jar (291 kB at 505 kB/s)
[INFO] 
[INFO] ------------------< com.ssongman.airport:airport-api >------------------
[INFO] Building airport-api 0.0.2.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- versions:2.16.1:display-property-updates (default-cli) @ airport-api ---
[INFO] 
[INFO] This project does not have any properties associated with versions.
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.937 s
[INFO] Finished at: 2023-10-08T23:13:50+09:00
[INFO] ------------------------------------------------------------------------

```





## 6) Install(version 명시)

pom.xml

```xml
...
<groupId>net.test</groupId>
<artifactId>test-parent</artifactId>
<version>${revision}</version>

<properties>
  <revision>0.1.0</revision>
</properties>
...


```



deploy

```sh
mvn -Drevision=0.0.2.0 -DskipTests clean compile 

mvn -Drevision=0.0.2.0 -DskipTests clean install 
mvn -Drevision=0.0.2.1 -DskipTests clean install 
mvn -Drevision=0.0.2.3 -DskipTests clean install 
mvn -X -Drevision=0.0.2.5 -DskipTests clean install -U
mvn -X -Drevision=0.0.2.6 -DskipTests clean install -U

```











# 4. flattern





## 1) airport-core

```sh
$ mvn -Drevision=1.0.0-SNAPSHOT -DskipTests clean package


$ mvn clean
  mvn -Drevisionsong=2.0.0.2 -DskipTests clean package


$ mvn -Drevision=2.0.0.2 -DskipTests clean package

$ mvn -Drevision=3.1.4 -DskipTests clean package

mvn -Drevision=2.0.0.4 -DskipTests clean install -U
mvn -Drevision=2.0.0.5 -DskipTests clean install -U
mvn -Drevision=2.0.0.6 -DskipTests clean install -U

```





pom.xml  - backup

```xml
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.1.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>



	<parent>
		<groupId>com.ssongman.airport</groupId>
		<artifactId>airport-api</artifactId>
	    <version>2.0.0.0</version>
	</parent>



	<parent>
		<groupId>com.ssongman.airport</groupId>
		<artifactId>airport-api</artifactId>
	    <version>${revision}</version>
	</parent>




	<properties>
		<java.version>17</java.version>
  		<revision>0.1.0</revision>
  		<parentVersion>3.1.4</parentVersion>
	</properties>



	<properties>
		<java.version>17</java.version>
  		<revision>3.1.4</revision>
	</properties>





	<build>
		<plugins>
			<plugin>
		       <groupId>org.codehaus.mojo</groupId>
		       <artifactId>flatten-maven-plugin</artifactId>
		       <version>${flatten.version}</version>
		       <configuration>
		         <updatePomFile>true</updatePomFile>
		         <flattenMode>resolveCiFriendliesOnly</flattenMode>
		       </configuration>
		       <executions>
		         <execution>
		           <id>flatten</id>
		           <phase>process-resources</phase>
		           <goals>
		             <goal>flatten</goal>
		           </goals>
		         </execution>
		         <execution>
		           <id>flatten.clean</id>
		           <phase>clean</phase>
		           <goals>
		             <goal>clean</goal>
		           </goals>
		         </execution>
		       </executions>
		 </plugin> 
 
		</plugins>
	</build>
```









## 2) airport-api

maven

```sh
$ mvn -Drevision=1.0.0-SNAPSHOT -DskipTests clean package


$ mvn clean
  mvn -Drevisionsong=2.0.0.2 -DskipTests clean package


$ mvn -Drevision=2.0.0.2 -DskipTests clean package

$ mvn -Drevision=3.1.4 -DskipTests clean package

mvn -Drevision=2.0.0.4 -DskipTests clean install -U
mvn -Drevision=2.0.0.5 -DskipTests clean install -U
mvn -Drevision=2.0.0.6 -DskipTests clean install -U

```





pom.xml 

```xml
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>${revision}</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>




	<parent>
		<groupId>com.ssongman.airport</groupId>
		<artifactId>airport-api</artifactId>
	    <version>${revision}</version>
	</parent>




	<properties>
		<java.version>17</java.version>
  		<revision>0.1.0</revision>
  		<parentVersion>3.1.4</parentVersion>
	</properties>



	<properties>
		<java.version>17</java.version>
  		<revision>3.1.4</revision>
	</properties>





	<build>
		<plugins>
			<plugin>
		       <groupId>org.codehaus.mojo</groupId>
		       <artifactId>flatten-maven-plugin</artifactId>
		       <version>${flatten.version}</version>
		       <configuration>
		         <updatePomFile>true</updatePomFile>
		         <flattenMode>resolveCiFriendliesOnly</flattenMode>
		       </configuration>
		       <executions>
		         <execution>
		           <id>flatten</id>
		           <phase>process-resources</phase>
		           <goals>
		             <goal>flatten</goal>
		           </goals>
		         </execution>
		         <execution>
		           <id>flatten.clean</id>
		           <phase>clean</phase>
		           <goals>
		             <goal>clean</goal>
		           </goals>
		         </execution>
		       </executions>
		 </plugin> 
 
		</plugins>
	</build>
```







# 5. multi module build 방안

 

N개의 module  과 Main AP 를 이어주는 중간 BOM project 를  생성하여 관리한다.

AP 는 BOM Proejct 를 parent 로 선언하여 사용한다.



## 1) module1 - airport-common

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.1.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	
	<groupId>com.ssongman.airport</groupId>
	<artifactId>airport-common</artifactId>
	<version>${revision}</version>
	<name>airport-common</name>
	<description>Spring Boot Multi-Module Project(Maven)</description>
	
	<properties>
		<java.version>17</java.version>
  		<revision>1.0.0.3</revision>
	</properties>

</project>


```



```sh
$ mvn -Drevision=1.0.0.2 -DskipTests clean install -U
$ mvn -Drevision=1.0.0.3 -DskipTests clean install -U

```









## 2) module2 - airport-core



```xml

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.1.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	
	<groupId>com.ssongman.airport</groupId>
	<artifactId>airport-core</artifactId>
	<version>${revision}</version>
	<name>airport-core</name>
	<description>Demo project for Spring Boot Mult-Module</description>
	
	<properties>
		<java.version>17</java.version>
  		<revision>0.1.0</revision>
	</properties>
	
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	
	<distributionManagement>
	    <repository>
	      <id>ssongman-repo</id>
	      <name>ssongman nexus repository</name>
	      <url>http://nexus.ssongman.duckdns.org/repository/ssongman-repo/</url>
	    </repository>
	</distributionManagement>


</project>


```





```sh
$ mvn -Drevision=2.0.0.5 -DskipTests clean install -U
$ mvn -Drevision=2.0.0.6 -DskipTests clean install -U

```









## 3) icis-bom

module 들을 version range 로 선언하여 최신버젼을 가져오거나

사용자가 지절할 수 있도록 한다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.1.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	
	<groupId>com.ssongman.airport</groupId>
	<artifactId>icis-bom</artifactId>
	<version>dev</version>
	<name>icis-bom</name>
	<description>ICIS TR Top Module</description>
	
	<packaging>pom</packaging>
	
	<properties>
		<java.version>17</java.version>
		<airport-core.version>2.0.0.4</airport-core.version>
  		<airport-common.version>2.0.0.4</airport-common.version>
	</properties>
	
	<dependencyManagement>
		<dependencies>
			
			<dependency>
			  <groupId>com.ssongman.airport</groupId>
			  <artifactId>airport-core</artifactId>
				<version>[2.0.0.0,)</version>
			</dependency>
			
			<dependency>
			  <groupId>com.ssongman.airport</groupId>
			  <artifactId>airport-common</artifactId>
				<version>[1.0.0.0,)</version>
			</dependency>			
	
		</dependencies>
	</dependencyManagement>	
    
    
	<!-- 라이브러리가 업로드된 Nexus Repository 정보 -->
	<repositories>
	  <repository>
	      <id>ssongman-repo</id>
	      <name>ssongman nexus repository</name>
	      <url>http://nexus.ssongman.duckdns.org/repository/ssongman-repo/</url>
	  </repository>
	</repositories>

</project>

```



versions:resolve-ranges 수행하여 max version 을 할당한 후 local repo 에 install 한다.

```sh

$ mvn versions:resolve-ranges
$ mvn clean install

# commit & push 를 하지 않는다.




# commit - versionsBackup 삭제된다.
$ mvn versions:commit

# rollback - versionsBackup 파일로 원복후 파일은 삭제된다.
$ mvn versions:revert

```





## 4) main AP

icis-bom 을 parent 로 받는다.

module 들의 버젼은 bom 프로젝트에서 명시된 버젼들로 override 된다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	
	<parent>
		<groupId>com.ssongman.airport</groupId>
		<artifactId>airport-bom</artifactId>
		<version>dev</version>
	</parent>
	
	<artifactId>airport-api</artifactId>
    <version>${revision}</version>
	<name>airport-api</name>
	<description>Spring Boot Multi-Module Project(Maven)</description>
	
	<properties>
		<java.version>17</java.version>
  		<revision>0.0.2.0</revision>
	</properties>
	
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		
		
		<dependency>
		  <groupId>com.ssongman.airport</groupId>
		  <artifactId>airport-core</artifactId>
		</dependency>
			
		<dependency>
		  <groupId>com.ssongman.airport</groupId>
		  <artifactId>airport-common</artifactId>
		</dependency>

	</dependencies>
	

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			
			<!-- To use the plugin goals in your POM or parent POM -->
	        <plugin>
	          <groupId>org.codehaus.mojo</groupId>
	          <artifactId>versions-maven-plugin</artifactId>
	        </plugin>
        
		</plugins>
	</build>

</project>

```





```sh
$ mvn -Drevision=2.0.0.5 -DskipTests clean install -U
$ mvn -Drevision=2.0.0.6 -DskipTests clean install -U

```






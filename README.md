# MavenModuleNexus
Nexus를 활용한 Maven Module







# Nexus를 활용한 Maven Module



# 1. Nexus 셋팅



## 1) Nexus 설치

http://nexus.ssongman.duckdns.org/#admin/security/users







## 2) maven repo 추가



## 3) Role 생성

메뉴 : Administration > Security > Roles

ID :  ssongman-admin-role



필터란에 생성한 레파지토리명으로 검색해서 **nx-repository-view-…** 로 시작하는 권한들을 넣어주고, 해당 Role로 레파지토리에 업로드도 할 수 있게 하기 위해 **nx-component-upload** 권한도 넣어준다.





## 4) User 생성

메뉴 : Administration > Security > Users

ID : ssongman-admin

pass : new1234!

Roles : ssongman-admin-role





# 2. library 업로드



## 1) settings.xml 설정



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
     <password>new1234!</password>
   </server>
 </servers>
</settings>
---
```





## 2) pom.xml

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



## 3) library 배포



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





## 4) version 명시 deploy

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

$ mvn -Drevision=0.1.0  clean compile deploy
$ mvn -Drevision=0.1.1  clean compile deploy
$ mvn -Drevision=0.1.2  clean compile deploy
$ mvn -Drevision=0.1.3 -DskipTests clean compile deploy
$ mvn -Drevision=0.1.4 -DskipTests clean compile deploy
$ mvn -Drevision=0.1.5 -DskipTests clean compile deploy



mvn -Drevision=0.1.5.0 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.9 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.10 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.11 -DskipTests clean compile deploy
mvn -Drevision=0.1.5.12 -DskipTests clean compile deploy





############# 참고 ##############################


$ mvn -Drevision=0.1.1 clean

$ mvn -Drevision=0.1.1 clean package



```













# 3. 업로드된 library 사용

업로드된 라이브러리를 사용하기 위해서 사용하고자 하는 프로젝트의 pom.xml에 아래와 같은 설정을 추가해준다.



## 1) pom.xml

사용하고자 하는 프로젝트의 pom.xml

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





## 2) 실행





## 3) maven version range

참고링크 : https://maven.apache.org/enforcer/enforcer-rules/versionRanges.html



RequireMavenVersion 및 RequireJavaVersion 규칙은 사용 편의성을 위해 한 가지 사소한 변경 사항이 포함된 표준 Maven 버전 범위 구문을 사용합니다(*로 표시).

| Range         | Meaning                                                      |
| ------------- | ------------------------------------------------------------ |
| 1.0           | x >= 1.0 * The default Maven meaning for 1.0 is everything (,) but with 1.0 recommended. Obviously this doesn't work for enforcing versions here, so it has been redefined as a minimum version. |
| (,1.0]        | x <= 1.0                                                     |
| (,1.0)        | x < 1.0                                                      |
| [1.0]         | x == 1.0                                                     |
| [1.0,)        | x >= 1.0                                                     |
| (1.0,)        | x > 1.0                                                      |
| (1.0,2.0)     | 1.0 < x < 2.0                                                |
| [1.0,2.0]     | 1.0 <= x <= 2.0                                              |
| (,1.0],[1.2,) | x <= 1.0 or x >= 1.2. Multiple sets are comma-separated      |
| (,1.1),(1.1,) | x != 1.1                                                     |





### (1) range 지정

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





### (2) 최신버젼 적용

최신버전을 가져오기 위해서는 maven update 가 되어야 한다.

실제로 "update maven project"  메뉴 실행으로 최신화 되었다.

```sh
mvn clean -U   <-- 안된다.

mvn install -U   <-- 안된다.

mvn clean install -U   <-- 안된다.

mvn -X clean install -U   <-- 안된다.

mvn dependency:resolve   <-- 안된다.

mvn dependency:resolve -U  <-- 안된다.

mvn dependency:resolve -U -Dmaven.wagon.http.ssl.insecure=true 


# refresh workspace 

mvn dependency:purge-local-repository  <-- 안된다.
mvn dependency:resolve -U
mvn clean install -U

mvn dependency:purge-local-repository clean install  <-- 안된다.
mvn clean install -e -U -Dmaven.test.skip=true

```







새로운 version 을 가져와야 하는 상황에서는 아래와 같이 

"Could not transfer artifact ..."  에러 발생한다.



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





실행



```sh
$ curl localhost:8081/api/health

$ curl localhost:8081/api/planes

```







## 4) version 명시 deploy

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


```




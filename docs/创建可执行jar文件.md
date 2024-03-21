
Spring项目创建可执行jar的5种方法

1. 使用maven-jar-plugin和maven-dependency-plugin插件
pom依赖如下
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.tekin</groupId>
    <artifactId>spring_demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring.version>5.3.29</spring.version>
    </properties>
    <dependencies>
        <!-- https://mavenlibs.com/maven/dependency/org.springframework/spring-core -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!-- https://mavenlibs.com/maven/dependency/org.springframework/spring-context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>

    </dependencies>

    <build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <!-- 指定所有依赖的jar包的输出路径 -->
                            <outputDirectory>
                                ${project.build.directory}/libs
                            </outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>libs/</classpathPrefix>
                            <!-- 这里写入你的可执行Class的Reference 即包含main入口类的Reference -->
                            <mainClass>cn.tekin.MyApp</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>


</project>
~~~

- 构建jar命令
这里构建的jar文件是不包含依赖文件的, 所有的依赖文件全部位于libs目录下
~~~sh
mvn package
~~~

- jar包中的清单文件 META-INF/MANIFEST.MF
生成的MANIFEST.MF内容如下
~~~mf
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Built-By: Tekin
Class-Path: libs/spring-core-5.3.29.jar libs/spring-jcl-5.3.29.jar lib
 s/spring-context-5.3.29.jar libs/spring-aop-5.3.29.jar libs/spring-be
 ans-5.3.29.jar libs/spring-expression-5.3.29.jar
Created-By: Apache Maven 3.6.3
Build-Jdk: 1.8.0_362
Main-Class: cn.tekin.MyApp
~~~

- 执行jar
java -jar target/spring_demo.jar



## 2. 使用Apache Maven Assembly Plugin插件

~~~xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
            <configuration>
                <archive>
                <manifest>
                    <mainClass>
                        cn.tekin.MyApp
                    </mainClass>
                </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </execution>
    </executions>
</plugin>
~~~
注意这里省略的其他配置,其他配置见1













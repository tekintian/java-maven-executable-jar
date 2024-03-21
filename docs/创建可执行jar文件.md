
java maven项目创建可执行jar的6种方法,可以用于任何JAVA项目包含spring, springboot项目


## 1. 使用maven-jar-plugin和maven-dependency-plugin插件
优点: 处理过程透明，我们可以自定义每个步骤
不足: 手动; 依赖项不在最终的jar中，这意味着只有当libs文件夹对jar是可访问和可见的时，我们的可执行jar才会运行


- pom依赖如下
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
优点: 依赖包含在jar文件中
不足: 只能对制品进行基础控制, 如没有类重定位的支持


这个配置默认会生成2个jar文件, 一个包含配置清单和依赖的jar包 xxx-jar-with-dependencies.jar, 另外一个是不包含依赖且没有清单的jar包.
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
注意这里省略的其他配置,其他配置见1;

- 生成jar文件
mvn package

- 清单文件
~~~mf
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Created-By: Apache Maven
Built-By: Tekin
Build-Jdk: 1.8.0_362
Main-Class: cn.tekin.MyApp
~~~

- 运行:
这里生成的可执行jar是包含依赖包的jar文件.
java -jar target/spring_demo-jar-with-dependencies.jar


## 3. Apache Maven Shade Plugin
优点:  依赖包含在jar文件中, 可对我们的jar制品进行高级控制, 可对依赖的类进行重命名和重新定位.
不足: 我们想使用高级功能时配置较为复杂, 生成的jar文件大小比上面2种稍大.

~~~xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <shadedArtifactAttached>true</shadedArtifactAttached>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>cn.tekin.MyApp</mainClass>
                </transformer>
            </transformers>
        </configuration>
        </execution>
    </executions>
</plugin>
~~~

- 清单文件
~~~mf
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Built-By: Tekin
Created-By: Apache Maven 3.6.3
Build-Jdk: 1.8.0_362
Main-Class: cn.tekin.MyApp
~~~



## 4. Spring Boot Maven Plugin
优点: 依赖项包含在jar文件中，我们可以在每个可访问的位置运行它，可对打包制品进行高级控制，排除jar文件的依赖项等，还可以打包war文件

不足: 添加可能不必要的Spring和Spring Boot相关类, 生成的jar文件大小比方法3稍大

~~~xml
 <build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <!-- 注意这里如果项目是1.8需要指定对应的版本号 否则默认会自动依赖最新版本 从而导致版本不一致而编译失败 -->
                <version>2.3.12.RELEASE</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                        <configuration>
                            <classifier>spring-boot</classifier>
                            <mainClass>
                                cn.tekin.MyApp
                            </mainClass>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

        </plugins>
    </build>
~~~

- 清单文件
~~~mf
Manifest-Version: 1.0
Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
Archiver-Version: Plexus Archiver
Built-By: Tekin
Start-Class: cn.tekin.MyApp
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Spring-Boot-Version: 2.3.12.RELEASE
Created-By: Apache Maven 3.6.3
Build-Jdk: 1.8.0_362
Main-Class: org.springframework.boot.loader.JarLauncher
~~~



## 5. Web Application With Executable Tomcat
优点: 依赖项包含在jar文件中，容易部署和运行

不足: 很多年未发行新版本, 且因为在内置的war文件中打包了一个内置的tomcat发行包, 所以jar文件比较大. 

~~~xml
 	<dependencies>
 	<!-- 其他依赖..... -->

 	<!-- servlet-api依赖 -->
	  <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>

	<build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.2</version>
                <executions>
                    <execution>
                        <id>tomcat-run</id>
                        <goals>
                            <goal>exec-war-only</goal>
                        </goals>
                        <phase>package</phase>
                        <configuration>
                            <path>/</path>
                            <enableNaming>false</enableNaming>
                            <finalName>webapp.jar</finalName>
                            <charset>utf-8</charset>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

        </plugins>
    </build>
~~~

- 清单文件
~~~mf

~~~



## 6. One Jar Maven Plugin
优点: 干净的委托模型，允许类处于OneJar的顶级，支持外部Jar，并且可以支持Native库
不足: 停止维护很多年了.
~~~xml
<plugin>
    <groupId>com.jolira</groupId>
    <artifactId>onejar-maven-plugin</artifactId>
    <executions>
        <execution>
            <configuration>
                <mainClass>cn.tekin.MyApp</mainClass>
                <attachToBuild>true</attachToBuild>
                <filename>
                  ${project.build.finalName}.${project.packaging}
                </filename>
            </configuration>
            <goals>
                <goal>one-jar</goal>
            </goals>
        </execution>
    </executions>
</plugin>
~~~

- 清单文件
~~~mf

~~~


## 总结
创建可运行的jar文件其实就是在jar文件中构建清单文件META-INF/MANIFEST.MF, 在这个前端文件中需要指定入口类 main-class, 还有打包相关的依赖项目等.
第一种方法依赖的额外包最少, 第五种方法生成的jar文件最大.

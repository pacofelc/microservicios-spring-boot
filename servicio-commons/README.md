https://www.udemy.com/course/microservicios-con-spring-boot-y-spring-cloud/learn/lecture/15373308
  
Asegúrate tener JAVA_HOME bien apuntado.  

> set JAVA_HOME=C:\Program Files\Java\jdk-13.0.2  

Para compilar la librería abre un terminal:  

> mvnw.cmd install  

El jar resultante quedará en el directorio de librerías de maven de tu equipo:

> C:\Users\pacof\.m2\repository\com\micro\commons\servicio-commons

Luego podrás añadir la dependencia a los POM de tus proyectos:

    <dependency>  
            <groupId>com.micro.commons</groupId>  
            <artifactId>servicio-commons</artifactId>  
            <version>0.0.1-SNAPSHOT</version>  
    </dependency>  


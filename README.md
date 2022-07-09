# Liquidación Automática con Java y Selenium

_Sistema de liquidación automática de contratos de locación de servicios e incentivos al personal._
_El sistema fue desarrollado para su implementación en la Dirección General de Contabilidad de la Universidad Nacional de Cuyo._
_El sistema se encarga de leer un archivo excel con extensión .xlsx o .xlsm suministrado por el usuario al momento de ejecutar la aplicación._
_Posteriormente, el sistema ingresará a una sesión un navegador instalado en la computado (Brave Browser, Google Chrome o Mozilla Firefox) y procederá a ingresar al sistema SIU-Pilagá para realizar las liquidaciones correspondientes._

_Para acceder con éxito al sistema SIU-Diaguita, será necesario encontrase conectado a una red de la Universidad Nacional de Cuyo_

## Tecnologías y herramientas utilizadas: 🛠️

* [Apache Maven 2.4](https://maven.apache.org/)
* [JDK version 1.8](https://www.oracle.com/java/technologies/downloads/#java8)
* [Selenium version 4](https://www.selenium.dev/)
* [Apache POI 5.2.2](https://poi.apache.org/)
* [JavaMail 1.6.2](https://mvnrepository.com/artifact/javax.mail)

## Instalación 🔧

**Paso 1: Descargar el proyecto**

```
git clone https://github.com/rbayarri/LiquidacionAutomatica-Selenium.git
```

**Paso 2: Compilar el programa**

```
cd LiquidacionAutomatica-Selenium
mvn install
```

**Paso 3: Ejecutar la aplicación**

```
java -jar target/LiquidacionAutomatica-1.0.jar
```

**Paso 4: Ingresar usuario y clave de SIU-Pilagá (por única vez)** 

_Cuando sea solicitado, completar usuario y clave personal para loguearse al sistema SIU-Pilagá.


**Paso 5: Subir el archivo Excel.**

_Subir archivo excel en formato .xlsx o .xlsm que contenga la información necesaria para realizar la liquidación._

**Paso 6: Consultar archivo de resultados**

_Una vez finalizado el proceso, en el mismo directorio donde se encuentra el Excel sumistrado, se crea el archivo resultados.txt_
_El mismo cuenta con la información de la liquidación, números de órdenes de pago, o mensajes de error en caso que no se haya logrado realizar alguna liquidación_
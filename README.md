# ANALISIS
---

## Entidad Relación

````plantuml
@startuml
entity "Cuenta de Usuario"{
  * **Id : INT <<AUTO_INCREMENT>> **
  --
  *Dni : VARCHAR
  *Nombre : VARCHAR
  SegundoNombre : VARCHAR
  *Apellidos : VARCHAR
  *Telefono : VARCHAR
  *Contraseña : VARCHAR
  CuentaBizum : VARCHAR <<FK Cuentas>>
}

entity CuentasBancarias{
    * **NumCuenta : INT <<AUTO_INCREMENT>>**
    --
    * IdCuentaUsuario : INT <<FK Cuenta de Usuario>>
    * Saldo : Double
}
"Cuenta de Usuario" ||..|{ CuentasBancarias
@enduml
````

Como no se dice nada se implementará la aplicación de un banco en la que se puede realizar distintas opreciones de las diversas cuentas que se pueden tener. Hay un solo banco que puede tener muchas cuentas de usuario, a su vez cada cuenta de usuario puede tener muchas cuentas bancarias.

Es importante marcar que para este proyecto no se ha considerado el guardar las transacciones que se realizan, en su defecto se ven los cambios en las cuentas donde el dinero fluctúa.

## Relacional

CuentaUsuario(Id, Dni, Nombre, SegundoNombre, Apellidos, Telefono, Contrasena, CuentaBizum) --> Id como clave primaria. Dni, Nombre, Apellidos, Telefono, Contrasena no pueden ser nulos. CuentaBizum es clave ajena de CuentasBancarias

CuentasBancarias(NumCuenta, IdCuentaUsuario, Saldo) --> NumCuenta actúa como clave primaria. IdCuentaUsuario y Saldo no pueden ser nulo

## Diagrama de casos de uso y actividad

````plantuml
@startuml
left to right direction
Actor Cliente
Rectangle App{
    usecase "Añadir cuenta" as uc1
    usecase "Sacar dinero" as uc2
    usecase "Ingresar dinero" as uc3
    usecase "Hacer bizum" as uc4
    usecase "Ingresar telefono beneficiario" as uc5
    usecase "Iniciar sesión" as uc6
    usecase Registrarse as uc7
}

Cliente -- uc1
Cliente -- uc2
Cliente -- uc3
Cliente -- uc4

uc1 ..> uc6 :include
uc2 ..> uc6 :include
uc3 ..> uc6 :include
uc4 ..> uc6 :include
uc6 ..>uc7 :include
uc4 ..> uc5 :include
@enduml
````

La aplicación se contempla que maneje las diferentes cuentas de un banco. Para iniciar sesión es necesario registrarse. Una vez se inicia sesión se accede a los diferentes casos de uso de la aplicación.

### Caso de uso hacer bizum
````plantuml
@startuml
Left to right direction
(*) -->  "Iniciar sesión"
if "Registrado" then
  -->[true] "Hacer bizum"
  --> "Ingresar teléfono del beneficiario"
  --> (*)
else
  -->[false] "Registrarse"
  -->"Iniciar sesión"
endif
@enduml
````

Se inicia sesión, si no se está registrado hay que registrarse. Cuando se inicia sesión se procede a hacer el bizum y para ello hay que ingresar el número de teléfono.

## Prototipado

# DISEÑO
---

### Diagrama de componentes

````plantuml
@startuml
package vista{
  [ LoginView ] <<FXML>>
  [ RegistroView ] <<FXML>>
  [ MenuView ] <<FXML>>
  [ LoginViewController ] <<JavaFX>>
  [ RegistroViewController ] <<JavaFX>>
  [ MenuViewController ] <<JavaFX>>
}
package controlador{
  [ BancoController ] <<Java>>
}
package modelo{
  [ BancoDAO ] <<Java>>
  [ Encriptador ] <<Java>>
}

[ LoginView ] <.. [ LoginViewController ] :use
[ LoginViewController ] ..> [ BancoController ] :use
[ RegistroView ] <.. [ RegistroViewController ] :use
[ RegistroViewController ] ..> [ BancoController ] :use
[ MenuView ] <.. [ MenuViewController ] :use
[ MenuViewController ] ..> [ BancoController ] :use  
[ BancoController ] ..> [ Encriptador ] : use
[ BancoController ] ..> DAO :use
[ BancoDAO ] ..|> DAO
@enduml
````
El gráfico representa la arquitectura del proyecto, que sigue el patrón de diseño modelo-vista-controlador (MVC). BizumController usa la interfaz DAO para mostrar que puede usar cualquier implementación de esta interfaz, que en este caso es BancoDAO. Se podría indicar, específico para este proyecto, que usa BancoDAO.

### Diagrama de clases

````plantuml
@startuml
package es.tiernoparla.bizum.modelo{
  class CuentaUsuario{
    - id: int
    - dni: String
    - nombre: String
    - segundoNombre: String
    - apellidos: String
    - telefono: int
    - contraseña: String
  }

  class CuentaBancaria{
    - numCuenta: int
    - idCuentaUsuario: int
    - saldo : Double
    - esBizum : boolean
  }
  CuentaUsuario "1..*" o--- "1..1"CuentaBancaria 
}
@enduml
````
Clases de negocio.

# DOCUMENTACION
---

### Enlace prpyecto GitHub

https://github.com/ljhonyl/bizum

Clonando el repositorio desde cualquier entorno no hace falta retocar las variables propias del equipo.

## Requisitos previos

No olvidar cambiar las direcciones del servidor y base de datos en las variables de MiBancoDAO.

Se recomienda comprobar que el .jar de mysql este añadido, si se usa intellij idea se le elimina de las referencias y se vuelve a añadir el .jar (se adjunta en la raíz del proyecto).


En el proyecto click derecho, open module settings

![](/doc/images/7.jpg)
![](/doc/images/8.jpg)

En el menu que se abre primero se elimina la dependencia antigüa en de dependencies y se añade luego en libraries (lado izquierdo) + (zona central) Java, y se le añade (se adjunta el .jar en este proyecto)

![](/doc/images/9.jpg)
![](/doc/images/10.jpg)

## Aplicación

### Inicio de sesión y registro


La primera pantalla en mostrarse es la de inicio de sesión. Donde se podrá o inicar sesión o registrarse.

![](/doc/images/1.png)

![](/doc/images/5.png)

### Menú

Una vez se inicia sesión se muestran varias opciones, se pedía que se mostraran las cuentas pero como en un principio no se tendrán se optó por dejarlas en otra ventana mas secundaria.

![](/doc/images/2.png)

## Sección cuentas

Las cuentas se muestran pulsando sobre cuentas. Aquí se podra añadir o seleccionar una cuenta con la que funcione bizum.

![](/doc/images/3.png)

### Bizum

Si se desea hacer un  bizum se mostrará un pequeño cuadro donde se pide ingresar el número y la cantidad a transferir. Al confirmar se muestra el nombre del beneficiario como se pedía. *

![](/doc/images/11.png)

![](/doc/images/4.png)
*Aunque el numero de teléfono no este registrado o no haya cuenta que trabaje con bizum para emisor y beneficiario este cuadro se mostrará sin nombre de beneficiario, si se producen estos factores la transacción no se realizará.

### Ingresar y retirar

A la hora de ingresar o retirar se deberá ingresar la cantidad en el cuadro de texto y una vez se pulsa sobre uno de los botones se tiene que seleccionar la cuenta donde realizar la operación y confirmar.

![](/doc/images/6.png)
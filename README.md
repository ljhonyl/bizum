# ANALISIS

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

entity Cuentas{
    * **NumCuenta : INT <<AUTO_INCREMENT>>**
    --
    * IdCuentaUsuario : INT <<FK Cuenta de Usuario>>
    * Saldo : Double
}
"Cuenta de Usuario" ||..|{ Cuentas
@enduml
````

## Relacional

CuentaUsuario(Id, Dni, Nombre, SegundoNombre, Apellidos, Telefono, Contrasena, CuentaBizum) --> Id como calve primaria. Dni, Nombre, Apellidos, Telefono, Contrasena no pueden ser nulos. CuentaBizum es clave ajena de Cuentas

Cuentas(NumCuenta, IdCuentaUsuario, Saldo) --> NumCuenta e IdCuentaUsuario actúan como clave primaria. Saldo no puede ser nulo

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

### Caso de uso hacer bizum
````plantuml
@startuml
(*) --> "Iniciar sesión"

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

## Prototipado


# DISEÑO

### Diagrama de componentes

````plantuml
@startuml
package vista{
  [ InicioViewController ] <<JavaFX>>
  [ InicioView ] <<JavaFX>>
  [ VistaGlobalViewController ] <<JavaFX>>
  [ VistaGlobalView ] <<JavaFX>>
}
package controlador{
  [ BancoController ] <<Java>>
}
package modelo{
  [ BancoDAO ] <<Java>>
}
@enduml

[ InicioView ] <.. [ InicioViewController ] :use
[ InicioViewController ] ..> [ BancoController ] :use
[ VistaGlobalView ] <.. [ VistaGlobalViewController ] :use
[ VistaGlobalViewController ] ..> [ BancoController ] :use 
[ BancoController ] ..> DAO :use
[ BancoDAO ] ..|> DAO
````

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
    - IdCuentaUsuario: int
    - Saldo : Double
  }
  CuentaUsuario "1..*" o--- "1..1"CuentaBancaria 
}
@enduml
````
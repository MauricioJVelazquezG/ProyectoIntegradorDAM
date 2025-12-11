# El otso

> **Proyecto Integrador - Desarrollo de Aplicaciones Móviles**
>
> **Semestre:** 4°E
> **Fecha de entrega:** 11 de Diciembre

---

## Equipo de Desarrollo

| Nombre Completo | Rol / Tareas Principales | Usuario GitHub |
| :--- | :--- | :--- |
| Cisneros Zaragoza Sylvanna Michelle| Diseño de las pantallas y Conectividad entre ellas  | @SylvannaCiss |
| Peralta Trujillo Josseph Levi | Retrofit, Backend, Lógica| @LeviPeralta |
| Velazquez Garcia Mauricio Javier | Screens y Repositorio | @MauricioJVelazquezG |

---

## Descripción del Proyecto

**¿Qué hace la aplicación?**
[Nuestra aplicación funciona como lector de códigos e inventario para una tienda de abarrotes. Escanea el producto con lector de codigos para editarlo, almacenarlo, eliminarlo y agregarlo  ]

**Objetivo:**
Demostrar la implementación de una arquitectura robusta en Android utilizando servicios web y hardware del dispositivo.

---

## Stack Tecnológico y Características

Este proyecto ha sido desarrollado siguiendo estrictamente los lineamientos de la materia:

* **Lenguaje:** Kotlin 100%.
* **Interfaz de Usuario:** Jetpack Compose.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Conectividad (API REST):** Retrofit.
    * **GET:** Nuestra aplicacion nos muestra la foto del producto, el precio, la cantidad, nombre y tipo con la opcion de modificar el producto
    * **POST:** Se escanea el codigo del producto y enviamos el formulario con los datos del producto
    * **UPDATE:** Se actualiza los datos del producto en el inventario (precio, cantidad, nombre y tipo)
    * **DELETE:** Se eliminan la cantidad productos agregados
* **Sensor Integrado:** Cámara
    * *Uso:* Con la cámara se escanea el codigo del producto para despues agregarlo al inventario, donde se almacena. El producto se puede editar y eliminar. 

---

## Capturas de Pantalla

[Coloca al menos 3 (investiga como agregarlas y se vean en GitHub)]

| Pantalla de Inicio | Operación CRUD | Uso del Sensor |
| :---: | :---: | :---: |

| ![Inicio](https://github.com/user-attachments/assets/d409246a-44d0-49d6-8bbd-eddcd66ba26f) | ![CRUD](https://github.com/user-attachments/assets/bd05ecbc-6787-4af0-a3d0-96af351bde7a) | ![sensor](https://github.com/user-attachments/assets/ed9f2262-9f84-4e68-bcf2-d8eaf421e7bb) |

---

## Instalación y Releases

El ejecutable firmado (.apk) se encuentra disponible en la sección de **Releases** de este repositorio.

[Liga correctamente tu link de releases en la siguiente sección]

1.  Ve a la sección "Releases" (o haz clic [aquí](https://github.com/MauricioJVelazquezG/ProyectoIntegradorDAM/releases)).
2.  Descarga el archivo `.apk` de la última versión.
3.  Instálalo en tu dispositivo Android (asegúrate de permitir la instalación de orígenes desconocidos).

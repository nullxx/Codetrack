# Overview
<details><summary>Estructura general</summary>
<p>

```console
nullxx@github:~$ tree server
|____server
| |____.DS_Store
| |____bin
| | |____www
| |____Dockerfile
| |____jest.config.js
| |____.dockerignore
| |____public
| |____.gitignore
| |____package-lock.json
| |____package.json
| |____.env
| |____undefined
| | |____error.log
| | |____combined.log
| |____.eslintrc.js
| |____log
| | |____error.log
| | |____combined.log
| |____.eslintignore
| |____.env.example
| |____app.js
| |____src
| | |____models.old
| | | |____projects.model.js
| | | |____projectObservations.js
| | | |____snapshots.model.js
| | | |____groups.model.js
| | | |____collisions.model.js
| | | |____index.js
| | | |____commonOptions.js
| | | |____files.model.js
| | | |____observations.model.js
| | | |____roles.model.js
| | | |____projectFiles.model.js
| | | |____users.model.js
| | |____test
| | | |____file.utils.test.js
| | | |____auth.utils.test.js
| | | |____static
| | | | |____auth.utils.test.file.example
| | | |____.testenv
| | |____utils
| | | |____file.utils.js
| | | |____models.utils.js
| | | |____index.js
| | | |____error.utils.js
| | | |____project.utils.js
| | | |____auth.utils.js
| | |____models
| | | |____projectFiles.js
| | | |____observations.js
| | | |____projectObservations.js
| | | |____groups.js
| | | |____collisions.js
| | | |____files.js
| | | |____snapshots.js
| | | |____roles.js
| | | |____users.js
| | | |____init-models.js
| | | |____projects.js
| | |____scripts
| | | |____exportDotenv.script.js
| | |____lib
| | | |____database.js
| | | |____logger.js
| | | |____collision.js
| | | |____auth.js
| | | |____project.js
| | | |____admin.js
| | | |____hash.js
| | | |____storage.js
| | |____api
| | | |____middlewares
| | | | |____project.snapshot.middleware.js
| | | | |____auth.middleware.js
| | | |____validations
| | | | |____project.snapshot.validation.js
| | | | |____admin.validation.js
| | | | |____project.root.validation.js
| | | |____controllers
| | | | |____auth.controller.js
| | | | |____project.snapshot.controller.js
| | | | |____project.controller.js
| | | | |____admin.controller.js
| | | |____routes
| | | | |____project.route.js
| | | | |____auth.route.js
| | | | |____index.js
| | | | |____project.root.route.js
| | | | |____project.snapshot.route.js
| | | | |____admin.route.js
```
</p>
</details>

- LIBRERIAS
  - `database.js`
  - `logger.js`
  - `auth.js`
  - `project.js`
  - `admin.js`
  - `hash.js`
  - `collision.js`
  - `storage.js`
- HERRAMIENTAS
- ENRUTADORES
- CONTROLADORES


## File hashing

Para diferenciar entre distintos archivos utilizamos el hashing. Es un método muy utilizado para encontrar diferencias entre archivos.

Un hash es un conjunto de letras y numeros de longitud variable  que identifica únicamente al input que le des. En nuestro caso cada archivo tedrá un único hash (ver [colisiones](#colisiones)) 

---

## Snapshots
 
To create a snapshot we just need the base local path, and the file (with its attributes). 

When recived those things, we create a record at `projectFiles` (at database) with `localFilePath` set to ```${localPath}/${fileData.name}``` and a `project` that belongs to. After that we store on the file system the file and create a referente at `files` (at database). Then the snapshot is created an referenced at database.

We can see it in the following diagram.

<img src="https://github.com/nullxx/EclipseTracker/raw/master/wiki/assets/images/db.png">


Un snapshot siempre será creado aunque exista en el servidor un archivo identico. Que se cree un snapshot no quiere decir que guardemos la información duplicada. Cuando el archivo llega, se compara el hash de este archivo con los existentes de la base de datos. Si se encuentra un hash identico, referenciaremos ese archivo al nuevo snapshot.

Veamos un ejemplo.
-----
```
Usuario 1
Proyecto 1
localPath: src/main/
archivo1: Main.java
archivo2: Utils.java
```
Esto querrá decir que hay dos archivos localizados en `src/main/Main.java` y `src/main/Utils.java`.

En cierto momento los datos se enviarán al servidor para crear una captura de dichos archivos.

Cuando estos llegan lo que basicamente se hace es:


* **Buscar los hash** de los archivos en la base de datos.
    * Si el hash de uno de los archivos coincide con uno existente, este es el que asociaremos al snapshot (para evitar duplicados)
    * Si ninguno de los hash es encontrado **guardamos el archivo en el sistema de archivos** de la siguiente forma: `uploads/snapshots/${FECHA}/{USERID}/${TIMESTAMP}` (ej: `uploads/snapshots/2020-11-7/1/1604757096847`.
        * Tras guardar en el sistema de archivos, **guardamos toda la metadata del archivo y el path donde lo hemos guardado en la base de datos** para poder recuperarlo en un futuro.

* Cada snapshot ademas de ir referenciado a un archivo, lo referenciamos también a un elemento de la lista de archivos del proyecto del Usuario 1. Es decir el proyecto 1 tendrá diferentes archivos `src/main/Main.java` y `src/main/Utils.java`. Cada snapshot va asociada a la version se uno de esos archivos (data) y al archivo en sí (ej: `src/main/Main.java`)

Por lo tanto un snapshot será:

* file: ID: 1 (archivo guardado en el sistema de archivos que esta referenciado en la base de datos con id=1)
* archivo: ID: 3 => `src/main/Main.java` (referencia de el archivo que estamos guardando)

---

### [Colisiones](https://es.wikipedia.org/wiki/Colisi%C3%B3n_(hash))
Para evitar colisiones se utilizan dos algoritmos de hashing al mismo tiempo. [MD5](https://es.wikipedia.org/wiki/MD5) Y [SHA512](https://es.wikipedia.org/wiki/SHA-2) son los afortundos. De todas formas si cualquiera de estos dos colisionara a la vez (muy poco probable) la colisión sería guardada en la base de datos para un posterior análisis.

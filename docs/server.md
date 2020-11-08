## Snapshots
 
To create a snapshot we just need the base local path, and the file (with its attributes). 

When recived those things, we create a record at `projectFiles` (at database) with `localFilePath` set to ```${localPath}/${fileData.name}``` and a `project` that belongs to. After that we store on the file system the file and create a referente at `files` (at database). Then the snapshot is created an referenced at database.

We can see it in the following diagram.

![Estructura general usuarios](https://github.com/nullxx/EclipseTracker/blob/master/wiki/assets/images/db.png?raw=1)


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

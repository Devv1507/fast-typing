En caso de presentar problemas con las dependencias de JavaFX, es posible solucionarlo mediante IntelijIDEA. Nos dirigimos al archivo Main.java,
vamos a la barra superior de herramientas a la ruta Run/Edit configurations..., se abrira una ventana con las dependencias del Maven. Tendremos que seleccionar "Modify options"
y "Add VM options".

![image](https://github.com/user-attachments/assets/1113a7a6-ad4f-4a42-87da-71c24e0f73e1)

En el input que aparece debemos anadir:
```
 --module-path "C:\JavaFX\javafx-sdk-21.0.6\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics
```
Cabe aclarar que la ruta despues del argumento --module-path es la ruta en donde se descargo JavaFX, por ejemplo, si se trata de un usuario de Windows se puede descargar
la version 21.0.6 (compatible con JDK 17): https://download2.gluonhq.com/openjfx/21.0.6/openjfx-21.0.6_linux-x64_bin-sdk.zip


Se descomprime este ZIP, y te diriges a la carpeta lib y copias esa direccion en tu computador.

![image](https://github.com/user-attachments/assets/23a9b046-c9d0-42ae-b015-57cde813583d)


Ahora basta con undir a Apply y Run.

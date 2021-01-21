# Eclipse plugin

Eclipse nos proporciona todas las herramientas para poder desarrollar un plugin.

<details>
<summary>Estructura</summary>
<p>

```console
nullxx@github:~$ tree eclipsePlugin 
eclipsePlugin
|____build.properties
|____swing2swt.jar
|____test
| |____UserTest.java
| |____CryptoTest.java
|____bin
| |____org
| | |____eclipse
| | | |____wb
| | | | |____swt
| | | | | |____SWTResourceManager.class
| |____CryptoTest.class
| |____UserTest.class
| |____codetrack
| | |____Storage.class
| | |____Config.class
| | |____api
| | | |____Crypto.class
| | | |____RestAPI.class
| | | |____HttpCaller.class
| | |____Utils$1.class
| | |____Utils.class
| | |____views
| | | |____BrowserListener.class
| | | |____MainView$1.class
| | | |____RichClient$1.class
| | | |____MainView$3.class
| | | |____Codetrack.class
| | | |____BrowserListenerResponse.class
| | | |____MainView$2.class
| | | |____CompositeWithLoader$1.class
| | | |____CompositeWithLoader.class
| | | |____RichClient.class
| | | |____BrowserThreadBridge.class
| | | |____BrowserListenerResponseType.class
| | | |____MainView.class
| | | |____RichClient$CallbackDebugFunction.class
| | |____client
| | | |____LocalProjectConfig.class
| | | |____LocalProject.class
| | | |____Snapshot.class
| | | |____Workspace.class
| | | |____User.class
| | | |____RemoteProject.class
| | | |____Project.class
| | | |____Group.class
| | | |____Observation.class
| | |____listeners
| | | |____Listeners.class
| | | |____Listeners$1.class
| | |____Utils$2.class
| | |____Activator.class
|____plugin.xml
|____resources
| |____pages
| | |____userCheckProjects.html
| | |____css
| | | |____login.css
| | |____js
| | | |____login.js
| | | |____userCheckProjects.js
| | | |____adminView.js
| | |____login.html
| | |____img
| | |____adminView.html
|____META-INF
| |____MANIFEST.MF
|____.classpath
|____.gitignore
|____.settings
| |____org.eclipse.jdt.core.prefs
| |____org.eclipse.ltk.core.refactoring.prefs
| |____org.eclipse.pde.core.prefs
|____.project
|____icons
| |____sample@2x.png
| |____sample.png
|____contexts.xml
|____src
| |____org
| | |____eclipse
| | | |____wb
| | | | |____swt
| | | | | |____SWTResourceManager.java
| |____codetrack
| | |____Storage.java
| | |____Utils.java
| | |____Activator.java
| | |____Config.java
| | |____api
| | | |____HttpCaller.java
| | | |____Crypto.java
| | | |____RestAPI.java
| | |____views
| | | |____BrowserThreadBridge.java
| | | |____BrowserListenerResponse.java
| | | |____BrowserListenerResponseType.java
| | | |____Codetrack.java
| | | |____CompositeWithLoader.java
| | | |____BrowserListener.java
| | | |____RichClient.java
| | | |____MainView.java
| | |____client
| | | |____Group.java
| | | |____LocalProjectConfig.java
| | | |____RemoteProject.java
| | | |____User.java
| | | |____Snapshot.java
| | | |____Observation.java
| | | |____Project.java
| | | |____LocalProject.java
| | | |____Workspace.java
| | |____listeners
| | | |____Listeners.java
```
</p>
</details>

--- 

## Frontend
Para para poder acceder a la tecnologia de hotreload y de un desarrollo más simple y rapido he optado por usar html/css en la parte del frontend.

Para poder comunicarse entre las paginas estaticas del frontend y el codigo de java he construido una [clase](https://github.com/nullxx/Codetrack/blob/master/eclipsePlugin/src/codetrack/views/RichClient.java) ([documentation](https://nullxx.github.io/Codetrack/eclipsePluginJavadoc/codetrack/views/RichClient.html)) que comunica ambos procesos


## Backend
Para la sincronización de datos entre el cliente y el servidor se hace uso de peticiones http. ([documentation](api.html))

```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
```
[source](https://github.com/nullxx/Codetrack/blob/master/eclipsePlugin/src/codetrack/api/HttpCaller.java)

[documentation](https://nullxx.github.io/Codetrack/eclipsePluginJavadoc/codetrack/api/HttpCaller.html)

La codificación del body de las peticiones es mayoritariamente ```application/json```
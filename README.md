# MicroPlay

A shared library with common toolset for all Play based RESTfull micro services in borderfree 
This library is not intended as a seed/scaffold project, so copy-pasting is discouraged

#### Main Features

- Base REST API Controller `com.borderfree.microplay.controllers.components.ApiControllerActions` supporting:
    - cross-thread request context tracking in logs with a correlation id. injected for any `apiAction` in controllers inheriting from `ApiControllerActions`.  implementation: `com.borderfree.microplay.logging.MDCManager`, `com.borderfree.microplay.logging.MDCPropagatingDispatcherConfigurator`
    - json/xml content negotiation
    - catch all to json error handler
- HTTP trace filter. GZIP content negotiation filter
- swagger UI and swagger core integration
- /health /status API endpoints 
- Standard json based REST API consumer - `com.borderfree.microplay.services.RestApiConsumer`
- Objects to Json conversion utils. under `\app\com\borderfree\microplay\utils`   

#### Installation/Usage

 - Add the artifact generated by the library as an sbt dependency to your play build.sbt:
    ```scala
    "com.borderfree"  %% "microplay" % "2.6.7.1-1"
    or
    "com.borderfree"  %% "microplay" % "2.6.7.1-+"
    ```
    Find [latest versions](https://artifactory-dev.bfretail.pitneycloud.com/artifactory/libs-release-local/com/borderfree/microplay_2.12/)

- Use `conf/application.routes` instead of the default `conf/routes`. specify microplay end points you wish to expose explicitly - review example projects below.

- Your projects `application.conf` will inherit defaults from microplay `application.conf` - review overridable microplay default settings under 
  ```javascript
  micro {
      //microplay default settings....
  }
  
  ```

#### Examples

Projects currently using microplay lib:

| Project                                                                         | Implementation Notes                                 | Integrations |
|---------------------------------------------------------------------------------|------------------------------------------------------|--------------|
| [bfx-police](https://gitlab.pitneycloud.com/borderfree/BFX2/bfx-police)         | consume/provide REST API. Web frontend. Web scraper. | Mongodb      |
| [loca-service](https://gitlab.pitneycloud.com/borderfree/LOC/loca-service)      | consume/provide REST API. Akka Streams/Alpakka       | Mongodb      |
| [welcome-mat](https://gitlab.pitneycloud.com/borderfree/LOC/welcomemat-service) | consume REST API. Web assets provider                |              |
| [xl-service](https://gitlab.pitneycloud.com/borderfree/LOC/xl-service)          | consume/provide REST API                             |              |
| [merchconf](https://gitlab.pitneycloud.com/borderfree/CNF/merchconf)            | provide GraphQL API (Sangria). consume REST          |              |
| [facelift](https://gitlab.pitneycloud.com/borderfree/KEPLER/facelift-service)   | consume/provide REST API                             |              |
| [datapier](https://gitlab.pitneycloud.com/borderfree/SVC/datapier)              | provide REST API. Akka Streams/Alpakka               | RabbitMQ     |


#### Versioning
Microplay library version is prefixed with the version of play framework which it depends upon. 

version format:
`<play-major>.<play-minor>.<play-patch>.<microplay-patch>-<microplay-buildNo>`
 
#### Upgrading from 2.6.7.x to 2.6.20.x

1. upgrade play plugin in `plugins.sbt`: `addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")`.
2. it's recommended to upgrade all sbt plugins - please refer to `plugins.sbt` in this project. in such update - `jenkinsfile` - rename the application package sbt task from `universal:package-zip-tarball` to `universal:packageZipTarball`
3. its recommended to upgrade sbt to 1.2.x as well.
4. For reference, please refer to these commits that includes all relevant changes mentioned above:
http://dbygitmsprod.pbi.global.pvt/borderfree/BFX2/bfx-police/commit/9f59685e0b9f0e72f3813a5cbabaed78261b81c3,
http://dbygitmsprod.pbi.global.pvt/borderfree/BFX2/bfx-police/commit/be9718357b491d12134f2b2d18bef8ab661d42c4

#### IJ IDEA Project setup:
1. `git clone git@gitlab.pitneycloud.com:borderfree/SHD/microplay.git` 
2. import build.sbt in IDEA 
3. run sbt compile ( required so BuildInfoPlugin will generate BuildInfo class)
4. right click microplay-lib/build.bst -> Run Play 2 App (verify PlayFramework support plugin installed)
5. on code changes - > compile (Ctrl F9) and refresh browser
6. Publish locally - `sbt clean compile test it:test publishLocal`

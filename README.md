# Microplay

A scala library with common toolset for RESTfull micro services built on Play Framework.

[ ![Download](https://api.bintray.com/packages/yarony/PB_Maven/microplay/images/download.svg) ](https://bintray.com/yarony/PB_Maven/microplay/_latestVersion)
[![Actions Status](https://github.com/PitneyBowes/microplay/workflows/Test/badge.svg)](https://github.com/PitneyBowes/microplay/actions)

#### Main Features

- Base REST API Controller `com.pb.microplay.controllers.components.ApiControllerActions` supporting:
    - cross-thread request context tracking in logs with a correlation id. injected for any `apiAction` in controllers inheriting from `ApiControllerActions`.  implementation: `com.pb.microplay.logging.MDCManager`, `com.pb.microplay.logging.MDCPropagatingDispatcherConfigurator`
    - json/xml content negotiation
    - global error handlers returns json response
- HTTP trace filter. GZIP content negotiation filter
- swagger UI and swagger core integration
- exposes /health, /status /docs API endpoints 
- Standard json based REST API consumer - `com.pb.microplay.services.RestApiConsumer`
- Objects to Json conversion utils. under `\app\com\pb\microplay\utils`   

#### Installation/Usage
In sbt build file:
1. add JCenter repository resolver:
    ```scala
    resolvers += Resolver.jcenterRepo
    ```

2. sdd the library as an sbt dependency:   
    ```scala
    libraryDependencies += "com.pb"  %% "microplay" % "26.3.64"
    //or
    libraryDependencies += "com.pb"  %% "microplay" % "26.3.+"
    ```

- Your projects configuration will inherit default configuration from microplay [`application.conf`](/microplay-lib/conf/application.conf) - you can review and decide to override these settings under the `micro` node 
  ```javascript
  micro {
      //microplay default settings....
  }  
  ```

#### Versioning
Microplay library version is prefixed with the version of play framework which it depends upon. 

version format:
`<play-major><play-minor>.<microplay-major>.<microplay-minor>`
 
#### IJ IDEA Project setup:
1. git clone 
2. import build.sbt in IDEA ®
3. run sbt compile ( required so BuildInfoPlugin will generate BuildInfo class)
4. right click microplay-lib/build.bst -> Run Play 2 App (verify PlayFramework support plugin installed)
5. on code changes - > compile (Ctrl F9) and refresh browser
6. re-run tests & publish locally - `sbt "project microplay-lib" clean compile test it:test publishLocal`

### Contributing/Developing
Please refer to [`CONTRIBUTING.md`](./CONTRIBUTING.md) file.

### License
Copyright (c) 2020  [Pitney Bowes Inc,](https://www.pitneybowes.com).
Licensed for free usage under the terms and conditions of Apache V2 - [Apache V2 License](https://www.apache.org/licenses/LICENSE-2.0).

![Pitney Bowes](PB_Logo.jpg)
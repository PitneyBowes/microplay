package com.borderfree.microplay.configuration

import java.io.File

import javax.inject.Inject
import play.api.Application
import play.api.inject.ApplicationLifecycle
import play.api.routing.Router
import play.modules.swagger.SwaggerPluginImpl
import play.routes.compiler.{RoutesFileParser, StaticPart, Include => PlayInclude, Route => PlayRoute}

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 6/17/2019
  * Time: 3:03 PM
  *
  * @see play.modules.swagger.SwaggerPluginImpl
  *
  * Based on SwaggerPluginImpl, addresses 2 issues:
  * 1. supports included Routes classes without matching routes text file ( i.e. RoutesDelegator). currently, SwaggerPluginImpl errors on such use cases
  * 2. includes default routes file ( expected in dependant project) as an optional dependency even if a custom routes class is configured in play - to cater for dependant project
  */

class SwaggerPluginExt @Inject()(lifecycle: ApplicationLifecycle, router: Router, app: Application) extends SwaggerPluginImpl(lifecycle ,router,app)
{
  /**
    * @see play.modules.swagger.SwaggerPluginImpl#parseRoutes
    * @return
    */
  override def parseRoutes: List[PlayRoute] = {

    def playRoutesClassNameToFileName(className: String) = className.replace(".Routes", ".routes")

    //Parses multiple route files recursively
    def parseRoutesHelper(routesFile: String, prefix: String): List[(String,PlayRoute)] = {
      logger.debug(s"Processing route file '$routesFile' with prefix '$prefix'")
      Try(Source.fromInputStream(app.classloader.getResourceAsStream(routesFile)).mkString) match {
        case Success(routesContent) =>
          val parsedRoutes = RoutesFileParser.parseContent(routesContent, new File(routesFile))
          val routes = parsedRoutes.right.get.collect {
            case route: PlayRoute => {
              logger.debug(s"Adding route '$route'")
              Seq(routesFile -> route.copy(path = route.path.copy(parts = StaticPart(prefix) +: route.path.parts)))
            }
            case include: PlayInclude => {
              logger.debug(s"Processing route include $include")
              parseRoutesHelper(playRoutesClassNameToFileName(include.router), include.prefix)
            }
          }.flatten
          logger.debug(s"Finished processing route file '$routesFile'")
          routes
        case Failure(_) =>
          logger.debug(s"routes file '$routesFile' not found")
          List[(String, PlayRoute)]()
      }
    }

    val res2routes = parseRoutesHelper(config.getOptional[String]("play.http.router").map(playRoutesClassNameToFileName).getOrElse("routes"), "")
    val res2routesWithDefault = res2routes ++  ( if (res2routes.exists(entry => Set("routes", "router.routes").contains(entry._1.toLowerCase))) {
      List[(String, PlayRoute)]()
    }
    else {
      parseRoutesHelper("routes", "")
    })
    res2routesWithDefault.map(_._2)
  }
}

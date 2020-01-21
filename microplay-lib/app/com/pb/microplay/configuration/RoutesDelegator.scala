package com.pb.microplay.configuration

import com.pb.microplay.logging.LogSupport
import com.pb.microplay.logging.LogSupport
import javax.inject.Inject
import play.api.Environment
import play.api.inject.{Injector, bind}
import play.api.routing.Router
import play.api.routing.Router.Routes
import play.utils.Reflect

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 6/13/2019
  * Time: 11:55 AM
  *
  * Delegate to default route file - assumed to be located in dependant project ( otherwise a custom router is set as the entry point)
  */

class RoutesDelegator @Inject() (injector: Injector, env: Environment) extends Router with LogSupport
{
  lazy val delegateRouter: Router =  try {
    injector.instanceOf(bind(Reflect.getClass[Router]("router.Routes", env.classLoader)))
//    injector.instanceOf(bind(this.getClass.getClassLoader.loadClass("router.Routes"))).asInstanceOf[Router]
  }
  catch {
    case _:Throwable =>
      logger.debug("can't find default generated Routes from `routes` file")
      Router.empty
  }

  override def routes: Routes = delegateRouter.routes
  override def documentation: Seq[(String, String, String)] = delegateRouter.documentation
  override def withPrefix(prefix: String): Router = delegateRouter.withPrefix(prefix)

}

/*
 * Copyright (c) 2020 Pitney Bowes Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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

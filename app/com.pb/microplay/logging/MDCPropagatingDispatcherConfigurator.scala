package com.pb.microplay.logging

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 11/19/2015
 * Time: 10:20 AM
 */
/**
 * reference : http://yanns.github.io/blog/2014/05/04/slf4j-mapped-diagnostic-context-mdc-with-play-framework/
 */
import java.util.concurrent.TimeUnit

import akka.dispatch._
import com.typesafe.config.Config
import org.slf4j.MDC

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, FiniteDuration}

class MDCPropagatingDispatcherConfigurator(config: Config, prerequisites: DispatcherPrerequisites)
  extends MessageDispatcherConfigurator(config, prerequisites) {

  private val instance = new MDCPropagatingDispatcher(
    this,
    config.getString("id"),
    config.getInt("throughput"),
    FiniteDuration(config.getDuration("throughput-deadline-time", TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS),
    configureExecutor(),
    FiniteDuration(config.getDuration("shutdown-timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS))

  override def dispatcher(): MessageDispatcher = instance
}

/**
 * A MDC propagating dispatcher.
 *
 * This dispatcher propagates the MDC current request context if it's set when it's executed.
 */
class MDCPropagatingDispatcher(_configurator: MessageDispatcherConfigurator,
                               id: String,
                               throughput: Int,
                               throughputDeadlineTime: Duration,
                               executorServiceFactoryProvider: ExecutorServiceFactoryProvider,
                               shutdownTimeout: FiniteDuration)
  extends Dispatcher(_configurator, id, throughput, throughputDeadlineTime, executorServiceFactoryProvider, shutdownTimeout ) {

  self =>

  override def prepare(): ExecutionContext = new ExecutionContext {
    // capture the MDC
    val mdcContext = Option(MDC.getCopyOfContextMap)

    def execute(r: Runnable): Unit = self.execute(new Runnable {
      def run(): Unit = {
        // backup the callee MDC context
        val oldMDCContext = Option(MDC.getCopyOfContextMap)

        // Run the runnable with the captured context
        setContextMap(mdcContext)
        try {
          r.run()
        } finally {
          // restore the callee MDC context
          setContextMap(oldMDCContext)
        }
      }
    })
    def reportFailure(t: Throwable): Unit = self.reportFailure(t)
  }

  private[this] def setContextMap(context: Option[java.util.Map[String, String]]):Unit = {
    context map (MDC.setContextMap(_)) getOrElse MDC.clear()
  }

}
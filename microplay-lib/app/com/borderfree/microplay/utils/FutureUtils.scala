package com.borderfree.microplay.utils

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/6/2017
  * Time: 10:41 AM
  */
object FutureUtils
{
  /**
    * Implicitly add to Option[Future] types a method - toFutureOption - to convert itself into Future[Option]
    */
  implicit class OptionalFuture[A](val optFuture: Option[Future[A]]){
    def toFutureOption(implicit ec: ExecutionContext): Future[Option[A]] =
      optFuture match {
        case Some(f) => f.map(Some(_))
        case None    => Future.successful(None)
      }
  }
}

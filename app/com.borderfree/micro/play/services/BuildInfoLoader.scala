package com.borderfree.micro.play.services

import javax.inject.Inject

import com.borderfree.micro.play.configuration.AppConfiguration
import com.google.inject.ImplementedBy

import scala.reflect.ClassTag

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/1/2018
  * Time: 3:46 PM
  */
@ImplementedBy(classOf[BuildInfoLoaderImpl])
trait BuildInfoLoader{
  def getBuildInfo: BuildInfoMeta
}

class BuildInfoLoaderImpl @Inject()(appConfiguration: AppConfiguration, env:play.Environment ) extends BuildInfoLoader {

  private lazy val buildInfoClassName: String = appConfiguration.getOptional[String]("micro.build.info").getOrElse("com.borderfree.micro.play.BuildInfo")

  def getBuildInfo: BuildInfoMeta ={
    val buildInfoClass = env.classLoader().loadClass(buildInfoClassName)
    companionObj(ClassTag(buildInfoClass)).asInstanceOf[BuildInfoMeta]
  }
  private def companionObj[T](implicit tag : reflect.ClassTag[T] ): AnyRef = {
    val c = env.classLoader().loadClass(buildInfoClassName + "$")
//    val c = env.classLoader().loadClass(tag.runtimeClass.getName + "$")
    c.getField("MODULE$").get(c)
  }
}

trait BuildInfoMeta{
  val toJson: String
  val toMap: Map[String, Any]
}
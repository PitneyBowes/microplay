package com.pb.microplay.controllers

import akka.stream.Materializer
import javax.inject.{Inject, Singleton}
import play.api.http.HeaderNames
import play.filters.gzip.GzipFilter

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 11/6/2018
  * Time: 3:08 PM
  *
  * Returns gzip response only if explicitly requested by api consumer
  */
@Singleton
class GZipContentNegotiationFilter @Inject()(implicit mat: Materializer)  extends GzipFilter(shouldGzip = (request, _) => request.headers.getAll(HeaderNames.ACCEPT_ENCODING).contains("gzip"))(mat)

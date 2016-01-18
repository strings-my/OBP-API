package code.api.ResourceDocsRD1

import code.api.OBPRestHelper


import net.liftweb.common.Loggable


object ResourceDocs extends OBPRestHelper with ResourceDocsAPIMethods with Loggable {

  val VERSION = "rd1"


  val routes = List(
    ImplementationsResourceDocs.getResourceDocsObp,
    ImplementationsResourceDocs.getResourceDocsSwagger
  )

  routes.foreach(route => {
    oauthServe(apiPrefix{route})
  })

}
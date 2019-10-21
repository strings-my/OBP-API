/**
Open Bank Project - API
Copyright (C) 2011-2018, TESOBE Ltd.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Email: contact@tesobe.com
TESOBE Ltd.
Osloer Strasse 16/17
Berlin 13359, Germany

This product includes software developed at
TESOBE (http://www.tesobe.com/)

  */
package code.api.v4_0_0

import code.api.OBPRestHelper
import code.api.util.APIUtil.{OBPEndpoint, ResourceDoc, getAllowedEndpoints}
import code.api.util.{ApiVersion, VersionedOBPApis}
import code.api.v1_3_0.APIMethods130
import code.api.v1_4_0.APIMethods140
import code.api.v2_0_0.APIMethods200
import code.api.v2_1_0.APIMethods210
import code.api.v2_2_0.APIMethods220
import code.api.v3_0_0.APIMethods300
import code.api.v3_0_0.custom.CustomAPIMethods300
import code.api.v3_1_0.{APIMethods310, OBPAPI3_1_0}
import code.util.Helper.MdcLoggable

/*
This file defines which endpoints from all the versions are available in v4.0.0
 */
object OBPAPI4_0_0 extends OBPRestHelper with APIMethods130 with APIMethods140 with APIMethods200 with APIMethods210 with APIMethods220 with APIMethods300 with CustomAPIMethods300 with APIMethods310 with APIMethods400 with MdcLoggable with VersionedOBPApis{

  val version : ApiVersion = ApiVersion.v4_0_0

  val versionStatus = "BLEEDING-EDGE" // TODO this should be a property of ApiVersion.

  // Possible Endpoints from 4.0.0, exclude one endpoint use - method,exclude multiple endpoints use -- method,
  // e.g getEndpoints(Implementations4_0_0) -- List(Implementations4_0_0.genericEndpoint, Implementations4_0_0.root)
  val endpointsOf4_0_0 = getEndpoints(Implementations4_0_0) - Implementations4_0_0.genericEndpoint
  
  def allResourceDocs = MockerConnector.doc ++
                        Implementations4_0_0.resourceDocs ++
                        OBPAPI3_1_0.allResourceDocs


  def findResourceDoc(pf: OBPEndpoint): Option[ResourceDoc] = {
    allResourceDocs.find(_.partialFunction==pf)
  }

  // Filter the possible endpoints by the disabled / enabled Props settings and add them together
  val routes : List[OBPEndpoint] =
      Implementations4_0_0.root :: // For now we make this mandatory
      getAllowedEndpoints(endpointsOf4_0_0, Implementations4_0_0.resourceDocs) ++
      OBPAPI3_1_0.routes // ***** here the previous version routes added at last, so there no need exclude any previous endpoints, because they will be omit.



  // register v4.0.0 apis first, Make them available for use!
  routes.foreach(route => {
    oauthServe(apiPrefix{route}, findResourceDoc(route))
  })

  oauthServe(apiPrefix{Implementations4_0_0.genericEndpoint}, None)
  logger.info(s"version $version has been run! There are ${routes.length} routes.")

}

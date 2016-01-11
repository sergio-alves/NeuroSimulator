package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.api.NerveCell
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import java.util.UUID

/**
 * Simple SynapticLink implementation 
 * 
 * @author Sergio Alves
 */
class SynapticLinkImpl (_uuid:UUID,n:String, n1: NerveCell, n2: NerveCell, weight:Double) extends SynapticLink {  
  def this(n:String, n1: NerveCell, n2: NerveCell, weight:Double) = {
    this(UUID.randomUUID(),n,  n1, n2, weight)
  }
  
  uuid = _uuid
  name = n
  source = n1
  target = n2
  n1-->(n2, weight)
}
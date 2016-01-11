package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import java.util.UUID

@XStreamAlias("Neuron")
class NeuronConfiguration(_uuid:String, n: String, t: Double, l: LocationConfiguration, d:String) {  
  def this(_uuid:String, n: String, t: Double, l: LocationConfiguration) = {
    this(_uuid, n,t,l, "")
  }
  
  def this(n: String, t: Double, l: LocationConfiguration) = {
    this(UUID.randomUUID().toString(), n,t,l)
  }

  def this(n: String, t: Double) = {
    this(UUID.randomUUID().toString(), n, t, null)
  }
  
  @XStreamAsAttribute
  val uuid = _uuid
  
  val description = d
  
  @XStreamAsAttribute
  val name = n
  
  @XStreamAsAttribute
  val threshold = t
  
  val location = l
}

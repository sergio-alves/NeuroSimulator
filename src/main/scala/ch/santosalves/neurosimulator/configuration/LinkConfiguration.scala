package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import java.util.UUID

@XStreamAlias("Link")
class LinkConfiguration(_uuid:String, n: String, inr: String, onr: String, w: Double) {
  def this(n: String, inr: String, onr: String, w: Double)= {
    this(UUID.randomUUID().toString(), n, inr, onr, w)
  }
  @XStreamAsAttribute
  val uuid = _uuid
  @XStreamAsAttribute
  val name = n
  val inputNeuronId = inr
  val outputNeuronId = onr
  @XStreamAsAttribute
  val weight = w
}
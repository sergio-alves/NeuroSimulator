package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute

@XStreamAlias("Location")
class LocationConfiguration(_x: Int, _y: Int) {
  @XStreamAsAttribute
  val x = _x
  @XStreamAsAttribute
  val y = _y
}
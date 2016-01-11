package ch.santosalves.neurosimulator.api

import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * 
 * @author Sergio Alves
 */
trait NeuronalNetworkSimulator {
  var network: NeuronalNetwork = _
  def simulateNext(): Unit  
  def startSimulation(): Unit
  def StopSimulation(): Unit
}
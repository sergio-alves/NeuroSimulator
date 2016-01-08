package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NeuronalNetworkSimulator
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.api.NeuronalNetwork

/**
 * A simple Neuronal Simulator that takes only sub classes of NerveCell
 * 
 * @author Sergio Alves
 */
class SimpleNeuronalSimulator(neuralNetwork: NeuronalNetwork) extends NeuronalNetworkSimulator {
  network = neuralNetwork

  def getNeuronsCount(): Int =  network.neurons.size
  def getSynapticsLinksCount(): Int = network.synapticLinks.size
  
  def simulateNext(): Unit  = {
    
  }
  
  def startSimulation(): Unit = {
    
  }
  
  def StopSimulation(): Unit = {
    
  }
  
  def loadNeuronalNetwork():Unit = {
    
  }
}
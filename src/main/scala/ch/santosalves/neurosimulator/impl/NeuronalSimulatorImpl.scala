package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NeuronalNetworkSimulator
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.api.NeuronalNetwork
import ch.santosalves.neurosimulator.api.Simulation
import ch.santosalves.neurosimulator.api.Simulation
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.XStream

/**
 * A simple Neuronal Simulator that takes only sub classes of NerveCell
 * 
 * @author Sergio Alves
 */
class NeuronalSimulatorImpl extends NeuronalNetworkSimulator {
  def this(neuralNetwork: NeuronalNetwork) = {
    this()
    network = neuralNetwork  
  }
  

  /**
   * returns the number of neurons attached to this simulation
   */
  def getNeuronsCount(): Int =  network.neurons.size
  
  /**
   * returns the number of links attached to this simulation
   */
  def getSynapticsLinksCount(): Int = network.synapticLinks.size
  
  /**
   * Reads the simulation plan and executes next step
   */
  def simulateNext(): Unit  = {
    
  }
  
  /**
   * Starts the simulation
   */
  def startSimulation(): Unit = {
    
  }
  
  /**
   * Stops the simulation
   */
  def StopSimulation(): Unit = {
    
  }
  

}
package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.Simulation
import ch.santosalves.neurosimulator.api.ExecutionPlan
import ch.santosalves.neurosimulator.api.NeuronalNetwork
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import java.io.FileOutputStream
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAliasType
import com.thoughtworks.xstream.MarshallingStrategy

/**
 * 
 * @author Sergio Alves
 */
class SimulationImpl extends Simulation {
  def this(network : NeuronalNetwork, execPlan : ExecutionPlan) = {
    this()    
    neuronalNetwork = network
    executionPlan = execPlan    
  }
}
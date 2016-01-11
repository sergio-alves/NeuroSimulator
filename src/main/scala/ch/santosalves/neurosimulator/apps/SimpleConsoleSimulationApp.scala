package ch.santosalves.neurosimulator.apps

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import ch.santosalves.neurosimulator.impl.SimulationImpl
import ch.santosalves.neurosimulator.impl.ExecutionPlanImpl
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.impl.SynapticLinkImpl
import ch.santosalves.neurosimulator.impl.NerveCellFactory
import ch.santosalves.neurosimulator.impl.NerveCellTypes
import ch.santosalves.neurosimulator.configuration.ConfigurationLoader
import ch.santosalves.neurosimulator.api.Simulation
import ch.santosalves.neurosimulator.configuration.SimulationConfiguration
import scala.io.StdIn
import java.util.UUID
import scala.collection.mutable.HashMap
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.ExecutionPlan
import ch.santosalves.neurosimulator.impl.ExecutionPlanImpl
import ch.santosalves.neurosimulator.impl.NeuronalNetworkImpl
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.impl.SimulationStepImpl
import scala.collection.immutable.ListSet
import ch.santosalves.neurosimulator.impl.SimulationStepImpl
import ch.santosalves.neurosimulator.impl.NerveCellExcitationImpl
import ch.santosalves.neurosimulator.impl.ExpectedNeuronOutputImpl
import ch.santosalves.neurosimulator.impl.SimulationStepImpl
import java.util.Observer
import java.util.Observable
import ch.santosalves.neurosimulator.api.ValueChanged
import ch.santosalves.neurosimulator.api.OutputTrigged
import ch.santosalves.neurosimulator.api.ValueChanged
import ch.santosalves.neurosimulator.api.ObservableObject
import ch.santosalves.neurosimulator.api.InputTriggered
import org.apache.logging.log4j.LogManager
import ch.santosalves.neurosimulator.impl.McCullochPittsNeuron

object SimpleConsoleSimulationApp extends App with Observer {
  val logger = LogManager.getLogger(classOf[App])
  var simulation: Simulation = _
  var outputs = ListSet[NerveCell]()

  override def update(arg0: Observable, arg1: Any) = {
    val neuron = arg0.asInstanceOf[NerveCell]

    arg1.asInstanceOf[ObservableObject] match {
      case ValueChanged(x, y) => logger.debug(s"[${neuron.name}] Sum value changed (old,new)->(${x}, ${y})")
      case OutputTrigged(v)   => logger.debug(s"[${neuron.name}] Output triggered with value ${v}")
      case InputTriggered(v)  => logger.debug(s"[${neuron.name}] Input triggered with value ${v}")
    }
  }

  def displayNetorkState(deep: Int, neuron: NerveCell): Unit = {
    val sb = new StringBuilder()
    Range.apply(0, deep).foreach { x => if (x < (deep - 1)) sb.append("   ") else sb.append("|--") }
    logger.info(s"[${neuron.name}]${sb.toString()}${neuron.sum}")
    neuron.dendrites.foreach { case (n, w) => displayNetorkState(deep + 1, n) }
  }

  private def execute() = {
    //load simulation 
    //ConfigurationLoader.printDefaultConfiguration()
    simulation = loadSimulation()

    //look for simulation outputs list7
    simulation.executionPlan.steps.foreach { step => step.outputs.foreach { output => outputs += output.neuron } }
    //simulation.neuronalNetwork.neurons.foreach { neuron => if(neuron.axoneTerminals.size == 0) outputs = outputs.::(neuron)}

    logger.trace("Simulation built. Starting simulation")

    //Before simulation start attach monitors for each neuron
    simulation.neuronalNetwork.neurons.foreach { neuron => neuron.addObserver(this) }

    //Foreach step run it step by step
    simulation.executionPlan.steps.zipWithIndex.foreach {
      case (step, index) =>
        logger.info(s"--- Execution step ${index + 1}/${simulation.executionPlan.steps.size} ---")

        step.inputs.foreach {
          input => input.neuron.triggerInput(input.weight)
        }

        step.outputs.foreach { output =>
          if (output.neuron.triggerFunction() == output.output)
            logger.info(s"[${output.neuron.name}] output value match with expected value")
          else
            logger.warn(s"[${output.neuron.name}] output value doesn't match with expected value")
        }

        logger.info("Network state -----------------------------------")
        outputs.foreach { neuron => displayNetorkState(0, neuron) }
    }

    logger.trace("SimpleConsoleSimulationApp execution terminated")
  }

  private def buildSimulation(conf: SimulationConfiguration): Simulation = {
    var neurons = new HashMap[String, NerveCell]()
    var links = new Array[SynapticLink](conf.neuronalNetwork.links.size)

    //loaded neurons
    conf.neuronalNetwork.neurons.zipWithIndex.foreach {
      case (n, i) => neurons.put(n.uuid, new McCullochPittsNeuron(UUID.fromString(n.uuid), n.name, n.threshold, n.description))
    }
    //loading links
    conf.neuronalNetwork.links.zipWithIndex.foreach { case (n, i) => links(i) = new SynapticLinkImpl(UUID.fromString(n.uuid), n.name, neurons(n.inputNeuronId), neurons(n.outputNeuronId), n.weight) }

    var ar = new Array[NerveCell](neurons.size)
    neurons.values.copyToArray(ar)

    /**
     * build execution plan part
     */
    var steps = new ListSet[SimulationStepImpl] //  new ListSet[SimulationStepImpl](conf.executionPlan.steps.size)

    conf.executionPlan.steps.foreach { s =>
      var inputs = new ListSet[NerveCellExcitationImpl]
      var outputs = new ListSet[ExpectedNeuronOutputImpl]

      s.inputs.foreach { i => inputs += new NerveCellExcitationImpl(neurons(i.neuronUUID), i.weight) }
      s.outputs.foreach { o => outputs += new ExpectedNeuronOutputImpl(neurons(o.neuronUUID), o.expectedOutput) }

      steps += new SimulationStepImpl(s.time, inputs.toList, outputs.toList)
    }

    var network = new NeuronalNetworkImpl(ar, links)
    var executionPlan = new ExecutionPlanImpl(steps.toList)
    return new SimulationImpl(network, executionPlan)
  }
  private def loadSimulation(): Simulation = buildSimulation(ConfigurationLoader.loadFromXmlFile(getConfigurationFilename()))
  private def getConfigurationFilename(): String = "configuration.xml" //StdIn.readLine("Configuration filepath : ")

  //execute app
  execute()
}
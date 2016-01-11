package ch.santosalves.neurosimulator.impl

import scala.Array
import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.api.NeuronalNetwork
import ch.santosalves.neurosimulator.api.NerveCell
import org.junit.Test
import org.scalatest.Assertions
import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.impl.NerveCellTypes.McCullochAndPitts
import ch.santosalves.neurons.impl.McCullochPittsNeuron
import ch.santosalves.neurosimulator.api.SynapticLink

class SimpleNeuronalSimulatorSuite extends Assertions {
  val narray = Array[NerveCell](
    NerveCellFactory.createNerveCell(NerveCellTypes.McCullochAndPitts, "N1", 2),
    NerveCellFactory.createNerveCell(NerveCellTypes.McCullochAndPitts, "N2", 3))

  val synlnk = Array[SynapticLink](new SynapticLinkImpl("L1", narray(0), narray(1), 3.2))
    
  val sns = new NeuronalSimulatorImpl(new NeuronalNetworkImpl(narray, synlnk))    
    
  @Test def testThatObjectsAreTheSame () {
    narray(0).sum = 10
    assert(sns.network.neurons(0).sum  === 10)
    assert(narray(0).sum === 10)
    assert(narray(1).dendrites.unzip._1.head.sum === 10)    
    assert(sns.network.neurons(1).dendrites.unzip._1.head.sum === 10)    

    sns.network.neurons(1).dendrites.unzip._1.head.sum = 1
    assert(sns.network.neurons(0).sum  === 1)
    assert(narray(0).sum === 1)
    assert(narray(1).dendrites.unzip._1.head.sum === 1)    
    assert(sns.network.neurons(1).dendrites.unzip._1.head.sum === 1)    

  }
    
  @Test def testThatSimulationHas2NeuronsAndALink() {    
    assert(sns.network.neurons.size === 2)
    assert(sns.network.synapticLinks.size === 1)
  }
  
  @Test def testThatN2isLinkedWithN1With3_2Weight() {    
    assert(sns.network.neurons(1).dendrites.size === 1)
    assert(sns.network.neurons(1).dendrites.contains(Tuple2(sns.network.neurons(0), 3.2)))
  }
    
  @Test def testThatIfITriggerN1InputN2WillTriggerOutput() {
    //because we now that for mcculloch and pitts neuron triggerFunction only returns values 1.0 or 0.0
    Ensuring.ensuring(sns.network.neurons(0).isInstanceOf[McCullochPittsNeuron])    

    //triggered input value < threshold => no output trigger
    sns.network.neurons(0).triggerInput(1.9)
    assert(sns.network.neurons(0).mustTriggerOutput === false)
    assert(sns.network.neurons(0).triggerFunction() === 0.0)
    
    //Since a value we triggered now a value higher than threshold == 2 
    //the output must be set
    sns.network.neurons(0).triggerInput(4.1)
    assert(sns.network.neurons(0).mustTriggerOutput === true)
    assert(sns.network.neurons(0).triggerFunction() === 1.0)
    
    //because we set a 3.2 weight for the N1 --3.2--> N2 link and because
    //N2 threshold == 3 then n(1)==N2 should trigger output         
    println("N2 sum value ??? " + sns.network.neurons(1).sum)
    assert(sns.network.neurons(1).triggerFunction() === 1.0)
    assert(sns.network.neurons(0).axoneTerminals.unzip[NerveCell, Double]._1.head.sum === 3.2)
    assert(sns.network.neurons(1).mustTriggerOutput === true)
  }  
}
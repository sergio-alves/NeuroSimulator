package ch.santosalves.neurosimulator.impl

import org.junit.Test
import org.scalatest.Assertions
import ch.santosalves.neurons.impl.McCullochPittsNeuron
import ch.santosalves.neurosimulator.api.SynapticLink

class SimpleSynapticLinkSuite extends Assertions{
  val narray = Array[McCullochPittsNeuron](
    NerveCellFactory.createNerveCell(NerveCellTypes.McCullochAndPitts, "N1", 2).asInstanceOf[McCullochPittsNeuron],
    NerveCellFactory.createNerveCell(NerveCellTypes.McCullochAndPitts, "N2", 3).asInstanceOf[McCullochPittsNeuron])
  val synlnk = Array[SynapticLink](new SynapticLinkImpl("L1", narray(0), narray(1), 3.2))

  @Test def testInitialization() {
    assert(synlnk.size === 1)      
    narray(0).triggerInput(4)
    
    assert(narray(0).mustTriggerOutput === true)
    assert(synlnk(0).source.mustTriggerOutput === true)
    assert(synlnk(0).target.dendrites.head._1.mustTriggerOutput === true)   
    assert(synlnk(0).name.equals("L1") === true)    
    assert(synlnk(0).source.axoneTerminals.head._1.mustTriggerOutput === true)
  }
}
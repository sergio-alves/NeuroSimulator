package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurons.impl.McCullochPittsNeuron
import org.scalatest.Assertions
import org.junit.Test
import java.util.Observer
import ch.santosalves.neurosimulator.api.NerveCell

/**
 * Sample specification.
 *
 * This specification can be executed with: scala -cp <your classpath=""> ${package}.SpecsTest
 * Or using maven: mvn test
 *
 * For more information on how to write or run specifications, please visit:
 *   http://etorreborre.github.com/specs2/guide/org.specs2.guide.Runners.html
 *
 */
class McCullochPittNeuronSuite extends Assertions {
  val neuron = new McCullochPittsNeuron("N1", 2.0)
  
  @Test def testIfNeuronShouldHaveN1ForName() {    
    assert(neuron.name === "N1")
  }

  @Test def testIfNeuronShouldHaveTheRightThreshold() {
//    val neuron = new McCullochPittNeuron("N1", 2.0)
    assert(neuron.threshold === 2.0)
  }

  @Test def testIfNeuronShouldTriggerOutputIfInputWeightIsHighterThanThreshold() {
  //  val neuron = new McCullochPittNeuron("N1", 2.0)

    neuron.triggerInput(3.0)

    var outputTriggered = 0
    
    neuron.addObserver(new Observer() {
      def update(arg0: java.util.Observable, arg1: java.lang.Object) = {
        assert(arg0.asInstanceOf[McCullochPittsNeuron].triggerFunction() === 1.0)
      }
    })

    assert(neuron.triggerFunction() === 1.0)
    assert(neuron.mustTriggerOutput === true)
  }
}

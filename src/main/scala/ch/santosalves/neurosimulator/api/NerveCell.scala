package ch.santosalves.neurosimulator.api

import org.apache.logging.log4j.LogManager
import scala.collection.immutable.ListSet
import jdk.internal.util.xml.impl.Input
import java.util.Observer
import java.util.Observable

/**
 * The base brick to construct neuron networks
 *
 * @author Sergio Alves
 */
trait NerveCell extends Observable with Observer {
  val logger = LogManager.getLogger(classOf[NerveCell])

  /**
   * The NerveCell name. Should be unique
   */
  var name: String = ""

  /**
   * The threshold value @ wish the output should be triggered
   */
  var threshold: Double = 0.0

  /**
   * The neuron value
   */
  var sum: Double = 0.0

  /**
   * The list of connected axoneTerminals => aka neuron outputs
   */
  var axoneTerminals: ListSet[Tuple2[NerveCell, Double]] = new ListSet()

  /**
   * The list of connected dendrites => aka neuron inputs
   */
  var dendrites: ListSet[Tuple2[NerveCell, Double]] = new ListSet()

  /**
   * Adds 'output' connection between this nerve cell and a second one.
   * calls the terminalCell to add this cell as input
   *
   * @param terminalCell the cell we wish to connect to
   * @param synapticWeight the weighting value for the connection
   */
  def -->(terminalCell: NerveCell, synapticWeight: Double): Unit = {
    axoneTerminals += new Tuple2(terminalCell, synapticWeight)

    //We set the reciprocity between cells if it wasn't already added
    //Must be checked to avoid cycle problems
    if (!terminalCell.dendrites.contains(Tuple2(this, synapticWeight)))
      terminalCell <-- (this, synapticWeight)

    logger.debug("[" + name + "] ---(w=" + synapticWeight + ")---> [" + terminalCell.name + "]")
  }

  /**
   * Adds 'input' connection between this nerve cell and a second one.
   * Calls the terminalCell to add this cell as output
   *
   * @param originCell the cell that will be linked with current
   * @param synapticWeight the weighting value for the connection
   */
  def <--(originCell: NerveCell, synapticWeight: Double): Unit = {
    dendrites += new Tuple2(originCell, synapticWeight)

    //we start listening to incoming events from originCell
    originCell.addObserver(this)

    if (!originCell.axoneTerminals.contains(Tuple2(this, synapticWeight)))
      originCell --> (this, synapticWeight)

    logger.debug("[" + name + "] <---(w=" + synapticWeight + ")--- [" + originCell.name + "]");
  }

  /**
   * Manually trigger an input
   *
   * @param synapticWeight the weighting of the input
   */
  def triggerInput(synapticWeight: Double): Unit = {
    sum = (dendrites.map(f => f._1.triggerFunction * f._2).sum + synapticWeight)

    //Update all observers of this nervcell => other nerve cells or monitors
    update(this, new ValueChanged(sum - synapticWeight, sum))

    //If value above threshold trigger output
    if (mustTriggerOutput) {
      //setChanged()
      notifyObservers(new OutputTrigged())
    }
  }

  /**
   * Checks whether threshold was exceeded
   */
  def mustTriggerOutput: Boolean = sum >= threshold

  /**
   * Called by an upstream cell having triggered an output
   * => we update current sum
   */
  def onOutputTriggered(cell: NerveCell): Unit = {
    val sum = dendrites.map(f => f._1.triggerFunction * f._2).sum
    //If value above threshold trigger output
    if (mustTriggerOutput) {
      //setChanged()
      notifyObservers(new OutputTrigged())
    }
  }

  /**
   * Implements the update method of observer trait
   *
   * @param observable The observed object
   * @param objectConcerne The message concern
   */
  def update(observable: Observable, objectConcerne: Object): Unit = {
    if (observable.isInstanceOf[NerveCell] && objectConcerne.isInstanceOf[ObservableObject]) {
      objectConcerne.asInstanceOf[ObservableObject] match {
        case OutputTrigged() => onOutputTriggered(observable.asInstanceOf[NerveCell])
        case _               => //We don't care
      }
    }
  }
  
  /**
   * Defines the NerveCell trigger function => the output value
   */
  def triggerFunction(): Double
}
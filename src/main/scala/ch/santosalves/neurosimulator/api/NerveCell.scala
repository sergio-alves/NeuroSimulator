package ch.santosalves.neurosimulator.api

import org.apache.logging.log4j.LogManager
import scala.collection.immutable.ListSet
import jdk.internal.util.xml.impl.Input
import java.util.Observer
import java.util.Observable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.annotations.XStreamAliasType
import com.thoughtworks.xstream.annotations.XStreamOmitField
import java.util.UUID

/**
 * The base brick to construct neuron networks
 *
 * @author Sergio Alves
 */
trait NerveCell extends Observable with Observer {
  val logger = LogManager.getLogger(classOf[NerveCell])

  /**
   * The nerve cell unique identifier
   */
  var uuid: UUID = _

  /**
   * The NerveCell name. Should be unique
   */
  var name: String = _

  /**
   * The threshold value @ wish the output should be triggered
   */
  var threshold: Double = _

  /**
   * Description  
   */
  var description: String = _

  /**
   * The neuron value
   */
  var sum: Double = _

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

    logger.info("[" + name + "] ---(w=" + synapticWeight + ")---> [" + terminalCell.name + "]")
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

    logger.info("[" + name + "] <---(w=" + synapticWeight + ")--- [" + originCell.name + "]");
  }

  /**
   * Manually trigger an input
   *
   * @param synapticWeight the weighting of the input
   */
  def triggerInput(synapticWeight: Double): Unit = {
    //Update observers. tell them that input was triggered
    setChanged()
    notifyObservers(new InputTriggered(synapticWeight))

    //Calc nerve cell value and notify observers if value changed
    updateSum(synapticWeight)
    
    //If value above threshold trigger output and notify observers
    if (mustTriggerOutput) {
      setChanged()
      notifyObservers(new OutputTrigged())
    }
  }

  /**
   * overrides observable method
   */
  override def setChanged(): Unit = {
    super.setChanged()
  }

  /**
   * Checks whether threshold was exceeded
   */
  def mustTriggerOutput: Boolean = sum >= threshold

  private def updateSum(inputTriggered:Double = 0) = {
    var _sum = inputTriggered
    dendrites.foreach { case (neuron, d) => _sum += (neuron.triggerFunction * d) }

    if (sum != _sum) {
      setChanged()
      notifyObservers(new ValueChanged(sum, _sum))
    }

    sum = _sum
  }

  /**
   * Implements the update method of observer trait
   *
   * @param observable The observed object
   * @param objectConcerne The message concern
   */
  def update(observable: Observable, objectConcerne: Object): Unit = {
    objectConcerne.asInstanceOf[ObservableObject] match {
      case OutputTrigged() => {
        //Calc nerve cell value and notify observers if value changed        
        updateSum()

        //If value above threshold trigger output
        if (mustTriggerOutput) {
          setChanged()
          notifyObservers(new OutputTrigged())
        }
      }
      case _ => { /* do nothing */ }
    }
  }

  /**
   * Defines the NerveCell trigger function => the output value
   */
  def triggerFunction(): Double
}
package ch.santosalves.neurosimulator.api

import org.apache.logging.log4j.LogManager
import scala.collection.immutable.ListSet
import jdk.internal.util.xml.impl.Input
import java.util.Observer
import java.util.Observable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

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
  var name: String = _

  /**
   * The threshold value @ wish the output should be triggered
   */
  var threshold: Double = _

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

  implicit val ec = ExecutionContext.global

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
    sum = (dendrites.map(f => f._1.triggerFunction * f._2).sum + synapticWeight)

    logger.debug("[" + name + "] with sum = " + sum + " triggerInput thread id : " + Thread.currentThread().getId)

    //If value above threshold trigger output
    if (mustTriggerOutput) { 
      //Future {
        setChanged()
        notifyObservers(new OutputTrigged())
        logger.info("[" + name + "] Observers (" + countObservers + ") have been notified")
     // }
    }
  }
  
  override def setChanged():Unit = {
    super.setChanged()
  }

  /**
   * Checks whether threshold was exceeded
   */
  def mustTriggerOutput: Boolean = sum >= threshold

  /**
   * Implements the update method of observer trait
   *
   * @param observable The observed object
   * @param objectConcerne The message concern
   */
  def update(observable: Observable, objectConcerne: Object): Unit = {
    logger.debug("[" + name + "] Observer notified")
    
    objectConcerne.asInstanceOf[ObservableObject] match {
      case ValueChanged(x, y) => logger.info("[" + name + "] Observer received a ValueChanged (from -> " + x +
          " to -> " + y + ") notification from thread id : " + Thread.currentThread().getId +
          " and the notification is from [" + observable.asInstanceOf[NerveCell].name + "]")
      case OutputTrigged() => {
        logger.info("[" + name + "] Observer received a OutputTrigged notification from thread " +
          " id : " + Thread.currentThread().getId + " and the notification is from [" + 
          observable.asInstanceOf[NerveCell].name + "]")
        
        var _sum = 0.0
        dendrites.foreach(f => _sum += f._1.triggerFunction() * f._2)
                    
        val __sum = dendrites.map(f => f._1.triggerFunction * f._2).sum
        
        logger.info("Checking two ways to calc sum : [" +_sum +" == "+__sum +"]")
        
        sum =__sum
        logger.info("["+name+"] has " + dendrites.size + " dendritic connexions and sum is "+ sum)
        
        //If value above threshold trigger output
        if (mustTriggerOutput) {
          logger.info("["+name+"] sum>threshold => " + sum + " > " + threshold + " ? " + mustTriggerOutput)          
          setChanged()
          notifyObservers(new OutputTrigged())
        }        
      }
      case _ => logger.warn("Should never occur")
    }
  }
    
  override def hashCode():Int = {
    var prime = 7
    var result = 1
    
    result = prime * result + name.hashCode() 
    result = prime * result + threshold.hashCode()
    result = prime * result + dendrites.hashCode()
    //result = prime * result + axoneTerminals.hashCode()
    result = prime * result + sum.hashCode()
   
    return result
  }
  
  override def equals(arg0: Any): Boolean = {
    if(arg0==this) return true 
    else if(arg0 == null)
      return false
    else {          
      if(arg0.isInstanceOf[NerveCell]) {
        val v = arg0.asInstanceOf[NerveCell]      
        return v.name.equals(name) && v.threshold.equals(threshold) && 
        v.dendrites.hashCode() == dendrites.hashCode() && v.axoneTerminals.equals(axoneTerminals) &&
        sum == v.sum
      } else {
        return false
      }
    }
    
    return false
  }
  

  /**
   * Defines the NerveCell trigger function => the output value
   */
  def triggerFunction(): Double
}
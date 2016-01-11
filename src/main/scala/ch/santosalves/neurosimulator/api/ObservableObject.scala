package ch.santosalves.neurosimulator.api

/**
 * Some Messages Types to be exchanged between observables and observers
 * 
 * @author Sergio Alves
 */
abstract class ObservableObject
  case class OutputTrigged(valut:Double) extends ObservableObject
  case class ValueChanged(old:Double, current:Double) extends ObservableObject
  case class InputTriggered(value:Double) extends ObservableObject

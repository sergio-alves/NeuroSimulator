package ch.santosalves.neurosimulator.api

/**
 * Some Messages Types to be exchanged between observables and observers
 */
abstract class ObservableObject
  case class OutputTrigged() extends ObservableObject
  case class ValueChanged(old:Double, current:Double) extends ObservableObject

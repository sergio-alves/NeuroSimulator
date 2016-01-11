package ch.santosalves.neurosimulator.impl

/**
 * Different approach for an enumeration of nerve cell types
 * 
 * @author Sergio Alves
 */
object NerveCellTypes {
  sealed abstract class NerveCellType {}
  case object McCullochAndPitts extends NerveCellType
}
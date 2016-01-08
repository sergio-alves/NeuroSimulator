package ch.santosalves.neurosimulator.impl

/**
 * Different approach for an enumeration of nerve cell types
 */
object NerveCellTypes {
  sealed abstract class NerveCellType {}
  case object McCullochAndPitts extends NerveCellType
}
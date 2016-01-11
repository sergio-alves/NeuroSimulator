package ch.santosalves.neurosimulator.api

import java.util.UUID

/**
 * Not really usefull class, since links are closely related to nerve cells but
 * for coherence purpose with ui interface that use a dedicated component to handle
 * it I decide to create such class
 *
 * @author Sergio Alves
 */
trait SynapticLink {
  var uuid: UUID = _
  var name: String = _
  var source: NerveCell = _
  var target: NerveCell = _
}
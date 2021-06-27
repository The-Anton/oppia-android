package org.oppia.android.scripts

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Script for ensuring that all the Activities the
 * repo are defined with accessibility labels.
 */
class AccessibilityLabelCheck {
  companion object {
    @JvmStatic
    fun main(vararg args: String) {
      // path of the repo to be analyzed.
      val repoPath = args[0] + "/"

      // path of the manifest file relative to the repo.
      val manifesFilePath = args[1]

      // full path of the manifest file.
      val fullPathToManifestFile = repoPath + manifesFilePath

      // class object which is needed to acess the helper methods.
      val accessibilityLabelCheck: AccessibilityLabelCheck = AccessibilityLabelCheck()

      // builder factory which provides the builder to parse the XMl.
      val builderFactory = DocumentBuilderFactory.newInstance()

      // document builder which parses the XMl.
      val docBuilder = builderFactory.newDocumentBuilder()

      // parsed DOM of manifest file.
      val doc = docBuilder.parse(File(fullPathToManifestFile))
      doc.getDocumentElement().normalize()

      // list of all the activity elements.
      val nodeList = accessibilityLabelCheck.convertNodeListToListOfNode(
        doc.getElementsByTagName("activity")
      )

      // list of activity elements which lacks the label.
      val matchedNodes = nodeList.filter { node ->
        accessibilityLabelCheck.checkIfActivityHasMissingLabel(node)
      }

      accessibilityLabelCheck.logFailures(matchedNodes)

      if (matchedNodes.size != 0) {
        throw Exception(ScriptResultConstants.ACCESSIBILITY_LABEL_CHECK_FAILED)
      } else {
        println(ScriptResultConstants.ACCESSIBILITY_LABEL_CHECK_PASSED)
      }
    }
  }

  /**
   * Checks whether a node has a missing label.
   *
   * @param node instance of Node
   * @return label is present or not
   */
  private fun checkIfActivityHasMissingLabel(node: Node): Boolean {
    val attributesList = node.getAttributes()
    val activityPath = attributesList.getNamedItem("android:name").getNodeValue()
    if (
      activityPath !in ExemptionsList.ACCESSIBILITY_LABEL_CHECK_EXEMPTIONS_LIST &&
      attributesList.getNamedItem("android:label") == null
    ) {
      return true
    }
    return false
  }

  /**
   * The [nodeList] is not iterable. This helper
   * function converts it to List<Node>.
   *
   * @param nodeList instance of NodeList
   * @return a list of nodes
   */
  private fun convertNodeListToListOfNode(nodeList: NodeList): List<Node> {
    return IntStream.range(
      0, nodeList.getLength()
    ).mapToObj(
      nodeList::item
    ).collect(
      Collectors.toList()
    )
  }

  /**
   * Logs the failures for accessibility label check.
   *
   * @param matchedNodes a list of nodes having missing label
   * @return log the failures
   */
  private fun logFailures(
    matchedNodes: List<Node>
  ) {
    if (matchedNodes.size != 0) {
      println("Accessiblity labels missing for Activities:")
      matchedNodes.forEach { node ->
        println(node.getAttributes().getNamedItem("android:name").getNodeValue())
      }
      println()
    }
  }
}

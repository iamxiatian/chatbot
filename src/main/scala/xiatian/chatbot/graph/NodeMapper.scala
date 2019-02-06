package xiatian.chatbot.graph

import xiatian.chatbot.conf.MagicValues
import xiatian.chatbot.entity.Category

import scala.collection.mutable

/**
  * A <code>NodeMapper</code> maps the branches in a {@link GraphMaster} structure.
  */
class NodeMapper {
  var parent: Option[NodeMapper] = None

  /**
    * 当前节点是否对应到了一个结尾的pattern，如果是，则同时在节点中保存完整的category
    */
  var category: Option[Category] = None

  var height = MagicValues.max_graph_height


  /**
    * 当前节点的分支
    */
  val branches = mutable.Map.empty[String, NodeMapper]

  def getOrInsert(key: String): NodeMapper =
    branches.get(key) match {
      case Some(node) => node
      case None =>
        //插入由该位置到达的新节点
        val node = new NodeMapper()
        branches.put(key, node)
        node
    }

  //branches.put(key, next)
}

object NodeMapper {
  /**
    * 从当前节点中寻找通向word的孩子节点
    */
  def locateChild(node: NodeMapper, word: String): Option[NodeMapper] =
    node.branches.get(word)
}
package xiatian.chatbot.graph

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

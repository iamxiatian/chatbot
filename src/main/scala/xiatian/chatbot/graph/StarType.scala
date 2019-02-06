package xiatian.chatbot.graph

sealed trait StarType

case object InputStarType extends StarType

case object ThatStarType extends StarType

case object TopicStarType extends StarType

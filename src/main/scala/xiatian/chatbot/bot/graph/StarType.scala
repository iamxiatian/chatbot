package xiatian.chatbot.bot.graph

sealed trait StarType

case object InputStar extends StarType

case object ThatStar extends StarType

case object TopicStar extends StarType

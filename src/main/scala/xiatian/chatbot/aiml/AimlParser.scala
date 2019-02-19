package xiatian.chatbot.aiml

import xiatian.chatbot.conf.Logging

import scala.xml.Elem

/**
  * he core AIML parser and interpreter.
  * Implements the AIML 2.0 specification as described in
  * AIML 2.0 Working Draft document
  */
object AimlParser extends Logging {

  def eval(templateDoc: Elem): Option[String] = {

    Some(templateDoc.text)
  }
}

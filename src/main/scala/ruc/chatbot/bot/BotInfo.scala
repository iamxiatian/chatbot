package ruc.chatbot.bot

trait BotInfo {
  def firstName: String

  def lastName: String

  def language: String

  def email: String

  def gender: String

  def version: String

  def birthplace: String

  def job: String

  def species: String

  def birthday: String

  def sign: String

  def religion: String

  def botMaster: String

  def getValue(param: String): String
}

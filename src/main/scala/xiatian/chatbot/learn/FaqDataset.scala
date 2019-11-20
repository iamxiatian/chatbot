package xiatian.chatbot.learn

import java.nio.charset.StandardCharsets

import better.files.File
import io.circe._
import io.circe.syntax._
import xiatian.chatbot.ability.faq.{Faq, FaqIndexer}

/**
  * FAQ数据集，FAQ的格式如faq.template.json所示
  */
object FaqDataset {
  def parse(jsonFile: File): Unit = {
    val jsonString = jsonFile.contentAsString(StandardCharsets.UTF_8)
    val doc: Json = parser.parse(jsonString).getOrElse(Json.Null)

    val cursor: HCursor = doc.hcursor
    val domain = cursor.downField("domain").as[String].getOrElse("default")
    val items: Vector[Json] = cursor.downField("data").focus
      .flatMap(_.asArray)
      .getOrElse(Vector.empty)

    val indexer = new FaqIndexer(File("./data/faq/index").path)

    items.foreach {
      case json =>
        // {
        //      "questions": [
        //        "具有相同含义的一组问题列表，如下面所示",
        //        "上火了吃什么降火最快？",
        //        "上火了怎么办"
        //      ],
        //      "answers": [
        //        "针对问题的不同回答，每个回答都是正确的，当问题匹配成功后，系统会随机选择一个答案输出",
        //        "食多容易引起上火，这时就要减少食物的摄入，特别是油腻、辛辣的食物，同时需要清凉解火，清凉解火并不是食用生冷的食物，而是温度适宜、帮助脾胃进行消化的温食。也可以根据心火、胃火、肝火等找专业的医生开针对性的方剂，进行治疗。就食物而言，“苦”味食品是“火”的天敌。苦味食物之所以苦是因为其中含有生物碱、尿素类等苦味物质，中医研究发现，这些苦味物质有解热祛暑、消除疲劳的作用。最佳的苦味食物首推苦瓜，不管是凉拌、炒还是煲汤，只要能把苦瓜做得熟且不失“青色”，都能达到“去火”的目的。除了苦瓜，还有其他苦味食物也有不错的“去火”功效，如杏仁、苦菜、苦丁茶、芹菜、芥兰、等，同样能清热解暑。",
        //        "上火的时候一般选择吃含水量多，同时富含维生素c的水果，比如橙子，它的的去火效果就很不错。",
        //        "多喝开水，喝水是最好的排毒的方法，通过喝水我们的身体能够稀释体内的毒素",
        //        "莴笋：具有清热利尿的功效，在降火方面效果不错",
        //        "对于爱上火的人群，平时要多喝白开水，促进体内新陈代谢，减少上火的几率。"
        //      ],
        //      "category": "类别，方便区分不同类型的数据，如法律问答(LAW)和中医药问答(Medicine)，具体名称请根据业务赋予一个英文字符串"
        //    }
        val c = json.hcursor
        val category = c.downField("category").as[String].getOrElse("")
        val questions: Seq[String] = c.downField("questions").focus
          .flatMap(_.asArray) //questions字段转换为数组
          .getOrElse(Vector.empty)
          .flatMap(_.asString)

        val answers: Seq[String] = c.downField("answers").focus
          .flatMap(_.asArray) //questions字段转换为数组
          .getOrElse(Vector.empty)
          .flatMap(_.asString)

        val jsonAnswer = answers.asJson.pretty(Printer.noSpaces)
        if (answers.size > 0) {
          questions.filter(_.nonEmpty).foreach {
            input =>
              val faq = Faq(input, jsonAnswer, Faq.TYPE_JSON, domain)
              indexer.index(faq)
          }
        }
    }

    indexer.close()
    println("DONE!")
  }

  def main(args: Array[String]): Unit = {
    if (args.size != 1) {
      println("Usage: faq-dataset jsonfile")
    } else {
      parse(File(args(0)))
    }
  }

}

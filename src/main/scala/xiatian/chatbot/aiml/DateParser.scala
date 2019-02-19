package xiatian.chatbot.aiml

import org.joda.time.DateTime
import xiatian.chatbot.aiml.AimlParser.recurseParse
import xiatian.chatbot.graph.MatchContext

import scala.xml.Elem

object DateParser {
  /**
    * 获取当前的日期, 可以带有一个参数format，用于设置日期格式, 日期格式符合java的日期格式
    *
    * ```xml
    * <category>
    * <pattern>十天前是几号？</pattern>
    * <template><date format="yyyy-MM-dd">10 days after</date></template>
    * </category>
    * ```
    *
    * @param node
    * @return
    */
  def parse(node: Elem, context: MatchContext): String = {
    val pattern = (node \ "@format").headOption.map(_.text).getOrElse("yyyy年M月d日")

    //获取日期内容，如：10 days after
    val content = recurseParse(node, context)
    val items = content.split(" ").filter(_.nonEmpty)

    //如果包含数字，和before，则把数字转换为负数
    val interval = items.find(_.forall(_.isDigit)).map(_.toInt).getOrElse(0) * {
      if (content.contains("before")) -1 else 1
    }


    val d: DateTime = if (content.contains("year")) {
      DateTime.now().plusYears(interval)
    } else if (content.contains("month")) {
      DateTime.now().plusMonths(interval)
    } else if (content.contains("day")) {
      DateTime.now().plusDays(interval)
    } else if (content.contains("hour")) {
      DateTime.now().plusHours(interval)
    } else if (content.contains("minute")) {
      DateTime.now().plusMinutes(interval)
    } else if (content.contains("second")) {
      DateTime.now().plusSeconds(interval)
    } else {
      DateTime.now()
    }

    if (pattern == "week") {
      weekdays(d.getDayOfWeek)
    } else d.toString(pattern)
  }

  val weekdays = Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
}

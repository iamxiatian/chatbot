# AIML Syntax

http://callmom.pandorabots.com/static/reference/

https://www.tutorialspoint.com/aiml/aiml_topic_tag.htm

## set and get

## date

获取当前的日期, 参数：
    - format：用于设置日期格式, 日期格式符合java的日期格式, 或者设置为week，返回星期几
    - 内容：可选节点，指明和当前的时间间隔，默认为0，负数为之前的时间，正数为之后的时间，如5 days, 8 minutes, 10 weeks

```xml
<category>
<pattern>* 天后是星期几</pattern>
<template>
<star/>天后<date format="yyyy-MM-dd"><star/> days after</date>是<date format="week"><star/> days after</date>
</template>
</category>
```
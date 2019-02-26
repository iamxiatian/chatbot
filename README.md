# Chatbot

## 功能改进
    - [X] 处理标点符号问题，所有标点符号，看作是一类符号。
    - [X] 问句分析支持词性处理
    - [X] pattern支持通过<term pos="xx"/>支持词性统配符匹配，例如限制为人名命中的情况： <pattern><term pos="nr"/>的故乡在哪里？</pattern>
    - [X] 改进Category对象，支持在pattern中加入子节点
    - [ ] 把统计得到的词语加入到分词词典之中，增加新词性

1. 人工整理100条常见的有关寻医问药的问句模板, 例子附后
2. 整理现有的医药相关词条的准确解释
3. 可视化演示界面
4. 学习aiml2.0语法 http://callmom.pandorabots.com/static/reference/

## 计划支持的通用功能：

1. 天气预报
1. 古文
1. 诗词
1. 百科
1. 音乐播放

## 功能列表
1. 核心功能：对话推理引擎，知识节点图的内部表示和匹配推理，各类标签处理。
    - [DONE]知识节点图的内部表示
    - [DONE]问句向匹配路径的转换（Path Generator）
    - [DONE] input, that, topic三个核心概念的组合处理
    - [DONE] AIML的加载
    - [DONE] 支持多个人的同时会话(chat session)
    - [DONE] 多个response的random处理
    - [DONE] template的基本解析
    - [PART] 会话历史的处理
    - [ ] Bot会话中知识的学习(set/get的处理)
    - [ ] srai循环处理标签
    - [DONE] star统配处理
    - [ ] 知识变量的替换处理
    - [ ] think语法的处理
    - [ ] map语法的处理
    - [ ] condition条件语法的处理
    - [ ] loop语法的处理
    - [ ] learn语法的处理


2. 问句核心词汇提取
    - [DONE]问句中的命名实体提取
    - [DONE]领域词汇处理
    - [PART]词语相似度计算(embedding)
    - 如何高效的在图中进行匹配？

3. 通用领域逻辑处理
    - 天气预报（注意上下文，参考小米语音助手）
    - 古文
    - 诗词
    - 百科
    - 音乐播放/歌词 ...

4. 问句分析失败后的检索补充处理
    - Lucene处理 

5. 专业领域支持
    - [DONE]药品症状数据的知识抽取（整理成百科形式）
    - [DONE]常见的药品问句分析(只加入了少量模式)
    - [DONE]常见的症状问句分析(只加入了少量模式)


6. 基于深度学习的通用对话逻辑(TODO)
    - 利用doc2vec训练每一个问句，生成句子向量PV(Paragraph Vector)，然后当匹配不成功时，
    利用PV寻找最接近的问句pattern
    
    
7. TODO:
   1. 根据抽取出的药品、疾病和症状数据，获取其对应的解释文本
   2. 药品、疾病和症状数据的分词处理 

## 参考资料

1. https://blog.csdn.net/qin8752/article/details/79386121
2. http://callmom.pandorabots.com/static/reference/

## Thanks

1. mp3agic: 
   A java library for reading mp3 files and manipulating mp3 file ID3 tags (ID3v1 and ID3v2.2 to
   		ID3v2.4). https://github.com/mpatric/mp3agic
   		
   		
   		
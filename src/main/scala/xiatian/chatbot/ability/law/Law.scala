package xiatian.chatbot.ability.law

//法规名称: 新疆维吾尔自治区电话和互联网用户真实身份信息登记管理条例
//法规简称及别称: 新疆电话和互联网用户真实身份信息登记管理条例
//发文字号: 新疆维吾尔自治区第十二届人民代表大会常务委员会公告第26号
//时效性: 现行有效
//发布时间: 2016-09-29
//实施时间: 2016-10-01
//发布部门: 新疆维吾尔自治区人民代表大会常务委员会
//效率级别: 地方性法规
//适用范围: 新疆
//法规类别: 互联网管理
//法规标签: 信息安全、身份信息、身份认证
//法规ID: 略
//信息来源: 中国人大网

case class Law(id: Int,
               fullName: String,
               abbName: String,
               issuedNumber: String,
               validity: String,
               pubTime: String,
               activeTime: String,
               issuedDept: String,
               effLevel: String,
               scope: String,
               category: String,
               tags: String,
               lawId: String,
               source: String
              ) {

}

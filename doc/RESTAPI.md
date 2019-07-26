# Restful API #

## 在线添加FAQ数据 ##

以POST方式，将JSON内容POST到如下地址： http://server_ip:7000/api/faq/add.do

内容如下：

```json
[
{"question": "什么是区块链", "answer": "区块链是BlockChahin..."},
{"question": "什么是区块链", "answer": "区块链是BlockChahin..."}
]
```
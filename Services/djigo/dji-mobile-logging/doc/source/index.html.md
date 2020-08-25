---
title: Dji mobile日志收集API

language_tabs:
  - ruby
  - Objective-C
  - Java

toc_footers:
  - <a href='http://120.27.228.232/app/kibana'>可视化报表</a>
  - <a href='mailto:glen.wang@dji.com?cc=jim.xiang@dji.com'>问题与建议</a>


search: true
---

# Introduction

这是一个简单实用的日志收集系统， 支持两种上传方式:

1. 使用http post表单
2. 上传日志文件

传输数据格式使用 [msgpack](http://msgpack.org)， 原因是：

* 目前报表需求不是特别明确，使用可以任意定义key的序列化格式更加符合实际。当整个系统和业务需求更加明确以后，可以考虑效率更高的Avro、Protobuf。
* 相对于同样规则自由的JSON， msgpack格式序列化之后数据更小，传输效率更高。
* 支持的语言多， 使用简单。

目前系统运行在测试环境中，地址：120.27.228.232


# 使用HTTP Post表单

## HTTP Request
`POST http://host/api/v1/log`

### 用Token识别APP
在HTTP header利用关键字:
`dji_app_token`
标识当前发送日志的app名称和客户端，比如，我们可以使用唯一的token表明这是Dji Go的安卓客户端。


> 将序列化后的数据post到这个API，可以发送单个或者多个事件

```ruby
require 'rest-client'
require 'msgpack'

header = {dji_app_token: "token_for_your_app_and_client"
          content-type: "application/x-msgpack"}
# 发送单个事件
RestClient.post('http://#{host}/api/log', MessagePack.pack({k:"v"}), header)

# 使用数组发送多个事件
RestClient.post('http://#{host}/api/log',
   MessagePack.pack([{k1:"v1", k2:"v2"}, {k3:"v3", k4:"v4"}]),
   header)
```

> 这段代码会发送一个单事件和一个批量事件

```json
{
    "k":"v"
}
和
[
  {
    "k1":"v1",
    "k2":"v2"
  },
  {
    "k3":"v3",
    "k4":"v4"
  }
]
```
<aside class="notice">
  表单内容是msgpack序列化结构体后的二进制数据，适合发送比较小的日志
</aside>


# 使用Log文件上传

## HTTP Request
`POST http://host/api/v1/upload`
### 用Token识别APP
在HTTP header利用关键字:
`dji_app_token`
标识当前发送日志的app名称和客户端，比如，我们可以使用唯一的token表明这是Dji Go的安卓客户端。

> 如果日志数据比较大，可以通过上传文件的方式发送

```ruby
require 'rest-client'
require 'msgpack'

# 写文件的过程，注意多个纪录之间不需要换行符
filename = 'logfile'
File.open(filename, "a+") do |f|
  f.write(MessagePack.pack({k:"v"}))
  f.write(MessagePack.pack([{k1:"v1", k2:"v2"}, {k3:"v3", k4:"v4"}]))
end

# 真正发送上传文件请求
header = {dji_app_token: "token_for_your_app_and_client",
          content-type: "application/x-msgpack"}
RestClient.post('http://#{host}:4567/api/upload', log: File.new(filename), header)
```

> 这段代码会把一个单事件和一个批量事件写入文件中，然后上传

```json
{
    "k":"v"
}
和
[
  {
    "k1":"v1",
    "k2":"v2"
  },
  {
    "k3":"v3",
    "k4":"v4"
  }
]
```

<aside class="notice">
  文件内容跟/api/v1/log这个接口的表单内容一样，多个纪录之间不需要分隔符，每次都写在文件末尾即可
</aside>

# Log上传之后

## ELK服务
日志数据会保存在[Elasticsearch](https://www.elastic.co/)中，自动建立索引，并且通过[Kibana](https://www.elastic.co/products/kibana)能够进行可视化的关键字搜索和报表生成。

![可视化](images/kibana.png)

<aside class="notice">
  kibana测试服务地址：[http://120.27.228.232/app/kibana](http://120.27.228.232/app/kibana)
</aside>

## AWS s3
日志会定期归档到AWS s3里， 目标是保留所有用户的数据。

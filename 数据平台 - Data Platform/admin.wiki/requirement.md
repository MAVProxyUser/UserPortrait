# 需求文档

[回首页](home)

## 需求列表

### 数据需求

+ 中日美德四国用户的基本行为统计（飞行时间、飞行次数）
+ 热力图数据标准化（算平均，而非总数）
+ 从出厂到激活时间统计计算
+ 代理商串货行为监测（比如发货到欧洲但第一次激活在美国）
+ 飞行数据的挖掘和利用

### 日志需求

logstash + logrotate

+ 对接 DJI Service 的日志
+ 对接监管项目的日志

### 平台需求

+ AWS 启用 ES 集群，从 1 台扩展为 2 台
+ ~~阿里云为监管项目部署一台 ES 做测试~~

## 任务列表

### 进行中

+ 导出已有的统计数据：daily-app-usages, daily-connected-products, daily-flight-stats, flight-distance-stats，保存到服务器 -> 上传到 S3 保存
+ 把飞行数据导出为 parquet 格式并保存在 S3中
+ 基于 AWS SDK 的 Spark 集群的 Java 管理脚本
+ 找 IT 要发货的历史数据
+ 从 S3(US) 和 OSS(CN) 处直接获取完整的飞行日志文件，确定 Bucket 和访问方式，需要对应的 access key 和 access id，具体如何访问看 AWS 和 Aliyun 文档
+ ES 的外网访问安全问题
+ 了解日志格式和编写读取脚本，考虑写入到 ES 中再进行处理统计

### 已排期

+ 检查重复点，导入 Aliyun 飞行记录
+ 分布式 ES 

### 已完成

+ 导入 USA 飞行记录（不再导入到 es 中）
+ 导入 USA 激活信息
+ 导入 Aliyun 激活信息
+ 基本文档整理
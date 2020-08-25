[回首页](home)

## 基本状况

+ 周报内容：报告进展状态，是完成进度还是拖延，简单说明即可
+ 美国联系人：walter.stockwell@dji.com

## 周报一览

### 160801-160807

UTM 项目本周因需求变更而重新置顶计划，本周已完成：

1. 和 Mingyu 汇报，确定一期任务
2. 和 DJI GO 团队沟通
3. 新的项目计划与需求文档
4. 技术调研与测试

### 160725-160731

UTM 项目本周已完成进度：

1. 和美国同事Walter对接，确定防撞方案、数据收集范围和空域预留机制
2. 完成技术调研&测试，包括后端服务器框架、信息源获取、接口设计方案
3. 向mingyu汇报，制定短期内项目需求（两个功能：临近飞机/航线提醒、威胁天气提醒）

## 时间线

### 160803 和 DJI GO 对接

几个问题

+ 监管那边的数据上传是怎么做的，有没有具体的文档
+ 我们这边如何可以自己更改应用做测试版？主要涉及两个方面：地图绘制和即时通知
+ iOS 和安卓这边有技术问题可以问谁？监管那边是怎么合作的？
+ 数据上传部分问题，界面设计问题和谁对接

### 160801 向 Mingyu 汇报

**项目计划大改**

mingyu聊一下大方向，以免跑偏了后面需要花比较多时间。

和现在 utm 项目的异同，为什么需要重新设计

utm 是我们第一次为全部用户提供了解周围情况，以及其他飞机，甚至是社交领域的基础，在上面可以有很多不一样的探索和尝试

**从中学到的，理清核心需求，找出核心贡献点，太长远的且依赖别人的暂时看来虚无缥缈的需求不做，这一点很重要**

#### 目标

+ 阶段目标：可实际测试运行的 UTM 系统
+ 意义：参与到欧美市场的发展趋势中，达到入场门槛，占据一席之地，拓宽受众群体

#### 开发计划

+ 基础系统搭建（时间：2-3 周，8/19，产出：可运行的基础系统）
    + 虚拟机申请和配置、技术可行性测试（1周）
    + 后台服务器架构设计与搭建，数据流设计与处理（1-2周）
+ 基础功能（时间 4-6 周，9/30，产出：可以实地测试的 UTM 系统）
    + 数据传输：DJI GO 与服务端连接，数据双向传输测试（1周）
    + 飞行通知：分为两种，针对起飞用户和附近用户（1-2周）
    + 预留空域：暂时以通知的形式，例如当用户飞入已预留的空域则进行通知（1-2周）
    + 天气状况：雨、雪、雹、大风、雾（1周）
    + 飞机航线状况（1周）
+ 高级功能（时间 4-6 周，11/11，产出：带高级功能的 UTM 系统）
    + 数据分析统计：热点飞行地区，危险地区（1周）
    + 用户广播消息：商业用户、娱乐用户，结合 Facebook 等社交平台（1-2周）
    + 智能防撞：具体细则还需要跟 Walter 讨论（2-4周）

注：目前此项目的开发人员只有我一人，还需要承担一部分产品经理的职责，另外因为如故团队人员调整我还需要负责数据平台事宜，故各阶段可能会比计划中延迟 1-3 周

#### 与现有监管项目的区别

+ 用户
    + 监管：政府或其他监管部门；用户量有限，平稳增长
    + 服务：所有 DJI 无人机用户 及 互联网用户；用户量较多，可能爆发增长
+ 目的
    + 监管：提供一种监管方式，推进立法
    + 服务：支持超视距飞行与智能辅助飞行
+ 意义
    + 监管：促进无人机产业的规范化，满足政府的监管需求
    + 服务：参与到欧美市场的发展趋势中，入场占位，拓宽受众群体
+ 方法
    + 监管：审批、流程、管理；半自动、应用
    + 服务：稳定、服务、社交；自动、服务
+ 模式
    + 监管：信息收集、管理；信息流聚合；无向外传播
    + 服务：基于地理位置的信息聚合；信息流发散； LBS 方式、社交网络方式
+ 地区
    + 监管：国内
    + 服务：美国、欧洲

#### 未来方向

+ 无人机社交的尝试，和 Facebook 连接，走 Nike Running Plus 路线，可以在 Facebook/Twitter 发送状态（包含路线、主体、图片等各类信息），别人可以点赞关注和评论欢呼等
+ 超视距飞行的基础技术支持，更高级的环境感知
+ NASA UTM 系统的接入
+ 多机集群协同飞行的支持，表演性质的固定线路飞行支持

#### 需求

+ 开发人员
+ 测试用无飞机：用于数据传输测试与实地飞行测试
+ 测试版 DJI GO 程序：用于技术可行性测试、数据传输测试与实地飞行测试

### 160721 Andrew 开会

+ 决定重新设计系统，根据需求来 break down
+ 走 mobile sdk 系统的工作流程，脚本部署，统一登录与帐号管理，很先进（这个要具体说一下） OneLogin，包含具体的帐号和配置
+ node.js
+ Amazon IoT 服务
+ ansible
+ terraform
+ 有自动部署的脚本
+ Amazon Lambda
+ Elastic Search
+ Blue / Green 发布
+ Amazon Kinesis
+ Amazon Container
+ ELK， Elasticsearch, Logstash, Kibana
+ grpc.io
+ Jenkins
+ Dev, Staging, Product
+ 基于 mobile SDK 自己开发
+ Springboot 和 websocket 会有问题

### 160718 更新

+ 代码无文档、无单元测试，通过 jar 包部署，没有持续集成
+ 无人机上传数据的格式，绘图与通知稍后我与 DJI GO 对接
+ 代码 Host 在我们的 gitlab
+ 机器由我们这边申请
+ 初步构想：Jenkins+Gradle+gitlab 自动构建发布，spring boot
+ Andrew 部分的数据上传代码

让 Walter 跟 mingyu 说该项目和 onboard sdk 不冲突，可以安排人（Andrew）来做

代码能跑起来了

后面加入数据分析的东西，Spark Streaming

快立项了申请招人


### 160712 邮件回复

+ 立项相关翻译与事宜，邮件同意，等等
+ 目前我们只能用自己制造的数据来进行测试，美国那边收集信息和测试的可能性
+ 应该很快会收到代码访问的权限（只读）
+ list of actions 可以应用于目前系统
+ DJI GO 具体的测试方式，能否有测试版
+ 会部署另一套在美国的系统
+ Weekly meeting call 时间
+ Confluence is better
+ 如何看待 google 和 amazon 提出的 proposal

Purpose: Deploy a UTM system as part of DJI GO in NA and EU to help our drones to know more about the environment to make flights safer and more convenient.

Abstract:

1. For User: The UTM system will be combined with DJI GO, providing the features of data uploading, message notification and environment sensing.
2. For Management: Different types of administrators can set up NFZ(no flying zone) or notify nearby drones' pilots
3. Provide importing and exporting solution for 3rd party application or other UTM system.

Plan:

1. Explore the possibility of UTM system in promoting safety and efficiency for drones
2. Build the system based on requirements and coop with other system in order to make full use of resources
3. Define protocol and standard along with other information sources and UTM system.

### 160708 团队会议

+ 基本组件
    + 【厂方】适航系统
    + 【监管方】飞机注册系统：身份绑定
    + 【监管方】公安局对接 APP
+ 相关问题
    + 给谁做，Demo 还是产品
    + 确定需求
    + 客户端开发与展示，geofencing 
    + 拉上 Branden 一起聊一次
    + 交互哪边出

### 160708 与美国团队会议要点

+ 项目正式成立，下午丁准去要项目编号
+ Walter 是 leader，Andrew 是 Tech leader
+ 不仅仅要考虑美国、欧洲，还有其他地区，本地化
+ 沟通机制建立，开发方式，需求讨论
+ 下午两点沟通

### 160630 与 Branden 沟通

拨打美国电话 9001 + 美国电话，比如 9001 202 826 3111 电话是真的

> UTM 这个项目怎么看，价值所在

有很大的价值，在安全上有很大提升。FAA 最近的趋势是无人飞行器要在视距范围内飞行，这样才能保证安全，不然一旦超过视距，就没有办法避免碰撞。而 UTM 项目可以突破这个限制，即使在用户视距之外，也可以通过手机屏幕了解周围情况（环境感知），来避免碰撞。

> 和 Geo Fencing 项目是否冲突

不冲突，是很好的互为补充，可以统一通过 DJI GO 来展示给用户，比方说禁飞区，或者诸如火灾、球赛等涉及『动态交通流量』管制的信息，但具体还有一些技术问题需要解决。

> 如何做，具体的时间安排

以为简单的事情可能很难（显示多边形地图，目前由于飞控的计算能力问题以圆形为主），以为很难的问题可能很简单。要考虑具体展示什么给用户。

最重要的信息是获取民航飞机、直升飞机、热气球等载人飞行器的飞行信息，避免无人机和它们相撞。这部分工作可能比较复杂，在不同的国家和地区需要不同的对接方式

> Walter, Darren 是否需要参与，扮演什么角色

背景：以 NASA 牵头，联合许多美国科技公司（尤其是 Amazon，因为要使用无人机送货）在做一套与我们设想类似但不太一样的 UTM 系统（根据美国法律规定只有美国公司可以参与和 NASA 的合作研究），但更多主要的是涉及受控飞行，类似于航空航线系统的无人机版，给不同的无人机设定好不同的路线避免碰撞（这里我们针对个人飞行行为，有比较大的不同）。这里可以考虑换个代号，不然以后推广很容易造成误解。

Walter 对于 NASA UTM 有很深的了解，我们可以从他身上了解 NASA UTM 的相关技术标准和动态，从中参考借鉴

#### 给 Walter 的话

Hi Walter,

John and I had a conversation with Branden and he said this UTM project is good and worth developing. I've sent Mingyu a email about it. We can find some time to have further discussion about our DJI UTM project. 

Besides, Branden told us that you are quite familiar with the NASA UTM project. We really like to have more information about it from you. But before that, you can talk to Mingyu about this UTM project and make it a real project. We think you can lead the project and we can give you support on building the system.

#### 杂项

+ Branden 可能在 9 月份会来深圳，可以当面沟通
+ 跟 Walter 发消息，要他跟 mingyu 说一下，沟通一下接下来的安排
+ 跟 hank，mingyu，fiona，john，pillar 发邮件


### 160623 会议准备

Unmanned Traffic Management System(UTMS)

#### Target

1. 摸底
2. 功能（原型，API）
3. 合作方式（我们这边来主导？）

+ we should define the concept -> UTMS -> standard -> laws
+ define protocol
+ should not use the word 'tracking' as it's related to some privacy issue.
+ focus on traffic(whole thing), not tracking(more personal). the focusing points are different

#### Overview

+ China UTM project. Testing by ourself in hainan province(plan)
+ collect data from other sources
+ connect to other UTMS for drone such as package delivery plan from Amazon
+ cloud platform - realtime traffic, weather, other info about safer operation
+ Mobile app - send, location, altitude, speed, other info about telemetry, in real time

#### Feature

+ Nearby DJI UAS Operations Warning, popular flying area, anonymized telemetry
+ With other extended features
+ Real-time ID - share data

#### Plan

1. staffing, backend engineering team(US), android app -> their progress, what's the team.
2. integrated with DJIGO app and backend -> we'd like to make the UTMs system independent and provide apis to DJIGO

#### Questions

+ prototype status, how they use mobile SDK? displaying?
+ tech stack
+ drone -> phone -> server, how to upload?
+ detailed plan for the UTMS
+ admin interface needed？monitor the conditions
+ DJIGO app 和 SDK app 的共性
+ Web Client 和 ID Client 是什么
+ ADS-B range, the protocol in sending the data

#### People

+ Backend, 3 FTE(US), architecture, infrastructure, coding, testing, operations
+ DJIGO app, 2 PTE for the functions in the app. join US mobile SDK team? 这里有个疑问，darren 和 sdk 那哥们儿是一派的？
+ Product, 1 PTE product management, fully specified features, mocks, functionalities
+ Project, 1 PTE project management, coordinate
+ Marketing, 1 PTE marketing

每个人是干嘛的

+ Andrew: 硅谷工程师
+ 秉臻: 软件部的 pm，DJI GO
+ Darren: 美国那边弄 UTM 标准的，没啥进展
+ Walter: 硅谷那边的头

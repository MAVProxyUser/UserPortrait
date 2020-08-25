# 数据平台相关资料

[回首页](home)

+ [Web 界面](http://statistical-admin.djiservice.org/djigo)
+ [Kibana 视图](http://statistical.active.kibana.djiservice.org/app/kibana)

## 登录跳板机

可以使用 `ssh-copy-id` 和 `expect` （都需要自行安装）配合写脚本进行自动登录跳转并切换用户。具体原理和教程后面补充

## Rsyslog 配置

### 转发日志
 
DJIService 的配置 `mina pro-hz-dsweb01 run[bash]` 这台机对 log 进行转发，发到 pro-hz-dsweb02 进行转发

rsyslog 的配置在 `/etc/rsyslog.d` 文件夹中的 `10-file.conf` 中，内容为

杭州配置

```
$ModLoad imfile
$template myFileMonitorTemplate,"%msg%"

# File access
#$InputFileName /data/home/service/project/current/log/logstash_production.log
$InputFileName /data/home/service/project/shared/log/logstash_production.log
$InputFileTag dji-log:
# 下面这行是用来保存文件状态的
$InputFileStateFile stat1470781524
$InputFileSeverity info
$InputFileFacility local7
$InputFilePollInterval 1
$InputFilePersistStateInterval 1
$InputRunFileMonitor

if $syslogtag contains 'dji-log' and $syslogfacility-text == 'local7' then @@192.168.6.26:15228;myFileMonitorTemplate
& stop
```

美国配置

```
$ModLoad imfile
$template myFileMonitorTemplate,"%msg%"

# File access
#$InputFileName /data/home/service/project/current/log/logstash_production.log
$InputFileName /data/home/service/project/shared/log/logstash_production.log
$InputFileTag dji-log:
$InputFileStateFile stat1470812347
$InputFileSeverity info
$InputFileFacility local7
$InputFilePollInterval 1
$InputFilePersistStateInterval 1
$InputRunFileMonitor

if $syslogtag contains 'dji-log' and $syslogfacility-text == 'local7' then @@10.1.1.57:15228;myFileMonitorTemplate
& stop
```

然后看 `/etc/logrotate.d` 文件夹中的 `_pro-hz-dsweb01` 文件，内容为

```
/data/home/service/project/shared/log/*.log {
    daily
    size 200M
    missingok
    rotate 52
    compress
    delaycompress
    notifempty
    copytruncate
postrotate
 (sudo stop rsyslog && sudo sed -i "s/InputFileStateFile.*/InputFileStateFile stat$(date +'%s')/" /etc/rsyslog.d/10-file.conf && sudo start rsyslog 2>&1) || true
        endscript
}
```

这里需要注意的在 `postrotate` 部分会把 logrotate 处理完的文件的状态位置保存下来。

如果需要把之前的日志导入 es，只需要进入 `/data/home/service/project/current/log` 文件夹，对每个文件进行 cat xxx.log | nc 192.168.6.26:15288 即可

### 转发到 ES

用来转发的机器也是会利用 rsyslog 转发到 logstash 的端口

包括 logstash server 配置

然后到 `mina pro-hz-dsweb02 run[bash]`

老的日志在 `/data/home/service/project/current/log`，用同样的方法导入

修改在 `/etc/logstash/conf.d` 的配置可以更改位置，杭州配置内容为

```
# 杭州配置
input {
 tcp {
   port=> 15228
   codec =>   json_lines
   buffer_size => 65536
   workers => 8
  }
}

filter {
  geoip {
    source => "ip"
    fields => ["location"]
  }
}

output {
    elasticsearch {
        hosts => ["http://statistical-es.djiservice.org:80"]
        index => "djiservice-log-" + "%{time}"[0,10] # 这里按照时间来写入
        user => "x"
        password => "x"
    }
}
```

AWS 配置内容为
 
```
input {
 tcp {
   port=> 15228
   codec =>   json_lines

buffer_size => 65536
workers => 8
  }
}

filter {
  geoip {
    source => "ip"
    fields => ["location"]
  }
}

output {
    elasticsearch {
        hosts => ["http://statistical-es.djiservice.org:80"]
        index => "djiservice-log-" + "%{time}"[0,10]
        user => "x"
        password => "x"
    }
}
```
## 虚拟机配置

先登录跳板机，再进行下列操作


### 杭州 阿里云

切换到杭州阿里云服务器 `pro-hz-dsweb02`，这上面有 Logstash 相关配置

```bash
# 杭州
mina pro-hz-dsweb02 run[bash]
# 需要进入 Logstash 所在文件夹
cd ../logstash-5.0.0-alpha4
# 运行模板
./bin/logstash -f file.conf
# 例子：测试导入 hz actived info
./bin/logstash -f ./confs/hz_actived_info.conf
```

这里有一点需要注意，因为有一段时间杭州和 AWS 的数据库有主从同步，所以需要过滤掉前面一致的记录，具体的间隔段在代码中有，只有第一次导入的时候需要，之后就可以利用记录的值继续，记得修改代码。

目前已经把之前的数据导入完成，新的数据请按照服务器上的代码进行导入


### 美国 AWS

```bash
# AWS
mina spro-usa-dsweb03 run[bash]
# 需要进入 Logstash 所在文件夹
cd ~/work/logstash-active/
# 例子：导入飞行数据和飞行记录（总览）
bin/logstash -f confs/daily_flights_stats.conf
bin/logstash -f confs/fly_records.conf
```

### Elasticsearch 

因为 ES 支持 RESTful API，所以我们可以利用 `curl` 命令来进行增删查改（可以把 ES 当面向文档的数据库用）

```bash
# 例子：删除飞行记录索引
curl -XDELETE -u logstash:PNIJZJWZ.NaJU 'statistical.active.djiservice.org:80/fly-records'
# 例子：删除激活信息索引
curl -XDELETE -u logstash:PNIJZJWZ.NaJU 'statistical.active.djiservice.org:80/actived-info'

# 例子：获取飞行记录索引信息
curl -u logstash:PNIJZJWZ.NaJU 'statistical.active.djiservice.org:80/fly-records'
# 例子：获取激活索引信息
curl -u logstash:PNIJZJWZ.NaJU 'statistical.active.djiservice.org:80/actived-info'
```

## Rake 任务

目前已迁移到 statistic-server 项目中

从 Flurry 和 Google Analysis 导入数据的代码在 `data_center_backend/lib/tasks` 文件夹中，具体导入的命令为

```ruby
# 导入 flurry
rake flurry:fetch

# 导入 google analysis
rake ga:fetch
```

## Logstash 任务

无论在杭州还是在 AWS 上的 Logstash 文件夹中，都有 `confs` 文件夹，里面包含具体的 logstash 配置，其中，每次正常运行后，都会把之前的运行记录在类似 `.[usa|hz]_*_*_last_run` 的隐藏文件中，注意不要删除。

实在不放心可以用 `cp` 命令进行备份，如 `cp .hz_actived_info_last_run .hz_actived_info_last_run.bak`

如果遇到内存不够的情况，考虑去掉 prefetch，并且开启 paging 功能

```
jdbc_fetch_size => 100

jdbc_paging_enabled => true,
jdbc_page_size => 2000,
```

daily-app-usages, daily-connected-products, daily-flight-stats, flight-distance-stats, 数据都很少，基本是直接都可以用的统计数据

## Kibana 相关

在使用 Logstash 把数据导入 Elasticsearch 后，需要在 Kibana 的 Setting 中手动添加对应数据的索引，才能显示出来。

## 飞行日志原始数据

数据因为历史原因保存在不同地方，具体的时间轴大概如下：

```txt
                    16年3月       16年6月
|----------------------|-------------|--------------> 时间轴
|       旧 AWS S3      |   阿里 OSS   |  阿里 OSS 国内
|     （国内外均在）     | （国内外均在） | 新 AWS S3 国外
```

### 数据异常问题

+ 固件版本号为00.00.00.00，说明固件需要升级
+ OSMO sn非法或为空，表现为 sn:(null), sn:-1-1-1-1-1-1-1-1， 需要返厂
    + 例子：邮箱:”ryan500168@gmail.com"  7月20号
+ 飞机激活提示非法设备，固件版本号为00.00.00.00，需要升级飞机固件
    + 例子: 邮箱: “1073457478@qq.com” 7月20号
    + 737207405@qq.com 7月17号
    + 437321421@qq.com 7月18号
+ 服务器错误，可能是设备取名非法字符导致的
    + 例子：”xiaobudian712@163.com"

## DJI Service 相关

### 日志信息

详细内容均在 `dji-service/lib/dji/` 文件下，包含下载与解析日志内容，这部分可以重用，这里只简要记录关键信息

飞行记录字段

+ `total_time` 该次飞行总计飞行时间，单位毫秒 ms
+ `total_distance` 该次飞行总计距离，单位米 m
+ `max_height` 该次飞行最大高度，单位米 m
+ `max_horizontal_speed` 该次飞行水平最大速度，单位米每秒 m/s
+ `max_vertical_speed` 该次飞行垂直最大速度，单位米每秒 m/s
+ `capture_num` 该次飞行拍摄次数，单位次
+ `video_time` 该次飞行录像时间，单位秒 s
+ `drone_type` 该次飞行无人机类型，具体型号为
    +  Inspire = 1
    +  Phantom3C = 2 对应 P3S（具体产品型号）
    +  Phantom3S = 3 对应 P3A（具体产品型号）
    +  Phantom3X = 4 对应 P3P（具体产品型号）
    +  Longan = 5
    +  N1 = 6
    +  Phantom4 = 7
    +  LB2 = 8 飞控
    +  Inspire1Pro = 9
+ `app_version` 该次飞行应用版本

### 收集的信息

具体可以参见 `app/models` 文件夹

+ 激活信息 Active Info `acitve_info.rb`
+ App 信息 App `app.rb`
+ 封禁 SN 信息 BanSnInfo `ban_sn_info.rb`
+ 电池信息 BatteryInfo `battery_info.rb`
+ 校准信息 CalibrationInfo `calibration_info.rb`
+ 每日活动 DailyActivation `daily_activation.rb`
+ 每日 App 使用 DailyAppUsage `daily_app_usage.rb`
+ 每日连接产品 DailyConnectedProduct `daily_connected_product.rb`
+ 每日飞行数据 DailyFlightStat `daily_flight_stat.rb`
+ 每日固件使用 DailyUsingFirmware `daily_using_firmware.rb`
+ 设备信息 Device `device.rb`
+ 设备注册日志 DeviceRegisterLog `device_register_log.rb`
+ 固件 Firmware `firmware.rb`
+ 固件详情 FirmwareDetail `firmware_detail.rb`
+ 固件包 FirmwarePack `firmware_pack.rb`
+ 固件升级日志 FirmwareUpgradeLog `firmware_upgrade_log.rb`
+ 飞行距离数据 FlightDistanceStat `flight_distance_stat.rb`
+ 飞行概况 FlightOverview `flight_overview.rb`
+ 飞行概况（旧） FlightOverviewOldNotInNew `flight_overview_old_not_in_new.rb`
+ 飞行问题 FlightProblem `flight_problem.rb`
+ 飞行记录详情 FlightRecordDetail `flight_record_detail.rb`
+ 飞行 UUID 概况 FlightUuidOverview `flight_uuid_overview.rb`
+ 飞行 UUID 概况（3）FlightUuidOverview3 `flight_uuid_overview3.rb`
+ 飞行记录 FlyRecord `fly_record.rb`
+ 硬件固件 HardwareFirmware `hardware_firmware.rb`
+ 硬件信息 HardwareInfo `hardware_info.rb`
+ 信息日志 InfoLogger `info_logger.rb`
+ 月飞行数据 MonthFlightStat `month_flight_stat.rb`
+ 问卷提交 QuestionaireSubmission `questionaire_submission`
+ SN 信息 SnInfo `sn_info.rb`
+ 特殊链接 SpecialLink `special_link.rb`
+ 特殊链接（改） SpecialLinkModified `special_link_modified.rb`
+ 用户 User `user.rb`
+ 用户检查 UserCheck `user_check.rb`

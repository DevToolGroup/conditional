## Conditional 规则引擎
[![](https://img.shields.io/badge/官网-DevTool-green)](http://devtoolgroup.github.io)
![](https://img.shields.io/badge/语言-Java-blue)
![](https://img.shields.io/badge/许可证-GPL-red)
![](https://img.shields.io/badge/版本-1.0_SNAPSHOT-orange)
![](https://img.shields.io/badge/代码-6.6K-green)

Conditional 规则引擎，参考 Rete 算法实现，相较于优秀的Drools引擎，禁用了事实修改，新增，删除的场景，增加了输入参数定义，输出参数定义，约束规则引擎的行为，划分程序员与业务员的边界，降低规则复杂度，方便业务人员维护使用。

### 简要说明
#### 许可证
    GPL 3.0

#### Java版本
    Java 8+

#### 功能特点
conditional主要功能特点：
- 规则语法，包含类型定义，输入参数定义，输出参数定义，规则定义三部分
- 规则部分基于词法分析，语法分析生成AST，并根据AST生成RETE规则网络
- 规则语法支持： 比较（> | >= | < | <= | ==）、逻辑（&& | || | !）、函数调用、函数嵌套

常用函数包括：
- `PUT`，Map结构赋值操作。示例：<<PUT(score, "score", 100)>>
- `ADD`，List结构增加元素操作。示例：<<ADD(score.histories, MAP("time", 1, "count", 200))>>
- `LIST`，List结构初始化。示例：<<LIST(1, 2, 3)>>
- `MAP`，Map结构初始化。示例：<<MAP("time", 1, "count", 200)>>
- `MAX`，比较两个数字的大小，返回最大值。示例：<<MAX(1, 2)>>
- `MIN`，比较两个数字的大小，返回最小值。示例：<<MIN(1, 2)>>
- `SUBS`，获取字符串的子串，返回子串。示例：<<SUBS("time", 2, 3)>>
- `ABS`，返回一个数字的绝对值。示例：<<ABS(1)>>
- `AT`，返回字符串指定位置的字符。示例：<<AT("time", 1)>>
- `IN`，判断字符串或者对象是否存在于字符串中，列表中或者Map中。示例：<<IN("time", "me")>>
- `LEN`，返回字符串的长度，列表的长度，Map的大小。示例：<<LEN("time")>>
- `LOWER`，字符串转小写。示例：<<LOWER("time")>>
- `UPPER`，字符串转大写。示例：<<UPPER("time")>>
- `ROUND`，返回一个四舍五入向上取整的整数。示例：<<ROUND(1.5)>>
- `TRIMS`，删除字符串前后的空格。示例：<<TRIMS(" 123 ")>>
- `FILTER`，过滤列表中元素属性值等于目标值。示例：<<FILTER(user.histories, "time", 1)>>
- `SUM`，列表中的数值求和。示例：<<SUM(LIST(1, 2, 3))>>
- `RETRIEVE`，返回列表中元素对应字段的值组成的列表。示例：<<RETRIEVE(user.histories, "count")>>
- `NOW`，返回当前时间。示例：<<NOW()>>

### 快速开始
当前conditional处于SNAPSHOT版本，具体使用方法可以进入[官网](http://devtoolgroup.github.io)，如果感兴趣可以加入交流群，详情见官网导航。

> 建议使用前充分验证

觉得还可以麻烦给个star以示认可，非常感谢🙏🙏

### 沟通交流
[***交流地址***](http://devtoolgroup.github.io)

如果你也是一名热爱代码的朋友，非常非常欢迎你的加入一起讨论学习，作者也是一名热爱代码的小白，期待你的加入。

### 后续迭代
重点任务：
1. 前端页面设计，提高易用性
2. 参数类型定义增加嵌套定义
3. 缺陷修复支持。


### 规则语法示例如下
```plain text
# 类型定义
TYPE History "积分历史"
  Time    publishTime "时间"
  Integer count       "积分数量"
END

# 类型定义
TYPE User "用户"
  Integer id              "用户ID"
  Integer score           "用户积分"
  List<History> histories "积分历史"
END

# 类型定义
TYPE Order "订单"
  User    user "用户"
  Integer amount "订单金额"
END

# 类型定义
TYPE Score "积分"
  Integer score "积分数量"
  List<History> histories "积分记录"
END

# 输入参数定义
ARG Order order, User user

# 输出参数定义
RETURN Score score

# 全局变量定义
CONST List<History> dayHistory "当日积分记录" = FILTER(user.histories, "time", now())
CONST Integer dayScore "当日积分数量" = SUM(RETRIEVE(dayHistory, "count"))

# 规则定义
# 如果消费金额小于100，不加积分
IF
  user.id == order.userId && order.amount < 100
THEN
  PUT(score, "score", 1)
END


# 如果消费金额小于500，并且累计积分小于10000，增加100积分
IF
  user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000
THEN
  PUT(score, "score", 100)
END


# 如果消费金额大于500，并且累计积分小于10000,增加500积分
IF
  user.id == order.userId && order.amount > 500 && dayScore < 10000
THEN
  PUT(score, "score", 500)
END
```



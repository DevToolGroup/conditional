# 类型定义
TYPE History "积分历史"
  Date    publishTime "时间"
  Integer count       "积分数量"
END

TYPE User "用户"
  Integer id              "用户ID"
  Integer level           "等级"
  String  name            "等级名称"
  Integer score           "用户积分"
  List<History> histories "积分历史"
END

TYPE Order "订单"
  User    user "用户"
  Integer amount "订单金额"
END

TYPE Score "积分"
  Date    publishTime "时间"
  Integer score "积分数量"
END

# 输入参数
ARG Order order, User user

# 输出参数
RETURN Score score

# 全局变量
CONST List<History> currentDayScoreHistory "当日积分记录" = Filter(user.histories, sameDay(history.publishTime, now()))
CONST Integer globalCurrentDayScore "当日积分数量" = 0


# 如果消费金额小于100，不加积分
IF
  user.id == order.userId && order.amount < 100
THEN
  SET(score.score, 1)
END


# 如果消费金额小于500，并且累计积分小于10000，增加100积分
IF
  user.id == order.userId && order.amount > 100 && order.amount < 500 && currentDayScore < 10000
THEN
  SET(score.score, 100)
END


# 如果消费金额大于500，增加500积分，并且累计积分小于10000
IF
  user.id == order.userId && order.amount > 500 && currentDayScore < 10000
THEN
  SET(score.score, 500)
END
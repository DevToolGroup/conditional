TYPE History "积分历史"
  Time    publishTime "时间"
  Integer count       "积分数量"
END

TYPE User "用户"
  Integer id              "用户ID"
  Integer score           "用户积分"
  List<History> histories "积分历史"
END

TYPE Order "订单"
  User    user "用户"
  Integer amount "订单金额"
END

TYPE Score "积分"
  Integer score "积分数量"
  List<History> histories "积分记录"
END

# 输入参数
ARG Order order, User user

# 输出参数
RETURN Score score

# 全局变量
CONST List<History> histories "当日积分记录" = Filter(user.histories, now())
CONST Integer dayScore "当日积分数量" = 0


# 如果消费金额小于100，不加积分
IF
  user.id == order.userId && order.amount < 100
THEN
  SET(score.score, 1)
END


# 如果消费金额小于500，并且累计积分小于10000，增加100积分
IF
  user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000
THEN
  SET(score.score, 100)
END


# 如果消费金额大于500，增加500积分，并且累计积分小于10000
IF
  user.id == order.userId && order.amount > 500 && dayScore < 10000
THEN
  SET(score.score, 500)
END
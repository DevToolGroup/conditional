# 参数类型定义
# 积分历史
TYPE History "积分历史"
    Integer time "时间"
    Integer count "积分数量"
END

# 用户信息
TYPE User "用户"
    Integer       id        "用户ID"
    List<History> histories "积分历史"
END

# 订单信息
TYPE Order "订单"
    Integer    userId       "用户"
    Integer    amount     "订单金额"
END

# 积分信息
TYPE Score "积分"
    Integer    score      "积分数量"
    List<History>    histories     "积分记录"
END

# 输入参数定义
ARG Order order, User user

# 输出参数定义
RETURN Score score

# 全局变量定义
CONST List<History> dayHistory "当日积分记录" = FILTER(user.histories, "time", 1)
CONST Integer dayScore "当日积分数量" = SUM(RETRIEVE(dayHistory, "count"))

# 规则定义
IF
 user.id == order.userId && order.amount > 100 && order.amount < 500 && dayScore < 10000
THEN
 PUT(score, "score", 100)
END

IF
 user.id == order.userId && order.amount > 500 && dayScore < 10000
THEN
 PUT(score, "score", 500)
END
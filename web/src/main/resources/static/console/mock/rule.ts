import Mock from 'mockjs';

export default {
  'GET /conditional/api/rule': (req: any, res: any) => {
    const data = Mock.mock({
      'list|1-100': [
        {'id|+1': 1, 'code|1-5': 'a', 'name|1-10': 'a', 'description|1-20': 'a'}
      ]
    })
    res.json({
      success: true,
      data: {
        total: data.list.length,
        list: data.list,
        pageNumber: 0,
        pageSize: 10
      },
      errorCode: 0,
    });
  },
  'GET /conditional/api/rule/:ruleId': (req: any, res: any) => {
    res.json({
      success: true,
      data: Mock.mock({'id|+1': 1, 'code|1-5': 'a', 'name|1-10': 'a', 'description|1-20': 'a'}),
      errorCode: 0,
    });
  },
  'PUT /conditional/api/rule/:ruleId': (req: any, res: any) => {
    res.json({
      success: true,
      errorCode: 0,
    });
  },
  'POST /conditional/api/rule': (req: any, res: any) => {
    res.json({
      success: true,
      errorCode: 0,
    });
  },
  'DELETE /conditional/api/rule/:ruleId': (req: any, res: any) => {
    res.json({
      success: true,
      errorCode: 0,
    });
  },
};
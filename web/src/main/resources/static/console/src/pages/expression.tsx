import React, { useEffect, useRef, useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import type { CascaderProps, InputRef } from 'antd';
import { Cascader, Flex, Input, Tag, theme } from 'antd';

const tagInputStyle: React.CSSProperties = {
  width: 64,
  height: 22,
  marginInlineEnd: 8,
  verticalAlign: 'top',
};


interface Option {
  value: string | number;
  label: string;
  children?: Option[];
}

const options: Option[] = [
  {
    value: 'number',
    label: '数字常量',
    children: [

    ],
  },
  {
    value: 'character',
    label: '字符常量',
    children: [

    ],
  },
  {
    value: 'variable',
    label: '变量',
    children: [
      {
        value: 'user',
        label: '用户',
        children: [
          {
            value: 'code',
            label: '用户编码',
          },
        ],
      },
    ],
  },
  {
    value: 'logic',
    label: '逻辑运算',
    children: [
      {
        value: '&&',
        label: '且',
      },
      {
        value: '||',
        label: '或',
      },
      {
        value: '!',
        label: '非',
      },
    ],
  },
  {
    value: 'arith',
    label: '算术运算',
    children: [
      {
        value: '+',
        label: '加',
      },
      {
        value: '-',
        label: '减',
      },
      {
        value: '*',
        label: '乘',
      },
      {
        value: '/',
        label: '除',
      },
      {
        value: '%',
        label: '取余',
      },
    ],
  },
  {
    value: 'compare',
    label: '比较运算',
    children: [
      {
        value: '>',
        label: '大于',
      },
      {
        value: '>=',
        label: '大于等于',
      },
      {
        value: '<',
        label: '小于',
      },
      {
        value: '<=',
        label: '小于等于',
      },
      {
        value: '==',
        label: '等于',
      },
      {
        value: '!=',
        label: '不等于',
      },
    ],
  },
];

interface TagOption {
  id: number;
  type: string | number;
  value: string;
}

const Expression: React.FC = () => {
  const { token } = theme.useToken();
  const [tags, setTags] = useState<TagOption[]>([]);
  const [inputVisible, setInputVisible] = useState(false);
  const [inputValue, setInputValue] = useState<TagOption>({ id: -1, value: '', type: '' });
  const [editInputIndex, setEditInputIndex] = useState(-1);
  const [editInputValue, setEditInputValue] = useState<TagOption>({ id: -1, value: '', type: '' });
  const inputRef = useRef<InputRef>(null);
  const editInputRef = useRef<InputRef>(null);

  const tagPlusStyle: React.CSSProperties = {
    height: 22,
    background: token.colorBgContainer,
    borderStyle: 'dashed',
  };

  useEffect(() => {
    if (inputVisible) {
      inputRef.current?.focus();
    }
  }, [inputVisible]);

  useEffect(() => {
    editInputRef.current?.focus();
  }, [editInputValue]);

  const handleClose = (removedTag: number) => {
    const newTags = tags.filter((tag) => tag.id !== removedTag);
    setTags(newTags);
  };

  const showInput = () => {
    setInputVisible(true);
  };
  const handleEditInputChange: CascaderProps<Option>['onChange'] = (value) => {
    const newTags = [...tags];
    for (const tag of newTags) {
      if (tag.id === editInputIndex) {
        const type = value[0];
        tag.value = type === 'number' || type === 'character' ? '双击输入值': value.join('.');
        tag.type = value[0];
      }
    }
    setTags(newTags);
    setEditInputIndex(-2);
    setEditInputValue({ id: -1, value: '', type: '' });
  };

  const handleInputChange: CascaderProps<Option>['onChange'] = (value) => {
    if (value.length > 0) {
      const type = value[0];
      if (type === 'number' || type === 'character') {
        setInputValue({ type: type, value: '双击输入值', id: tags.length })
      } else {
        setInputValue({ type: type, value: value.join('.'), id: tags.length })
      }
    }
  };

  const handleInputConfirm = () => {
    if (inputValue?.id !== -1) {
      setTags([...tags, inputValue]);
      setInputValue({ id: -1, value: '', type: '' });
      setInputVisible(false);
    }
    setEditInputIndex(-2);
  };

  const handleValueInputConfirm = (value: TagOption) => {
    const newTags = [...tags];
    for (const tag of newTags) {
      if (tag.id === value.id) {
        tag.value = value.value;
      }
    }
    setTags(newTags);
    setEditInputIndex(-2);
    setEditInputValue({ id: -1, value: '', type: '' });
  }

  const translate = (value: string): string => {
    const items = value.split('.');
    var index = 0;
    var result = '';
    var nextOptions = options;
    while (nextOptions.length > 0) {
      var flag = false;
      for (const option of nextOptions) {
        if (option.value === items[index]) {
          result += option.label
          index += 1;
          flag = true;
          nextOptions = option.children ?? [];
          if (nextOptions.length > 0) {
            result += '.';
          }
          break;
        }
      }
      if (!flag) {
        nextOptions = []
      }
    }

    return result;
  }
  return (
    <>
      <Flex gap="2px 0" wrap>
        {tags.map<React.ReactNode>((tag) => {
          if (editInputIndex === tag.id) {
            if (editInputIndex === tag.id) {
              if (tag.type === 'number' || tag.type === 'character') {
                return (
                  <Input
                    ref={editInputRef}
                    key={tag.id}
                    size="small"
                    style={tagInputStyle}
                    onBlur={(e) => handleValueInputConfirm({ id: tag.id, value: e.target.value, type: 'value' })}
                  />
                );
              } else {
                return (
                  <Cascader
                    ref={editInputRef}
                    key={tag.id}
                    size="small"
                    options={options}
                    style={tagInputStyle}
                    onChange={handleEditInputChange}
                  />
                );
              }
            }
          }
          if ((tag.type === 'number' || tag.type === 'character') && tag.value === '双击输入值') {
            const tagElem = (
              <Tag
                bordered={false}
                key={tag.id}
                style={{ userSelect: 'none' }}
                color="error"
                closable={true}
                onClose={() => handleClose(tag.id)}
              >
                <span onDoubleClick={(e) => {
                  setEditInputIndex(tag.id);
                  setEditInputValue(tag);
                  e.preventDefault();
                }}>
                  {tag.value}
                </span>
              </Tag>
            );
            return tagElem
          }
          let expression = tag.value;
          if (tag.type === 'character') {
            expression = '"' + tag.value + '"'
          } else if (tag.type === 'number') {
            expression = tag.value
          } else {
            expression = translate(tag.value);
          }
          const tagElem = (
            <Tag
              key={tag.id}
              bordered={false}
              style={{ userSelect: 'none' }}
              closable={true}
              onClose={() => handleClose(tag.id)}
            >
              <span onDoubleClick={(e) => {
                setEditInputIndex(tag.id);
                setEditInputValue(tag);
                e.preventDefault();
              }}>
                {expression}
              </span>
            </Tag>
          );
          return tagElem
        })}
        {inputVisible ? (
          <Cascader
            ref={editInputRef}
            size="small"
            options={options}
            style={tagInputStyle}
            onChange={handleInputChange}
            onBlur={handleInputConfirm}
          />
        ) : (
          <Tag style={tagPlusStyle} icon={<PlusOutlined />} onClick={showInput}>
            表达式
          </Tag>
        )}
      </Flex>
      <div style={{ margin: '10px 0', fontSize: 14, fontWeight: 800, backgroundColor: '#f5f5f5b3', padding: '12px' }}>
        表达式：{tags.map((tag) => { return tag.type === 'number' ? tag.value : tag.type === 'character' ? '"' + tag.value + '"' : tag.value.split('.').slice(1).join('.') }).join(' ')}
      </div>
    </>
  );
};

export default Expression;
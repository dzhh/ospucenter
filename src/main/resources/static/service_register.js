webpackJsonp([13],{

/***/ 1155:
/***/ function(module, exports, __webpack_require__) {

	/* REACT HOT LOADER */ if (false) { (function () { var ReactHotAPI = require("D:\\project\\WebstormProjects\\APIStore\\node_modules\\react-hot-api\\modules\\index.js"), RootInstanceProvider = require("D:\\project\\WebstormProjects\\APIStore\\node_modules\\react-hot-loader\\RootInstanceProvider.js"), ReactMount = require("react/lib/ReactMount"), React = require("react"); module.makeHot = module.hot.data ? module.hot.data.makeHot : ReactHotAPI(function () { return RootInstanceProvider.getRootInstances(ReactMount); }, React); })(); } try { (function () {
	
	'use strict';
	
	Object.defineProperty(exports, "__esModule", {
	    value: true
	});
	exports.default = undefined;
	
	var _card = __webpack_require__(97);
	
	var _card2 = _interopRequireDefault(_card);
	
	var _button = __webpack_require__(92);
	
	var _button2 = _interopRequireDefault(_button);
	
	var _extends2 = __webpack_require__(7);
	
	var _extends3 = _interopRequireDefault(_extends2);
	
	var _input = __webpack_require__(120);
	
	var _input2 = _interopRequireDefault(_input);
	
	var _getPrototypeOf = __webpack_require__(37);
	
	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);
	
	var _classCallCheck2 = __webpack_require__(2);
	
	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);
	
	var _createClass2 = __webpack_require__(6);
	
	var _createClass3 = _interopRequireDefault(_createClass2);
	
	var _possibleConstructorReturn2 = __webpack_require__(4);
	
	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);
	
	var _inherits2 = __webpack_require__(3);
	
	var _inherits3 = _interopRequireDefault(_inherits2);
	
	var _form = __webpack_require__(108);
	
	var _form2 = _interopRequireDefault(_form);
	
	var _dec, _dec2, _class; /**
	                          * Created by kwx on 2017/9/22.
	                          */
	
	var _react = __webpack_require__(1);
	
	var _react2 = _interopRequireDefault(_react);
	
	var _redux = __webpack_require__(47);
	
	var _reactRedux = __webpack_require__(42);
	
	var _reactRouter = __webpack_require__(43);
	
	__webpack_require__(103);
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
	
	var FormItem = _form2.default.Item;
	
	//连接 Redux 的组件 不可复用
	var service_register = (_dec = (0, _reactRedux.connect)(function (state, props) {
	    return {
	        config: state.config,
	        loginResponse: state.loginResponse
	    };
	}), _dec2 = _form2.default.create({
	    onFieldsChange: function onFieldsChange(props, items) {
	        // console.log(items)
	        // props.cacheSearch(items);
	    }
	}), _dec(_class = _dec2(_class = function (_Component) {
	    (0, _inherits3.default)(service_register, _Component);
	
	    // 初始化页面常量 绑定事件方法
	    function service_register(props, context) {
	        (0, _classCallCheck3.default)(this, service_register);
	
	        var _this = (0, _possibleConstructorReturn3.default)(this, (service_register.__proto__ || (0, _getPrototypeOf2.default)(service_register)).call(this, props));
	
	        _this.handleReset = function () {
	            _this.props.form.resetFields();
	        };
	
	        _this.state = {
	            confirmDirty: false
	        };
	        _this.handleSubmit = _this.handleSubmit.bind(_this);
	
	        return _this;
	    }
	
	    (0, _createClass3.default)(service_register, [{
	        key: 'handleSubmit',
	        value: function handleSubmit(e) {
	
	            e.preventDefault();
	            this.props.form.validateFieldsAndScroll(function (err, values) {
	
	                if (!err) {
	                    console.log('Received values of form: ', values);
	                    // const user = {};
	                    // user.userName = values.userName
	                    // user.userEmail = values.userEmail
	                    // user.userPwd = values.password
	                    // fetchRegister(user, (res) => {
	                    //     if (res.ospState == 200) {
	                    //         message.success("注册成功", 2, ()=>{ hashHistory.push('/login')})
	                    //     } else  {
	                    //         message.error(res.data.msg)
	                    //         this.setState({
	                    //             loading: false
	                    //         })
	                    //     }
	                    // })
	                }
	            });
	        }
	    }, {
	        key: 'render',
	        value: function render() {
	            var getFieldDecorator = this.props.form.getFieldDecorator;
	
	            var formItemLayout = {
	                labelCol: {
	                    xs: { span: 24 },
	                    sm: { span: 6 }
	                },
	                wrapperCol: {
	                    xs: { span: 24 },
	                    sm: { span: 14 }
	                }
	            };
	            var tailFormItemLayout = {
	                wrapperCol: {
	                    xs: {
	                        span: 24,
	                        offset: 0
	                    },
	                    sm: {
	                        span: 14,
	                        offset: 6
	                    }
	                }
	            };
	
	            return _react2.default.createElement(
	                'div',
	                { className: 'pageStyle' },
	                _react2.default.createElement(
	                    _card2.default,
	                    { title: '\u670D\u52A1\u6CE8\u518C', bordered: false, className: 'cardStyle' },
	                    _react2.default.createElement(
	                        _form2.default,
	                        { onSubmit: this.handleSubmit, style: { marginLeft: '18%', width: '60%' } },
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u63A5\u53E3\u5730\u5740',
	                                hasFeedback: true
	                            }),
	                            getFieldDecorator('interfaceAddress', {
	                                rules: [{
	                                    required: true, message: '请输入接口地址!'
	                                }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u8BF7\u6C42\u65B9\u6CD5',
	                                hasFeedback: true
	                            }),
	                            getFieldDecorator('requestMethod', {
	                                rules: [{
	                                    required: true, message: '请输入请求方法!'
	                                }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(FormItem, (0, _extends3.default)({}, formItemLayout, {
	                            label: '\u8BF7\u6C42\u53C2\u6570\uFF08header\uFF09:'
	                        })),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u53C2\u6570\u540D'
	                            }),
	                            getFieldDecorator('header_paramName', {
	                                rules: [{ required: true, message: '请输入你的参数名!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u7C7B\u578B'
	                            }),
	                            getFieldDecorator('header_type', {
	                                rules: [{ required: true, message: '请输入类型!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u53C2\u6570\u4F4D\u7F6E'
	                            }),
	                            getFieldDecorator('header_paramAddress', {
	                                rules: [{ required: true, message: '请输入参数位置!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u63CF\u8FF0'
	                            }),
	                            getFieldDecorator('header_describe', {
	                                rules: [{ required: true, message: '请输入描述!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(FormItem, (0, _extends3.default)({}, formItemLayout, {
	                            label: '\u8BF7\u6C42\u53C2\u6570(urlParam)'
	
	                        })),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u53C2\u6570\u540D'
	                            }),
	                            getFieldDecorator('urlParam_paramName', {
	                                rules: [{ required: true, message: '请输入你的参数名!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u7C7B\u578B'
	                            }),
	                            getFieldDecorator('urlParam_type', {
	                                rules: [{ required: true, message: '请输入类型!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u53C2\u6570\u4F4D\u7F6E'
	                            }),
	                            getFieldDecorator('urlParam_paramAddress', {
	                                rules: [{ required: true, message: '请输入参数位置!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u63CF\u8FF0'
	                            }),
	                            getFieldDecorator('urlParam_describe', {
	                                rules: [{ required: true, message: '请输入描述!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(FormItem, (0, _extends3.default)({}, formItemLayout, {
	                            label: '\u9519\u8BEF\u7801\u53C2\u7167'
	                        })),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u9519\u8BEF\u7801'
	                            }),
	                            getFieldDecorator('errorCode', {
	                                rules: [{ required: true, message: '请输入错误码!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u9519\u8BEF\u7801\u8FD4\u56DE'
	                            }),
	                            getFieldDecorator('errorCodeReturn', {
	                                rules: [{ required: true, message: '请输入错误码返回!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u9519\u8BEF\u7801\u8FD4\u56DE\u8BF4\u660E'
	                            }),
	                            getFieldDecorator('errorCodeReturnIns', {
	                                rules: [{ required: true, message: '请输入错误码返回说明!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            (0, _extends3.default)({}, formItemLayout, {
	                                label: '\u63CF\u8FF0'
	                            }),
	                            getFieldDecorator('error_describe', {
	                                rules: [{ required: true, message: '请输入描述!' }]
	                            })(_react2.default.createElement(_input2.default, null))
	                        ),
	                        _react2.default.createElement(
	                            FormItem,
	                            tailFormItemLayout,
	                            _react2.default.createElement(
	                                _button2.default,
	                                { style: { marginRight: 8 }, onClick: this.handleReset },
	                                '\u91CD\u7F6E'
	                            ),
	                            _react2.default.createElement(
	                                _button2.default,
	                                { type: 'primary', htmlType: 'submit', size: 'large' },
	                                '\u6CE8\u518C'
	                            )
	                        )
	                    )
	                )
	            );
	        }
	    }]);
	    return service_register;
	}(_react.Component)) || _class) || _class);
	exports.default = service_register;
	
	/* REACT HOT LOADER */ }).call(this); } finally { if (false) { (function () { var foundReactClasses = module.hot.data && module.hot.data.foundReactClasses || false; if (module.exports && module.makeHot) { var makeExportsHot = require("D:\\project\\WebstormProjects\\APIStore\\node_modules\\react-hot-loader\\makeExportsHot.js"); if (makeExportsHot(module, require("react"))) { foundReactClasses = true; } var shouldAcceptModule = true && foundReactClasses; if (shouldAcceptModule) { module.hot.accept(function (err) { if (err) { console.error("Cannot apply hot update to " + "service_register.js" + ": " + err.message); } }); } } module.hot.dispose(function (data) { data.makeHot = module.makeHot; data.foundReactClasses = foundReactClasses; }); })(); } }

/***/ }

});
//# sourceMappingURL=service_register.js.map
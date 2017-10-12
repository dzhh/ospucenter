webpackJsonp([12],{

/***/ 891:
/***/ function(module, exports, __webpack_require__) {

	exports = module.exports = __webpack_require__(299)();
	// imports
	
	
	// module
	exports.push([module.id, ".login {\n  display: flex;\n  justify-content: center;\n  align-items: center;\n  height: 100%;\n  background: #f3f3f3;\n}\n.login .login-form {\n  width: 320px;\n  height: 340px;\n  padding: 36px;\n  box-shadow: 0 0 100px rgba(0, 0, 0, 0.08);\n  background: #fff;\n}\n.login .login-form .login-logo {\n  text-align: center;\n  height: 40px;\n  line-height: 40px;\n  cursor: pointer;\n  margin-bottom: 24px;\n}\n.login .login-form .login-logo span {\n  vertical-align: text-bottom;\n  font-size: 16px;\n  text-transform: uppercase;\n  display: inline-block;\n}\n", ""]);
	
	// exports


/***/ },

/***/ 1140:
/***/ function(module, exports, __webpack_require__) {

	/* REACT HOT LOADER */ if (false) { (function () { var ReactHotAPI = require("D:\\project\\WebstormProjects\\APIStore\\node_modules\\react-hot-api\\modules\\index.js"), RootInstanceProvider = require("D:\\project\\WebstormProjects\\APIStore\\node_modules\\react-hot-loader\\RootInstanceProvider.js"), ReactMount = require("react/lib/ReactMount"), React = require("react"); module.makeHot = module.hot.data ? module.hot.data.makeHot : ReactHotAPI(function () { return RootInstanceProvider.getRootInstances(ReactMount); }, React); })(); } try { (function () {
	
	'use strict';
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	exports.default = undefined;
	
	var _button = __webpack_require__(92);
	
	var _button2 = _interopRequireDefault(_button);
	
	var _checkbox = __webpack_require__(24);
	
	var _checkbox2 = _interopRequireDefault(_checkbox);
	
	var _input = __webpack_require__(120);
	
	var _input2 = _interopRequireDefault(_input);
	
	var _icon = __webpack_require__(19);
	
	var _icon2 = _interopRequireDefault(_icon);
	
	var _message = __webpack_require__(98);
	
	var _message2 = _interopRequireDefault(_message);
	
	var _stringify = __webpack_require__(256);
	
	var _stringify2 = _interopRequireDefault(_stringify);
	
	var _keys = __webpack_require__(257);
	
	var _keys2 = _interopRequireDefault(_keys);
	
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
	
	var _dec, _dec2, _class;
	// import { fetchLogin, receiveLogin } from '../../actions/login'
	
	
	var _react = __webpack_require__(1);
	
	var _react2 = _interopRequireDefault(_react);
	
	var _redux = __webpack_require__(47);
	
	var _reactRedux = __webpack_require__(42);
	
	var _reactRouter = __webpack_require__(43);
	
	var _login = __webpack_require__(542);
	
	var _leftBg = __webpack_require__(354);
	
	var _leftBg2 = _interopRequireDefault(_leftBg);
	
	var _reactRouterRedux = __webpack_require__(91);
	
	__webpack_require__(1266);
	
	var _login2 = __webpack_require__(326);
	
	var _tabList = __webpack_require__(248);
	
	var _utils = __webpack_require__(119);
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
	
	var FormItem = _form2.default.Item;
	var webStyle = {
	  // backgroundImage: `url(${imgUrlWeb})`
	  width: "100%",
	  height: "100%",
	  backgroundImage: "url('" + _leftBg2.default + "')"
	};
	//连接 Redux 的组件 不可复用
	var Login = (_dec = (0, _reactRedux.connect)(function (state, props) {
	  return {
	    config: state.config,
	    loginResponse: state.loginResponse,
	    logout: state.logout
	  };
	}, function (dispatch) {
	  return { actions: (0, _redux.bindActionCreators)(_reactRouterRedux.routerActions, dispatch),
	    dispatch: dispatch };
	}), _dec2 = _form2.default.create({
	  onFieldsChange: function onFieldsChange(props, items) {
	    // console.log(items)
	    // props.cacheSearch(items);
	  }
	}), _dec(_class = _dec2(_class = function (_Component) {
	  (0, _inherits3.default)(Login, _Component);
	
	  // 初始化页面常量 绑定事件方法
	  function Login(props, context) {
	    (0, _classCallCheck3.default)(this, Login);
	
	    var _this = (0, _possibleConstructorReturn3.default)(this, (Login.__proto__ || (0, _getPrototypeOf2.default)(Login)).call(this, props));
	
	    _this.state = {
	      //为true时  登陆页面一直加载
	      loading: false
	    };
	    _this.handleSubmit = _this.handleSubmit.bind(_this);
	    _this.handleChange = _this.handleChange.bind(_this);
	    _this.checkPass = _this.checkPass.bind(_this);
	    _this.checkName = _this.checkName.bind(_this);
	    _this.noop = _this.noop.bind(_this);
	    return _this;
	  }
	
	  (0, _createClass3.default)(Login, [{
	    key: 'componentWillMount',
	    value: function componentWillMount() {
	      var dispatch = this.props.dispatch;
	
	      dispatch((0, _tabList.deleteAllTab)());
	      //  dispatch(changeLogout())
	    }
	  }, {
	    key: 'handleSubmit',
	    value: function handleSubmit(e) {
	      var _this2 = this;
	
	      e.preventDefault();
	      this.props.form.validateFields(function (err, values) {
	        if (!err) {
	          _this2.state.loading = true;
	          _this2.setState({ loading: true });
	          var re = {};
	          (0, _keys2.default)(values).map(function (key) {
	            return values[key] = values[key] && typeof values[key] == 'string' && values[key].trim();
	          });
	          //请求后台数据  派发事件 放到回调里面
	          (0, _login.fetchLogin)(values, function (res) {
	            console.log("res1" + res.data);
	            console.log("res2" + res.token);
	            // this.props.dispatch(fetchLogin(values, (res) => {
	            if (res.ospState == 200) {
	              //const query = this.props.form.getFieldsValue()
	              sessionStorage.setItem('userName', res.data.ucUser.userName);
	              sessionStorage.setItem('userId', res.data.ucUser.userId);
	              sessionStorage.setItem('menus', (0, _stringify2.default)(res.data.menuTrees));
	              //this.dealMenu(res.data.menuTrees)
	
	              sessionStorage.setItem('token', res.token);
	              sessionStorage.setItem('isLeftNavMini', false);
	              //this.props.config.WEBDATA.login=true
	              // this.props.dispatch(changeLogout());
	              // this.props.dispatch(receiveLogin({ req: data, res: resp }))
	              _reactRouter.hashHistory.push('/');
	            } else if (res.ospState == 401) {
	
	              _message2.default.warning("账号或密码错误");
	              _this2.setState({
	                loading: false
	              });
	            } else {
	              _message2.default.warning(res.data.msg);
	              _this2.setState({
	                loading: false
	              });
	            }
	          });
	        }
	      });
	    }
	  }, {
	    key: 'handleChange',
	    value: function handleChange(e) {
	      var newState = {};
	      newState[e.target.name] = e.target.value;
	      this.setState(newState);
	    }
	
	    // 组件已经加载到dom中
	
	  }, {
	    key: 'componentDidMount',
	    value: function componentDidMount() {
	      // this.props.dispatch(fetchLogin({ currentPage: 1 }))
	    }
	  }, {
	    key: 'checkName',
	    value: function checkName(rule, value, callback) {
	      var validateFields = this.props.form.validateFields;
	
	      if (value) {
	        // validateFields([''])
	      }
	      callback();
	    }
	  }, {
	    key: 'checkPass',
	    value: function checkPass(rule, value, callback) {
	      var validateFields = this.props.form.validateFields;
	
	      if (value) {
	        // validateFields([''])
	      }
	      callback();
	    }
	  }, {
	    key: 'noop',
	    value: function noop() {
	      return false;
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var loginResponse = this.props.loginResponse.loginResponse;
	      var getFieldDecorator = this.props.form.getFieldDecorator;
	
	      return _react2.default.createElement(
	        'div',
	        { className: 'login', style: webStyle },
	        _react2.default.createElement(
	          'div',
	          { className: 'login-form' },
	          _react2.default.createElement(
	            'div',
	            { className: 'login-logo' },
	            _react2.default.createElement(
	              'span',
	              null,
	              '\u6B22\u8FCE\u767B\u9646'
	            )
	          ),
	          _react2.default.createElement(
	            _form2.default,
	            { onSubmit: this.handleSubmit, style: { maxWidth: '300px' } },
	            _react2.default.createElement(
	              FormItem,
	              { hasFeedback: true },
	              getFieldDecorator('userName', {
	                rules: [{ required: true, message: '请输入用户名' }, { validator: this.checkName },
	                // { pattern: regExpConfig.IDcardTrim, message: '身份证号格式不正确' }
	                { pattern: "[A-Za-z0-9]{5,8}", message: '用户名只能为5-8位字符或数字' }]
	                //validateTrigger: 'onBlur',
	              })(_react2.default.createElement(_input2.default, {
	                prefix: _react2.default.createElement(_icon2.default, { type: 'user', style: { fontSize: 13 } }),
	                placeholder: '\u8BF7\u8F93\u5165\u7528\u6237\u540D',
	                type: 'text'
	              }))
	            ),
	            _react2.default.createElement(
	              FormItem,
	              { hasFeedback: true },
	              getFieldDecorator('userPwd', {
	                rules: [{ required: true, message: '请输入密码' },
	                //{ pattern: regExpConfig.pwd, message: '密码只能是6-16个数字或者字母组成' }
	                { pattern: "[A-Za-z0-9]{5,10}", message: '密码只能为5-10位字符' }]
	                // validateTrigger: 'onBlur',
	              })(_react2.default.createElement(_input2.default, {
	                prefix: _react2.default.createElement(_icon2.default, { type: 'lock', style: { fontSize: 13 } }),
	                placeholder: '\u8BF7\u8F93\u5165\u5BC6\u7801',
	                type: 'password'
	              }))
	            ),
	            _react2.default.createElement(
	              FormItem,
	              null,
	              getFieldDecorator('remember', {
	                valuePropName: 'checked',
	                initialValue: true
	              })(_react2.default.createElement(
	                _checkbox2.default,
	                null,
	                '\u8BB0\u4F4F\u6211'
	              )),
	              _react2.default.createElement(
	                'a',
	                { className: 'login-form-forgot', href: '', style: { float: 'right' } },
	                '\u5FD8\u8BB0\u5BC6\u7801'
	              ),
	              _react2.default.createElement(
	                _button2.default,
	                { type: 'primary', htmlType: 'submit', className: 'login-form-button', style: { width: '100%' } },
	                '\u767B\u5F55'
	              ),
	              '\u6216 ',
	              _react2.default.createElement(
	                'a',
	                { href: '/#/register' },
	                '\u73B0\u5728\u5C31\u53BB\u6CE8\u518C!'
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return Login;
	}(_react.Component)) || _class) || _class);
	
	// handleSubmit (e) {
	//     e.preventDefault();
	//
	//     this.props.form.validateFields((err, values) => {
	//         if (!err) {
	//             post('http://localhost:3000/login', values)
	//                 .then((res) => {
	//                     if (res) {
	//                         message.info('登录成功');
	//                         this.context.router.push('/');
	//                     } else {
	//                         message.info('登录失败，账号或密码错误');
	//                     }
	//                 });
	//         }
	//     });
	// }
	
	exports.default = Login;
	
	/* REACT HOT LOADER */ }).call(this); } finally { if (false) { (function () { var foundReactClasses = module.hot.data && module.hot.data.foundReactClasses || false; if (module.exports && module.makeHot) { var makeExportsHot = require("D:\\project\\WebstormProjects\\APIStore\\node_modules\\react-hot-loader\\makeExportsHot.js"); if (makeExportsHot(module, require("react"))) { foundReactClasses = true; } var shouldAcceptModule = true && foundReactClasses; if (shouldAcceptModule) { module.hot.accept(function (err) { if (err) { console.error("Cannot apply hot update to " + "login.js" + ": " + err.message); } }); } } module.hot.dispose(function (data) { data.makeHot = module.makeHot; data.foundReactClasses = foundReactClasses; }); })(); } }

/***/ },

/***/ 1266:
/***/ function(module, exports, __webpack_require__) {

	// style-loader: Adds some css to the DOM by adding a <style> tag
	
	// load the styles
	var content = __webpack_require__(891);
	if(typeof content === 'string') content = [[module.id, content, '']];
	// add the styles to the DOM
	var update = __webpack_require__(353)(content, {});
	if(content.locals) module.exports = content.locals;
	// Hot Module Replacement
	if(false) {
		// When the styles change, update the <style> tags
		if(!content.locals) {
			module.hot.accept("!!../../node_modules/css-loader/index.js!../../node_modules/postcss-loader/index.js!../../node_modules/less-loader/index.js!./login.less", function() {
				var newContent = require("!!../../node_modules/css-loader/index.js!../../node_modules/postcss-loader/index.js!../../node_modules/less-loader/index.js!./login.less");
				if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
				update(newContent);
			});
		}
		// When the module is disposed, remove the <style> tags
		module.hot.dispose(function() { update(); });
	}

/***/ }

});
//# sourceMappingURL=login.js.map
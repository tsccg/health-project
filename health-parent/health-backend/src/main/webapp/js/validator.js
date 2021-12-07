//手机号校验方法
var validatorPhone = function(rule, value, callback) {
    if (!value) {
        callback(new Error('手机号不能为空!'))
    } else if (!/^[1][3,4,5,7,8][0-9]{9}$/.test(value)) {
        callback(new Error('手机号格式错误!'))
    } else {
        callback()
    }
}
//身份证号校验方法
var validatorIdCard = function(rule, value, callback) {
    if (!value) {
        callback(new Error('身份证号码不能为空!'))
    } else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value)) {
        callback(new Error('身份证号码格式错误!'))
    } else {
        callback()
    }
}
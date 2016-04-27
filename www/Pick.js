
var exec = require('cordova/exec');

/*
exports.echo = function(arg0, success, error) {
    exec(success, error, "Pick", "echo", [arg0]);
};
*/
function Pick() {}
Pick.prototype.echo =function(arg0, success, error) {
    exec(success, error, "Pick", "echo", [arg0]);
};
module.exports = new Pick();
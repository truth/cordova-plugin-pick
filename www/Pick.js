/*
var exec = require('cordova/exec');
exports.echo = function(arg0, success, error) {
    exec(success, error, "Pick", "echo", [arg0]);
};
module.exports = new Pick();
*/
function Pick() {}
Pick.prototype.echo =function(arg0, success, error) {
    cordova.exec(success, error, "Pick", "echo", [arg0]);
};

Pick.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.pick = new Pick();
  return window.plugins.pick;
};

cordova.addConstructor(Pick.install);
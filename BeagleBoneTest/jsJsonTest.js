var text = '{"command1":"data1","command2":"data2"}';
var obj = JSON.parse(text);
if ("undefined" === typeof obj.command2) {
    console.log('the property is not available...');
}
else {
    console.log(obj.command2);
}

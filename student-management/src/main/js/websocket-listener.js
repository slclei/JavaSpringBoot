'use strict';

const SockJS=require('sockjs-client');
require('stompjs');

function register(registrations) {
    const socket =SockJS('/payroll');
    const stompClient=Stomp.over(socket);
    stompClient.connect({},function(frame){
        registrations.forEach(function (registation){
            stompClient.subscribe(registation.route,registation.callback);
        });
    });
}

module.exports.register=register;
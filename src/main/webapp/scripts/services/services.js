EYApp.service('chatService', ['$http','Constants', '$q', function ($http,Constants, $q) {
    var constants = Constants.getConstants();
    var chatURL = constants.apiUrl;
    return {
        
        postChat: function(sessionId, message){
            var req = {
                url: chatURL + '/ai?sessionId=' + sessionId + '&message=' + message,
                method: 'POST'
            };
            return $http(req);
        }
    }
}]);

EYApp.service('topicService', ['$http','Constants', '$q', function ($http,Constants, $q) {
    var constants = Constants.getConstants();
    var topicUrl = constants.avapiUrl;
    return {
        
        createTopic: function(topicData){
            var req = {
                url: topicUrl + '/addTopic',
                method: 'POST',
				data : topicData
            };
            return $http(req);
        }
    }
}]);
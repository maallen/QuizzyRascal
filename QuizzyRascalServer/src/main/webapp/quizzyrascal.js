var Quiz = Quiz || {};

Quiz.Notification = Backbone.Model.extend({});

Quiz.NotificationList = Backbone.Collection.extend({
	model: Quiz.Notification
});

Quiz.NotificationView = Backbone.View.extend({
	tagName: 'li',

	initialize: function(){
		_.bindAll(this, 'render');
		this.template = _.template($('#notification-template').html());
	},

	render: function(){
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}
});

Quiz.NotificationListView = Backbone.View.extend({
	el: $('#notificationList'),

	initialize: function(){
		_.bindAll(this, 'render');
		this.model.on('add', this.render);
		this.model.on('remove', this.render);
		this.template = _.template($('#notificationList-template').html());
	},

	render: function(){
		$('#notificationList').empty();
		Quiz.notificationList.each(function(notification){
			var notificationView = new Quiz.NotificationView({model: notification});
			$('#notificationList').append(notificationView.render().el);
		});
	}

});

Quiz.AppView = Backbone.View.extend({
	el: $('#container'),

	events:{
		'click #testButton' 	: 	'createNotification'
	},

	createNotification: function(){
		var notification = new Quiz.Notification({ id: Quiz.incrementIdCounter(), deviceId: 200});
		var notification2 = new Quiz.Notification({ id: Quiz.incrementIdCounter(), deviceId: 300});
		Quiz.notificationList.add(notification);
		Quiz.notificationList.add(notification2);
	}
});

Quiz.incrementIdCounter = function(){
	var id = Quiz.idCounter;
	Quiz.idCounter++;
	return id;
}

Quiz.Router = Backbone.Router.extend({

	routes:{
		'':'home'
	},

	home: function(){
		var app = new Quiz.AppView();
		Quiz.notificationList = new Quiz.NotificationList();
		Quiz.notificationListView = new Quiz.NotificationListView({model: Quiz.notificationList});
		Quiz.idCounter = 1;
	}

});

$(function(){
	
	Quiz.router = new Quiz.Router();
	Backbone.history.start({});
});
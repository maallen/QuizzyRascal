var Quiz = Quiz || {};

Quiz.Notification = Backbone.Model.extend({});

Quiz.NotificationList = Backbone.Collection.extend({
	model: Quiz.Notification
});

Quiz.NotificationView = Backbone.View.extend({
	tagName: 'li',

	initialize: function(){
		_.bindAll(this, 'render');
		this.template = _.template($('#notification-template')).html();
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
		this.model.each(function(notification){
			var notificationView = new Quiz.NotificationView({model: notification});
			$('#notificationList').append(notificationView.render().el);
		});
	}

});
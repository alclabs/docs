var docs = docs || {};

$(function($) {
    //'use strict';

    docs.DocsView = Backbone.View.extend({
        el: '#page',

        statsTemplate: _.template( $('#stats-template').html() ),

        // button handlers
        events : {
            'click #reloadbutton': 'reload'
        },

        initialize: function() {
            this.$statsContainer = this.$('#stats-container');
            this.$missingContainer = this.$('#missing-container');

            $("#tabs" ).tabs();
            $('.help').button({
                icons:{primary:'ui-icon-help'},
                text:false
            });

            docs.Documents.on('all', this.render, this);
            //docs.Documents.on('add', this.addMissing, this);
            docs.Documents.on('reset', this.addAll, this);
            docs.Documents.fetch();
        },

        render: function() {
            var missing = docs.Documents.missing();
            this.$statsContainer.html(this.statsTemplate({
                total: docs.Documents.length,
                missing: missing.length,
                none: docs.Documents.none().length
            }));
        },

        renderMissing: function(doc) {
            var view = new docs.MissingRowView({ model: doc });
            this.$missingContainer.append( view.render().el );
        },

        addAll: function() {
			this.$missingContainer.empty();
			_.each(docs.Documents.missing(), this.renderMissing, this);
		},

        reload: function() {
            docs.Documents.fetch({data: {reload:true}});
        }

    });
});

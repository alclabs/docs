var docs = docs || {};

docs.MissingRowView = Backbone.View.extend({
    tagName:  'tr',

    template: _.template( $('#missing-row-template').html(), null, {'variable': 'doc'} ),

    initialize: function() {
      this.model.on( 'change', this.render, this );
    },

    render: function() {
      this.$el.html( this.template(this.model.toJSON()  ) );
      return this;
    }
});
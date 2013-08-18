var docs = docs || {};

(function() {
    'use strict';

    var Documents = Backbone.Collection.extend({
        model: docs.Reference,

        url: 'documents',

        missing: function() {
			return this.filter(function( ref ) {
				return !ref.get('checkDocExists');
			});
		},

        none: function() {
            return this.filter(function(ref) {
                return ref.get('docPath').length === 0
            });
        }
    });

    docs.Documents = new Documents();
}());
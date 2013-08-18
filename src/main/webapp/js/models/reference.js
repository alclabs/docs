var docs = docs || {};

(function() {
    'use strict';

    docs.Reference = Backbone.Model.extend({
        defaults: {
            referencePath: '',
            title: '',
            docPath: '',
            pathType: 'DOC',
            category: '',
            checkDocExists: false
            // ignore extra columns for now
        }
    });

}());